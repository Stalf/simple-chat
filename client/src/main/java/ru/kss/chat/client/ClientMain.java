package ru.kss.chat.client;

import ru.kss.chat.commands.ChatCommand;
import ru.kss.chat.Utils;
import ru.kss.chat.client.sockets.ClientHandler;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import static ru.kss.chat.ConsolePrinter.*;

/**
 * Simple Console Chat Client main class
 */
public class ClientMain {

    public static void main(String... args) throws IOException {
        ChatCommand.registerAllCommands();

        printGreeting();

        int portNumber = Utils.DEFAULT_PORT_NUMBER;
        String serverAddress = Utils.DEFAULT_SERVER_ADDRESS;
        if (args.length >= 1) {
            serverAddress = args[0];
        }
        if (args.length >= 2) {
            portNumber = Integer.valueOf(args[1]);
        }

        println(String.format("Connecting to the server at %s:%d...", serverAddress, portNumber));

        try {
            ClientHandler clientHandler;
            try {
                Socket socket = new Socket(serverAddress, portNumber);

                clientHandler = new ClientHandler(socket);
                Thread clientHandlerThread = new Thread(clientHandler);
                clientHandlerThread.start();

            } catch (Exception e) {
                println("Error starting Chat Client.");
                println("To connect to another server - please provide it's address as the first command line argument.");
                println("If your chat server uses non-default port - please provide it as the second command line argument.");
                throw e;
            }

            Scanner input = new Scanner(System.in);
            while (true) {
                String message = input.nextLine();
                // Common console client can't output and input symbols simultaneously.
                // So, that code construction pauses message flow from the server on first Enter key pressed to let the user write his message.
                if (clientHandler.startUserInput()) {
                    clientHandler.processUserInput(message);
                    clientHandler.stopUserInput();
                } else {
                    printMessageInvite();
                    message = input.nextLine();
                    println();
                    clientHandler.processUserInput(message);
                    clientHandler.stopUserInput();
                }
                // infinite loop, System.exit(0) is called on /q command
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
