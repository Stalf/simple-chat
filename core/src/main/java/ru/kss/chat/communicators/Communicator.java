package ru.kss.chat.communicators;

import ru.kss.chat.messages.Message;

/**
 * Root Communicator interface for implementing server- and client-side business-logic
 */
public interface Communicator {

    /**
     * Common logic for handling data received from other communication participant
     * @param input raw message received over network
     * @return Communicator instance responsible for further actions
     */
    Communicator process(Message input);

    /**
     * @return {@code Message} prepared for sending
     */
    Message message();
}
