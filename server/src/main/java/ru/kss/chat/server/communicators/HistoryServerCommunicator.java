package ru.kss.chat.server.communicators;

import ru.kss.chat.Command;
import ru.kss.chat.Handler;
import ru.kss.chat.communicators.Communicator;
import ru.kss.chat.messages.Message;

import java.util.Queue;

public class HistoryServerCommunicator extends ServerCommunicator {

    public static final int HISTORY_DEPTH = 100;
    private final Queue<Message> lastMessages;

    public HistoryServerCommunicator(Handler handler) {
        super(handler);
        lastMessages = handler.connectionPool().storage().getLastMessages(HISTORY_DEPTH);
        this.update(Command.PING, "");
    }

    @Override
    protected Communicator innerProcess(Message message) {
        Command command = message.getCommand();

        switch (command) {
            case ACK: {
                Message poll = lastMessages.poll();
                if (poll != null) {
                    return this.update(poll);
                } else {
                    return new ChatServerCommunicator(this.handler);
                }
            }
            default: {
                return error(message);
            }
        }
    }
}
