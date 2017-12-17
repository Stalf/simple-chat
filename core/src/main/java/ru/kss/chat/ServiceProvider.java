package ru.kss.chat;

/**
 * Service provider is responsible for executing client requests
 */
public interface ServiceProvider {

    /**
     * @return current quantity of users connected to the server
     */
    int getUserCount();

}
