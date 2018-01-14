package ru.kss.chat.commands;

import ru.kss.chat.Handler;

import static ru.kss.chat.ConsolePrinter.printHelp;

/**
 * Command prints help information to client console
 */
public class HelpCommand extends ChatCommand {

    @Override
    public Command getRequest() {
        return Command.HELP;
    }

    @Override
    public Command getResponse() {
        return Command.EMPTY;
    }

    @Override
    public String clientExecute(String input) {
        printHelp();
        return null;
    }

    @Override
    public String serverExecute(Handler handler, String input) {
        return null;
    }

    @Override
    public String getInstructions() {
        return "show this page";
    }
}
