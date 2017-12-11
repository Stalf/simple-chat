package ru.kss.chat.client;

import org.apache.commons.io.IOUtils;
import ru.kss.chat.Config;

import java.io.*;
import java.net.Socket;

import static ru.kss.chat.client.Printer.printHelp;
import static ru.kss.chat.client.Printer.println;

/**
 * Command executor class
 */
public class CommandExecutor implements AutoCloseable {

    private final Socket clientSocket;
    private final DataOutputStream os;
    private final BufferedReader ir;

    public CommandExecutor() throws IOException {
        clientSocket = new Socket("localhost", Config.PORT_NUMBER);
        os = new DataOutputStream(clientSocket.getOutputStream());
        ir = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    /**
     * Parses and executes received command
     *
     * @param inputString user input
     */
    public void execute(String inputString) {

        Command command = Command.parse(inputString);

        switch (command) {
            case HELP: {
                printHelp();
                break;
            }
            case QUIT: {
                println("Goodbye!");
                System.exit(0);
            }
            default: {
                System.out.println(inputString);

                try {
                    os.writeBytes(inputString + '\n');
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    @Override
    public void close() throws IOException {
        os.close();
        ir.close();
        clientSocket.close();
    }
}
