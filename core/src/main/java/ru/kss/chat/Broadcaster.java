package ru.kss.chat;

import ru.kss.chat.ConnectionPool;

/**
 * Server-side Runnable object responsible for broadcasting messages to chat clients.
 */
public interface Broadcaster extends Runnable {

    /**
     * Subscribes to send messages to provided ConnectionPool`s clients
     * @param pool ConnectionPool to which messages will be broadcast
     */
    void subscribe(ConnectionPool pool);

}
