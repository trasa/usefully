package com.meancat.usefully.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UUIDGenerator {

    public UUID randomUUID() {
         return UUID.randomUUID();
    }

    public String randomString() {
        return randomUUID().toString().replace("-", "");
    }
}
