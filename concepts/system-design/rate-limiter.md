# Rate Limiter — System Design

## Why Rate Limiting?
- Prevent abuse and DDoS attacks
- Protect backend resources
- Ensure fair usage across clients
- Comply with third-party API limits

## Algorithms

### 1. Token Bucket
- Bucket holds N tokens, refills at rate R
- Each request consumes 1 token
- If empty → reject request
- **Pros**: Allows burst traffic
- **Used by**: AWS, Stripe

### 2. Sliding Window Log
- Store timestamp of each request
- Count requests in the last N seconds
- **Pros**: Very accurate
- **Cons**: Memory-heavy

### 3. Sliding Window Counter
- Combine fixed window + sliding estimate
- `count = prev_window * overlap_% + curr_window`
- **Pros**: Memory efficient, reasonably accurate

## Implementation (Node.js + Redis)
```javascript
const Redis = require('ioredis');
const redis = new Redis();

async function slidingWindowRateLimit(userId, limit = 100, windowSec = 60) {
  const key = `rate:${userId}`;
  const now = Date.now();
  const windowStart = now - windowSec * 1000;

  const pipeline = redis.pipeline();
  pipeline.zremrangebyscore(key, 0, windowStart); // Remove old
  pipeline.zadd(key, now, `${now}`);
  pipeline.zcard(key);
  pipeline.expire(key, windowSec);

  const results = await pipeline.exec();
  const requestCount = results[2][1];

  return {
    allowed: requestCount <= limit,
    remaining: Math.max(0, limit - requestCount),
    resetAt: new Date(now + windowSec * 1000),
  };
}
```

## HTTP Headers (Standard)
```
X-RateLimit-Limit: 100
X-RateLimit-Remaining: 42
X-RateLimit-Reset: 1609459200
Retry-After: 30
```


# Rate Limiter — System Design + Java Implementation

## Why Rate Limiting?
- Prevent abuse and DDoS attacks
- Protect backend resources
- Ensure fair usage across clients
- Comply with third-party API limits

## Algorithms
```
1. Token Bucket:
   - Bucket holds N tokens, refills at rate R
   - Each request consumes 1 token. If empty → reject
   - Pros: allows burst traffic
   - Used by: AWS API Gateway, Stripe

2. Sliding Window Log:
   - Store timestamp of each request in sorted set
   - Count requests in the last N seconds
   - Pros: very accurate | Cons: memory-heavy

3. Sliding Window Counter:
   - count = prev_window * overlap_% + curr_window_count
   - Pros: memory-efficient, reasonably accurate
```

## Java Implementation (Redis + Lua, Sliding Window)
```java
@Component
public class RedisRateLimiter {
    private final RedisTemplate<String, String> redis;
    private static final String SCRIPT =
        "local key = KEYS[1] " +
        "local now = tonumber(ARGV[1]) " +
        "local window = tonumber(ARGV[2]) " +
        "local limit = tonumber(ARGV[3]) " +
        "redis.call('ZREMRANGEBYSCORE', key, 0, now - window) " +
        "local count = redis.call('ZCARD', key) " +
        "if count < limit then " +
        "  redis.call('ZADD', key, now, now) " +
        "  redis.call('EXPIRE', key, math.ceil(window/1000)) " +
        "  return 1 " +
        "else return 0 end";

    public boolean isAllowed(String userId, int limitPerMin) {
        long nowMs = System.currentTimeMillis();
        DefaultRedisScript<Long> script = new DefaultRedisScript<>(SCRIPT, Long.class);
        Long result = redis.execute(script,
            List.of("rl:" + userId),
            String.valueOf(nowMs), String.valueOf(60_000), String.valueOf(limitPerMin));
        return Long.valueOf(1).equals(result);
    }
}

// Spring Boot Filter
@Component
public class RateLimitFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res,
            FilterChain chain) throws IOException, ServletException {
        String userId = extractUserId(req);
        if (!rateLimiter.isAllowed(userId, 100)) {
            res.setStatus(429);
            res.setHeader("Retry-After", "60");
            res.getWriter().write("Rate limit exceeded");
            return;
        }
        chain.doFilter(req, res);
    }
    private String extractUserId(HttpServletRequest req) {
        String auth = req.getHeader("Authorization");
        return auth != null ? jwtUtil.getUserId(auth.substring(7)) : req.getRemoteAddr();
    }
}
```

## Response Headers (Standard)
```
X-RateLimit-Limit: 100
X-RateLimit-Remaining: 42
X-RateLimit-Reset: 1609459200
Retry-After: 30   (only on 429)
```
