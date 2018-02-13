package com.meancat.usefully.jersey;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meancat.usefully.messaging.messages.Request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
public class RequestBodyConverter extends BodyConverter<Request> {
    private static final Logger logger = LoggerFactory.getLogger(RequestBodyConverter.class);


    public RequestBodyConverter(ObjectMapper mapper, MetricRegistry metricRegistry) {
        super(mapper, metricRegistry);
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        logger.debug("is writeable: {} - {} - {}", mediaType, type, genericType);
        return mediaType.equals(MediaType.APPLICATION_JSON_TYPE);
    }


    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        logger.debug("is readable: {} - {} - {}", mediaType, type, genericType);
        return mediaType.equals(MediaType.APPLICATION_JSON_TYPE);
    }

}