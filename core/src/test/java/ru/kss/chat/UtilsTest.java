package ru.kss.chat;

import org.junit.Test;
import ru.kss.chat.commands.Command;
import ru.kss.chat.messages.Message;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class UtilsTest {

    private static final String INPUT = "TXT" + Utils.SIMPLE_DELIMITER
        + "text" + Utils.SIMPLE_DELIMITER
        + "author" + Utils.SIMPLE_DELIMITER
        + "2010-01-14T20:31:40.854+05:00";

    private static final String INPUT_TIME_CORRUPTED = "TXT" + Utils.SIMPLE_DELIMITER
        + "text" + Utils.SIMPLE_DELIMITER
        + "author" + Utils.SIMPLE_DELIMITER
        + "2010-01-14Z20:31:40.854+05:00";

    private static final String INPUT_COMMAND_CORRUPTED = "testtest" + Utils.SIMPLE_DELIMITER
        + "text" + Utils.SIMPLE_DELIMITER
        + "author" + Utils.SIMPLE_DELIMITER
        + "2010-01-14Z20:31:40.854+05:00";

    private static final String INPUT_NO_TEXT_CORRUPTED = "TXT" + Utils.SIMPLE_DELIMITER
//        + "text" + Utils.SIMPLE_DELIMITER
        + "author" + Utils.SIMPLE_DELIMITER
        + "2010-01-14Z20:31:40.854+05:00";

    @Test
    public void parseMessage() {

        Message message = Utils.parseMessage(INPUT);
        assertEquals(Command.TXT, message.getCommand());
        assertEquals("text", message.getText());
        assertEquals("author", message.getAuthor());
        assertEquals(2010, message.getTimestamp().getYear());
        assertEquals(1, message.getTimestamp().getMonthValue());
        assertEquals(14, message.getTimestamp().getDayOfMonth());
        assertEquals(20, message.getTimestamp().getHour());
        assertEquals(31, message.getTimestamp().getMinute());
        assertEquals(40, message.getTimestamp().getSecond());
    }

    @Test
    public void parseMessageDefaultDateTimeValue() {

        Message message = Utils.parseMessage(INPUT_TIME_CORRUPTED);
        assertEquals(Command.TXT, message.getCommand());
        assertEquals("text", message.getText());
        assertEquals("author", message.getAuthor());
        assertNotEquals(2010, message.getTimestamp().getYear());
    }

    @Test
    public void parseMessageDefaultCommand() {

        Message message = Utils.parseMessage(INPUT_COMMAND_CORRUPTED);
        assertEquals(Command.EMPTY, message.getCommand());
        assertEquals("text", message.getText());
        assertEquals("author", message.getAuthor());
        assertNotEquals(2010, message.getTimestamp().getYear());
    }

    @Test
    public void parseMessageAuthorCorrupted() {

        Message message = Utils.parseMessage(INPUT_NO_TEXT_CORRUPTED);
        assertEquals(Command.TXT, message.getCommand());
        assertNotEquals("text", message.getText());
        assertEquals("author", message.getText());
        assertNotEquals("author", message.getAuthor());
        assertNotEquals(2010, message.getTimestamp().getYear());
    }
}
