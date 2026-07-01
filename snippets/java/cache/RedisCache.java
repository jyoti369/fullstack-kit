package snippets.java.cache;

import redis.clients.jedis.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;
import java.util.function.*;

/**
 * Redis Cache Wrapper in Java (Jedis)
 * Cache-aside pattern with TTL and JSON serialization.
 */
public class RedisCache {

    private final JedisPool pool;
    private final ObjectMapper mapper;
    private static final int DEFAULT_TTL = 300; // 5 min

    public RedisCache(String redisUrl) {
        pool = new JedisPool(redisUrl);
        mapper = new ObjectMapper();
    }

    // Cache-aside: get from cache or compute and store
    public <T> T getOrSet(String key, Supplier<T> fetchFn, Class<T> type, int ttl) {
        try (Jedis jedis = pool.getResource()) {
            String cached = jedis.get(key);
            if (cached != null) return mapper.readValue(cached, type);
            T data = fetchFn.get();
            jedis.setex(key, ttl, mapper.writeValueAsString(data));
            return data;
        } catch (Exception e) {
            throw new RuntimeException("Cache error", e);
        }
    }

    public <T> T getOrSet(String key, Supplier<T> fetchFn, Class<T> type) {
        return getOrSet(key, fetchFn, type, DEFAULT_TTL);
    }

    public void invalidate(String key) {
        try (Jedis jedis = pool.getResource()) { jedis.del(key); }
    }

    public void invalidateByPattern(String pattern) {
        try (Jedis jedis = pool.getResource()) {
            Set<String> keys = jedis.keys(pattern);
            if (!keys.isEmpty()) jedis.del(keys.toArray(new String[0]));
        }
    }

    // Distributed lock (Redlock simplified)
    public boolean acquireLock(String lockKey, String token, int ttlSeconds) {
        try (Jedis jedis = pool.getResource()) {
            String result = jedis.set(lockKey, token, SetParams.setParams().nx().ex(ttlSeconds));
            return "OK".equals(result);
        }
    }

    public void releaseLock(String lockKey, String token) {
        try (Jedis jedis = pool.getResource()) {
            // Only release if we own the lock
            String luaScript = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            jedis.eval(luaScript, List.of(lockKey), List.of(token));
        }
    }

    // Rate limiting (sliding window)
    public boolean isRateLimited(String userId, int maxRequests, int windowSeconds) {
        String key = "rl:" + userId;
        long now = System.currentTimeMillis();
        long windowStart = now - windowSeconds * 1000L;
        try (Jedis jedis = pool.getResource()) {
            jedis.zremrangeByScore(key, 0, windowStart); // remove old entries
            long count = jedis.zcard(key);
            if (count >= maxRequests) return true;
            jedis.zadd(key, now, UUID.randomUUID().toString());
            jedis.expire(key, windowSeconds);
            return false;
        }
    }

    public void close() { pool.close(); }

    // Usage example (illustrative)
    public static void main(String[] args) {
        RedisCache cache = new RedisCache("redis://localhost:6379");
        String user = cache.getOrSet("user:1:profile", () -> "{name:'Alice'}", String.class, 600);
        System.out.println(user);
        System.out.println("Rate limited: " + cache.isRateLimited("user1", 100, 60));
        cache.close();
    }
}


package snippets.java.cache;

import redis.clients.jedis.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;
import java.util.function.*;

/**
 * Redis Cache Wrapper in Java (Jedis)
 * Cache-aside pattern with TTL and JSON serialization.
 */
public class RedisCache {

    private final JedisPool pool;
    private final ObjectMapper mapper;
    private static final int DEFAULT_TTL = 300; // 5 min

    public RedisCache(String redisUrl) {
        pool = new JedisPool(redisUrl);
        mapper = new ObjectMapper();
    }

    // Cache-aside: get from cache or compute and store
    public <T> T getOrSet(String key, Supplier<T> fetchFn, Class<T> type, int ttl) {
        try (Jedis jedis = pool.getResource()) {
            String cached = jedis.get(key);
            if (cached != null) return mapper.readValue(cached, type);
            T data = fetchFn.get();
            jedis.setex(key, ttl, mapper.writeValueAsString(data));
            return data;
        } catch (Exception e) {
            throw new RuntimeException("Cache error", e);
        }
    }

    public <T> T getOrSet(String key, Supplier<T> fetchFn, Class<T> type) {
        return getOrSet(key, fetchFn, type, DEFAULT_TTL);
    }

    public void invalidate(String key) {
        try (Jedis jedis = pool.getResource()) { jedis.del(key); }
    }

    public void invalidateByPattern(String pattern) {
        try (Jedis jedis = pool.getResource()) {
            Set<String> keys = jedis.keys(pattern);
            if (!keys.isEmpty()) jedis.del(keys.toArray(new String[0]));
        }
    }

    // Distributed lock (Redlock simplified)
    public boolean acquireLock(String lockKey, String token, int ttlSeconds) {
        try (Jedis jedis = pool.getResource()) {
            String result = jedis.set(lockKey, token, SetParams.setParams().nx().ex(ttlSeconds));
            return "OK".equals(result);
        }
    }

    public void releaseLock(String lockKey, String token) {
        try (Jedis jedis = pool.getResource()) {
            // Only release if we own the lock
            String luaScript = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            jedis.eval(luaScript, List.of(lockKey), List.of(token));
        }
    }

    // Rate limiting (sliding window)
    public boolean isRateLimited(String userId, int maxRequests, int windowSeconds) {
        String key = "rl:" + userId;
        long now = System.currentTimeMillis();
        long windowStart = now - windowSeconds * 1000L;
        try (Jedis jedis = pool.getResource()) {
            jedis.zremrangeByScore(key, 0, windowStart); // remove old entries
            long count = jedis.zcard(key);
            if (count >= maxRequests) return true;
            jedis.zadd(key, now, UUID.randomUUID().toString());
            jedis.expire(key, windowSeconds);
            return false;
        }
    }

    public void close() { pool.close(); }

    // Usage example (illustrative)
    public static void main(String[] args) {
        RedisCache cache = new RedisCache("redis://localhost:6379");
        String user = cache.getOrSet("user:1:profile", () -> "{name:'Alice'}", String.class, 600);
        System.out.println(user);
        System.out.println("Rate limited: " + cache.isRateLimited("user1", 100, 60));
        cache.close();
    }
}
