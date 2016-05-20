package com.meancat.usefully.messaging.mapping.key;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meancat.usefully.messaging.mapping.MappingException;
import com.meancat.usefully.messaging.messages.MessageHeader;


/**
 * Determine message handling key using socket handler
 *
 *      public void handleSomeCustomThing(MessageHeader header, Session session, [as many anythings here as you want], MyCustomPayload payload);
 *
 * and we want to determine the static mapping based on the type of the payload.
 */
public class SocketKeyStrategy implements StaticKeyStrategy {
    private static final Logger logger = LoggerFactory.getLogger(SocketKeyStrategy.class);

    @Override
    public Class<?> determineKey(Class<?> handlerClass, Method method) throws Exception{
        Class<?>[] paramTypes = method.getParameterTypes();
        if (paramTypes.length < 2) {
            logger.error("This handler method doesn't have enough param methods! {}.{}",
                    handlerClass.getName(),  method.getName());
            throw new MappingException(
                    String.format("This handler method doesn't have correct number of parameters and seems to be broken: %s.%s",
                            handlerClass.getName(),  method.getName()));
        }

        if (!MessageHeader.class.isAssignableFrom(paramTypes[0])) {
            logger.error("This handler is broken! {}.{} does not have something implementing 'MessageHeader' type as first parameter.",
                    handlerClass.getName(), method.getName());
            throw new MappingException(
                    String.format("This handler does not have 'MessageHeader' type as first parameter: %s.%s",
                            handlerClass.getName(),  method.getName()));
        }
        return paramTypes[paramTypes.length - 1];
    }
}
