# Scalability — Interview Patterns & Answers

## "How would you scale X to 10x users?"

### Scaling Ladder (always start here)
```
1. Vertical scale (bigger server) — easy, has limits
2. Caching (CDN, Redis) — biggest bang for buck
3. Read replicas — for read-heavy workloads
4. Database indexing optimization
5. Horizontal scaling + load balancer
6. Database sharding — last resort, complex
7. Async processing + queues — decouple heavy work
8. Microservices — only when team/scale justifies
```

## Common Bottleneck Patterns

### Hot Key Problem
```
Symptom: One Redis/DB key getting 99% of traffic
Example: Celebrity tweet, viral product

Solutions:
  1. Local in-process cache + small TTL
  2. Scatter: shard hot key → key_shard_1, key_shard_2, ...
  3. Bloom filter before DB (check if exists cheaply)
```

### Write Bottleneck
```
Symptom: DB writes can't keep up

Solutions:
  1. Write buffer in Redis → batch writes
  2. Message queue → async DB writes
  3. Command Query Responsibility Segregation (CQRS)
  4. Eventual consistency (if tolerable)
  5. Database sharding
```

### Read Bottleneck
```
Symptom: DB reads saturated

Solutions:
  1. Read replicas (primary writes, replicas read)
  2. CDN for static content
  3. Application cache (Redis/Memcached)
  4. Full-page cache for anonymous users
  5. Precompute heavy queries (materialized views)
```

### Network Bottleneck
```
Symptom: High latency for global users

Solutions:
  1. CDN (static assets, API Gateway)
  2. Multi-region deployment
  3. Edge computing (Cloudflare Workers)
  4. Reduce payload size (compression, pagination)
  5. HTTP/2 multiplexing, connection pooling
```

## Numbers Every Engineer Should Know
```
Latency:
  L1 cache: 0.5ns     | Memory: 100ns
  SSD read: 100μs     | HDD read: 10ms
  Same DC roundtrip: 500μs
  Cross-country RTT: 150ms

Bandwidth:
  Memory: 50 GB/s     | SSD: 500 MB/s
  1 Gbps LAN: 125 MB/s

Scale:
  1 server: ~10K QPS (simple queries)
  Redis: ~100K ops/sec per node
  Kafka: ~1M msgs/sec per broker
  PostgreSQL: ~10K writes/sec (tuned)

Storage:
  1M users × 1KB profile = 1GB
  1M photos/day × 3MB avg = 3TB/day
```
