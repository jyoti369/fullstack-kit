package snippets.java.spring;

import org.springframework.cache.annotation.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Spring Boot Performance Patterns
 */
public class PerformanceTuning {

    // 1. Caching with Spring Cache (@Cacheable, @CacheEvict)
    @Service
    static class ProductService {
        @Cacheable(value = "products", key = "#id")
        public Product findById(Long id) {
            // Expensive DB call — cached after first invocation
            return fetchFromDB(id);
        }

        @CacheEvict(value = "products", key = "#product.id")
        public Product update(Product product) {
            // Clears cache on update
            return saveToDb(product);
        }

        @CachePut(value = "products", key = "#result.id")
        public Product create(Product product) {
            // Updates cache after insert
            return saveToDb(product);
        }

        @CacheEvict(value = "products", allEntries = true)
        @Scheduled(fixedDelay = 3600000) // every hour
        public void evictAllCaches() { /* cache warm periodically */ }

        private Product fetchFromDB(Long id) { return new Product(); }
        private Product saveToDb(Product p) { return p; }
    }

    // 2. Async methods
    @Service
    static class AsyncService {
        @Async("taskExecutor") // use custom thread pool
        public CompletableFuture<String> asyncFetch(Long id) {
            // Runs in thread pool, doesn't block HTTP thread
            String result = "result-" + id;
            return CompletableFuture.completedFuture(result);
        }
    }

    // 3. Pagination to avoid N+1 and large result sets
    @org.springframework.data.jpa.repository.JpaRepository
    interface OrderRepo extends org.springframework.data.jpa.repository.JpaRepository<Object, Long> {}

    // 4. Read-only transactions (skip dirty checking overhead)
    @Service
    static class ReadService {
        @Transactional(readOnly = true)
        public List<Object> getAll() { return List.of(); }

        // Batch inserts — avoid N single inserts
        @Transactional
        public void batchInsert(List<Object> items) {
            // spring.jpa.properties.hibernate.jdbc.batch_size=50
            // spring.jpa.properties.hibernate.order_inserts=true
        }
    }

    // 5. Projections — fetch only needed columns
    interface UserSummary {
        Long getId();
        String getName();
        // Only 2 columns fetched, not entire entity
    }

    // 6. Connection pool tuning (HikariCP — Spring Boot default)
    // application.properties:
    // spring.datasource.hikari.maximum-pool-size=20
    // spring.datasource.hikari.minimum-idle=5
    // spring.datasource.hikari.connection-timeout=20000
    // spring.datasource.hikari.idle-timeout=300000
    // spring.datasource.hikari.max-lifetime=1200000

    // 7. Lazy loading for related entities
    @jakarta.persistence.Entity
    static class Product {
        @jakarta.persistence.Id Long id;
        String name;
        // @OneToMany(fetch = FetchType.LAZY) — default, don't change to EAGER
        // Use JOIN FETCH in JPQL when you need the collection
    }

    // JVM Tuning flags (add to JVM args):
    // -Xms512m -Xmx2g              → initial/max heap
    // -XX:+UseG1GC                 → G1 garbage collector (default Java 9+)
    // -XX:MaxGCPauseMillis=200     → target max GC pause
    // -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/tmp/heap.hprof
}
