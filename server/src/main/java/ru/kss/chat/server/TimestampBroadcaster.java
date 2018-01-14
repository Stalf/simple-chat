package ru.kss.chat.server;

import lombok.extern.slf4j.Slf4j;
import ru.kss.chat.Broadcaster;
import ru.kss.chat.ChatService;
import ru.kss.chat.server.messages.ServerTextMessage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import static java.lang.Thread.sleep;

/**
 * Periodically broadcast current server time to all chat clients
 */
@Slf4j
public class TimestampBroadcaster implements Broadcaster {

    /**
     * Time value to wait between message broadcasting (in milliseconds)
     */
    private final long sleepPeriod;
    private ChatService service;

    /**
     * Creates TimestampBroadcaster
     * @param sleepPeriod time period between message broadcasting (in seconds)
     */
    public TimestampBroadcaster(long sleepPeriod) {
        this.sleepPeriod = sleepPeriod * 1000;
    }

    @Override
    public void run() {

        try {
            log.info("TimestampBroadcaster started");
            while (!Thread.currentThread().isInterrupted()) {
                service.broadcast(new ServerTextMessage("Current server local time: " + LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM))));

                try {
                    sleep(this.sleepPeriod);
                } catch (InterruptedException e) {
                    break;
                }
            }
        } catch (Exception e) {
            log.error("Error", e);
        }
        log.info("TimestampBroadcaster stopped");
    }

    @Override
    public void subscribe(ChatService service) {
        this.service = service;
    }
}
