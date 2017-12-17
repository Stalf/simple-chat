package ru.kss.chat.server.messages;

import ru.kss.chat.Command;
import ru.kss.chat.messages.Message;

import static ru.kss.chat.Utils.SERVER_NAME;

public class ServerCommandMessage extends Message {
    public ServerCommandMessage(Command command, String text) {
        super(command, text, SERVER_NAME);
    }
}
