package ru.kss.chat.server.communicators;

import lombok.extern.slf4j.Slf4j;
import ru.kss.chat.Command;
import ru.kss.chat.communicators.Communicator;
import ru.kss.chat.Handler;
import ru.kss.chat.messages.Message;

/**
 * Server-side communicator implementing user authentication and username unique checking logic
 */
@Slf4j
public class WelcomeServerCommunicator extends ServerCommunicator {

    public WelcomeServerCommunicator(Handler handler) {
        super(handler);
        this.update(Command.HI, "");
    }

    @Override
    protected Communicator innerProcess(Message message) {

        Command command = message.getCommand();
        String text = message.getText();

        switch (command) {
            case NAME: {
                try {
                    this.handler.setUsername(text);
                    return this.update(Command.WELCOME, text);
                } catch (IllegalArgumentException e) {
                    return this.error(text);
                }
            }
            case ACK: {
                return new HistoryServerCommunicator(this.handler);
            }
            default: {
                return error(message);
            }
        }

    }

}
