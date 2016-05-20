package com.meancat.usefully.messaging.mapping;


import com.meancat.usefully.messaging.messages.Message;

public class TestMessageImpl implements Message {

    public Object body;

    public TestMessageImpl(Object body) {
        this.body = body;
    }

    @Override
    public Class<?> getPayloadClass() {
        return body == null ? null : body.getClass();
    }

    @Override
    public Object getPayload() {
        return body;
    }
}
