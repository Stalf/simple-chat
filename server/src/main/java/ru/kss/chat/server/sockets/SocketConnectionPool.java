package ru.kss.chat.server.sockets;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import ru.kss.chat.ChatService;
import ru.kss.chat.ConnectionPool;
import ru.kss.chat.Handler;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Map;
import java.util.Objects;

import static ru.kss.chat.Utils.DEFAULT_PORT_NUMBER;

/**
 * Central pool for holding client connections and broadcasting messages to all clients
 */
@Slf4j
public class SocketConnectionPool implements ConnectionPool {

    private Map<Handler, Thread> handlerThreads = Maps.newHashMap();
    private Thread connectionListener;
    private ChatService chatService;

    public ConnectionPool start(ChatService chatService, Map<String, String> config) {
        Objects.requireNonNull(config);
        this.chatService = chatService;

        // Main thread listening for new Clients
        connectionListener = new Thread(() -> {
            log.info("Connection listener thread started");

            try (ServerSocket listener =
                     new ServerSocket(Integer.parseInt(config.getOrDefault("portNumber", Integer.toString(DEFAULT_PORT_NUMBER))))) {
                log.info("Server started and listening to port {}", listener.getLocalPort());

                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        ConnectionHandler handler = new ConnectionHandler(listener.accept(), this.chatService);
                        Thread thread = new Thread(handler);
                        handlerThreads.put(handler, thread);
                        thread.start();
                    } catch (IOException e) {
                        log.error("Error in connection listening thread. Interrupting", e);
                        Thread.currentThread().interrupt();
                    }
                }
            } catch (IOException e) {
                log.error("Socket error", e);
            }

            log.info("Connection listener thread stopped");
        });
        connectionListener.start();

        return this;
    }

    public void register(Handler handler) {
        chatService.register(handler);
    }

    @Override
    public void unRegister(Handler handler) {
        chatService.unRegister(handler);

        // remove handler thread from pool
        Thread thread = handlerThreads.remove(handler);
        if (thread != null) {
            thread.interrupt();
        } else {
            log.error("Error removing thread for handler {}", handler);
        }
    }

}
