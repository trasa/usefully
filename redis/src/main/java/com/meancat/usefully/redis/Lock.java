package com.meancat.usefully.redis;

import java.util.Random;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisDataException;

/**
 * Try and get a lock which is hosted in Redis.
 * This requires that all attempting to use the Lock are using the same
 * instance of this class (this is not a distributed lock).
 *
 * tryAcquire attempts to get a Lock for key-(random) and will
 * hold it for 30 seconds (or the value of EXPIRES_SECONDS). If the
 * lock is not released within that time it will expire and be
 * available for the next caller.
 */
@SuppressWarnings("deprecation")
public class Lock {
    public static final int EXPIRES_SECONDS = 30;

    protected JedisPool jedisPool = null;
    protected Random random = new Random();

    protected String key = null;
    protected String randomValue = null;
    protected boolean acquired = false;

    /**
     * This is a simple locking mechanism that does
     * not accommodate more than one Redis master.
     *
     * For more information, see:
     * http://redis.io/topics/distlock
     */

    public Lock(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
        randomValue = Long.toString(random.nextLong());
    }

    public synchronized boolean tryAcquire(String name) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            // Set an expiration TTL to prevent permanent lock leakage if the owner neglects to call release().
            String result = jedis.set(name, randomValue, "NX", "EX", EXPIRES_SECONDS);
            if ("OK".equals(result)) {
                // success!
                key = name;
                acquired = true;
            } else {
                acquired = false;
            }
        } catch(JedisConnectionException | JedisDataException e) {
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
                jedis = null;
            }
            throw e;
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }

        return acquired;
    }

    public synchronized void release() {
        if (!acquired) {
            return;
        }

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String retrievedValue = jedis.get(key);
            // if the key expired, the lock is released..
            if (retrievedValue == null || retrievedValue.equals(randomValue)) {
                jedis.del(key);
                key = null;
                acquired = false;
            }
        } catch(JedisConnectionException | JedisDataException e) {
            if (jedis != null) {
                jedisPool.returnBrokenResource(jedis);
                jedis = null;
            }
            throw e;
        } finally {
            if (jedis != null) {
                jedisPool.returnResource(jedis);
            }
        }
    }
}
