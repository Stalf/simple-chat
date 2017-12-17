package ru.kss.chat.client;

import ru.kss.chat.Utils;
import ru.kss.chat.client.communicators.ChatClientCommunicator;
import ru.kss.chat.client.communicators.ClientCommunicator;
import ru.kss.chat.client.sockets.ClientHandler;

import java.io.IOException;
import java.net.Socket;
import java.util.UUID;

import static java.lang.Thread.sleep;

/**
 * Bot for chat stress testing.
 * Connects with random UUID name and sends a message once per {@value PERIOD} milliseconds
 */
public class BotMain {

    public static final int PERIOD = 10_000;

    public static void main(String... args) {

        Socket socket = null;
        try {
            socket = new Socket(Utils.DEFAULT_SERVER_ADDRESS, Utils.DEFAULT_PORT_NUMBER);

            ClientHandler handler = new ClientHandler(socket);

            Thread clientHandlerThread = new Thread(handler);
            clientHandlerThread.start();

            while (handler.getCurrentCommunicator() != null && !handler.getCurrentCommunicator().isAwaitingUserInput()) {
                sleep(1000);
            }

            //random username
            handler.processUserInput(UUID.randomUUID().toString());

            // Wait until we skip to ChatClientCommunicator
            ClientCommunicator communicator;
            do {
                sleep(1000);
                communicator = handler.getCurrentCommunicator();
            } while (!(
                (communicator != null) &&
                    (communicator.getClass().equals(ChatClientCommunicator.class))));

            int i = 0;
            while (true) {
                sleep(PERIOD);
                handler.processUserInput("text message " + i++);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }


    }

}
