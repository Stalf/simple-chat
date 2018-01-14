package ru.kss.chat;

/**
 * Server-side Runnable object responsible for broadcasting messages to chat clients.
 */
public interface Broadcaster extends Runnable {

    /**
     * Subscribes to send messages to provided ChatService`s clients
     * @param service ChatService to broadcast the messages
     */
    void subscribe(ChatService service);

}
