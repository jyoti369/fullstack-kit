# Database Sharding — Complete Guide

## What is Sharding?
Horizontally partitioning data across multiple database instances.
Each shard has a subset of the total data.

## Sharding Strategies

### 1. Range-Based Sharding
```
Shard 1: user_id 1–1,000,000
Shard 2: user_id 1,000,001–2,000,000
Shard 3: user_id 2,000,001+

+ Simple, range queries easy
- Hotspots if new IDs cluster on one shard
```

### 2. Hash-Based Sharding
```
shard = hash(user_id) % num_shards

+ Even distribution
- Range queries hit multiple shards
- Resharding is painful (many keys move)
```

### 3. Directory-Based Sharding
```
Lookup table: user_id → shard_id

+ Flexible, easy to move data
- Lookup service is a bottleneck/SPOF
```

### 4. Geographic Sharding
```
Shard US: users in North America
Shard EU: users in Europe

+ Reduces latency (data close to users)
+ GDPR compliance
- Uneven growth
```

## Problems with Sharding

| Problem | Solution |
|---------|----------|
| Cross-shard queries | Denormalize, or accept slow queries |
| Cross-shard transactions | Saga pattern, avoid when possible |
| Joins | Move join logic to application layer |
| Hotspots | Virtual shards or consistent hashing |
| Resharding | Consistent hashing minimizes movement |

## When to Shard?
```
1. First: Optimize queries, add indexes
2. Then: Read replicas (mostly-read workloads)
3. Then: Caching layer (Redis)
4. Then: Vertical scaling (bigger machine)
5. Last resort: Sharding
```

## Real World
- **Instagram**: Shards by user_id, uses PostgreSQL
- **Twitter**: Shards tweets by tweet_id with logical shards
- **MongoDB**: Built-in sharding with chunks and balancers
- **Vitess**: MySQL sharding used by YouTube, Slack
