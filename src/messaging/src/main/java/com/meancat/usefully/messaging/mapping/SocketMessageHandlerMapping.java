package com.meancat.usefully.messaging.mapping;

import org.springframework.stereotype.Component;

import com.meancat.usefully.messaging.mapping.key.SocketKeyStrategy;
import com.meancat.usefully.messaging.mapping.key.StaticKeyStrategy;

@Component
public class SocketMessageHandlerMapping extends MessageHandlerMappingBase<SocketMessageHandler> {

    SocketKeyStrategy keyStrategy = new SocketKeyStrategy();

    @Override
    protected StaticKeyStrategy getKeyStrategy() {
        return keyStrategy;
    }

    @Override
    protected Class<SocketMessageHandler> getHandlerAnnotationClass() {
        return SocketMessageHandler.class;
    }
}
