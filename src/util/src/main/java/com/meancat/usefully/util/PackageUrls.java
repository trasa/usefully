package com.meancat.usefully.util;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * A HashSet of URLs. Used with Reflections package to discover classes,
 * which in turn is used to load annotations into Jackson ObjectMapper.
 */
public class PackageUrls extends HashSet<URL> {
    private static final long serialVersionUID = 4077144295898095756L;

    public PackageUrls() {
    }

    public PackageUrls(Set<URL> packageUrls) {
        addAll(packageUrls);
    }

}
