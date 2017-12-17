package ru.kss.chat.client.communicators;

import ru.kss.chat.Command;
import ru.kss.chat.Handler;
import ru.kss.chat.communicators.Communicator;
import ru.kss.chat.messages.Message;

import static ru.kss.chat.client.ConsolePrinter.*;

/**
 * Client-side communicator implementing user authentication and username selection logic
 */
public class WelcomeClientCommunicator extends ClientCommunicator {

    public WelcomeClientCommunicator(Handler handler) {
        super(handler);
    }

    @Override
    protected Communicator innerProcess(Message message) {

        Command command = message.getCommand();
        String text = message.getText();

        switch (command) {
            case HI: {
                printUsernameRequest();
                this.awaitingUserInput = true;
                return this.update();
            }
            case INPUT: {
                this.awaitingUserInput = false;
                return this.update(Command.NAME, text);
            }
            case WELCOME:
                this.handler.setUsername(text);
                printServerGreeting(text);
                return new ChatClientCommunicator(this.handler);
            case NAK: {
                print(String.format("Sorry, username \"%s\" already used. Choose another one: ", text));
                this.awaitingUserInput = true;
                return this.update();
            }
            default: {
                return this.error(message);
            }
        }

    }

}
