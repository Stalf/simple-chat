package ru.kss.chat.commands;

import ru.kss.chat.Handler;

import java.text.DecimalFormat;

public class MessageCountCommand extends ChatCommand {

    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");

    @Override
    public Command getRequest() {
        return Command.MESSAGE_COUNT;
    }

    @Override
    public Command getResponse() {
        return Command.TXT;
    }

    @Override
    public String clientExecute(String input) {
        return "";
    }

    @Override
    public String serverExecute(Handler handler, String input) {
        return String.format("Current message count: %d; Pending message queue size: %d; load: %s/sec",
            handler.chatService().getMessageCount(),
            handler.chatService().storage().pendingMessagesQueue().size(),
            DECIMAL_FORMAT.format(handler.chatService().getMessageCount() / (float) handler.chatService().uptime()));
    }

    @Override
    public String getInstructions() {
        return "get message count";
    }
}
