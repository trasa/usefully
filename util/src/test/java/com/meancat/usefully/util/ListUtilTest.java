package com.meancat.usefully.util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.*;

public class ListUtilTest {

    @Test
    public void removeDuplicates_unique() {
        List<String> allUnique = newArrayList("a", "b", "c");
        List<String> allDuplicates = newArrayList("a", "a", "a");

        List<String> uniqueResult = ListUtil.removeDuplicates(allUnique);
        assertEquals(3, uniqueResult.size());
        assertTrue(uniqueResult.contains("a"));
        assertTrue(uniqueResult.contains("b"));
        assertTrue(uniqueResult.contains("c"));
    }

    @Test
    public void removeDuplicates_allDupes() {
        List<String> allDuplicates = newArrayList("a", "a", "a");

        List<String> uniqueResult = ListUtil.removeDuplicates(allDuplicates);

        assertEquals(1, uniqueResult.size());
        assertTrue(uniqueResult.contains("a"));
    }

    @Test
    public void removeDuplicates_empty() {
        List<String> result = ListUtil.removeDuplicates(new ArrayList<String>());
        assertTrue(result.isEmpty());
    }

    @Test
    public void removeDuplicates_null() {
        assertNull(ListUtil.removeDuplicates(null));
    }
}
