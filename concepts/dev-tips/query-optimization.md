# Database Query Optimization — Practical Guide

## EXPLAIN ANALYZE (PostgreSQL)
```sql
EXPLAIN ANALYZE
SELECT * FROM orders o
JOIN users u ON o.user_id = u.id
WHERE o.status = 'pending'
AND o.created_at > NOW() - INTERVAL '7 days';

-- Look for:
-- Seq Scan = full table scan = needs index
-- Index Scan = good
-- Nested Loop vs Hash Join (hash better for large tables)
-- Actual rows vs Estimated rows (big diff = stale stats)
```

## Index Types
```sql
-- B-Tree (default): =, <, >, BETWEEN, LIKE 'prefix%'
CREATE INDEX idx_user_email ON users(email);

-- Partial index: only index subset of rows (smaller, faster)
CREATE INDEX idx_pending_orders ON orders(user_id)
WHERE status = 'pending';  -- only index pending orders

-- Composite index: order matters!
CREATE INDEX idx_user_created ON orders(user_id, created_at);
-- Good for: WHERE user_id=X, WHERE user_id=X AND created_at>Y
-- Bad for: WHERE created_at>Y alone (leftmost rule!)

-- Covering index: includes all columns query needs
CREATE INDEX idx_orders_cover ON orders(user_id)
INCLUDE (status, total_amount);  -- index-only scan

-- GIN index for full-text search or JSONB
CREATE INDEX idx_content ON articles USING GIN(to_tsvector('english', content));
```

## Common N+1 Problem
```python
# BAD: N+1 queries
users = db.query('SELECT * FROM users LIMIT 100')
for user in users:
    orders = db.query(f'SELECT * FROM orders WHERE user_id = {user.id}')
    # 1 query + 100 queries = 101 queries!

# GOOD: JOIN or IN
result = db.query('''
    SELECT u.*, o.id as order_id, o.total
    FROM users u
    LEFT JOIN orders o ON o.user_id = u.id
    WHERE u.id IN (SELECT id FROM users LIMIT 100)
''')
```

## Query Antipatterns
```sql
-- ❌ Function on indexed column disables index
WHERE YEAR(created_at) = 2024
-- ✅ Use range instead
WHERE created_at BETWEEN '2024-01-01' AND '2024-12-31'

-- ❌ Wildcard at start prevents index use
WHERE email LIKE '%@gmail.com'
-- ✅ Full-text search or reverse index

-- ❌ SELECT * (transfers unnecessary data)
SELECT * FROM users
-- ✅ Select only needed columns
SELECT id, name, email FROM users

-- ❌ OR on different columns (can't use index efficiently)
WHERE first_name = 'John' OR last_name = 'John'
-- ✅ UNION
SELECT * FROM users WHERE first_name = 'John'
UNION
SELECT * FROM users WHERE last_name = 'John'
```

## Pagination
```sql
-- ❌ OFFSET pagination (slow for large offsets, O(n))
SELECT * FROM posts ORDER BY id LIMIT 20 OFFSET 10000;

-- ✅ Cursor pagination (always O(log n))
SELECT * FROM posts
WHERE id > :last_seen_id  -- cursor
ORDER BY id
LIMIT 20;
```
