-- =============================================================
-- SQL Common Queries Cheatsheet
-- Useful patterns for everyday backend development
-- =============================================================

-- 1. Pagination with OFFSET/LIMIT
SELECT id, name, email
FROM users
ORDER BY created_at DESC
LIMIT 20 OFFSET 40; -- Page 3, 20 items/page

-- 2. Cursor-based pagination (better for large datasets)
SELECT id, name, email
FROM users
WHERE id > :last_seen_id
ORDER BY id ASC
LIMIT 20;

-- 3. Upsert (INSERT or UPDATE)
-- PostgreSQL
INSERT INTO user_preferences (user_id, theme, language)
VALUES (1, 'dark', 'en')
ON CONFLICT (user_id)
DO UPDATE SET theme = EXCLUDED.theme, language = EXCLUDED.language;

-- 4. Running totals with Window Functions
SELECT
  date,
  amount,
  SUM(amount) OVER (ORDER BY date) AS running_total,
  ROW_NUMBER() OVER (ORDER BY date) AS row_num
FROM transactions;

-- 5. Find duplicates
SELECT email, COUNT(*) AS count
FROM users
GROUP BY email
HAVING COUNT(*) > 1;

-- 6. Recursive CTE — org chart / tree traversal
WITH RECURSIVE org_tree AS (
  SELECT id, name, manager_id, 1 AS depth
  FROM employees WHERE manager_id IS NULL
  UNION ALL
  SELECT e.id, e.name, e.manager_id, ot.depth + 1
  FROM employees e
  JOIN org_tree ot ON e.manager_id = ot.id
)
SELECT * FROM org_tree ORDER BY depth, name;

-- 7. JSON operations (PostgreSQL)
SELECT
  id,
  metadata->>'name' AS name,
  metadata->'address'->>'city' AS city
FROM profiles
WHERE metadata @> '{"active": true}';
