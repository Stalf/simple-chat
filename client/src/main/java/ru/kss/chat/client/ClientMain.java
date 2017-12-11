package ru.kss.chat.client;

import lombok.extern.slf4j.Slf4j;
import ru.kss.chat.Utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;
import java.util.Scanner;

import static ru.kss.chat.client.Printer.*;

public class ClientMain {

    public static void main(String... args) throws InterruptedException, IOException {
        printGreeting();

        print("Input in your name:");

        Scanner input = new Scanner(System.in);
        String username = input.nextLine();

        println("Logging in...");

        try (CommandExecutor commandExecutor = new CommandExecutor()) {
            printServerGreeting();

            Command command;
            do {
                printUsername(username);
                String inputString = input.nextLine();

                commandExecutor.execute(inputString);

                // infinite loop, System.exit(0) is called on /q command
            } while (true);
        }
    }


}
