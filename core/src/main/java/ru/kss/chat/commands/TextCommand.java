package ru.kss.chat.commands;

import ru.kss.chat.Handler;

/**
 * Most common command for sending text message to chat
 */
public class TextCommand extends ChatCommand {

    @Override
    public Command getRequest() {
        return Command.TXT;
    }

    @Override
    public Command getResponse() {
        return Command.ACK;
    }

    @Override
    public String clientExecute(String input) {
        return input;
    }

    @Override
    public String serverExecute(Handler handler, String input) {
        handler.broadcast(input);
        return "";
    }

    @Override
    public String getInstructions() {
        return "send message";
    }
}
