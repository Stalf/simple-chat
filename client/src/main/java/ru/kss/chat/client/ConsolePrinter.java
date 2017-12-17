package ru.kss.chat.client;

import ru.kss.chat.messages.Message;

import java.util.Arrays;

import static ru.kss.chat.Utils.buildMessage;

/**
 * Helper class for printing out messages to client console
 */
public class ConsolePrinter {

    public static void print(String s) {
        System.out.print(s);
    }

    public static void println(String s) {
        System.out.println(s);
    }

    public static void println() {
        System.out.println("");
    }

    public static void printMessage(Message message) {
        println(buildMessage(message.getText(), message.getAuthor(), message.getTimestamp()));
    }

    public static void printGreeting() {
        println("Hello, friend");
        println("Welcome to the Simple Chat!");
    }

    public static void printServerGreeting(String username) {
        println(String.format("Successfully logged into the chat server as \"%s\"", username));
        printlnHR();
        printHelp();
    }

    public static void printUsernameRequest() {
        printlnHR();
        println("Successfully connected to the chat server!");
        print("Please type your name:  ");
    }

    public static void printMessageInvite() {
        System.out.print("Type your message: ");
    }

    /**
     * Prints star divider string
     */
    public static void printlnHR() {
        println("********************************************************************************************");
    }

    /**
     * Prints string with standard whitespace indent
     */
    public static void printIndented(String s) {
        print(String.format("%-30s", s));
    }

    public static void printHelp() {
        println("While in common chat mode, you should hit Enter to switch to command input mode.");
        println("Then input your message or special command and hit Enter again.");
        println("List of chat commands:");
        printlnHR();
        Arrays.stream(ChatCommand.values()).forEach(command -> {
            printIndented(command.getInput() == null ? "<any other>" : command.getInput());
            println(command.getInstructions());
        });
    }

    public static void printError(Exception e) {
        println(String.format("Sorry, we have got an error with message: %s", e.getMessage()));
    }

}
