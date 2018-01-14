package ru.kss.chat.server;

import lombok.extern.slf4j.Slf4j;
import ru.kss.chat.ChatService;
import ru.kss.chat.Utils;
import ru.kss.chat.server.sockets.SocketConnectionPool;

import java.util.concurrent.TimeUnit;

/**
 * Simple Chat Server main class
 */
@Slf4j
public class ServerMain {

    public static void main(String... args) {
        log.info("Default port number is {}. If you need the server to listen to another port number - provide it as the first command line argument",
            Utils.DEFAULT_PORT_NUMBER);
        log.info("The Server can hold multiple chat rooms on different port numbers, provided as subsequent command line arguments");
        log.info("Starting Chat server...");

        for (String arg : args) {
            ChatService chatService = new SimpleChatService(new SimpleMessageStorage(), new SocketConnectionPool(), Integer.parseInt(arg));
            chatService.register(new TimestampBroadcaster(TimeUnit.MINUTES.toSeconds(1)));
            chatService.start();
        }
    }
}
