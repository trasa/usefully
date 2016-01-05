package com.meancat.usefully.messaging;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A Request. Expects a Response in return.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Request {

}
