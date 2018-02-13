package com.meancat.usefully.util.metrics;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.joda.time.Instant;
import org.junit.Test;

import com.codahale.metrics.health.HealthCheck;

public class CachingHealthCheckTest {

    public static class ContainerCachingHealthCheck extends CachingHealthCheck {

        int freq = 10;
        Result expectedResult = Result.healthy();

        @Override
        protected int getFrequencySeconds() {
            return freq;
        }

        @Override
        protected Result doActualCheck() throws Exception {
            return expectedResult;
        }
    }


    @Test
    public void firstTimeSuccessful() throws Exception {
        ContainerCachingHealthCheck hc = new ContainerCachingHealthCheck();
        HealthCheck.Result result = hc.doCheck();
        assertTrue(result.isHealthy());
    }

    @Test
    public void dontRunWhenItsNotTimeYet() throws Exception {
        ContainerCachingHealthCheck hc = new ContainerCachingHealthCheck();
        Instant now = Instant.now();
        hc.setLastRun(now);
        // when time expires we'll be unhealthy, but until then report healthy.
        hc.expectedResult = HealthCheck.Result.unhealthy("BLARGH");

        assertTrue(hc.doCheck().isHealthy());
    }

    @Test
    public void runWhenItIsTime() throws Exception {
        ContainerCachingHealthCheck hc = new ContainerCachingHealthCheck();
        Instant now = Instant.now();
        hc.setLastRun(now.minus(30000)); // 30 seconds ago
        // when time expires we'll be unhealthy
        hc.expectedResult = HealthCheck.Result.unhealthy("BLARGH");

        assertEquals("BLARGH", hc.doCheck().getMessage());
        assertFalse(hc.doCheck().isHealthy());
    }

    @Test
    public void isItTimeYet() throws Exception {
        ContainerCachingHealthCheck hc = new ContainerCachingHealthCheck();
        Instant now = Instant.now();
        hc.setLastRun(now);

        assertFalse(hc.itsTime(now.plus(1000)));
        assertFalse(hc.itsTime(now.plus(10000)));
        assertTrue(hc.itsTime(now.plus(11000)));
    }
}
