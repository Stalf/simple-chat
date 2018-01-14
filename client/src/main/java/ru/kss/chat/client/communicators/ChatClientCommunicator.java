package ru.kss.chat.client.communicators;

import ru.kss.chat.commands.ChatCommand;
import ru.kss.chat.commands.Command;
import ru.kss.chat.Handler;
import ru.kss.chat.communicators.Communicator;
import ru.kss.chat.messages.Message;

import static ru.kss.chat.ConsolePrinter.printMessage;

/**
 * Communicator implementing main chat behavior for client side
 */
public class ChatClientCommunicator extends ClientCommunicator {

    public ChatClientCommunicator(Handler handler) {
        super(handler);
        this.update(Command.ACK, handler.getUsername());
    }

    @Override
    protected Communicator innerProcess(Message message) {

        Command command = message.getCommand();
        String text = message.getText();

        switch (command) {
            case TXT: {
                printMessage(message);
                return this.update(Command.ACK, "");
            }
            case ACK:
                return this.update();
            case NAK: {
                logError(message);
                return this.update();
            }
            case QUIT: {
                printMessage(message);
                System.exit(0);
            }
            case INPUT: {
                // User input commands
                return this.update(ChatCommand.input(text), text);
            }
            default: {
                return this.error(message);
            }
        }
    }

}
