package ru.kss.chat.server;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import ru.kss.chat.Storage;
import ru.kss.chat.messages.Message;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Simple in-memory message storage implementation
 */
public class SimpleMessageStorage implements Storage {

    private final ConcurrentLinkedDeque<Message> queue = new ConcurrentLinkedDeque<>();
    private final BlockingQueue<Message> pendingMessagesQueue = Queues.newLinkedBlockingQueue();

    @Override
    public Queue<Message> getLastMessages(int count) {
        LinkedList<Message> result = Lists.newLinkedList();

        Iterator<Message> iterator = queue.descendingIterator();
        int i = 0;
        while (iterator.hasNext() && (i++ < count)) {
            result.addFirst(iterator.next());
        }
        return result;
    }

    @Override
    public Message save(Message message) {
        queue.add(message);

        if (pendingMessagesQueue.add(message)) {
            return message;
        } else {
            return null;
        }
    }

    @Override
    public BlockingQueue<Message> pendingMessageQueue() {
        return pendingMessagesQueue;
    }

    @Override
    public int getMessageCount() {
        return queue.size();
    }
}
