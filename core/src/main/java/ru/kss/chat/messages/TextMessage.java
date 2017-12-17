package ru.kss.chat.messages;

import ru.kss.chat.Command;

/**
 * Simple Chat text message
 */
public class TextMessage extends Message {

    public TextMessage(String text, String author) {
        super(Command.TXT, text, author);
    }
}
