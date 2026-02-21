# Caching Strategies — Deep Dive

## Cache Patterns

### 1. Cache-Aside (Lazy Loading)
```
Read:
  1. Check cache
  2. Cache miss → read DB → store in cache → return

Write:
  1. Write to DB
  2. Invalidate (delete) cache entry

+ Only caches what's needed
+ Cache failure doesn't break app
- Cache miss penalty (3 round trips)
- Data can be stale
```

### 2. Write-Through
```
Write:
  1. Write to cache
  2. Write to DB (synchronously)

+ No stale data
+ Cache always warm
- Write latency (2 writes)
- Caches data that may never be read
```

### 3. Write-Behind (Write-Back)
```
Write:
  1. Write to cache (fast return)
  2. Async write to DB in background

+ Best write performance
- Risk of data loss if cache crashes
- Complex implementation
```

### 4. Read-Through
Cache sits in front, automatically loads from DB on miss.
```
+ Transparent to application
- First request always misses (warm-up needed)
```

### 5. Refresh-Ahead
Proactively refresh cache before TTL expires.
```
+ No cold starts
- May refresh data that's never read
```

## Eviction Policies
| Policy | Description | Use When |
|--------|-------------|----------|
| LRU | Evict least recently used | General purpose |
| LFU | Evict least frequently used | Stable hot data |
| TTL | Expire after time | Time-sensitive data |
| Random | Random eviction | Simple, good enough |
| FIFO | Evict oldest | Simple queues |

## Cache Stampede (Thundering Herd)
When many requests hit the DB simultaneously after cache expiry.
```python
# Solution 1: Mutex / locking
if key not in cache:
    with lock:
        if key not in cache:  # Double check
            cache[key] = load_from_db(key)

# Solution 2: Probabilistic early expiration
# Randomly refresh before TTL to avoid synchronized expiry

# Solution 3: Stale-while-revalidate
# Return stale data, refresh in background
```

## Cache Invalidation — Hard Problem
> "There are only two hard problems in CS: cache invalidation and naming things."

Strategies:
1. **TTL**: Simple, eventual consistency
2. **Event-driven**: DB triggers or CDC (Debezium) → invalidate
3. **Write-through**: Invalidate on every write
4. **Versioned keys**: `user:v2:123` — never invalidate, just use new key
