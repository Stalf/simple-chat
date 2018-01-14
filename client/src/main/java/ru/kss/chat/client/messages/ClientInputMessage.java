package ru.kss.chat.client.messages;

import ru.kss.chat.commands.Command;

public class ClientInputMessage extends ClientCommandMessage{
    public ClientInputMessage(String text) {
        super(Command.INPUT, text, "");
    }
}
