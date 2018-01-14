package ru.kss.chat.server.sockets;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import ru.kss.chat.AbstractHandler;
import ru.kss.chat.ConnectionPool;
import ru.kss.chat.Handler;
import ru.kss.chat.communicators.Communicator;
import ru.kss.chat.messages.Message;
import ru.kss.chat.messages.TextMessage;
import ru.kss.chat.server.communicators.FinCommunicator;
import ru.kss.chat.server.communicators.WelcomeServerCommunicator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static ru.kss.chat.Utils.composeMessage;
import static ru.kss.chat.Utils.parseMessage;

/**
 * Client connection and communication handler thread. An instance of this class is spawned
 * from connections listening loop and deals with one specific client
 */
@Slf4j
public class ConnectionHandler extends AbstractHandler implements Handler {

    private Socket socket;
    private ConnectionPool pool;
    private PrintWriter writer;
    private String username;

    public ConnectionHandler(Socket socket, ConnectionPool pool) {
        this.socket = socket;
        this.pool = pool;
        log.debug("Established socket connection with a client at {}, threadID {}", socket.getInetAddress(), this.getUsername());
    }

    public void setUsername(String username) {
        if (Strings.isNullOrEmpty(username)){
            throw new IllegalArgumentException("Username should not be empty");
        }
        if (pool.checkUsernameExists(username)) {
            throw new IllegalArgumentException("Username should be unique");
        }
        log.debug("New client name: {}", username);
        this.username = username;
        pool.register(this);
    }

    public String getUsername() {
        return this.username != null ? this.username : Long.toString(Thread.currentThread().getId());
    }

    @Override
    public void innerSend(Message message) {
        if (this.writer != null) {
            log.debug("Sending message {} to client \"{}\"", message, this.getUsername());
            this.writer.println(compose(message));
        }
    }

    @Override
    public void broadcast(String text) {
        pool.broadcast(new TextMessage(text, this.getUsername()));
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
        return this.pool;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            this.writer = out;

            Communicator communicator = new WelcomeServerCommunicator(this);
            String inputString;
            do {
                send(communicator.message());
                inputString = in.readLine();
                communicator = communicator.process(parse(inputString));
            } while (!(communicator instanceof FinCommunicator));

            // Send FIN command to client
            send(communicator.message());

            log.debug("Closing client connection at {}", socket.getInetAddress());
        } catch (IOException e) {
            log.error("Socket error", e);
        } finally {
            this.writer = null;
            pool.unRegisterClient(this);
        }
    }

    @Override
    public String toString() {
        return "ConnectionHandler{" +
            "username='" + this.getUsername() + '\'' +
            '}';
    }
}
