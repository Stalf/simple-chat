package ru.kss.chat.server.broadcasters;

import org.junit.Test;
import ru.kss.chat.ChatService;

import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TimestampBroadcasterTest {

    @Test
    public void run() throws InterruptedException {

        ChatService chatService = mock(ChatService.class);
        TimestampBroadcaster broadcaster = new TimestampBroadcaster(1);
        broadcaster.subscribe(chatService);

        Thread thread = new Thread(broadcaster);
        thread.start();

        sleep(TimeUnit.SECONDS.toMillis(2));

        verify(chatService, atLeast(2)).broadcast(any());

    }
}
