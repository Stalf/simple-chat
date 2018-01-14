package ru.kss.chat.server.communicators;

import lombok.extern.slf4j.Slf4j;
import ru.kss.chat.*;
import ru.kss.chat.commands.ChatCommand;
import ru.kss.chat.commands.Command;
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

    /**
     * Sets message for current communicator
     *
     * @param chatCommand {@code ChatCommand} instance to be executed
     * @param message text message received with command
     * @return current communicator
     */
    protected Communicator update(ChatCommand chatCommand, String message) {
        if (chatCommand.getResponse() != Command.EMPTY) {
            return this.update(chatCommand.getResponse(), chatCommand.serverExecute(handler, message));
        } else {
            return this.update();
        }
    }

    @Override
    public Communicator update(Message message) {
        this.setMessage(message);
        return this;
    }
}
