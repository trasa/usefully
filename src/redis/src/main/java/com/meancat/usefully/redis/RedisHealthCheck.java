package com.meancat.usefully.redis;

import static com.codahale.metrics.MetricRegistry.name;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.annotation.PostConstruct;

import org.joda.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.meancat.usefully.util.metrics.CachingHealthCheck;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisDataException;

/**
 * Is this redis link alive and healthy?
 *
 * Uses a CachingHealthCheck to avoid hugging Redis to death with
 * excessive checks.
 */
public class RedisHealthCheck extends CachingHealthCheck {

    @Autowired
    HealthCheckRegistry healthCheckRegistry;

    @Autowired
    List<JedisPool> allJedisPools;


    @Value("${usefully.redis.healthcheck.timeoutSeconds:10}")
    int redisTimeoutSec = 10;

    @Value("${usefully.redis.healthcheck.frequencySeconds:60}")
    int frequencySec = 60;

    @Value("${usefully.redis.healthcheck.jedisHealthKeyLifeSec:60}")
    int jedisHealthKeyLifeSec = 60;

    private ExecutorService executorService;

    @PostConstruct
    public void init() {
        healthCheckRegistry.register(name(RedisHealthCheck.class), this);
        executorService = Executors.newSingleThreadExecutor();
    }


    @Override
    protected int getFrequencySeconds() {
        return frequencySec;
    }

    @Override
    protected HealthCheck.Result doActualCheck() throws Exception {
        Future<HealthCheck.Result> futureResult = executorService.submit(new JedisHealth());
        try {
            return futureResult.get(redisTimeoutSec, TimeUnit.SECONDS);
        } catch(TimeoutException e) {
            return HealthCheck.Result.unhealthy("No response from Redis within timeout: " + redisTimeoutSec + " s", e);
        }
    }


    private class JedisHealth implements Callable<HealthCheck.Result> {

        @Override
        public HealthCheck.Result call() throws Exception {
            for(JedisPool jp : allJedisPools) {
                HealthCheck.Result r = checkJedisPool(jp);
                if (!r.isHealthy()) {
                    // fail
                    return r;
                }
            }
            return HealthCheck.Result.healthy("last run " + Instant.now());
        }

        HealthCheck.Result checkJedisPool(JedisPool jedisPool) {
            Jedis j = null;
            try {
                j = jedisPool.getResource();

                String id = UUID.randomUUID().toString();
                Pipeline p = j.pipelined();
                String key = "redisHealthCheck-" + id;
                Response<String> setResponse = p.setex(key, jedisHealthKeyLifeSec, id);
                Response<String> getResponse = p.get(key);
                p.sync();

                if (!"OK".equals(setResponse.get())) {
                    return HealthCheck.Result.unhealthy("Failed to set key: " + setResponse.get());
                }
                if (!id.equals(getResponse.get())) {
                    return HealthCheck.Result.unhealthy("Set ok, but Get was different: " + getResponse.get());
                }
                return HealthCheck.Result.healthy();

            } catch (JedisConnectionException | JedisDataException e) {
                if (j != null) {
                    jedisPool.returnBrokenResource(j);
                    j = null;
                }
                throw e;
            } finally {
                if (j != null) {
                    jedisPool.returnResource(j);
                }
            }
        }
    }
}
