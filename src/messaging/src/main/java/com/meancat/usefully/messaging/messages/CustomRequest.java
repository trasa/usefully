package com.meancat.usefully.messaging.messages;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.fasterxml.jackson.annotation.JsonTypeName;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@JsonTypeName
public @interface CustomRequest {
}

