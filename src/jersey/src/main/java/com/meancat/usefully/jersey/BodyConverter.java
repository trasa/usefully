package com.meancat.usefully.jersey;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import static com.codahale.metrics.MetricRegistry.name;

/**
 * MessageBodyWriter and Reader for type T, using jackson ObjectMapper.
 *
 * @param <T>
 */
public abstract class BodyConverter<T> implements MessageBodyWriter<T>, MessageBodyReader<T> {
    private static final Logger logger = LoggerFactory.getLogger(BodyConverter.class);
    private static final String MESSAGE_RECEIVED_METER = "MessagesReceived";
    private static final String MESSAGE_SENT_METER = "MessagesSent";

    private final Meter messagesReceivedMeter;
    private final Meter messagesSentMeter;
    private final ObjectMapper objectMapper;

    protected BodyConverter(ObjectMapper mapper, MetricRegistry metricRegistry) {
        this.objectMapper = mapper;
        this.messagesReceivedMeter = metricRegistry.meter(name(this.getClass(), MESSAGE_RECEIVED_METER));
        this.messagesSentMeter = metricRegistry.meter(name(this.getClass(), MESSAGE_SENT_METER));
    }


    @Override
    public long getSize(T o, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        // According to Jersey docs this is deprecated
        return -1;
    }

    @Override
    public void writeTo(T o, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
        logger.debug("write '{}' out to stream", o);
        objectMapper.writeValue(entityStream, o);
        this.messagesSentMeter.mark();
    }

    @Override
    public T readFrom(Class<T> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
        logger.debug("read a {} from stream", type);
        this.messagesReceivedMeter.mark();
        return objectMapper.readValue(entityStream, type);
    }

}
