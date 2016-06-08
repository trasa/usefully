package com.meancat.usefully.redis;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Strings;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisDataException;

/**
 * Wraps up Redis SCAN command with a cursor,
 * making it easier to page through results of a SCAN.
 */
@SuppressWarnings("deprecation")
public class Scanner {

    protected JedisPool jedisPool;

    public Scanner(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }


    /**
     * Scan for keys matching this pattern and return a list of them.
     * Note that the list might have duplicates and is a best guess
     * of the current set of matches for this pattern.
     *
     * @param pattern to search for
     * @return the list of matches, more or less
     */
    public List<String> scan(String pattern) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String cursor = "0";
            ScanParams scanParams = new ScanParams().match(pattern);
            List<String> result = new ArrayList<>();
            while(true) {
                ScanResult<String> thisScan = jedis.scan(cursor, scanParams);
                result.addAll(thisScan.getResult());
                cursor = thisScan.getStringCursor();
                if (Strings.isNullOrEmpty(cursor) || "0".equals(cursor)) {
                    // done
                    break;
                }
            }
            return result;
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

    /**
     * Determine how many keys match this pattern, more or less.
     * This number is a best guess and could be off in either
     * direction.
     *
     * @param pattern to search for
     * @return number of keys matching the pattern, more or less
     */
    public long scanCount(String pattern) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String cursor = "0";
            ScanParams scanParams = new ScanParams().match(pattern);
            long count = 0;
            while(true) {
                ScanResult<String> thisScan = jedis.scan(cursor, scanParams);
                count += thisScan.getResult().size();
                cursor = thisScan.getStringCursor();
                if (Strings.isNullOrEmpty(cursor) || "0".equals(cursor)) {
                    // done
                    break;
                }
            }
            return count;
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
