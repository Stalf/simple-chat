package ru.kss.chat.commands;

import ru.kss.chat.Handler;

/**
 * Command is issued by the client to leave the chat
 */
public class QuitCommand extends ChatCommand {

    public static final String DISCONNECTING_FROM_SERVER_GOODBYE = "Disconnecting from server. Goodbye!";

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
        Thread.currentThread().interrupt();
        return DISCONNECTING_FROM_SERVER_GOODBYE;
    }

    @Override
    public String getInstructions() {
        return "exit chat";
    }
}
