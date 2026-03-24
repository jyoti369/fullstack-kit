# CAP Theorem & Consistency Models

## CAP Theorem
In a distributed system, you can only guarantee **2 of 3**:

```
| Choose | Sacrifice          | Java/JVM Examples                      |
|--------|--------------------|----------------------------------------|
| CP     | Availability       | HBase, Zookeeper, MongoDB (strong)     |
| AP     | Consistency        | Cassandra, DynamoDB, CouchDB           |
| CA     | Partition Tolerance| Traditional RDBMS (single node)        |
```

> In practice: Network partitions WILL happen → choose CP or AP.

## Consistency Models (weakest → strongest)

```
1. Eventual Consistency
   - All nodes converge eventually
   - Reads may return stale data
   - Used by: DNS, S3, Cassandra
   - Java: @Version field + optimistic locking in JPA

2. Read-Your-Writes
   - You always read your own recent writes
   - Others may still see stale
   - Implement: route reads to primary for the writing user

3. Monotonic Reads
   - Never see older version after reading newer one
   - Implement: sticky sessions for reads

4. Strong Consistency (Linearizability)
   - All reads reflect the most recent write globally
   - Highest latency, lowest availability
   - Used by: Google Spanner, etcd, Zookeeper
```

## Java Distributed Consensus Tools
```
Zookeeper:
  - Leader election, distributed locks
  - Used by Kafka (coordinator), HBase
  - Java client: Apache Curator

etcd:
  - Key-value store with strong consistency
  - Used by Kubernetes
  - Java client: jetcd

Hazelcast:
  - In-process distributed cache and compute
  - CP subsystem for linearizable operations
  - Drop-in Spring Cache implementation
```

## PACELC Trade-off
```
P: Partition → choose Availability or Consistency
E: Else      → choose Latency or Consistency

Examples:
  Amazon DynamoDB: PA/EL (availability + low latency)
  Google Spanner:  PC/EC (consistency always)
  Apache Cassandra: PA/EL (tunable per-query)

In Spring/JPA:
  @Transactional(isolation = Isolation.SERIALIZABLE)   → strongest
  @Transactional(isolation = Isolation.READ_COMMITTED)  → default Postgres
  @Transactional(isolation = Isolation.READ_UNCOMMITTED)→ weakest, dirty reads
```
