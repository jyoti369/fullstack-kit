# Concurrency Control in Distributed Systems

## The Problem
Multiple services/servers modifying shared state simultaneously.
Goal: Avoid lost updates, dirty reads, inconsistent state.

## Pessimistic Locking
```
Lock before read, hold until write complete.

Database-level:
  SELECT ... FOR UPDATE  → exclusive row lock
  SELECT ... FOR SHARE   → shared row lock

Distributed lock (Redis):
  SET lock_key <uuid> NX PX 30000  → atomic, 30s TTL
  DEL lock_key WHERE value=<uuid>  → safe release

+ Strong consistency
- Low throughput (threads wait)
- Deadlock risk
- Lock holder crash = stale lock
```

## Optimistic Locking
```
Read without lock. When writing, verify nothing changed.

Version column:
  SELECT id, balance, version FROM accounts WHERE id=1;
  -- Check balance, compute new_balance
  UPDATE accounts
  SET balance=new_balance, version=version+1
  WHERE id=1 AND version=1;  -- fails if version changed
  -- Check affected rows; if 0 → retry

+ High throughput (no blocking)
- High contention = many retries
- Complex retry logic
```

## Distributed Locks with Redlock
```python
# Redis Redlock — lock across N Redis instances
def acquire_lock(resources, ttl_ms):
    start = time.time()
    acquired_on = []
    for redis_instance in resources:
        ok = redis_instance.set(key, uuid, nx=True, px=ttl_ms)
        if ok: acquired_on.append(redis_instance)
    elapsed = (time.time() - start) * 1000
    validity = ttl_ms - elapsed
    if len(acquired_on) >= (len(resources)//2 + 1) and validity > 0:
        return Lock(acquired_on, uuid, validity)
    else:
        release_lock(acquired_on, uuid)  # release partial
        return None
```

## MVCC (Multi-Version Concurrency Control)
```
Each transaction sees a snapshot of the database.
Readers never block writers, writers never block readers.

Timestamp ordering:
  Each transaction has start_ts and commit_ts
  Read: see all data committed before start_ts
  Write: conflict if another committed between start and commit

Used by:
  PostgreSQL, MySQL InnoDB, CockroachDB, Spanner
```

## Compare-and-Swap (CAS)
```java
// Hardware-level atomic operation
AtomicInteger counter = new AtomicInteger(0);
boolean success = counter.compareAndSet(expected=0, update=1);
// Only updates if current value == expected

// Used in lock-free data structures:
// LinkedList, Queue, Skip List

// In distributed systems:
redis.eval("""
  local current = redis.call('GET', KEYS[1])
  if current == ARGV[1] then
    redis.call('SET', KEYS[1], ARGV[2])
    return 1
  end
  return 0
""", [key], [expected_val, new_val])
```
