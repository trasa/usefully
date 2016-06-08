package com.meancat.usefully.util.metrics;

import org.joda.time.Instant;
import org.joda.time.Seconds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.health.HealthCheck;
import com.google.common.annotations.VisibleForTesting;

/**
 * a Healthcheck that keeps the last result around and only
 * gets new results after a timespan has elapsed.
 */
public abstract class CachingHealthCheck extends FailableHealthCheck {

    private static final Logger logger = LoggerFactory.getLogger(CachingHealthCheck.class);

    private HealthCheck.Result lastResult = HealthCheck.Result.healthy();
    private Instant lastRun;

    private final Object checkLock = new Object();

    /**
     * Frequency in Seconds that the actual healthcheck should be run.
     * @return
     */
    protected abstract int getFrequencySeconds();

    /**
     * Run the actual health check
     *
     * @return current health status
     */
    protected abstract HealthCheck.Result doActualCheck() throws Exception;

    @Override
    protected HealthCheck.Result doCheck() throws Exception {
        synchronized (checkLock) {
            if (itsTime()) {
                this.lastResult = doActualCheck();
                lastRun = Instant.now();
            }
            return lastResult;
        }
    }

    @VisibleForTesting
    protected void setLastRun(Instant lastRun) {
        this.lastRun = lastRun;
    }

    protected boolean itsTime() {
        return itsTime(Instant.now());
    }

    protected boolean itsTime(Instant now) {
        if (lastRun == null) {
            logger.debug("lastRun is null, so it's time!");
            return true;
        }
        int since = Math.abs(Seconds.secondsBetween(lastRun, now).getSeconds());
        int freq = getFrequencySeconds();
        logger.debug("seconds since last run {}s, freq is {}s", since, freq);
        return since > freq;
    }
}
