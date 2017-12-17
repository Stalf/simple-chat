package ru.kss.chat.server.messages;

import ru.kss.chat.messages.TextMessage;

import static ru.kss.chat.Utils.SERVER_NAME;

/**
 * Chat text message from server
 */
public class ServerTextMessage extends TextMessage {
    public ServerTextMessage(String text) {
        super(text, SERVER_NAME);
    }
}
