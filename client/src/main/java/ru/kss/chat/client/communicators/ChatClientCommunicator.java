package ru.kss.chat.client.communicators;

import ru.kss.chat.Command;
import ru.kss.chat.client.ChatCommand;
import ru.kss.chat.communicators.Communicator;
import ru.kss.chat.Handler;
import ru.kss.chat.messages.Message;

import static ru.kss.chat.client.ConsolePrinter.*;

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
            case FIN: {
                printMessage(message);
                System.exit(0);
            }
            case INPUT: {
                ChatCommand chatCommand = ChatCommand.parse(text);

                switch (chatCommand) {
                    case COUNT: {
                        return this.update(Command.RPC, chatCommand.name());
                    }
                    case MESSAGE: {
                        return this.update(Command.TXT, text);
                    }
                    case HELP: {
                        printHelp();
                        return this.update();
                    }
                    case QUIT: {
                        return this.update(Command.FIN, "");
                    }
                }
            }
            default: {
                return this.error(message);
            }
        }
    }

}
