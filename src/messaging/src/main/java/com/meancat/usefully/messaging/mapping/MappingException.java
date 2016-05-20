package com.meancat.usefully.messaging.mapping;

public class MappingException  extends Exception {
    private static final long serialVersionUID = 7744015691078230570L;

    public MappingException() {
    }

    public MappingException(String message) {
        super(message);
    }

    public MappingException(String message, Throwable cause) {
        super(message, cause);
    }

    public MappingException(Throwable cause) {
        super(cause);
    }
}
