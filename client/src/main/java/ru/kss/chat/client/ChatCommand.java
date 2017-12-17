package ru.kss.chat.client;

import java.util.Arrays;

/**
 * List of client-side commands
 * Defined as a command string and a help info string
 */
public enum ChatCommand {

    HELP("/help", "show this page"),
    COUNT("/count", "get client count"),
    QUIT("/q", "exit chat"),
    MESSAGE(null, "send message");

    private final String input;
    private final String instructions;

    ChatCommand(String input, String instructions) {
        this.input = input;
        this.instructions = instructions;
    }

    public String getInput() {
        return input;
    }

    public String getInstructions() {
        return instructions;
    }

    /**
     * Parses command from chat. Returns default {@code MESSAGE} if no command is found.
     *
     * @param inputString input string
     * @return {@code Command} corresponding received input string
     */
    public static ChatCommand parse(String inputString) {
        return Arrays.stream(ChatCommand.values()).filter(command -> inputString.equals(command.getInput())).findFirst().orElse(MESSAGE);
    }
}
