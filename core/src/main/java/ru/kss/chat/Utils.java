package ru.kss.chat;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import ru.kss.chat.messages.EmptyMessage;
import ru.kss.chat.messages.Message;
import ru.kss.chat.messages.ParsedMessage;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Iterator;

/**
 * Helper methods and constants
 */
public class Utils {

    /**
     * Default server address
     */
    public static final String DEFAULT_SERVER_ADDRESS = "localhost";
    /**
     * Default chat server name
     */
    public static final String SERVER_NAME = "SERVER";
    /**
     * Char used as a delimiter between Message fields for serialization/deserialization
     */
    public static final char SIMPLE_DELIMITER = 1;
    /**
     * Default communication port number
     */
    public static int DEFAULT_PORT_NUMBER = 5000;


    private static final Splitter COMMAND_SPLITTER = Splitter.on(SIMPLE_DELIMITER);

    public static String buildMessage(String text, String author, ZonedDateTime timestamp) {
        LocalDateTime localDateTime = timestamp.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
        return localDateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)) +
            " " + author+ " :>> " + text;
    }

    /**
     * Composes string from provided {@code Message} instance
     */
    public static String composeMessage(Message message) {
        return message.getCommand().toString() + SIMPLE_DELIMITER +
            message.getText() + SIMPLE_DELIMITER +
            message.getAuthor() + SIMPLE_DELIMITER +
            message.getTimestamp().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    /**
     * Parses input string to compose Message object. Fills with default values on exceptions
     * @return {@code ParsedMessage} instance
     */
    public static Message parseMessage(String input) {
        if (Strings.isNullOrEmpty(input)) {
            return new EmptyMessage();
        }

        Command command = Command.EMPTY;
        Iterator<String> iterator = COMMAND_SPLITTER.split(input).iterator();
        try {
            command = Command.valueOf(iterator.next());
        } catch (Exception e) {
            // Command.EMPTY in case of exception
        }

        String text = "";
        try {
            text = iterator.next();
        } catch (Exception e) {
            // Empty string in case of exception
        }

        String author = "";
        try {
            author = iterator.next();
        } catch (Exception e) {
            // Empty string in case of exception
        }

        ZonedDateTime timestamp = ZonedDateTime.now();
        try {
            timestamp = ZonedDateTime.parse(iterator.next(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        } catch (Exception e) {
            // Now() timestamp in case of exception
        }

        return new ParsedMessage(command, text, author, timestamp);
    }


}
