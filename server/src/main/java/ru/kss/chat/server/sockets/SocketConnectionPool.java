package ru.kss.chat.server.sockets;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.kss.chat.*;
import ru.kss.chat.messages.Message;
import ru.kss.chat.server.SimpleMessageStorage;
import ru.kss.chat.server.messages.ServerCommandMessage;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;

import static ru.kss.chat.Utils.DEFAULT_PORT_NUMBER;

/**
 * Central pool for holding client connections and broadcasting messages to all clients
 */
@Slf4j
public class SocketConnectionPool implements ConnectionPool {

    private final Storage chatStorage = new SimpleMessageStorage();
    /**
     * Client connections map with client username as a key
     */
    @Getter(AccessLevel.PROTECTED)
    private final Map<String, Handler> connections = Maps.newConcurrentMap();
    private Map<Broadcaster, Thread> broadcasterThreads = Maps.newHashMap();
    private Map<Handler, Thread> handlerThreads = Maps.newHashMap();

    private Thread connectionListener;
    private Thread messagesBroadcaster;

    private ServiceProvider serviceProvider;

    public ConnectionPool start(Map<String, String> config) {
        Objects.requireNonNull(config);

        // On Server shutdown - send Bye to all registered clients
        Runtime.getRuntime().addShutdownHook(
            new Thread(() -> {
                innerBroadcast(new ServerCommandMessage(Command.FIN, "Server is stopping... Disconnecting. Goodbye!"));
            }));

        // Main thread listening for new Clients
        connectionListener = new Thread(() -> {
            log.info("Connection listener thread started");

            try (ServerSocket listener =
                     new ServerSocket(Integer.parseInt(config.getOrDefault("portNumber", Integer.toString(DEFAULT_PORT_NUMBER))))) {
                log.info("Server started and listening to port {}", listener.getLocalPort());

                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        ConnectionHandler handler = new ConnectionHandler(listener.accept(), this);
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

        // Thread for sending text messages from storage buffer to all clients
        messagesBroadcaster = new Thread(() -> {
            log.info("Message broadcaster thread started");

            BlockingQueue<Message> queue = storage().pendingMessagesQueue();
            while (!Thread.currentThread().isInterrupted()) {

                try {
                    Message message = queue.take();
                    if (message != null) {
                        innerBroadcast(message);
                    }
                } catch (Exception e) {
                    log.error("Error in broadcaster thread", e);
                }
            }
            log.info("Message broadcaster thread stopped");
        });
        messagesBroadcaster.start();

        this.register(new SocketServiceProvider(this));

        return this;
    }

    /**
     * Inner method that enables messagesBroadcaster to access connections map and send messages directly to client sockets
     */
    private void innerBroadcast(Message message) {
        log.debug("Broadcasting message \"{}\" to all clients", message);
        connections.values().forEach(handler -> {
            try {
                handler.send(message);
            } catch (Exception e) {
                log.error("Exception broadcasting message to {}", handler.getUsername(), e);
            }
        });
    }

    @Override
    public Broadcaster register(Broadcaster broadcaster) {
        if (broadcasterThreads.containsKey(broadcaster)) {
            throw new IllegalArgumentException("Broadcaster already registered");
        }

        Thread thread = new Thread(broadcaster);
        broadcasterThreads.put(broadcaster, thread);
        broadcaster.subscribe(this);
        thread.start();

        return broadcaster;
    }

    @Override
    public Message broadcast(Message message) {
        return chatStorage.save(message);
    }

    @Override
    public Storage storage() {
        return chatStorage;
    }

    @Override
    public ServiceProvider provider() {
        return this.serviceProvider;
    }

    public void register(Handler handler) {
        log.debug("Register client {} in pool", handler.getUsername());
        connections.put(handler.getUsername(), handler);
    }

    @Override
    public void register(ServiceProvider provider) {
        this.serviceProvider = provider;
    }

    @Override
    public void unRegisterClient(Handler handler) {
        String username = handler.getUsername();
        if (!Strings.isNullOrEmpty(username)) {
            log.debug("Unregister client {} from pool", username);
            if (!connections.remove(username, handler)) {
                log.warn("Error on client {} unregister. Handlers doesn't match: {} and {}", username, handler, connections.get(username));
            }
        }
    }

    public boolean checkUsernameExists(String username) {
        return connections.containsKey(username);
    }
}
