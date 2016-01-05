package com.meancat.usefully.util;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NamingTest {

    Naming naming = new Naming();


    @Test
    public void nameWithClass() {
        String n = naming.name(NamingTest.class);
        assertEquals(NamingTest.class.getName(), n);
    }
    @Test
    public void nameWithClassAndStuff() {
        String n = naming.name(NamingTest.class, "a", "b");
        assertEquals(NamingTest.class.getName() + ".a.b", n);
    }

    @Test(expected=IllegalArgumentException.class)
    public void nameWithNullClass() {
        naming.name((Class)null);
    }

    @Test
    public void nameWithClassAndNull() {
        String n = naming.name(NamingTest.class, null, null);
        assertEquals(NamingTest.class.getName(), n);
    }

    @Test
    public void nameWithClassAndNullAndStuff() {
        String n = naming.name(NamingTest.class, null, null, "a");
        assertEquals(NamingTest.class.getName() + ".a", n);
    }

    @Test
    public void name() {
        String n = naming.name("a");
        assertEquals("a", n);
    }

    @Test
    public void nameWithStuff() {
        String n = naming.name("a", "b");
        assertEquals("a.b", n);
    }

    @Test
    public void nameWithNull() {
        @SuppressWarnings("NullArgumentToVariableArgMethod")
        String n = naming.name("a", (String)null); // never do this.
        assertEquals("a", n);
    }

    @Test
    public void nameAllNull() {
        String n = naming.name((String)null);
        assertEquals("", n);
    }

    @Test
    public void nameAllNullAndMoreNulls() {
        String n = naming.name((String)null, null, null);
        assertEquals("", n);
    }

    @Test
    public void nameWithStuffAndNull() {
        String n = naming.name("a", null, null, "b");
        assertEquals("a.b", n);
    }
}
