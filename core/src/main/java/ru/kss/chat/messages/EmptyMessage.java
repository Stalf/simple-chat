package ru.kss.chat.messages;

import ru.kss.chat.Command;

public class EmptyMessage extends Message {
    public EmptyMessage() {
        super(Command.EMPTY, "", "");
    }
}
