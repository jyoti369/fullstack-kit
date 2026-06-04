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


# Caching Strategies — Java/Spring

## Cache Patterns

### 1. Cache-Aside (Lazy Loading) — Most Common
```java
// Spring @Cacheable implements this automatically
@Cacheable(value = "users", key = "#id", unless = "#result == null")
public User findUser(Long id) {
    return userRepository.findById(id).orElse(null); // DB hit only on miss
}

@CacheEvict(value = "users", key = "#user.id")
public User updateUser(User user) {
    return userRepository.save(user); // invalidate on write
}
```

### 2. Write-Through
```java
@CachePut(value = "users", key = "#result.id")  // update cache AND DB
public User saveUser(User user) { return userRepository.save(user); }
```

### 3. Write-Behind (Write-Back)
```java
// Write to cache immediately, async flush to DB
@Async
public void flushToDb(List<CachedItem> items) {
    repository.saveAll(items);
}
// Use with: Redis + Caffeine local cache + scheduled flush
```

## Caffeine (Local In-process Cache)
```java
@Bean
public CacheManager cacheManager() {
    CaffeineCacheManager manager = new CaffeineCacheManager();
    manager.setCaffeine(Caffeine.newBuilder()
        .maximumSize(10_000)
        .expireAfterWrite(Duration.ofMinutes(5))
        .recordStats());    // enable hit/miss metrics
    return manager;
}
// Access stats:
// cache.stats() → CacheStats{hitCount=..., missCount=..., hitRate=...}
```

## Two-Level Cache (L1: Caffeine + L2: Redis)
```java
@Service
public class TwoLevelCache<K, V> {
    private final Cache<K, V> local;   // Caffeine — fast, local
    private final RedisCache redis;    // Redis — shared across instances

    public V get(K key, Supplier<V> loader) {
        V v = local.getIfPresent(key);
        if (v != null) return v;           // L1 hit
        v = redis.get(key);                // L2 hit
        if (v != null) { local.put(key, v); return v; }
        v = loader.get();                  // DB hit
        local.put(key, v); redis.put(key, v);
        return v;
    }
}
```

## Eviction Policies
```
LRU (Least Recently Used)  — Caffeine default, general purpose
LFU (Least Frequently Used) — better for hot/cold data (Caffeine's W-TinyLFU)
TTL (Time To Live)          — time-sensitive data
Size-based                  — maximumSize(N) + weight-based
```

## Cache Stampede Prevention (Java)
```java
// Problem: on cold start, all threads hit DB simultaneously
// Fix: probabilistic early expiration or locking
private final Map<String, CompletableFuture<User>> inFlight = new ConcurrentHashMap<>();

public CompletableFuture<User> getUserAsync(Long id) {
    return inFlight.computeIfAbsent(String.valueOf(id), k ->
        CompletableFuture.supplyAsync(() -> userRepository.findById(id).orElseThrow())
            .whenComplete((r, ex) -> inFlight.remove(k)));
}
// Multiple concurrent callers for same key → only ONE DB call, rest wait on same Future
```


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


# Caching Strategies — Java/Spring

## Cache Patterns

### 1. Cache-Aside (Lazy Loading) — Most Common
```java
// Spring @Cacheable implements this automatically
@Cacheable(value = "users", key = "#id", unless = "#result == null")
public User findUser(Long id) {
    return userRepository.findById(id).orElse(null); // DB hit only on miss
}

@CacheEvict(value = "users", key = "#user.id")
public User updateUser(User user) {
    return userRepository.save(user); // invalidate on write
}
```

### 2. Write-Through
```java
@CachePut(value = "users", key = "#result.id")  // update cache AND DB
public User saveUser(User user) { return userRepository.save(user); }
```

### 3. Write-Behind (Write-Back)
```java
// Write to cache immediately, async flush to DB
@Async
public void flushToDb(List<CachedItem> items) {
    repository.saveAll(items);
}
// Use with: Redis + Caffeine local cache + scheduled flush
```

## Caffeine (Local In-process Cache)
```java
@Bean
public CacheManager cacheManager() {
    CaffeineCacheManager manager = new CaffeineCacheManager();
    manager.setCaffeine(Caffeine.newBuilder()
        .maximumSize(10_000)
        .expireAfterWrite(Duration.ofMinutes(5))
        .recordStats());    // enable hit/miss metrics
    return manager;
}
// Access stats:
// cache.stats() → CacheStats{hitCount=..., missCount=..., hitRate=...}
```

## Two-Level Cache (L1: Caffeine + L2: Redis)
```java
@Service
public class TwoLevelCache<K, V> {
    private final Cache<K, V> local;   // Caffeine — fast, local
    private final RedisCache redis;    // Redis — shared across instances

    public V get(K key, Supplier<V> loader) {
        V v = local.getIfPresent(key);
        if (v != null) return v;           // L1 hit
        v = redis.get(key);                // L2 hit
        if (v != null) { local.put(key, v); return v; }
        v = loader.get();                  // DB hit
        local.put(key, v); redis.put(key, v);
        return v;
    }
}
```

## Eviction Policies
```
LRU (Least Recently Used)  — Caffeine default, general purpose
LFU (Least Frequently Used) — better for hot/cold data (Caffeine's W-TinyLFU)
TTL (Time To Live)          — time-sensitive data
Size-based                  — maximumSize(N) + weight-based
```

## Cache Stampede Prevention (Java)
```java
// Problem: on cold start, all threads hit DB simultaneously
// Fix: probabilistic early expiration or locking
private final Map<String, CompletableFuture<User>> inFlight = new ConcurrentHashMap<>();

public CompletableFuture<User> getUserAsync(Long id) {
    return inFlight.computeIfAbsent(String.valueOf(id), k ->
        CompletableFuture.supplyAsync(() -> userRepository.findById(id).orElseThrow())
            .whenComplete((r, ex) -> inFlight.remove(k)));
}
// Multiple concurrent callers for same key → only ONE DB call, rest wait on same Future
```
