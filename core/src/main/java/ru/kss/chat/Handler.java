package ru.kss.chat;

import ru.kss.chat.messages.Message;

/**
 * Root interface for classes implementing client-server interaction
 */
public interface Handler extends Runnable{

    /**
     * @return username of the client that this instance handles
     */
    String getUsername();

    /**
     * Sets client username
     * @param username unique username
     */
    void setUsername(String username);

    /**
     * Executes command/message transmission through the network.
     * Inner implemetation of transport layer is responsible for thread-safety of "send" operation. Each message should be sent atomically
     * @param message {@code Message} object holding complete information about command type, author, timestamp and message text
     */
    void send(Message message);

    /**
     * Takes message text for broadcasting to all other clients.
     * Forming {@code Message} instance from this text is the responsibility of Handler
     * @param text string containing only message text
     */
    void broadcast(String text);

    /**
     * Parses input string to compose Message object. Deserialization implementation depends on Handler transport layer
     * @param input string message received over network
     * @return parsed {@code Message} object
     */
    Message parse(String input);

    /**
     * Composes string from provided {@code Message} instance. Serialization implementation depends on Handler transport layer
     * @param message {@code Message} object prepared for sending
     * @return message serialized to string
     */
    String compose(Message message);

    /**
     * Returns linked {@code ChatService}.
     * It is necessary to get global data such as user count or messages history
     *
     * In production environment we`ll have to build the System Event Bus instead of such weird methods.
     */
    ChatService chatService();

    /**
     * @return uptime of this handler in seconds
     */
    long uptime();

}
