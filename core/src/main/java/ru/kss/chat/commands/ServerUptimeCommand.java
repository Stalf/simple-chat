package ru.kss.chat.commands;

import ru.kss.chat.Handler;

/**
 * Command prints server uptime information to client console
 */
public class ServerUptimeCommand extends ChatCommand {

    @Override
    public Command getRequest() {
        return Command.SERVER_UPTIME;
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
        return String.format("Server uptime: %d seconds; current client uptime: %d seconds", handler.chatService().uptime(), handler.uptime());
    }

    @Override
    public String getInstructions() {
        return "show server uptime";
    }
}
