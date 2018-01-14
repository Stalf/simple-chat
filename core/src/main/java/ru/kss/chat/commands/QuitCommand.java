package ru.kss.chat.commands;

import ru.kss.chat.Handler;

/**
 * Command is issued by the client to leave the chat
 */
public class QuitCommand extends ChatCommand {
    @Override
    public Command getRequest() {
        return Command.QUIT;
    }

    @Override
    public String clientExecute(String input) {
        return "";
    }

    @Override
    public Command getResponse() {
        return Command.QUIT;
    }

    @Override
    public String serverExecute(Handler handler, String input) {
        handler.broadcast("*** Leaves chat ***");
        return "Disconnecting from server. Goodbye!";
    }

    @Override
    public String getInstructions() {
        return "exit chat";
    }
}
