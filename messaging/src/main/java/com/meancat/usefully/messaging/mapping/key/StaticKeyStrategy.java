package com.meancat.usefully.messaging.mapping.key;

import java.lang.reflect.Method;

/**
 * Determine if this class and method signature matches a known form,
 * and if so return the payload.
 */
public interface StaticKeyStrategy {
    Class<?> determineKey(Class<?> handlerClass, Method method) throws Exception;
}
