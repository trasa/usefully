package com.meancat.usefully.messaging.mapping;


import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;

import com.meancat.usefully.messaging.messages.Message;

/**
 * A Mapping of all Handlers that are in TARGET_CLASS and are notated by MESSAGE_ANNOTATION
 */
public abstract class ClassMessageHandlerMapping<TARGET_CLASS, MESSAGE_ANNOTATION extends Annotation> {
    private static final Logger logger = LoggerFactory.getLogger(ClassMessageHandlerMapping.class);

    protected Map<Class<?>, Method> classMappings = new HashMap<>();

    protected abstract Class<TARGET_CLASS> getTargetClass();
    protected abstract Class<MESSAGE_ANNOTATION> getHandlerAnnotationClass();


    protected ClassMessageHandlerMapping() {
        for (Method method : getTargetClass().getMethods()) {
            MESSAGE_ANNOTATION mapping = AnnotationUtils.findAnnotation(method, getHandlerAnnotationClass());
            if (mapping != null) {
                Class<?>[] paramTypes = method.getParameterTypes();
                if (paramTypes != null && paramTypes.length > 0) {
                    Class<?> mappingKey = paramTypes[paramTypes.length - 1];
                    String msgType = mappingKey.getSimpleName();
                    logger.debug("Mapping '{}'.'{}' with type {}",
                            getTargetClass().getSimpleName(),
                            method.getName(),
                            msgType);
                    classMappings.put(mappingKey, method);
                }
            }
        }
    }


    public Method getMapping(Message message) {
        return classMappings.get(message.getPayloadClass());
    }
}
