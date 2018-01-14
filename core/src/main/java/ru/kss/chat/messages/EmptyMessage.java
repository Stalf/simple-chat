package ru.kss.chat.messages;

import ru.kss.chat.commands.Command;

public class EmptyMessage extends Message {
    public EmptyMessage() {
        super(Command.EMPTY, "", "");
    }
}
