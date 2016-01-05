package com.meancat.usefully.messaging;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.meancat.usefully.util.PackageUrls;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.ConfigurationException;
import java.util.Set;

/**
 * Creates configured ObjectMappers.
 *
 * ObjectMappers are threadsafe so actually you'd only need to create one of them -
 * but this is here to handle a lot of configuration that typically goes on in
 * a Spring Configuration object.
 */
public class ObjectMapperFactory {
    private static final Logger logger = LoggerFactory.getLogger(ObjectMapperFactory.class);

    private final Reflections reflections;
    private final PackageUrls packageUrls;

    public ObjectMapperFactory(Reflections reflections, PackageUrls packageUrls) {
        this.reflections = reflections;
        this.packageUrls = packageUrls;
    }


    /**
     * Create a fully registered ObjectMapper based on Reflections, PackageUrls,
     * CustomRequest, CustomResponse, and CustomNotificaiton.
     *
     * @return ObjectMapper
     */
    public ObjectMapper create() throws ConfigurationException {
        ObjectMapper objectMapper = new ObjectMapper(new JsonFactory());
        objectMapper.disableDefaultTyping();
        objectMapper.disable(SerializationFeature.WRITE_NULL_MAP_VALUES);
        objectMapper.disable(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS);

        // We need these so Enum objects don't crash if there's missing or bad data,
        // e.g. '' as a value or 'wind' instead of 'Wind' as an Element type
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        objectMapper.enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL);

        // java wont let us have an @interface inherit from another @interface so we're stuck with this:
        registerSubtypes(objectMapper, reflections.getTypesAnnotatedWith(CustomRequest.class));
        registerSubtypes(objectMapper, reflections.getTypesAnnotatedWith(CustomResponse.class));
        registerSubtypes(objectMapper, reflections.getTypesAnnotatedWith(CustomNotification.class));

        // this makes it so we can serialize and deserialize Joda Time correctly.
        objectMapper.registerModule(new JodaModule());

        return objectMapper;
    }


    private void registerSubtypes(ObjectMapper objectMapper, Set<Class<?>> customTypes) throws ConfigurationException {
        for(Class<?> clazz : customTypes) {
            JsonTypeName typeInfo = clazz.getAnnotation(JsonTypeName.class);
            if (typeInfo == null) {
                logger.debug("Registering subtype {}", clazz);
                objectMapper.registerSubtypes(clazz);
            } else {
                logger.debug("Registering subtype {}", typeInfo.value());
                objectMapper.registerSubtypes(new NamedType(clazz, typeInfo.value()));
            }
        }
    }
}
