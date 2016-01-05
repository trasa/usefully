package com.meancat.usefully.util;

import junit.framework.Assert;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Tests the {@link ExpirableAtomicReference}
 *
 * @author Elvir.Bahtijaragic
 */
public class ExpirableAtomicReferenceTest {
    @Test
    public void testNotExpired() throws Exception {
        final AtomicBoolean didCallListener = new AtomicBoolean(false);
        final Object obj = new Object();

        ExpirableAtomicReference<Object> ref = new ExpirableAtomicReference<Object>(10000, TimeUnit.MILLISECONDS, true, obj,
                new ExpirableAtomicReference.ExpirationListener<Object>() {
                    @Override
                    public void onExpire(Object value) {
                        Assert.assertEquals(obj, value);

                        didCallListener.set(true);
                    }
                });

        ref.get();

        Assert.assertFalse(didCallListener.get());
    }

    @Test
    public void testMiliseconds() throws Exception {
        final AtomicBoolean didCallListener = new AtomicBoolean(false);
        final Object obj = new Object();

        ExpirableAtomicReference<Object> ref = new ExpirableAtomicReference<Object>(100, TimeUnit.MILLISECONDS, true, obj,
                new ExpirableAtomicReference.ExpirationListener<Object>() {
                    @Override
                    public void onExpire(Object value) {
                        Assert.assertEquals(obj, value);

                        didCallListener.set(true);
                    }
                });

        Thread.sleep(200);

        ref.get();

        Assert.assertTrue(didCallListener.get());
    }
}
