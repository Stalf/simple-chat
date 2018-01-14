package ru.kss.chat.server.communicators;

import lombok.extern.slf4j.Slf4j;
import ru.kss.chat.Command;
import ru.kss.chat.communicators.Communicator;
import ru.kss.chat.Handler;
import ru.kss.chat.messages.Message;
import ru.kss.chat.server.messages.ServerTextMessage;

/**
 * Communicator implementing main chat behavior for server side
 */
@Slf4j
public class ChatServerCommunicator extends ServerCommunicator {

    public ChatServerCommunicator(Handler handler) {
        super(handler);
        handler.broadcast("*** Enters chat ***");
        this.update();
    }

    @Override
    protected Communicator innerProcess(Message message) {

        Command command = message.getCommand();
        String text = message.getText();

        switch (command) {
            case USER_COUNT: {
                return this.update(new ServerTextMessage("Current user count: " + handler.chatService().getUserCount()));
            }
            case MESSAGE_COUNT: {
                return this.update(new ServerTextMessage("Current message count: " + handler.chatService().getMessageCount()));
            }
            case TXT: {
                handler.broadcast(text);
                return this.update(Command.ACK, "");
            }
            case FIN: {
                log.debug("Quit command received from client {}", this.handler.getUsername());
                handler.broadcast("*** Leaves chat ***");
                return new FinCommunicator();
            }
            case ACK: {
                return this.update();
            }
            default: {
                return this.error(message);
            }
        }
    }

}
