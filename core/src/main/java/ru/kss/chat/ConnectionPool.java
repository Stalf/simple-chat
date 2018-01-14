package ru.kss.chat;

import java.util.Map;

/**
 * Server-side pool for creating and handling connections (of uncertain nature by interface design)
 */
public interface ConnectionPool {

    /**
     * Basic method for transport-level-specific ConnectionPool instantiation and configuration
     * @param chatService handling user intercation
     * @param config map of detailed configuration parameters necessary for specific pool startup
     * @return ConnectionPool instance configured, started and ready for service
     */
    ConnectionPool start(ChatService chatService, Map<String, String> config);

    /**
     * Registers client {@code Handler} instance in the pool
     * @param handler client handler
     */
    void register(Handler handler);

    /**
     * Remove client handler from pool on exit or connection loss
     * @param handler client handler
     */
    void unRegister(Handler handler);

}
