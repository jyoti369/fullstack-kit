package snippets.java.db;

import com.zaxxer.hikari.*;
import java.sql.*;
import java.util.*;

/**
 * Java JDBC + HikariCP Connection Pool Patterns
 */
public class JdbcPatterns {

    // ---- HikariCP Setup ----
    public static HikariDataSource createPool() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/mydb");
        config.setUsername("dbuser");
        config.setPassword(System.getenv("DB_PASS"));
        config.setMaximumPoolSize(20);
        config.setMinimumIdle(5);
        config.setConnectionTimeout(20_000);
        config.setIdleTimeout(300_000);
        config.setMaxLifetime(1_200_000);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        return new HikariDataSource(config);
    }

    // ---- CRUD with PreparedStatement ----
    private final HikariDataSource ds;
    JdbcPatterns(HikariDataSource ds) { this.ds = ds; }

    public Optional<User> findById(long id) {
        String sql = "SELECT id, name, email FROM users WHERE id = ?";
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("DB error", e);
        }
    }

    public void insertBatch(List<User> users) {
        String sql = "INSERT INTO users (name, email) VALUES (?, ?)";
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            for (User u : users) {
                ps.setString(1, u.name);
                ps.setString(2, u.email);
                ps.addBatch();  // accumulate
            }
            ps.executeBatch();  // single round-trip to DB
            conn.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Batch insert failed", e);
        }
    }

    public void executeInTransaction(Runnable work) {
        try (Connection conn = ds.getConnection()) {
            conn.setAutoCommit(false);
            Savepoint sp = conn.setSavepoint("before");
            try {
                work.run();
                conn.commit();
            } catch (Exception e) {
                conn.rollback(sp);  // rollback to savepoint
                conn.commit();      // commit the rest (if any)
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private User mapRow(ResultSet rs) throws SQLException {
        return new User(rs.getLong("id"), rs.getString("name"), rs.getString("email"));
    }

    record User(long id, String name, String email) {}

    public static void main(String[] args) throws Exception {
        HikariDataSource ds = createPool();
        JdbcPatterns jdbc = new JdbcPatterns(ds);
        Optional<User> user = jdbc.findById(1);
        user.ifPresent(u -> System.out.println(u.name()));
        ds.close();
    }
}
