package ru.kss.chat.server;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.kss.chat.*;
import ru.kss.chat.commands.Command;
import ru.kss.chat.messages.Message;
import ru.kss.chat.server.messages.ServerCommandMessage;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;

/**
 * {@code ChatService} transport-independent implementation
 */
@Slf4j
public class SimpleChatService implements ChatService {

    private final Storage messageStorage;
    private final ConnectionPool connectionPool;
    private final Integer portNumber;

    /**
     * Map for holding {@code Thread} instances running Broadcasters
     */
    @Getter(AccessLevel.PACKAGE)
    private Map<Broadcaster, Thread> broadcasterThreads = Maps.newHashMap();

    /**
     * User handlers map with username as a key
     */
    @Getter(AccessLevel.PACKAGE)
    private final Map<String, Handler> users = Maps.newConcurrentMap();

    public SimpleChatService(Storage messageStorage, ConnectionPool connectionPool, Integer portNumber) {
        this.messageStorage = messageStorage;
        this.connectionPool = connectionPool;
        this.portNumber = portNumber;

        register(new PendingMessagesBroadcaster());
    }

    @Override
    public void start() {
        Objects.requireNonNull(this.messageStorage);
        Objects.requireNonNull(this.connectionPool);

        Map<String, String> poolConfig = Maps.newHashMap();
        poolConfig.put("portNumber", portNumber.toString());

        connectionPool.start(this, poolConfig);

        // On Server shutdown - send Bye to all registered clients
        Runtime.getRuntime().addShutdownHook(
            new Thread(() -> {
                innerBroadcast(new ServerCommandMessage(Command.QUIT, "Server is stopping... Disconnecting. Goodbye!"));
            }));

        // Run all registered broadcasters
        broadcasterThreads.values().forEach(Thread::start);
    }

    /**
     * Inner class for broadcasting messages from storage.pendingQueue
     */
    private class PendingMessagesBroadcaster implements Broadcaster {
        private ChatService service;

        @Override
        public void subscribe(ChatService service) {
            this.service = service;
        }

        @Override
        public void run() {
            log.info("PendingMessagesBroadcaster started");

            BlockingQueue<Message> queue = this.service.storage().pendingMessagesQueue();
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Message message = queue.take();
                    if (message != null) {
                        // We need inner class for this innerBroadcast call
                        SimpleChatService.this.innerBroadcast(message);
                    }
                } catch (Exception e) {
                    log.error("Error in broadcaster thread", e);
                }
            }
            log.info("PendingMessagesBroadcaster stopped");
        }
    }


    @Override
    public Message broadcast(Message message) {
        return messageStorage.save(message);
    }

    /**
     * Inner method that enables messagesBroadcaster to access connections map and send messages directly to client sockets
     */
    private void innerBroadcast(Message message) {
        log.debug("Broadcasting message \"{}\" to all clients", message);
        users.values().forEach(handler -> {
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

        return broadcaster;
    }

    @Override
    public void unRegister(Broadcaster broadcaster) {
        if (broadcasterThreads.containsKey(broadcaster)) {
            Thread thread = broadcasterThreads.remove(broadcaster);
            thread.interrupt();
        } else {
            log.debug("Unable to unregister Broadcaster {}, not found in a map", broadcaster);
        }
    }

    @Override
    public Storage storage() {
        return messageStorage;
    }

    @Override
    public void register(Handler handler) {
        String username = handler.getUsername();
        if (users.containsKey(username)) {
            throw new IllegalArgumentException(String.format("User with name %s already registered", username));
        }
        log.debug("Register client {} in chat", username);
        users.put(username, handler);
    }

    @Override
    public void unRegister(Handler handler) {
        String username = handler.getUsername();
        if (!Strings.isNullOrEmpty(username)) {
            log.debug("Unregister client {} from pool", username);
            // Protection against erroneous removal of working handler
            if (!users.remove(username, handler)) {
                log.warn("Error on client {} unregister. Handlers doesn't match: {} and {}", username, handler, users.get(username));
            }
        }
    }

    @Override
    public int getUserCount() {
        return users.size();
    }

    @Override
    public boolean checkUsernameExists(String username) {
        return users.containsKey(username);
    }

    @Override
    public int getMessageCount() {
        return messageStorage.getMessageCount();
    }
}
