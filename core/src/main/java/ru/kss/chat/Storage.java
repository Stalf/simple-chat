package ru.kss.chat;

import ru.kss.chat.messages.Message;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;

/**
 * Message storage. By design ready to store messages of any class
 */
public interface Storage {

    /**
     * Method for getting messages from storage
     * @param count number of the requested messages
     * @return last {@code count} messages in a Queue. Most recent are last
     */
    Queue<Message> getLastMessages(int count);
    /**
     * Saves message to storage
     * @param message full message to store (with timestamp and author)
     * @return the same object that was stored (for example with storage id set if it is database-related storage).
     * Can be {@code null} if storage failed
     */
    Message save(Message message);

    /**
     * Queue holding messages pending for broadcast
     */
    BlockingQueue<Message> pendingMessageQueue();

    /**
     * @return total messages in server storage
     */
    int getMessageCount();

}
