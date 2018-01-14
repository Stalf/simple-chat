package ru.kss.chat.messages;

import lombok.Getter;
import lombok.ToString;
import ru.kss.chat.commands.Command;

import java.time.ZonedDateTime;

/**
 * Immutable message object
 */
@Getter
@ToString
public abstract class Message {

    private Command command;
    private ZonedDateTime timestamp;
    private String author;
    private String text;

    public Message(Command command, String text, String author) {
        this.command = command;
        this.text = text;
        this.author = author;
        this.timestamp = ZonedDateTime.now();
    }

    public Message(Command command, String text, String author, ZonedDateTime timestamp) {
        this.command = command;
        this.text = text;
        this.author = author;
        this.timestamp = timestamp;
    }
}

