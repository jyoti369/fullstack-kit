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
