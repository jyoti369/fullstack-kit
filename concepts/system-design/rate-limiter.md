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
