package ru.kss.chat;

import ru.kss.chat.messages.Message;

import java.util.Map;

/**
 * Server-side pool for creating and handling connections (of uncertain nature by interface design)
 */
public interface ConnectionPool {

    /**
     * Basic method for transport-level-specific ConnectionPool instantiation and configuration
     * @param config map of detailed configuration parameters necessary for specific pool startup
     * @return ConnectionPool instance configured, started and ready for service
     */
    ConnectionPool start(Map<String, String> config);

    /**
     * Registers Broadcaster to send messages to clients of this ConnectionPool
     * @param broadcaster message producer implementation
     * @return configured and started Broadcaster. The thread running this Broadcaster object will belong to ConnectionPool instance
     */
    Broadcaster register(Broadcaster broadcaster);

    /**
     * Registers client {@code Handler} instance in the pool
     * @param handler client handler
     */
    void register(Handler handler);

    /**
     * Remove client handler from pool on exit or connection loss
     * @param handler client handler
     */
    void unRegisterClient(Handler handler);

    /**
     * Receives and registers a message for broadcasting to all clients
     * @param message message object
     * @return object that will be sent to clients. As Message is immutable - it can be another instance of Message
     * or even {@code null} if the pool refuses to broadcast this message for some reason
     */
    Message broadcast(Message message);

    /**
     * @return {@code Storage} instance responsible for holding tias pool`s messages
     */
    Storage storage();

    /**
     * Look through the registered clients for username
     *
     * @param username client username
     * @return {@code true} if client with that username already exists in chat, {@code false} otherwise
     */
    boolean checkUsernameExists(String username);
}
