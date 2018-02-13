package com.meancat.usefully.messaging.messages;

public interface MessageHeader {
    /**
     * The first message in a "conversation" defines the conversation ID.  Subsequent messages--responses and responses-to-
     * responses--carry the same conversation ID, correlating the conversation.  Typically the conversation ID is just the
     * message ID of the initiating message.
     * @return the conversation ID of the message stream. Should not ever be null.
     */
    String getConversationId();
}
