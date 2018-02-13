package com.meancat.usefully.messaging.messages;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * a Notification is sent from one side to the other
 * without expecting a response.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Notification {
}
