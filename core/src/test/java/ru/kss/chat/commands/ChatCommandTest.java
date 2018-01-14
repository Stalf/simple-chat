package ru.kss.chat.commands;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ChatCommandTest {

    @Before
    public void setUp() {
        ChatCommand.registerAllCommands();
    }

    @Test
    public void input() {

        assertNotNull(ChatCommand.input("/quit"));
        assertEquals(QuitCommand.class, ChatCommand.input("/quit").getClass());
        assertNotNull(ChatCommand.input("/QuIt"));
        assertEquals(QuitCommand.class, ChatCommand.input("/QuIt").getClass());

        assertNotNull(ChatCommand.input("/user_count"));
        assertEquals(UserCountCommand.class, ChatCommand.input("/user_count").getClass());

        assertNotNull(ChatCommand.input("test test"));
        assertEquals(TextCommand.class, ChatCommand.input("test test").getClass());
        assertNotNull(ChatCommand.input(""));
        assertEquals(TextCommand.class, ChatCommand.input("").getClass());
        String s = null;
        assertNotNull(ChatCommand.input(s));
        assertEquals(TextCommand.class, ChatCommand.input(s).getClass());


    }

}
