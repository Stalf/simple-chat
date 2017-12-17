package ru.kss.chat.server.sockets;

import ru.kss.chat.ServiceProvider;

/**
 * {@code ServiceProvider} implementation coupled with {@code SocketConnectionPool}
 */
public class SocketServiceProvider implements ServiceProvider {

    private final SocketConnectionPool connectionPool;

    public SocketServiceProvider(SocketConnectionPool connectionPool) {
         this.connectionPool = connectionPool;
    }

    @Override
    public int getUserCount() {
        return this.connectionPool.getConnections().size();
    }
}
