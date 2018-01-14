package ru.kss.chat.commands;

import ru.kss.chat.Handler;

/**
 * Command requests server for current connected users count
 */
public class UserCountCommand extends ChatCommand {
    @Override
    public Command getRequest() {
        return Command.USER_COUNT;
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
        return "Current user count: " + handler.chatService().getUserCount();
    }

    @Override
    public String getInstructions() {
        return "get client count";
    }
}
