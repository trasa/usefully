package com.meancat.usefully.messaging.mapping;

import com.google.common.annotations.VisibleForTesting;
import com.meancat.usefully.messaging.mapping.key.StaticKeyStrategy;
import com.meancat.usefully.messaging.messages.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

/**
 * Base functionality for mapping Messages to Handlers
 */
public abstract class MessageHandlerMappingBase<MESSAGE_ANNOTATION extends Annotation> {
    private static final Logger logger = LoggerFactory.getLogger(MessageHandlerMappingBase.class);

    @VisibleForTesting
    @Autowired
    public DefaultListableBeanFactory beanFactory;

    @VisibleForTesting
    protected Map<Class<?>, HandlerMapping> staticHandlerMappings = newHashMap();

    protected abstract StaticKeyStrategy getKeyStrategy();

    protected abstract Class<MESSAGE_ANNOTATION> getHandlerAnnotationClass();


    @PostConstruct
    public void init() throws ClassNotFoundException, MappingException {
        // generate static mappings
        for (String beanName : beanFactory.getBeanDefinitionNames()) {
            BeanDefinition definition = beanFactory.getBeanDefinition(beanName);

            if (StringUtils.hasText(definition.getBeanClassName())) {
                Class<?> beanClass = Class.forName(definition.getBeanClassName());

                for (Method method : beanClass.getMethods()) {
                    MESSAGE_ANNOTATION mapping = AnnotationUtils.findAnnotation(method, getHandlerAnnotationClass());

                    if (mapping != null) {
                        String msgType = null;
                        Class<?>[] paramTypes = method.getParameterTypes();
                        if (paramTypes != null && paramTypes.length > 0) {
                            msgType = paramTypes[paramTypes.length - 1].getSimpleName();
                        }
                        logger.info("Mapping '{}'.'{}'. with type: {}",
                                beanClass.getSimpleName(),
                                method.getName(),
                                msgType);

                        // NOTE: this creates one of each type of bean that we find,
                        // in an attempt to discover problems at startup.  This also
                        // means that prototype beans such as Player might get created and destroyed
                        // many times (once for each messaage handler they've got) which could
                        // look weird in the logs.
                        Object bean = beanFactory.getBean(beanClass);

                        if (bean == null) {
                            throw new MappingException("Handler bean cannot be null: " + beanClass);
                        }

                        Class<?> mappingKey = determineHandlerMappingKey(beanClass, method);
                        staticHandlerMappings.put(mappingKey, new HandlerMapping(method, bean));
                    }
                }
            }
        }
    }



    /**
     * static handler mappings come in three types:
     *      public void handleSomeCmrsThing(MessageHeader header, GetCustomGameServerIdRequest request);
     * or
     *  (deprecated form...)
     *      public void handleSomeCustomThing(MessageHeader header, CustomGameServerRequest request, MyCustomPayload payload);
     * or
     *      public void handleSomeCustomThing(MessageHeader header, Object anything, MyCustomPayload payload);
     * or
     *      public void handleSomeCustomThing(MessageHeader header, MyCustomPayload payload);
     *
     * and we want to determine the static mapping based on the type of the payload.
     *
     * @param method a WebServiceMapping-annotated method.
     * @return correct payload mapping key
     */
    public Class<?> determineHandlerMappingKey(Class<?> handlerClass, Method method) throws MappingException {
        try {
            return getKeyStrategy().determineKey(handlerClass, method);
        } catch (Exception e) {
            throw new MappingException(e);
        }
    }

    public HandlerMapping getStaticMapping(Message message) {
        return staticHandlerMappings.get(message.getPayloadClass());
    }
}
