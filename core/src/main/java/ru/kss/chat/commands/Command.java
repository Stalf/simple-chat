package ru.kss.chat.commands;

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
     * Show info about available commands
     */
    HELP,
    /**
     * Request user count
     */
    USER_COUNT,
    /**
     * Request message count
     */
    MESSAGE_COUNT,
    /**
     * Goodbye message
     */
    QUIT,
    /**
     * User input command
     */
    INPUT,
    /**
     * Check alive command
     */
    PING,
    /**
     * Server uptime request
     */
    SERVER_UPTIME,
    /**
     * Request server to stop some clients
     */
    STOP_BOTS,

    /**
     * Do-nothing command
     */
    EMPTY

}
