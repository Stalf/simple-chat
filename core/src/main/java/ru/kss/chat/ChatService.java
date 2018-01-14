package ru.kss.chat;

import ru.kss.chat.messages.Message;

/**
 * Service provider is responsible for executing client requests
 */
public interface ChatService {

    /**
     * Bootstrap chat infrastructure and start listening for client connections
     */
    void start();

    /**
     * @return {@code Storage} instance responsible for holding this chat`s messages
     */
    Storage storage();

    /**
     * Registers a new user into chat
     * @param handler user handler
     */
    void register(Handler handler);

    /**
     * Removes user from chat on exit or connection loss
     * @param handler user handler
     */
    void unRegister(Handler handler);

    /**
     * @return current quantity of users connected to the server
     */
    int getUserCount();

    /**
     * Look through the registered clients for username
     *
     * @param username client username
     * @return {@code true} if client with that username already exists in chat, {@code false} otherwise
     */
    boolean checkUsernameExists(String username);

    /**
     * Receives and registers a message for broadcasting to all clients
     * @param message message object
     * @return object that will be sent to clients. As Message is immutable - it can be a new instance of {@code Message}
     * or even {@code null} if the system refuses to broadcast this message for some reason
     */
    Message broadcast(Message message);

    /**
     * @return total messages in server storage
     */
    int getMessageCount();

    /**
     * Registers Broadcaster to send messages to clients of this ConnectionPool
     * @param broadcaster message producer implementation
     * @return configured Broadcaster, it will be started after main {@code ChatServer.start()} call. The thread running this Broadcaster object will belong to ChatService instance
     */
    Broadcaster register(Broadcaster broadcaster);

    /**
     * UnRegisters Broadcaster from service
     * @param broadcaster message producer instance for unregister
     */
    void unRegister(Broadcaster broadcaster);

    /**
     * Tears down connection with @{code count} bot clients with largest uptime
     * @param count number of bots to stop
     */
    void stopBots(int count);

    /**
     * @return server uptime in seconds
     */
    long uptime();
}
