package com.meancat.usefully.messaging.messages.custom;


import com.meancat.usefully.messaging.messages.CustomNotification;

/**
 * Sent when the CGS is ready to process messages for a client.
 *
 * Note that it is NOT ok to send messages to the CGS before the client has
 * received this message!
 */
@CustomNotification
public class ReadyPlayerNotification {
    public String status;
    public String gameId;
    public String serviceHostName;
    public String serviceVersion;
    public String environment;

    public ReadyPlayerNotification() {
    }

    public ReadyPlayerNotification(String status, String gameId, String serviceHostName, String serviceVersion, String environment) {
        this.status = status;
        this.gameId = gameId;
        this.serviceHostName = serviceHostName;
        this.serviceVersion = serviceVersion;
        this.environment = environment;
    }

    @Override
    public String toString() {
        return "ReadyPlayerNotification{" +
                "status='" + status + '\'' +
                ", gameId='" + gameId + '\'' +
                ", serviceHostName='" + serviceHostName + '\'' +
                ", serviceVersion='" + serviceVersion + '\'' +
                ", environment='" + environment + '\'' +
                '}';
    }
}
