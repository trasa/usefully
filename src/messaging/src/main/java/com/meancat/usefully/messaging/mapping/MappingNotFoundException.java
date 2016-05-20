package com.meancat.usefully.messaging.mapping;

/**
 * Mapping could not be found for this message.
 */
public class MappingNotFoundException extends MappingException {
    private static final long serialVersionUID = -2751178858359526774L;

    public MappingNotFoundException() {
    }

    public MappingNotFoundException(String message) {
        super(message);
    }

    public MappingNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public MappingNotFoundException(Throwable cause) {
        super(cause);
    }
}
