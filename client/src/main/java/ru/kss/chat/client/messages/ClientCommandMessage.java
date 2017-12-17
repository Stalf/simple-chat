package ru.kss.chat.client.messages;

import ru.kss.chat.Command;
import ru.kss.chat.messages.Message;

public class ClientCommandMessage extends Message {
    public ClientCommandMessage(Command command, String text, String author) {
        super(command, text, author);
    }
}
