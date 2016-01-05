package com.meancat.usefully.util;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A reference wrapper that expires the value. The reference is atomic.
 *
 * @author Elvir.Bahtijaragic
 */
public class ExpirableAtomicReference<T> implements Serializable {
    private static final long serialVersionUID = 4936023590525891721L;

    private AtomicReference<T> ref = new AtomicReference<T>();

    private final long time;
    private final TimeUnit unit;
    private final boolean refreshAfterExpiration;

    private final AtomicLong nextExpiration = new AtomicLong(0);

    private final ExpirationListener<T> listener;

    /**
     * Creates a expirable atomic reference.
     *
     * @param time how long til expiration
     * @param unit units of time
     * @param refreshAfterExpiration refresh after?
     * @param listener what gets called on cleanup
     */
    public ExpirableAtomicReference(long time, TimeUnit unit, boolean refreshAfterExpiration, T initialValue, ExpirationListener<T> listener) {
        this.time = time;
        this.unit = unit;
        this.refreshAfterExpiration = refreshAfterExpiration;
        this.listener = listener;

        this.nextExpiration.set(System.currentTimeMillis() + unit.toMillis(time));
        this.ref.set(initialValue);
    }

    /**
     * Performs an expiration if needed.
     */
    public void cleanUp() {
        if (System.currentTimeMillis() < nextExpiration.get()) {
            return;
        }

        if (nextExpiration.compareAndSet(nextExpiration.get(), refreshAfterExpiration ? System.currentTimeMillis() + unit.toMillis(time) : Long.MAX_VALUE)) {
            listener.onExpire(ref.get());
        }
    }

    /**
     * Sets the current value.
     *
     * @param val value
     */
    public void set(T val) {
        cleanUp();

        ref.set(val);
    }

    /**
     * Gets the most up-to-date value.
     *
     * @return T
     */
    public T get() {
        cleanUp();

        return ref.get();
    }

    /**
     * Gets the value without explicitly calling cleanup
     * @return T
     */
    public T getWithoutExpire() {
        return ref.get();
    }

    /**
     * Sets the current value and gets the value it replaced.
     *
     * @param val value
     * @return T
     */
    public T getAndSet(T val) {
        cleanUp();

        return ref.getAndSet(val);
    }

    /**
     * Sets the current value only if the previous value matches the expected value.
     *
     * @param expect what we expect
     * @param update what we want
     * @return what we got
     */
    public boolean compareAndSet(T expect, T update) {
        cleanUp();

        return ref.compareAndSet(expect, update);
    }

    @Override
    public String toString() {
        return "ExpirableAtomicReference{" +
                "ref=" + ref +
                ", time=" + time +
                ", unit=" + unit +
                ", refreshAfterExpiration=" + refreshAfterExpiration +
                ", nextExpiration=" + nextExpiration +
                '}';
    }

    /**
     * A listener for expirations on the reference.
     *
     * @author Elvir.Bahtijaragic
     *
     * @param <T>
     */
    public interface ExpirationListener<T> {
        public void onExpire(T value);
    }
}
