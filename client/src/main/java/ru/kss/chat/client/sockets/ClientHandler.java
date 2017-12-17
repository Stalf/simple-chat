package ru.kss.chat.client.sockets;

import lombok.Getter;
import lombok.Setter;
import ru.kss.chat.AbstractHandler;
import ru.kss.chat.ConnectionPool;
import ru.kss.chat.Handler;
import ru.kss.chat.client.communicators.ClientCommunicator;
import ru.kss.chat.client.communicators.WelcomeClientCommunicator;
import ru.kss.chat.client.messages.ClientInputMessage;
import ru.kss.chat.messages.Message;
import ru.kss.chat.messages.TextMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.locks.ReentrantLock;

import static ru.kss.chat.Utils.composeMessage;
import static ru.kss.chat.Utils.parseMessage;
import static ru.kss.chat.client.ConsolePrinter.printError;

/**
 * Server connection and communication handler thread
 */
public class ClientHandler extends AbstractHandler implements Handler {

    @Setter
    private String username;

    private Socket socket;
    private PrintWriter writer;
    @Getter
    private ClientCommunicator currentCommunicator;
    private ReentrantLock userInput = new ReentrantLock();

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public boolean startUserInput() {
        if (!currentCommunicator.isAwaitingUserInput()) {
            userInput.lock();
            return false;
        } else {
            return true;
        }
    }

    public void stopUserInput() {
        if (userInput.isHeldByCurrentThread()) {
            userInput.unlock();
        }
    }

    public void processUserInput(String message) {
        if (this.currentCommunicator != null) {
            send(this.currentCommunicator.process(new ClientInputMessage(message)).message());
        }
    }

    @Override
    public String getUsername() {
        return username != null ? username : socket.getLocalAddress().toString();
    }

    @Override
    public void innerSend(Message message) {
        if (this.writer != null) {
            this.writer.println(compose(message));
        }
    }

    @Override
    public void broadcast(String text) {
        // for client side it is same as send()
        this.send(new TextMessage(text, this.getUsername()));
    }

    @Override
    public Message parse(String input) {
        return parseMessage(input);
    }

    @Override
    public String compose(Message message) {
        return composeMessage(message);
    }

    @Override
    public ConnectionPool connectionPool() {
        return null;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            this.writer = out;

            this.currentCommunicator = new WelcomeClientCommunicator(this);
            String inputString;
            while (true) {
                inputString = in.readLine();

                userInput.lock();
                try {
                    this.currentCommunicator = this.currentCommunicator.process(parse(inputString));
                    send(this.currentCommunicator.message());
                } finally {
                    userInput.unlock();
                }

                // infinite loop, System.exit(0) is called on /q command
            }
        } catch (IOException e) {
            printError(e);
            if (e instanceof SocketException) {
                // Close client if server connection is lost
                System.exit(0);
            }
        } finally {
            this.writer = null;
        }


    }


}
