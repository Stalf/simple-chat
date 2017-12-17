package ru.kss.chat.server;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import ru.kss.chat.ConnectionPool;
import ru.kss.chat.Utils;
import ru.kss.chat.server.sockets.SocketConnectionPool;

import java.util.Map;

/**
 * Simple Chat Server main class
 */
@Slf4j
public class ServerMain {

    public static void main(String... args) {
        log.info("Default port number is {}. If you need the server to listen to another port number - provide it as the first command line argument",
            Utils.DEFAULT_PORT_NUMBER);
        log.info("Starting Chat server...");

        Map<String, String> poolConfig = Maps.newHashMap();
        if (args.length >= 1) {
            poolConfig.put("portNumber", args[0]);
        }

        ConnectionPool connectionPool = new SocketConnectionPool().start(poolConfig);
        //Broadcaster timestampBroadcaster = connectionPool.register(new TimestampBroadcaster(TimeUnit.MINUTES.toSeconds(1)));

    }

}
