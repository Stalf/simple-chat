package ru.kss.chat.commands;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import ru.kss.chat.Handler;

import java.util.Arrays;
import java.util.Map;

/**
 * User input commands are handled by subclasses of ChatCommand
 */
public abstract class ChatCommand {
    /**
     * Holder object for all registered user-input-based commands
     */
    public static Map<Command, ChatCommand> registry = Maps.newHashMap();

    /**
     * Here we register all available commands
     */
    public static void registerAllCommands() {
        registerCommand(new UserCountCommand());
        registerCommand(new MessageCountCommand());
        registerCommand(new StopBotsCommand());
        registerCommand(new ServerUptimeCommand());
        registerCommand(new HelpCommand());
        registerCommand(new QuitCommand());
        registerCommand(new TextCommand());
    }

    /**
     * Register instance implementing user command
     */
    private static void registerCommand(ChatCommand command) {
        registry.put(command.getRequest(), command);
    }

    /**
     * @return ChatCommand associated with provided command code
     */
    public static ChatCommand input(Command command) {
        return registry.get(command);
    }

    /**
     * @return parse user input and find the appropriate ChatCommand instance. If none is found - returns {@code Command.TXT} as default
     */
    public static ChatCommand input(String input) {
        ChatCommand result = registry.get(Command.TXT);

        if (!Strings.isNullOrEmpty(input) && input.startsWith("/")) {
            input = input.substring(1).toUpperCase().trim();
            final String finalInput = input;
            Command inputCommand = Arrays.stream(Command.values()).filter(command -> finalInput.equals(command.name())).findFirst().orElse(Command.TXT);
            result = registry.get(inputCommand);
        }

        return result;
    }

    /**
     * @return {@code Command} value to be sent to server
     */
    public abstract Command getRequest();

    /**
     * @return {@code Command} value expected to be returned from server
     */
    public abstract Command getResponse();

    /**
     * Method is executed on client side when the command is issued by the user
     *
     * @param input user input string
     * @return string data to be sent to server
     */
    public abstract String clientExecute(String input);

    /**
     * This method will be executed on server side after the command is received from client
     *
     * @param handler user connection handler object
     * @param input   string data received from the client
     * @return string data to be sent back to client
     */
    public abstract String serverExecute(Handler handler, String input);

    /**
     * @return returns instructions about using this command to be printed to user
     */
    public abstract String getInstructions();


}
