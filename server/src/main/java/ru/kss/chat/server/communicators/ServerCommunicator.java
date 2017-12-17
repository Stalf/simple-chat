package ru.kss.chat.server.communicators;

import lombok.extern.slf4j.Slf4j;
import ru.kss.chat.*;
import ru.kss.chat.communicators.AbstractCommunicator;
import ru.kss.chat.communicators.Communicator;
import ru.kss.chat.messages.Message;
import ru.kss.chat.server.messages.ServerCommandMessage;

/**
 * Abstract class implementing common server-side communicator logic (primarily logging and error handling)
 */
@Slf4j
public abstract class ServerCommunicator extends AbstractCommunicator {

    public ServerCommunicator(Command command, String message) {
        this(null);
        this.update(command, message);
    }

    public ServerCommunicator(Handler handler) {
        super(handler);
    }

    @Override
    protected Communicator update(Command command, String message) {
        this.setMessage(new ServerCommandMessage(command, message));
        return this;
    }

    @Override
    protected void logError(Exception e) {
        log.error("Error in communicator", e);
    }

    @Override
    protected void logError(Message message) {
        log.warn("Error message received: {}", message.toString());
    }

    @Override
    public Communicator update(Message message) {
        this.setMessage(message);
        return this;
    }
}
