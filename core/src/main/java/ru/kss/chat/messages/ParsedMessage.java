package ru.kss.chat.messages;

import ru.kss.chat.commands.Command;

import java.time.ZonedDateTime;

public class ParsedMessage extends Message {
    public ParsedMessage(Command command, String text, String author, ZonedDateTime timestamp) {
        super(command, text, author, timestamp);
    }
}
