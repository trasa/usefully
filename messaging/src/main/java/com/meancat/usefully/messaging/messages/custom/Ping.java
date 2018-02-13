package com.meancat.usefully.messaging.messages.custom;


import com.meancat.usefully.messaging.messages.CustomNotification;
import com.meancat.usefully.messaging.messages.CustomRequest;
import com.meancat.usefully.messaging.messages.CustomResponse;
import com.meancat.usefully.messaging.messages.Response;

@CustomRequest
@CustomResponse
@CustomNotification
public class Ping extends Response {

    public long timestamp;
    public String result;

    public Ping() {}

    public Ping(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Ping " + timestamp;
    }
}