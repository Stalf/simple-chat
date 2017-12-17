package ru.kss.chat.communicators;

import com.google.common.base.Strings;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import ru.kss.chat.*;
import ru.kss.chat.messages.EmptyMessage;
import ru.kss.chat.messages.Message;

/**
 * Abstract communicator for common client/server logic
 */
public abstract class AbstractCommunicator implements Communicator {

    /**
     * Handler representing current communicating side (transport-specific logic)
     */
    protected Handler handler;

    /**
     * Current message that communicator has to send
     */
    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    private Message message;

    public AbstractCommunicator(Handler handler) {
        this.handler = handler;
    }

    /**
     * Inner business-logic-related implementation of communicator. This is the main method to write custom communicator code
     *
     * @param message representing received message that has to be processed
     * @return Communicator responsible for further actions (primarily handling answer for the received command)
     */
    protected abstract Communicator innerProcess(Message message);

    /**
     * Abstract error logger
     *
     * @param e Exception received
     */
    protected abstract void logError(Exception e);

    /**
     * Abstract error logger
     *
     * @param message {@code Message} instance that was the reason of this error
     */
    protected abstract void logError(Message message);

    public Communicator process(Message input) {
        try {

            // On PING command always answer ACK
            if (input.getCommand().equals(Command.PING)) {
                return this.update(Command.ACK, "");
            }

            // Specific Communicator logic
            Communicator communicator = innerProcess(input);

            // To prevent "NAK" command cycling
            if (input.getCommand().equals(Command.NAK) && communicator.message().getCommand().equals(Command.NAK)) {
                return this.update();
            }

            return communicator;
        } catch (Exception e) {
            logError(e);
        }
        return this.error(input);
    }

    /**
     * Setting message for current communicator to send NAK command if error occurs during message processing
     *
     * @param error error details
     * @return current communicator
     */
    protected Communicator error(String error) {
        return update(Command.NAK, Strings.nullToEmpty(error));
    }

    /**
     * Setting message for current communicator to send NAK command if error occurs during message processing
     *
     * @param error {@code Message} that produced an error
     * @return current communicator
     */
    protected Communicator error(Message error) {
        return update(Command.NAK, error.toString());
    }

    /**
     * Sets message for current communicator
     *
     * @param command command to be sent
     * @param message text message to be sent
     * @return current communicator
     */
    abstract protected Communicator update(Command command, String message);

    /**
     * Sets message for current communicator
     * @param message completly ready for send {@code Message} object
     * @return current communicator
     */
    abstract protected Communicator update(Message message);

    /**
     * Sets {@code EmptyMessage} and returns current communicator. Should be used when no further actions are required
     *
     * @return current communicator
     */
    public Communicator update() {
        this.setMessage(new EmptyMessage());
        return this;
    }

    @Override
    public Message message() {
        return this.getMessage();
    }

}
