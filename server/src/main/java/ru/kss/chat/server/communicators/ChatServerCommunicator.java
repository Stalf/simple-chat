package ru.kss.chat.server.communicators;

import lombok.extern.slf4j.Slf4j;
import ru.kss.chat.Handler;
import ru.kss.chat.commands.ChatCommand;
import ru.kss.chat.communicators.Communicator;
import ru.kss.chat.messages.Message;

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

        ChatCommand chatCommand = ChatCommand.input(message.getCommand());
        if (chatCommand != null ) {
            return this.update(chatCommand, message.getText());
        } else {
            return this.update();
        }

    }

}
