package com.meancat.usefully.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * List Utils
 */
public class ListUtil {

    /**
     * Create a new list that includes only unique items from
     * the source list.
     *
     * Note that this does not preserve order and does not alter
     * the original list.
     *
     * @param list to be de-duped
     * @param <T> type
     * @return list of uniques
     */
    public static <T> List<T> removeDuplicates(List<T> list) {
        if (list == null)
            return null;
        return new ArrayList<>(new HashSet<>(list));
    }
}
