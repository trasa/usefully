package com.meancat.usefully.messaging.mapping;


import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandlerMappingTest {
    private static final Logger logger = LoggerFactory.getLogger(HandlerMappingTest.class);

    @Test
    public void outputToString() throws NoSuchMethodException {
        HandlerMapping hm = new HandlerMapping(String.class.getMethod("toString"), this);
        logger.info(hm.toString());
    }
}
