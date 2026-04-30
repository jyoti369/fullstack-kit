# System Design Interview Checklist

## Step-by-Step Approach (45 minutes)

```
1. Clarify Requirements (5 min)
   □ Functional requirements — what features?
   □ Scale — users/day, QPS, data size
   □ Latency targets
   □ Consistency vs. availability tradeoffs accepted?
   □ Read-heavy or write-heavy?

2. Estimate Scale (3 min)
   □ DAU: 1M users
   □ QPS: 1M × 10 actions/day / 86400 = ~115 QPS peak × 2..5 = ~500 QPS
   □ Storage: 1M × 1KB/day × 365 = ~365GB/year
   □ Bandwidth: 500 QPS × 50KB = 25 MB/s

3. High-Level Design (10 min)
   □ Client → Load Balancer → Service → DB
   □ Identify main components: API, DB, Cache, Queue, CDN
   □ Sketch data flow for core use cases

4. Data Model (5 min)
   □ Key tables/collections
   □ SQL vs NoSQL decision
   □ Indexes needed

5. Deep Dive (15 min)
   □ Bottlenecks in your design
   □ How to scale each component
   □ Handle failures
   □ Monitoring strategy

6. Trade-offs (5 min)
   □ What you chose and why
   □ What you'd do differently with more time
```

## Key Decisions Framework

```
SQL vs NoSQL:
  SQL   → structured data, ACID, complex queries, relations
  NoSQL → flexible schema, horizontal scale, low latency

Cache Strategy:
  □ Cache-aside (lazy): load on miss, app manages cache
  □ Write-through: write to cache + DB simultaneously
  □ Write-behind: write to cache, async flush to DB

Consistency Level:
  □ Strong: reads always see latest write (expensive)
  □ Eventual: all nodes converge eventually (cheap, scalable)
  □ Read-your-writes: you see your own changes immediately

Scaling Pattern:
  □ Horizontal scaling + stateless services
  □ Database: read replicas → sharding
  □ Cache: local → distributed (Redis Cluster)
  □ Async: synchronous → message queue
```

## Numbers to Know

```
Latency:     SSD 0.1ms | Network 1ms | DB query 5ms | RAM 0.0001ms
Bandwidth:   1Gbps NIC | CDN edge typically 10Gbps+
Storage:     1 photo ~3MB | 1 tweet ~300B | 1 video min ~50MB
Computation: single server ~10K simple HTTP req/sec

1 day = 86,400 seconds
1 million users × 10 actions = 10M/day = ~115/sec peak ×5 = ~575 QPS
```


# System Design Interview Checklist

## Step-by-Step Approach (45 minutes)

```
1. Clarify Requirements (5 min)
   □ Functional requirements — what features?
   □ Scale — users/day, QPS, data size
   □ Latency targets
   □ Consistency vs. availability tradeoffs accepted?
   □ Read-heavy or write-heavy?

2. Estimate Scale (3 min)
   □ DAU: 1M users
   □ QPS: 1M × 10 actions/day / 86400 = ~115 QPS peak × 2..5 = ~500 QPS
   □ Storage: 1M × 1KB/day × 365 = ~365GB/year
   □ Bandwidth: 500 QPS × 50KB = 25 MB/s

3. High-Level Design (10 min)
   □ Client → Load Balancer → Service → DB
   □ Identify main components: API, DB, Cache, Queue, CDN
   □ Sketch data flow for core use cases

4. Data Model (5 min)
   □ Key tables/collections
   □ SQL vs NoSQL decision
   □ Indexes needed

5. Deep Dive (15 min)
   □ Bottlenecks in your design
   □ How to scale each component
   □ Handle failures
   □ Monitoring strategy

6. Trade-offs (5 min)
   □ What you chose and why
   □ What you'd do differently with more time
```

## Key Decisions Framework

```
SQL vs NoSQL:
  SQL   → structured data, ACID, complex queries, relations
  NoSQL → flexible schema, horizontal scale, low latency

Cache Strategy:
  □ Cache-aside (lazy): load on miss, app manages cache
  □ Write-through: write to cache + DB simultaneously
  □ Write-behind: write to cache, async flush to DB

Consistency Level:
  □ Strong: reads always see latest write (expensive)
  □ Eventual: all nodes converge eventually (cheap, scalable)
  □ Read-your-writes: you see your own changes immediately

Scaling Pattern:
  □ Horizontal scaling + stateless services
  □ Database: read replicas → sharding
  □ Cache: local → distributed (Redis Cluster)
  □ Async: synchronous → message queue
```

## Numbers to Know

```
Latency:     SSD 0.1ms | Network 1ms | DB query 5ms | RAM 0.0001ms
Bandwidth:   1Gbps NIC | CDN edge typically 10Gbps+
Storage:     1 photo ~3MB | 1 tweet ~300B | 1 video min ~50MB
Computation: single server ~10K simple HTTP req/sec

1 day = 86,400 seconds
1 million users × 10 actions = 10M/day = ~115/sec peak ×5 = ~575 QPS
```
