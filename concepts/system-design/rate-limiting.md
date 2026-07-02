# API Rate Limiting — Algorithms & Implementation

## Why Rate Limit?
- Prevent abuse / DoS attacks
- Ensure fair usage
- Protect downstream services
- Control costs (expensive LLM APIs)

## Algorithms

### 1. Fixed Window Counter
```
Window: 0-60s → count=45 ✅
Window: 60-120s → count=101 ❌ (limit=100)

+ Simple, low memory
- Burst at window edges: 100 req in last second + 100 in first second = 200 in 2s
```

### 2. Sliding Window Log
```
Store timestamps of all requests in Redis sorted set.
Count requests in last 60s.

+ Accurate
- High memory: O(requests) storage
```

### 3. Sliding Window Counter (Hybrid)
```
current_window_count + (prev_window_count × overlap_percentage)

If prev=84, current=36, overlap=75%:
Estimated = 36 + 84 × 0.75 = 99 ✅

+ Good approximation, low memory
```

### 4. Token Bucket ⭐ (Most Common)
```
Bucket capacity = 100 tokens
Fill rate = 10 tokens/second

Request comes in → consume 1 token
No tokens → reject
Bucket refills continuously

+ Allows bursts up to bucket size
+ Smooth over time
- Two parameters to tune
```

### 5. Leaky Bucket
```
Requests enter bucket (queue)
Processed at fixed rate (drips out)
Overflow = rejected

+ Constant output rate (good for downstream)
- Queued requests add latency
```

## Redis Implementation (Token Bucket)
```lua
-- Atomic Lua script in Redis
local key = KEYS[1]
local capacity = tonumber(ARGV[1])
local fill_rate = tonumber(ARGV[2])
local now = tonumber(ARGV[3])
local requested = tonumber(ARGV[4])

local data = redis.call('HMGET', key, 'tokens', 'last_refill')
local tokens = tonumber(data[1]) or capacity
local last_refill = tonumber(data[2]) or now

local elapsed = now - last_refill
tokens = math.min(capacity, tokens + elapsed * fill_rate)

if tokens >= requested then
  tokens = tokens - requested
  redis.call('HMSET', key, 'tokens', tokens, 'last_refill', now)
  redis.call('EXPIRE', key, 3600)
  return 1  -- allowed
else
  return 0  -- rejected
end
```

## Rate Limit Headers
```
X-RateLimit-Limit: 100
X-RateLimit-Remaining: 45
X-RateLimit-Reset: 1708531200
Retry-After: 30  (when rejected with 429)
```


# API Rate Limiting — Algorithms & Implementation

## Why Rate Limit?
- Prevent abuse / DoS attacks
- Ensure fair usage
- Protect downstream services
- Control costs (expensive LLM APIs)

## Algorithms

### 1. Fixed Window Counter
```
Window: 0-60s → count=45 ✅
Window: 60-120s → count=101 ❌ (limit=100)

+ Simple, low memory
- Burst at window edges: 100 req in last second + 100 in first second = 200 in 2s
```

### 2. Sliding Window Log
```
Store timestamps of all requests in Redis sorted set.
Count requests in last 60s.

+ Accurate
- High memory: O(requests) storage
```

### 3. Sliding Window Counter (Hybrid)
```
current_window_count + (prev_window_count × overlap_percentage)

If prev=84, current=36, overlap=75%:
Estimated = 36 + 84 × 0.75 = 99 ✅

+ Good approximation, low memory
```

### 4. Token Bucket ⭐ (Most Common)
```
Bucket capacity = 100 tokens
Fill rate = 10 tokens/second

Request comes in → consume 1 token
No tokens → reject
Bucket refills continuously

+ Allows bursts up to bucket size
+ Smooth over time
- Two parameters to tune
```

### 5. Leaky Bucket
```
Requests enter bucket (queue)
Processed at fixed rate (drips out)
Overflow = rejected

+ Constant output rate (good for downstream)
- Queued requests add latency
```

## Redis Implementation (Token Bucket)
```lua
-- Atomic Lua script in Redis
local key = KEYS[1]
local capacity = tonumber(ARGV[1])
local fill_rate = tonumber(ARGV[2])
local now = tonumber(ARGV[3])
local requested = tonumber(ARGV[4])

local data = redis.call('HMGET', key, 'tokens', 'last_refill')
local tokens = tonumber(data[1]) or capacity
local last_refill = tonumber(data[2]) or now

local elapsed = now - last_refill
tokens = math.min(capacity, tokens + elapsed * fill_rate)

if tokens >= requested then
  tokens = tokens - requested
  redis.call('HMSET', key, 'tokens', tokens, 'last_refill', now)
  redis.call('EXPIRE', key, 3600)
  return 1  -- allowed
else
  return 0  -- rejected
end
```

## Rate Limit Headers
```
X-RateLimit-Limit: 100
X-RateLimit-Remaining: 45
X-RateLimit-Reset: 1708531200
Retry-After: 30  (when rejected with 429)
```
