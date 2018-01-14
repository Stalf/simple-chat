package ru.kss.chat.server;

import org.junit.Before;
import org.junit.Test;
import ru.kss.chat.Broadcaster;
import ru.kss.chat.ConnectionPool;
import ru.kss.chat.Handler;
import ru.kss.chat.Storage;
import ru.kss.chat.messages.EmptyMessage;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SimpleChatServiceTest {

    private SimpleChatService chatService;
    private Handler user1;
    private Handler user2;
    private Handler userNullName;
    private Handler userEmptyName;
    private Storage storage;
    private ConnectionPool connectionPool;

    @Before
    public void setUp() {
        storage = mock(Storage.class);
        connectionPool = mock(ConnectionPool.class);
        chatService = new SimpleChatService(storage, connectionPool, 0);

        user1 = mock(Handler.class);
        when(user1.getUsername()).thenReturn("user1");
        user2 = mock(Handler.class);
        when(user2.getUsername()).thenReturn("user2");
        userNullName = mock(Handler.class);
        when(userNullName.getUsername()).thenReturn(null);
        userEmptyName = mock(Handler.class);
        when(userEmptyName.getUsername()).thenReturn("");
    }

    @Test
    public void registerUser() {
        chatService.register(user1);
        assertEquals(1, chatService.getUsers().size());
        chatService.register(user2);
        assertEquals(2, chatService.getUsers().size());
    }

    @Test(expected = NullPointerException.class)
    public void registerUserNullUsernameFail() {
        chatService.register(userNullName);
    }

    @Test
    public void registerUserEmptyUsername() {
        chatService.register(userEmptyName);
        assertEquals(1, chatService.getUsers().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void registerUserDuplicateFail() {
        chatService.register(user1);
        assertEquals(1, chatService.getUsers().size());
        chatService.register(user1);
    }

    @Test
    public void unRegisterUser() {
        chatService.register(user1);
        chatService.register(user2);
        assertEquals(2, chatService.getUsers().size());
        chatService.unRegister(user1);
        assertEquals(1, chatService.getUsers().size());
    }

    @Test
    public void unRegisterUserDuplicateCall() {
        chatService.register(user1);
        chatService.register(user2);
        assertEquals(2, chatService.getUsers().size());
        chatService.unRegister(user1);
        assertEquals(1, chatService.getUsers().size());
        chatService.unRegister(user1);
        assertEquals(1, chatService.getUsers().size());
    }

    @Test
    public void unRegisterUserNullOrEmptyName() {
        chatService.register(user1);
        assertEquals(1, chatService.getUsers().size());
        chatService.unRegister(userNullName);
        chatService.unRegister(userEmptyName);
        assertEquals(1, chatService.getUsers().size());
    }

    @Test
    public void getUserCount() {
        assertEquals(0, chatService.getUserCount());

        chatService.register(user1);
        chatService.register(user2);
        assertEquals(2, chatService.getUserCount());

        chatService.unRegister(user1);
        assertEquals(1, chatService.getUserCount());
    }

    @Test
    public void checkUsernameExists() {
        chatService.register(user2);
        assertTrue(chatService.checkUsernameExists(user2.getUsername()));

        assertFalse(chatService.checkUsernameExists(user1.getUsername()));
        chatService.register(user1);
        assertTrue(chatService.checkUsernameExists(user1.getUsername()));
        chatService.unRegister(user1);
        assertFalse(chatService.checkUsernameExists(user1.getUsername()));
    }

    @Test
    public void storage() {
        assertEquals(chatService.storage(), storage);
    }

    @Test
    public void broadcast() {
        EmptyMessage message = new EmptyMessage();
        chatService.broadcast(message);
        verify(storage).save(message);
    }

    @Test
    public void getMessageCount() {
        when(storage.getMessageCount()).thenReturn(5);

        assertEquals(5, chatService.getMessageCount());
    }

    @Test
    public void registerUnregisterBroadcaster() {
        Broadcaster broadcaster = mock(Broadcaster.class);
        Broadcaster broadcaster2 = mock(Broadcaster.class);
        chatService.register(broadcaster);
        assertEquals(1, chatService.getBroadcasterThreads().size());

        chatService.register(broadcaster2);
        assertEquals(2, chatService.getBroadcasterThreads().size());

        chatService.unRegister(broadcaster2);
        assertEquals(1, chatService.getBroadcasterThreads().size());
        chatService.unRegister(broadcaster);
        assertEquals(0, chatService.getBroadcasterThreads().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void registerDuplicateBroadcasterFail() {
        Broadcaster broadcaster = mock(Broadcaster.class);
        Broadcaster broadcaster2 = mock(Broadcaster.class);
        chatService.register(broadcaster);
        chatService.register(broadcaster2);
        assertEquals(2, chatService.getBroadcasterThreads().size());

        chatService.register(broadcaster);
    }

    @Test
    public void unRegisterDuplicateBroadcaster() {
        Broadcaster broadcaster = mock(Broadcaster.class);
        Broadcaster broadcaster2 = mock(Broadcaster.class);
        chatService.register(broadcaster);
        chatService.register(broadcaster2);
        assertEquals(2, chatService.getBroadcasterThreads().size());

        chatService.unRegister(broadcaster);
        assertEquals(1, chatService.getBroadcasterThreads().size());
        chatService.unRegister(broadcaster);
        assertEquals(1, chatService.getBroadcasterThreads().size());
    }

}

