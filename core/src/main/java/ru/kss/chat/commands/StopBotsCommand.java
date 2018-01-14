package ru.kss.chat.commands;

import ru.kss.chat.Handler;

/**
 * Command requests server to tear connections with 20 clients
 */
public class StopBotsCommand extends ChatCommand {


    public static final int STOP_COUNT = 20;

    @Override
    public Command getRequest() {
        return Command.STOP_BOTS;
    }

    @Override
    public Command getResponse() {
        return Command.TXT;
    }

    @Override
    public String clientExecute(String input) {
        return String.valueOf(STOP_COUNT);
    }

    @Override
    public String serverExecute(Handler handler, String input) {
        handler.chatService().stopBots(Integer.parseInt(input));
        return "Current user count: " + handler.chatService().getUserCount();
    }

    @Override
    public String getInstructions() {
        return String.format("request server to stop %d clients", STOP_COUNT);
    }
}
