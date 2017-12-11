package ru.kss.chat.server;

import lombok.extern.slf4j.Slf4j;
import ru.kss.chat.Config;
import ru.kss.chat.Utils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
public class ServerMain {

    public static void main(String... args) throws IOException {
        log.info("Starting Chat server...");

        try (ServerSocket serverSocket = new ServerSocket(Config.PORT_NUMBER)) {
            log.info("Server started and listening to port {}", serverSocket.getLocalPort());
            try (
                Socket serviceSocket = serverSocket.accept();
                BufferedReader ir = new BufferedReader(new InputStreamReader(serviceSocket.getInputStream()));
                DataOutputStream os = new DataOutputStream(serviceSocket.getOutputStream())) {

                log.info("Established socket connection with {}", serviceSocket.getInetAddress());

                String line;
                while (true) {
                    line = ir.readLine();
                    log.info("Got text from user: {}", line);
                }

            } catch (IOException e) {
                log.error("Socket error", e);
            }
        }
    }

}
