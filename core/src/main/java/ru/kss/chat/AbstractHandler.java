package ru.kss.chat;

import ru.kss.chat.commands.Command;
import ru.kss.chat.messages.Message;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public abstract class AbstractHandler implements Handler {

    private Instant start;

    public AbstractHandler() {
        start = Instant.now();
    }

    protected abstract void innerSend(Message message);

    @Override
    public void send(Message message) {
        if (message != null && !message.getCommand().equals(Command.EMPTY)) {
            this.innerSend(message);
        }
    }

    @Override
    public long uptime() {
        return start.until(Instant.now(), ChronoUnit.SECONDS);
    }
}
