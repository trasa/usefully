package com.meancat.usefully.messaging.mapping;

import java.lang.reflect.Method;

public class HandlerMapping {

    private final Method method;
    private final Object handler;

    public HandlerMapping(Method method, Object handler) {
        this.method = method;
        this.handler = handler;
    }

    public Method getMethod() {
        return method;
    }

    public Object getHandler() {
        return handler;
    }

    @Override
    public String toString() {
        return "HandlerMapping{" +
                "method=" + method +
                ", handler=" + handler +
                '}';
    }
}
