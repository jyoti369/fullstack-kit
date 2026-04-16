# Database Indexing — Deep Dive

## What is an Index?
An index is a data structure (typically B-Tree or Hash) that speeds up data retrieval at the cost of additional storage and slower writes.

## Types of Indexes

### 1. B-Tree Index (Default)
- Balanced tree structure with O(log n) lookups
- Supports range queries, sorting, and prefix matching
- Best for: `WHERE`, `ORDER BY`, `GROUP BY`

### 2. Hash Index
- O(1) lookups for exact matches
- Cannot handle range queries
- Best for: exact equality checks

### 3. Composite Index
```sql
CREATE INDEX idx_user_status_date ON orders(user_id, status, created_at);
```
- Follows **leftmost prefix rule** — query must use columns left-to-right
- ✅ `WHERE user_id = 1 AND status = 'active'`
- ❌ `WHERE status = 'active'` (skips user_id)

### 4. Covering Index
An index that contains all columns needed by a query — avoids table lookup.
```sql
CREATE INDEX idx_covering ON users(email) INCLUDE (name, avatar);
```

## When NOT to Index
- Small tables (< 1000 rows)
- Columns with low cardinality (e.g., boolean)
- Write-heavy tables with infrequent reads
- Columns used with functions: `WHERE LOWER(email)` won't use index on `email`

## Monitoring
```sql
-- PostgreSQL: find unused indexes
SELECT indexrelname, idx_scan
FROM pg_stat_user_indexes
WHERE idx_scan = 0
ORDER BY pg_relation_size(indexrelid) DESC;
```


# Database Indexing — Deep Dive

## What is an Index?
An index is a data structure (typically B-Tree or Hash) that speeds up data retrieval at the cost of additional storage and slower writes.

## Types of Indexes

### 1. B-Tree Index (Default)
- Balanced tree structure with O(log n) lookups
- Supports range queries, sorting, and prefix matching
- Best for: `WHERE`, `ORDER BY`, `GROUP BY`

### 2. Hash Index
- O(1) lookups for exact matches
- Cannot handle range queries
- Best for: exact equality checks

### 3. Composite Index
```sql
CREATE INDEX idx_user_status_date ON orders(user_id, status, created_at);
```
- Follows **leftmost prefix rule** — query must use columns left-to-right
- ✅ `WHERE user_id = 1 AND status = 'active'`
- ❌ `WHERE status = 'active'` (skips user_id)

### 4. Covering Index
An index that contains all columns needed by a query — avoids table lookup.
```sql
CREATE INDEX idx_covering ON users(email) INCLUDE (name, avatar);
```

### 5. Partial Index (PostgreSQL)
```sql
-- Only index active orders — smaller, faster
CREATE INDEX idx_active_orders ON orders(user_id) WHERE status = 'active';
```

## When NOT to Index
- Small tables (< 1000 rows)
- Columns with low cardinality (e.g., boolean)
- Write-heavy tables with infrequent reads
- Columns used with functions: `WHERE LOWER(email)` won't use index on `email`
  - Fix: `CREATE INDEX idx_email_lower ON users(LOWER(email));` (functional index)

## Spring Data JPA — Index Hints
```java
@Entity
@Table(indexes = {
    @Index(name = "idx_user_email", columnList = "email", unique = true),
    @Index(name = "idx_order_user", columnList = "user_id,status")
})
public class Order { ... }

// Force index hint in JPQL
@Query(value = "SELECT * FROM orders USE INDEX(idx_order_user) WHERE user_id = :uid",
       nativeQuery = true)
List<Order> findByUser(Long uid);
```

## Monitoring (PostgreSQL)
```sql
-- Find unused indexes
SELECT indexrelname, idx_scan
FROM pg_stat_user_indexes
WHERE idx_scan = 0
ORDER BY pg_relation_size(indexrelid) DESC;

-- Index hit rate
SELECT round(100.0 * idx_blks_hit / NULLIF(idx_blks_hit + idx_blks_read, 0), 2) AS "index_hit_rate"
FROM pg_statio_user_tables WHERE schemaname = 'public';
```
