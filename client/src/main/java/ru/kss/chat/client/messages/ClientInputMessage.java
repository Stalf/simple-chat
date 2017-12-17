package ru.kss.chat.client.messages;

import ru.kss.chat.Command;

public class ClientInputMessage extends ClientCommandMessage{
    public ClientInputMessage(String text) {
        super(Command.INPUT, text, "");
    }
}
