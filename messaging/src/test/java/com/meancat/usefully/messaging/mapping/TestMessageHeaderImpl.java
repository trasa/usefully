package com.meancat.usefully.messaging.mapping;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.meancat.usefully.messaging.messages.MessageHeader;

public class TestMessageHeaderImpl implements MessageHeader {

    @JsonIgnore
    @Override
    public String getConversationId() { return ""; }
}
