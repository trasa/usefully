package com.meancat.usefully.iap;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class IapErrorsTest {
    private IapErrors errors;

    @Before
    public void setUp() {
        errors = new IapErrors();
    }

    @Test
    public void canRetryBecauseNull() {
        assertTrue(errors.canRetry(null));
    }

    @Test
    public void canRetryBecauseNotFound() {
        assertTrue(errors.canRetry("OOGABOOGA"));
    }

    @Test
    public void canRetry() {
        assertTrue(errors.canRetry("VERIFY_EXCEPTION"));
    }

    @Test
    public void retryNotAllowed() {
        assertFalse(errors.canRetry("RETRIEVED_BID_MISMATCH"));
    }
}

