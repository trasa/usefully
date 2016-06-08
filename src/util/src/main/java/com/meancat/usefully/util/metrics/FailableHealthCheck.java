package com.meancat.usefully.util.metrics;

import com.codahale.metrics.health.HealthCheck;

/**
 * A Healthcheck that can be toggled to a 'fail'
 * state without actually doing whatever the
 * fail condition was supposed to do. Useful for testing.
 */
public abstract class FailableHealthCheck extends HealthCheck {
    protected boolean failCheck = false;

    public boolean getFailCheck() {
        return this.failCheck;
    }

    public boolean toggleFailCheck() {
        this.failCheck = !this.failCheck;
        return this.failCheck;
    }

    @Override
    protected Result check() throws Exception {
        if(failCheck) {
            return Result.unhealthy("FailCheck Set");
        }

        return doCheck();
    }

    abstract protected Result doCheck() throws Exception;
}
