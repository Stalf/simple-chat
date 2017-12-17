package ru.kss.chat;

import ru.kss.chat.messages.Message;

public abstract class AbstractHandler implements Handler {

    protected abstract void innerSend(Message message);

    @Override
    public void send(Message message) {
        if (message != null && !message.getCommand().equals(Command.EMPTY)) {
            this.innerSend(message);
        }
    }
}
