package snippets.java.db;

/**
 * Advanced SQL Patterns — used with JDBC/JPA
 */
public class AdvancedSQL {

    // All queries below are SQL strings used with JPA @Query or JDBC PreparedStatement

    // 1. Window Functions
    public static final String RANK_BY_DEPARTMENT = """
        SELECT name, department, salary,
               RANK()        OVER w AS rank_in_dept,
               DENSE_RANK()  OVER w AS dense_rank,
               ROW_NUMBER()  OVER w AS row_num,
               LAG(salary)   OVER w AS prev_salary,
               LEAD(salary)  OVER w AS next_salary,
               AVG(salary)   OVER (PARTITION BY department) AS dept_avg,
               SUM(salary)   OVER (PARTITION BY department ORDER BY salary
                                  ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW) AS running_total
        FROM employees
        WINDOW w AS (PARTITION BY department ORDER BY salary DESC)
        ORDER BY department, rank_in_dept;
        """;

    // 2. CTE + Recursive
    public static final String EMPLOYEE_HIERARCHY = """
        WITH RECURSIVE hierarchy AS (
            SELECT id, name, manager_id, 0 AS depth
            FROM employees WHERE manager_id IS NULL
            UNION ALL
            SELECT e.id, e.name, e.manager_id, h.depth + 1
            FROM employees e JOIN hierarchy h ON e.manager_id = h.id
        )
        SELECT id, name, depth FROM hierarchy ORDER BY depth, name;
        """;

    // 3. UPSERT patterns
    public static final String UPSERT_POSTGRES = """
        INSERT INTO user_stats (user_id, login_count, last_login)
        VALUES (?, 1, NOW())
        ON CONFLICT (user_id)
        DO UPDATE SET
            login_count = user_stats.login_count + 1,
            last_login  = EXCLUDED.last_login;
        """;

    // 4. JSON operations (PostgreSQL)
    public static final String JSON_QUERY = """
        SELECT
            id,
            metadata->>'name' AS name,         -- text
            metadata->'address'->>'city' AS city,
            jsonb_array_length(metadata->'tags') AS tag_count
        FROM products
        WHERE metadata @> '{"active": true}'   -- JSON contains
          AND metadata->'price' < '100';
        """;

    // 5. Explain Analyze (for performance tuning)
    public static final String EXPLAIN_EXAMPLE = """
        EXPLAIN (ANALYZE, BUFFERS, FORMAT JSON)
        SELECT u.name, COUNT(o.id)
        FROM users u
        LEFT JOIN orders o ON u.id = o.user_id
        WHERE u.created_at > '2024-01-01'
        GROUP BY u.id
        HAVING COUNT(o.id) > 5;
        -- Look for: Seq Scan (bad on large table), high cost, index misses
        """;

    // 6. Materialized View (pre-compute heavy queries)
    public static final String CREATE_MATERIALIZED_VIEW = """
        CREATE MATERIALIZED VIEW daily_revenue AS
        SELECT
            DATE_TRUNC('day', created_at) AS day,
            SUM(amount) AS total_revenue,
            COUNT(*) AS order_count
        FROM orders
        WHERE status = 'completed'
        GROUP BY 1;

        CREATE UNIQUE INDEX ON daily_revenue (day);

        -- Refresh options:
        REFRESH MATERIALIZED VIEW CONCURRENTLY daily_revenue; -- no lock
        """;

    // 7. Partitioning by range
    public static final String PARTITION_TABLE = """
        CREATE TABLE events (
            id         BIGSERIAL,
            created_at TIMESTAMP NOT NULL,
            payload    JSONB
        ) PARTITION BY RANGE (created_at);

        CREATE TABLE events_2024_q1 PARTITION OF events
            FOR VALUES FROM ('2024-01-01') TO ('2024-04-01');

        CREATE TABLE events_2024_q2 PARTITION OF events
            FOR VALUES FROM ('2024-04-01') TO ('2024-07-01');
        -- Queries automatically routed to correct partition
        """;

    public static void main(String[] args) {
        // These queries would be executed via JPA @Query or JDBC
        System.out.println("SQL patterns library loaded");
        System.out.println("Queries available: Window, CTE, Upsert, JSON, Explain, MatView, Partition");
    }
}
