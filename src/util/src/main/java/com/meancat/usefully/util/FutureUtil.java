package com.meancat.usefully.util;

import com.google.common.util.concurrent.SettableFuture;

public class FutureUtil {

    public static <T> SettableFuture<T> createSettableFuture(T obj) {
        SettableFuture<T> future = SettableFuture.create();
        future.set(obj);
        return future;
    }
}
