package ru.kss.chat.client;

import ru.kss.chat.Utils;
import ru.kss.chat.client.communicators.ChatClientCommunicator;
import ru.kss.chat.client.communicators.ClientCommunicator;
import ru.kss.chat.client.sockets.ClientHandler;
import ru.kss.chat.commands.ChatCommand;

import java.io.IOException;
import java.net.Socket;
import java.util.UUID;

import static java.lang.Thread.sleep;

/**
 * Bot for chat stress testing.
 * Connects with random UUID name and sends a message once per {@value PERIOD} milliseconds
 */
public class BotMain {

    public static final int PERIOD = 2_000;

    public static void main(String... args) {
        ChatCommand.registerAllCommands();

        Socket socket = null;
        try {
            socket = new Socket(Utils.DEFAULT_SERVER_ADDRESS, Utils.DEFAULT_PORT_NUMBER);

            ClientHandler handler = new ClientHandler(socket);

            Thread clientHandlerThread = new Thread(handler);
            clientHandlerThread.start();

            do {
                sleep(1000);
            }
            while (handler.getCurrentCommunicator() != null && !handler.getCurrentCommunicator().isAwaitingUserInput());

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
                handler.processUserInput("text message " + i++ + "*****" + UUID.randomUUID().toString());
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }


    }

}
