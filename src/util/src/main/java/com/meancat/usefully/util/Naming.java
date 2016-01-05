package com.meancat.usefully.util;

import org.springframework.stereotype.Component;

/**
 * Utilities for naming things.
 *
 * Taken from http://metrics.codahale.com/
 */
@Component
public class Naming {


    String separator = ".";

    public Naming() {
    }

    public Naming(String separator) {
        this.separator = separator;
    }

    /**
     * Concatenates elements to form a dotted name, eliding any null values or empty strings.
     *
     * @param name     the first element of the name
     * @param names    the remaining elements of the name
     * @return {@code name} and {@code names} concatenated by periods
     */
    public String name(String name, String... names) {
        final StringBuilder builder = new StringBuilder();
        append(builder, name);
        if (names != null) {
            for (String s : names) {
                append(builder, s);
            }
        }
        return builder.toString();
    }

    /**
     * Concatenates a class name and elements to form a dotted name, eliding any null values or
     * empty strings.
     *
     * @param klass    the first element of the name
     * @param names    the remaining elements of the name
     * @return {@code klass} and {@code names} concatenated by periods
     */
    public String name(Class<?> klass, String... names) {
        if (klass == null) {
            throw new IllegalArgumentException("klass can't be null");
        }
        return name(klass.getName(), names);
    }

    private void append(StringBuilder builder, String part) {
        if (part != null && !part.isEmpty()) {
            if (builder.length() > 0) {
                builder.append(separator);
            }
            builder.append(part);
        }
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }
}
