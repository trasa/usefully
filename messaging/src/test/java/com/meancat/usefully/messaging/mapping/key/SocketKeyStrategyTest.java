package com.meancat.usefully.messaging.mapping.key;

import static junit.framework.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.meancat.usefully.messaging.mapping.MappingException;
import com.meancat.usefully.messaging.messages.MessageHeader;


public class SocketKeyStrategyTest {

    SocketKeyStrategy strategy;

    @Before
    public void setUp() {
        strategy = new SocketKeyStrategy();
    }

    @Test
    public void twoArgs() throws Exception {
        Class<?> key = strategy.determineKey(SocketKeyStrategyTest.class, SocketKeyStrategyTest.class.getMethod("goodOne", MessageHeader.class, ExpectedKey.class));
        assertEquals(ExpectedKey.class, key);
    }

    @Test
    public void threeArgs() throws Exception {
        Class<?> key = strategy.determineKey(SocketKeyStrategyTest.class, SocketKeyStrategyTest.class.getMethod("goodOneMoreArgs", MessageHeader.class, String.class, ExpectedKey.class));
        assertEquals(ExpectedKey.class, key);
    }

    @Test
    public void fourArgs() throws Exception {
        Class<?> key = strategy.determineKey(SocketKeyStrategyTest.class, SocketKeyStrategyTest.class.getMethod("goodOneMoreArgs", MessageHeader.class, String.class, String.class, ExpectedKey.class));
        assertEquals(ExpectedKey.class, key);
    }

    @Test(expected = MappingException.class)
    public void noHeader() throws Exception {
        Class<?> key = strategy.determineKey(SocketKeyStrategyTest.class, SocketKeyStrategyTest.class.getMethod("noheader", String.class, ExpectedKey.class));
    }

    /* methods to test against */

    public Object noheader(String foo, ExpectedKey key) { return null; }

    public Object goodOne(MessageHeader messageHeader, ExpectedKey key) { return null; }

    public Object goodOneMoreArgs(MessageHeader messageHeader, String foo, ExpectedKey key) { return null; }
    public Object goodOneMoreArgs(MessageHeader messageHeader, String foo, String bar, ExpectedKey key) { return null; }

    public static class ExpectedKey {}
}
