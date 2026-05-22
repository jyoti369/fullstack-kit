# Database Transactions & ACID Properties

## ACID

### Atomicity
All operations succeed or all fail. No partial updates.
```sql
BEGIN;
  UPDATE accounts SET balance = balance - 100 WHERE id = 1;
  UPDATE accounts SET balance = balance + 100 WHERE id = 2;
COMMIT;  -- both succeed, or both roll back
```

### Consistency
Transaction brings DB from one valid state to another.
Constraints (FK, UNIQUE, NOT NULL) are never violated.

### Isolation
Concurrent transactions don't interfere.
Controlled by isolation level:

```
Isolation Level  | Dirty Read | Non-Rep Read | Phantom Read
READ UNCOMMITTED |     ✅      |      ✅       |     ✅
READ COMMITTED   |     ❌      |      ✅       |     ✅
REPEATABLE READ  |     ❌      |      ❌       |     ✅
SERIALIZABLE    |     ❌      |      ❌       |     ❌

✅ = anomaly CAN occur | ❌ = anomaly prevented
```

### Durability
Committed transactions survive system failure.
Achieved via: WAL (Write Ahead Log), fsync to disk.

## Isolation Anomalies Explained

### Dirty Read
Read data written by an uncommitted transaction.
```sql
-- T1 updates but doesn't commit
-- T2 reads T1's uncommitted value
-- T1 rolls back → T2 read phantom data
```

### Non-Repeatable Read
Same row, same transaction, two different values.
```sql
-- T1: SELECT balance → 100
-- T2: UPDATE balance = 200; COMMIT
-- T1: SELECT balance → 200  (different!)
```

### Phantom Read
Same query, same transaction, different set of rows.
```sql
-- T1: SELECT COUNT(*) WHERE age > 25 → 10
-- T2: INSERT new row with age 30; COMMIT
-- T1: SELECT COUNT(*) WHERE age > 25 → 11  (phantom!)
```

## Locking
```sql
-- Shared lock (read):
SELECT * FROM accounts WHERE id=1 FOR SHARE;

-- Exclusive lock (write):
SELECT * FROM accounts WHERE id=1 FOR UPDATE;

-- Optimistic locking (version column):
UPDATE accounts
SET balance = 200, version = version + 1
WHERE id = 1 AND version = 5;  -- fails if version changed
```

## MVCC (Multi-Version Concurrency Control)
Postgres/MySQL: readers never block writers.
- Each transaction sees a snapshot of DB at transaction start
- Writes create new versions, not overwrite
- Garbage collection removes old versions


# Database Transactions & ACID Properties

## ACID

### Atomicity
All operations succeed or all fail. No partial updates.
```sql
BEGIN;
  UPDATE accounts SET balance = balance - 100 WHERE id = 1;
  UPDATE accounts SET balance = balance + 100 WHERE id = 2;
COMMIT;  -- both succeed, or both roll back
```

### Consistency
Transaction brings DB from one valid state to another.
Constraints (FK, UNIQUE, NOT NULL) are never violated.

### Isolation
Concurrent transactions don't interfere.
Controlled by isolation level:

```
Isolation Level  | Dirty Read | Non-Rep Read | Phantom Read
READ UNCOMMITTED |     ✅      |      ✅       |     ✅
READ COMMITTED   |     ❌      |      ✅       |     ✅
REPEATABLE READ  |     ❌      |      ❌       |     ✅
SERIALIZABLE    |     ❌      |      ❌       |     ❌

✅ = anomaly CAN occur | ❌ = anomaly prevented
```

### Durability
Committed transactions survive system failure.
Achieved via: WAL (Write Ahead Log), fsync to disk.

## Isolation Anomalies Explained

### Dirty Read
Read data written by an uncommitted transaction.
```sql
-- T1 updates but doesn't commit
-- T2 reads T1's uncommitted value
-- T1 rolls back → T2 read phantom data
```

### Non-Repeatable Read
Same row, same transaction, two different values.
```sql
-- T1: SELECT balance → 100
-- T2: UPDATE balance = 200; COMMIT
-- T1: SELECT balance → 200  (different!)
```

### Phantom Read
Same query, same transaction, different set of rows.
```sql
-- T1: SELECT COUNT(*) WHERE age > 25 → 10
-- T2: INSERT new row with age 30; COMMIT
-- T1: SELECT COUNT(*) WHERE age > 25 → 11  (phantom!)
```

## Locking
```sql
-- Shared lock (read):
SELECT * FROM accounts WHERE id=1 FOR SHARE;

-- Exclusive lock (write):
SELECT * FROM accounts WHERE id=1 FOR UPDATE;

-- Optimistic locking (version column):
UPDATE accounts
SET balance = 200, version = version + 1
WHERE id = 1 AND version = 5;  -- fails if version changed
```

## MVCC (Multi-Version Concurrency Control)
Postgres/MySQL: readers never block writers.
- Each transaction sees a snapshot of DB at transaction start
- Writes create new versions, not overwrite
- Garbage collection removes old versions
