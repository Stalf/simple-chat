package ru.kss.chat.server;

import lombok.extern.slf4j.Slf4j;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Before;
import org.junit.Test;
import ru.kss.chat.messages.EmptyMessage;
import ru.kss.chat.messages.Message;
import ru.kss.chat.messages.TextMessage;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

@Slf4j
public class SimpleMessageStorageTest {

    private SimpleMessageStorage storage;
    private Message lastMessage;

    @Before
    public void setUp() {
        storage = new SimpleMessageStorage();

        for (int i = 0; i < 20; i++) {
            lastMessage = new TextMessage(Integer.toString(i), "test");
            log.debug("producing message = {}", lastMessage);
            storage.save(lastMessage);
        }
    }

    @Test
    public void getLastMessages10of20() {

        Queue<Message> messages = storage.getLastMessages(10);
        assertThat(messages, IsCollectionWithSize.hasSize(10));

        Message poll = null;
        for (int i = 0; i < 10; i++) {
            poll = messages.poll();
            log.debug("message = {}", poll);
        }
        assertEquals(lastMessage, poll);
    }

    @Test
    public void getLastMessages100of20() {

        Queue<Message> messages = storage.getLastMessages(100);
        assertThat(messages, IsCollectionWithSize.hasSize(20));

        Message poll = null;
        for (int i = 0; i < 20; i++) {
            poll = messages.poll();
            log.debug("message = {}", poll);
        }
        assertEquals(lastMessage, poll);
    }

    @Test
    public void pendingMessageQueue() {

        BlockingQueue<Message> blockingQueue = storage.pendingMessageQueue();
        assertThat(blockingQueue, IsCollectionWithSize.hasSize(20));

        Message poll = null;
        for (int i = 0; i < 20; i++) {
            poll = blockingQueue.poll();
            log.debug("message = {}", poll);
        }
        assertEquals(lastMessage, poll);
        assertThat(blockingQueue, IsCollectionWithSize.hasSize(0));
    }

    @Test
    public void getMessageCount() {

        assertEquals(20, storage.getMessageCount());
        storage.save(new EmptyMessage());
        assertEquals(21, storage.getMessageCount());

    }
}


