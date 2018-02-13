package com.meancat.usefully.messaging.messages;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A Response message to a Request
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Response {
    public boolean successful = true;
    @SuppressWarnings("UnusedDeclaration")
    public String description = "";

    // you're on your own to implement this:
    // public Result result = Result.OK;
    // public enum Result { OK }
}
