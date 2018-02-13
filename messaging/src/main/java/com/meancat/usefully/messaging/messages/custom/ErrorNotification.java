package com.meancat.usefully.messaging.messages.custom;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.meancat.usefully.messaging.messages.CustomNotification;
import com.meancat.usefully.messaging.messages.Message;

@CustomNotification
public class ErrorNotification {

    public Result result;
    public String description;

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY)
    public Message originatingMessage;

    public ErrorNotification() {
    }

    public ErrorNotification(Result result, Exception e) {
        this(result, e.toString());
    }

    public ErrorNotification(Result result, Message originatingMessage, Exception e) {
        this(result, e.toString());
        this.originatingMessage = originatingMessage;
    }

    public ErrorNotification(Result result, String description) {
        this.result = result;
        this.description = description;
    }

    @Override
    public String toString() {
        return "ErrorNotification{" +
                "result=" + result +
                ", description='" + description + '\'' +
                ", originatingMessage=" + originatingMessage +
                '}';
    }

    // TODO this needs to be moved out to be game-specific,
    // have some way to for the implementation to use either these
    // or to add their own string messages...
    public enum Result {
        VALIDATION_FAILED,
        FAILED_TO_INVOKE_HANDLER,
        HANDLER_THREW_EXCEPTION,
        HANDLER_NOT_FOUND,
        DESERIALIZE_FAILED,
        CATASTROPHIC_FAILURE,
        PREHANDLER_EXCEPTION,
        CONTEXT_CORRUPTED,
        DATA_VERSION_INVALID,
        SLICE_NOT_FOUND,
        OK
    }
}
