package ru.kss.chat.server.communicators;

import ru.kss.chat.Command;
import ru.kss.chat.communicators.Communicator;
import ru.kss.chat.messages.Message;

/**
 * Communicator sends goodbye message to the client and closes socket (implemented in {@code ServerHandler#run()}
 */
public class FinCommunicator extends ServerCommunicator {

    public FinCommunicator() {
        super(Command.FIN, "Disconnecting from server. Goodbye!");
    }

    @Override
    protected Communicator innerProcess(Message command) {
        return this;
    }
}
