package com.meancat.usefully.util.metrics;


import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.codahale.metrics.health.HealthCheck;

public class FailableHealthCheckTest {

    static class TestingHealthCheck extends FailableHealthCheck {

        @Override
        protected Result doCheck() throws Exception {
            return Result.healthy();
        }
    }
    TestingHealthCheck check;

    @Before
    public void setUp() {
        check = new TestingHealthCheck();
    }


    @Test
    public void forceFalse() {
        check.toggleFailCheck();
        HealthCheck.Result result = check.execute();
        assertFalse(result.isHealthy());
    }

    @Test
    public void dontForceFalse() {
        HealthCheck.Result result = check.execute();
        assertTrue(result.isHealthy());
    }

}
