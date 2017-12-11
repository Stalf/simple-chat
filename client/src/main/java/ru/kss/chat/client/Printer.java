package ru.kss.chat.client;

import java.util.Arrays;

public class Printer {

    public static void print(String s) {
        System.out.print(s);
    }

    public static void println(String s) {
        System.out.println(s);
    }

    public static void printGreeting() {
        println("Hello, friend");
        println("Welcome to the Simple Chat");
    }

    public static void printServerGreeting() {
        println("Successfully logged into the chat server");
        println("Type your message and hit Enter");
        println("For a complete list of commands type /help");
        println("Type /q to leave the chat");
    }

    public static void printUsername(String username) {
        System.out.print(username);
        System.out.print(">>>");
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
        println("List of chat commands:");
        printlnHR();
        Arrays.stream(Command.values()).forEach(command -> {
            printIndented(command.getInput() == null ? "<any other>" : command.getInput());
            println(command.getInstructions());
        });
    }

}
