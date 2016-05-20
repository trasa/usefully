package com.meancat.usefully.messaging.mapping;


import org.springframework.stereotype.Component;

import com.meancat.usefully.messaging.mapping.key.MessageHeaderKeyStrategy;
import com.meancat.usefully.messaging.mapping.key.StaticKeyStrategy;

@Component
public class MessageHandlerMapping extends MessageHandlerMappingBase<MessageHandler> {

    StaticKeyStrategy keyStrategy = new MessageHeaderKeyStrategy();

    @Override
    protected StaticKeyStrategy getKeyStrategy() {
        return keyStrategy;
    }

    @Override
    protected Class<MessageHandler> getHandlerAnnotationClass() {
        return MessageHandler.class;
    }
}
