# System Design: Key-Value Store (DynamoDB/Redis)

## Requirements
- get(key) / put(key, value) / delete(key)
- Low latency: <10ms (p99)
- High availability (99.99%)
- Tunable consistency
- Scale to billions of keys

## Architecture

### Data Partitioning — Consistent Hashing
```
Hash ring: each key maps to a server

         S1 (0-90)
       /          \
  S4 (270-360)    S2 (90-180)
       \          /
         S3 (180-270)

key 'user:123' → hash=150 → S2
```

### Replication
```
N = 3 replicas per key
After locating coordinator node, replicate to next N-1 nodes clockwise.

Read/Write Quorum:
  W + R > N → strong consistency
  W=1, R=1 → eventual consistency, max performance
  W=2, R=2 → balance (DynamoDB default)
```

### Storage Engine
```
LSM Tree (Log-Structured Merge Tree) — used by Cassandra, RocksDB
  Write: in-memory memtable → WAL → flush to SSTable (sorted)
  Read:  memtable → bloom filter → SSTables (from newest)
  Compact: background merge of SSTables

  ✅ Fast writes (sequential), ✅ Space efficient
  ❌ Read amplification (multiple SSTables)

B-Tree — used by MySQL, PostgreSQL
  ✅ Fast reads, ✅ Range queries,
  ❌ Random writes (slower than LSM)
```

### Handling Failures
```
Hinted Handoff:
  If target node down → write to next available node
  Include "hint" of intended node
  When target recovers → replay hints

Read Repair:
  If replicas disagree during read → update stale ones
  Version via Vector Clocks

Anti-Entropy:
  Background sync using Merkle Trees
  Hash each subtree → only sync subtrees that differ
```

### Conflict Resolution
```
Last-Write-Wins (LWW): use timestamp → simple, may lose data
Vector Clocks: track causality → detect conflicts → user resolves
  [S1:2, S2:1] → S1 made 2 writes, S2 made 1

CRDT (Conflict-free Replicated Data Types):
  G-Counter, PNCounter, LWW-Element-Set
  → merge is always deterministic
```


# System Design: Key-Value Store (DynamoDB/Redis)

## Requirements
- get(key) / put(key, value) / delete(key)
- Low latency: <10ms (p99)
- High availability (99.99%)
- Tunable consistency
- Scale to billions of keys

## Architecture

### Data Partitioning — Consistent Hashing
```
Hash ring: each key maps to a server

         S1 (0-90)
       /          \
  S4 (270-360)    S2 (90-180)
       \          /
         S3 (180-270)

key 'user:123' → hash=150 → S2
```

### Replication
```
N = 3 replicas per key
After locating coordinator node, replicate to next N-1 nodes clockwise.

Read/Write Quorum:
  W + R > N → strong consistency
  W=1, R=1 → eventual consistency, max performance
  W=2, R=2 → balance (DynamoDB default)
```

### Storage Engine
```
LSM Tree (Log-Structured Merge Tree) — used by Cassandra, RocksDB
  Write: in-memory memtable → WAL → flush to SSTable (sorted)
  Read:  memtable → bloom filter → SSTables (from newest)
  Compact: background merge of SSTables

  ✅ Fast writes (sequential), ✅ Space efficient
  ❌ Read amplification (multiple SSTables)

B-Tree — used by MySQL, PostgreSQL
  ✅ Fast reads, ✅ Range queries,
  ❌ Random writes (slower than LSM)
```

### Handling Failures
```
Hinted Handoff:
  If target node down → write to next available node
  Include "hint" of intended node
  When target recovers → replay hints

Read Repair:
  If replicas disagree during read → update stale ones
  Version via Vector Clocks

Anti-Entropy:
  Background sync using Merkle Trees
  Hash each subtree → only sync subtrees that differ
```

### Conflict Resolution
```
Last-Write-Wins (LWW): use timestamp → simple, may lose data
Vector Clocks: track causality → detect conflicts → user resolves
  [S1:2, S2:1] → S1 made 2 writes, S2 made 1

CRDT (Conflict-free Replicated Data Types):
  G-Counter, PNCounter, LWW-Element-Set
  → merge is always deterministic
```


# System Design: Key-Value Store (DynamoDB/Redis)

## Requirements
- get(key) / put(key, value) / delete(key)
- Low latency: <10ms (p99)
- High availability (99.99%)
- Tunable consistency
- Scale to billions of keys

## Architecture

### Data Partitioning — Consistent Hashing
```
Hash ring: each key maps to a server

         S1 (0-90)
       /          \
  S4 (270-360)    S2 (90-180)
       \          /
         S3 (180-270)

key 'user:123' → hash=150 → S2
```

### Replication
```
N = 3 replicas per key
After locating coordinator node, replicate to next N-1 nodes clockwise.

Read/Write Quorum:
  W + R > N → strong consistency
  W=1, R=1 → eventual consistency, max performance
  W=2, R=2 → balance (DynamoDB default)
```

### Storage Engine
```
LSM Tree (Log-Structured Merge Tree) — used by Cassandra, RocksDB
  Write: in-memory memtable → WAL → flush to SSTable (sorted)
  Read:  memtable → bloom filter → SSTables (from newest)
  Compact: background merge of SSTables

  ✅ Fast writes (sequential), ✅ Space efficient
  ❌ Read amplification (multiple SSTables)

B-Tree — used by MySQL, PostgreSQL
  ✅ Fast reads, ✅ Range queries,
  ❌ Random writes (slower than LSM)
```

### Handling Failures
```
Hinted Handoff:
  If target node down → write to next available node
  Include "hint" of intended node
  When target recovers → replay hints

Read Repair:
  If replicas disagree during read → update stale ones
  Version via Vector Clocks

Anti-Entropy:
  Background sync using Merkle Trees
  Hash each subtree → only sync subtrees that differ
```

### Conflict Resolution
```
Last-Write-Wins (LWW): use timestamp → simple, may lose data
Vector Clocks: track causality → detect conflicts → user resolves
  [S1:2, S2:1] → S1 made 2 writes, S2 made 1

CRDT (Conflict-free Replicated Data Types):
  G-Counter, PNCounter, LWW-Element-Set
  → merge is always deterministic
```
