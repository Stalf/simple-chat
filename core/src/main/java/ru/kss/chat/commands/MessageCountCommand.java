package ru.kss.chat.commands;

import ru.kss.chat.Handler;

public class MessageCountCommand extends ChatCommand {
    @Override
    public Command getRequest() {
        return Command.MESSAGE_COUNT;
    }

    @Override
    public Command getResponse() {
        return Command.TXT;
    }

    @Override
    public String clientExecute(String input) {
        return "";
    }

    @Override
    public String serverExecute(Handler handler, String input) {
        return "Current message count: " + handler.chatService().getMessageCount();
    }

    @Override
    public String getInstructions() {
        return "get message count";
    }
}
