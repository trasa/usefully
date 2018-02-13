package com.meancat.usefully.messaging.messages;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface Message {
    /**
     * Returns the type of the body (or payload) of this Message.
     * @return Class of body or payload, as appropriate
     */
    @JsonIgnore
    Class<?> getPayloadClass();

    /**
     * Return the correct payload object for this type of message, based on the type of the body.
     *
     * @return payload which might be the body, or body.payload, depending on the type of body this is.
     */
    @JsonIgnore
    Object getPayload();
}
