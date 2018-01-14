package ru.kss.chat.client.communicators;

import ru.kss.chat.*;
import ru.kss.chat.client.messages.ClientCommandMessage;
import ru.kss.chat.commands.ChatCommand;
import ru.kss.chat.commands.Command;
import ru.kss.chat.communicators.AbstractCommunicator;
import ru.kss.chat.communicators.Communicator;
import ru.kss.chat.messages.Message;

import static ru.kss.chat.ConsolePrinter.printError;
import static ru.kss.chat.ConsolePrinter.println;

/**
 * Abstract class implementing common client-side communicator logic (primarily error handling)
 */
public abstract class ClientCommunicator extends AbstractCommunicator {

    protected volatile boolean awaitingUserInput;

    public ClientCommunicator(Handler handler) {
        super(handler);
    }

    @Override
    public ClientCommunicator process(Message input) {
        Communicator result = super.process(input);
        if (result instanceof ClientCommunicator) {
            return (ClientCommunicator) result;
        } else {
            // Impossible, but the compiler needs this
            error(String.format("Wrong Communicator class: %s", result.getClass().getTypeName()));
            return null;
        }
    }

    @Override
    protected void logError(Exception e) {
        printError(e);
    }

    @Override
    protected void logError(Message message) {
        println(String.format("Error message received: %s", message.toString()));
    }

    @Override
    protected Communicator update(Command command, String message) {
        this.setMessage(new ClientCommandMessage(command, message, handler.getUsername()));
        return this;
    }

    /**
     * Sets message for current communicator
     *
     * @param chatCommand {@code ChatCommand} instance to be executed
     * @param message text message received with command
     * @return current communicator
     */
    protected Communicator update(ChatCommand chatCommand, String message) {
        String execute = chatCommand.clientExecute(message);
        if (chatCommand.getResponse() != Command.EMPTY) {
            return this.update(chatCommand.getRequest(), execute);
        } else {
            return this.update();
        }
    }


    @Override
    protected Communicator update(Message message) {
        this.setMessage(message);
        return this;
    }


    /**
     * Method shows if chat Client is waiting for console user input
     */
    public boolean isAwaitingUserInput() {
        return awaitingUserInput;
    }


}
