# System Design: Distributed Cache

## Why Distributed Cache?
- Single Redis node: ~100K ops/sec, ~100GB RAM limit
- Distributed: millions of ops/sec, TB of memory
- Fault tolerance: data survives node failure

## Data Partitioning

### Consistent Hashing
```
Hash ring: 0 ─────────────────── 2³²─┐
Nodes:     ──N1──────N2──────N3──────┘

Key k → hash(k) → find next clockwise node

Add node N4 between N2-N3:
  Only keys between N2-N4 move to N4
  Other keys unaffected → minimal resharding
```

### Virtual Nodes
Each physical node has V virtual positions on the ring.
```
N1 → positions: 12, 45, 78, 112, ...
N2 → positions: 7, 34, 89, 134, ...

+ Better load distribution
+ Handles heterogeneous node sizes (more vnodes = more data)
```

## Replication
```
Write to primary shard → async replicate to N replicas

Quorum reads/writes:
  N=3 replicas, W=2 (write quorum), R=2 (read quorum)
  W + R > N → strong consistency

Eventual consistency:
  W=1, R=1 → fastest but may read stale data
```

## Redis Cluster Architecture
```
16384 hash slots divided among nodes:
  Node A: slots 0-5460
  Node B: slots 5461-10922
  Node C: slots 10923-16383

Each node has replicas:
  A-primary + A-replica
  B-primary + B-replica
  C-primary + C-replica

Failover: if A-primary dies, A-replica promoted automatically
```

## Eviction Policies (Redis)
```
noeviction   → error on new writes (cache is full)
allkeys-lru  → evict least recently used across all keys
volatile-lru → LRU only among keys with TTL
allkeys-random → random eviction
volatile-ttl → evict soonest-to-expire first
```

## Thundering Herd Protection
```python
# Probabilistic early expiration
import math, random

def get_with_pex(key, beta=1.0):
    value, ttl, delta = cache.get_with_metadata(key)
    if value is None: return reload(key)
    # Recompute if: current_time - delta*beta*log(random) > expiry
    if time.time() - delta * beta * math.log(random.random()) > cache.expiry(key):
        return reload(key)  # Proactively refresh
    return value
```


# System Design: Distributed Cache

## Why Distributed Cache?
- Single Redis node: ~100K ops/sec, ~100GB RAM limit
- Distributed: millions of ops/sec, TB of memory
- Fault tolerance: data survives node failure

## Data Partitioning

### Consistent Hashing
```
Hash ring: 0 ─────────────────── 2³²─┐
Nodes:     ──N1──────N2──────N3──────┘

Key k → hash(k) → find next clockwise node

Add node N4 between N2-N3:
  Only keys between N2-N4 move to N4
  Other keys unaffected → minimal resharding
```

### Virtual Nodes
Each physical node has V virtual positions on the ring.
```
N1 → positions: 12, 45, 78, 112, ...
N2 → positions: 7, 34, 89, 134, ...

+ Better load distribution
+ Handles heterogeneous node sizes (more vnodes = more data)
```

## Replication
```
Write to primary shard → async replicate to N replicas

Quorum reads/writes:
  N=3 replicas, W=2 (write quorum), R=2 (read quorum)
  W + R > N → strong consistency

Eventual consistency:
  W=1, R=1 → fastest but may read stale data
```

## Redis Cluster Architecture
```
16384 hash slots divided among nodes:
  Node A: slots 0-5460
  Node B: slots 5461-10922
  Node C: slots 10923-16383

Each node has replicas:
  A-primary + A-replica
  B-primary + B-replica
  C-primary + C-replica

Failover: if A-primary dies, A-replica promoted automatically
```

## Eviction Policies (Redis)
```
noeviction   → error on new writes (cache is full)
allkeys-lru  → evict least recently used across all keys
volatile-lru → LRU only among keys with TTL
allkeys-random → random eviction
volatile-ttl → evict soonest-to-expire first
```

## Thundering Herd Protection
```python
# Probabilistic early expiration
import math, random

def get_with_pex(key, beta=1.0):
    value, ttl, delta = cache.get_with_metadata(key)
    if value is None: return reload(key)
    # Recompute if: current_time - delta*beta*log(random) > expiry
    if time.time() - delta * beta * math.log(random.random()) > cache.expiry(key):
        return reload(key)  # Proactively refresh
    return value
```
