package ru.kss.chat;

/**
 * Client-server interaction commands
 */
public enum Command {

    /**
     * Acknowledgement
     */
    ACK,
    /**
     * Negative Acknowledgement
     */
    NAK,
    /**
     * Handshake from server to client
     */
    HI,
    /**
     * Client username message
     */
    NAME,
    /**
     * Greeting from server to successfully authenticated client
     */
    WELCOME,
    /**
     * Text message
     */
    TXT,
    /**
     * Goodbye message
     */
    FIN,
    /**
     * User input command
     */
    INPUT,
    /**
     * Check alive command
     */
    PING,
    /**
     * Do-nothing command
     */
    EMPTY

}
