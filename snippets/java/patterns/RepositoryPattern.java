package snippets.java.patterns;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;

/**
 * Generic Repository Pattern in Java
 * Abstracts data access, enables easy swapping of implementations.
 */
public class RepositoryPattern {

    interface Entity { Long getId(); }

    // Generic Repository interface
    interface Repository<T extends Entity> {
        Optional<T> findById(Long id);
        List<T> findAll();
        List<T> findWhere(Predicate<T> filter);
        T save(T entity);
        boolean deleteById(Long id);
        long count();
    }

    // In-memory implementation (for testing / prototyping)
    abstract static class InMemoryRepository<T extends Entity> implements Repository<T> {
        protected final ConcurrentHashMap<Long, T> store = new ConcurrentHashMap<>();
        protected final AtomicLong idGen = new AtomicLong(1);

        public Optional<T> findById(Long id) { return Optional.ofNullable(store.get(id)); }
        public List<T> findAll() { return new ArrayList<>(store.values()); }
        public List<T> findWhere(Predicate<T> filter) { return store.values().stream().filter(filter).toList(); }
        public boolean deleteById(Long id) { return store.remove(id) != null; }
        public long count() { return store.size(); }
        protected abstract T assignId(T entity, Long newId);

        public T save(T entity) {
            if (entity.getId() == null) {
                T withId = assignId(entity, idGen.getAndIncrement());
                store.put(withId.getId(), withId);
                return withId;
            }
            store.put(entity.getId(), entity);
            return entity;
        }
    }

    // Domain entity
    record User(Long id, String name, String email, String role) implements Entity {
        public Long getId() { return id; }
    }

    // Concrete repository
    static class UserRepository extends InMemoryRepository<User> {
        public Optional<User> findByEmail(String email) {
            return store.values().stream().filter(u -> u.email().equalsIgnoreCase(email)).findFirst();
        }
        public List<User> findByRole(String role) {
            return findWhere(u -> u.role().equals(role));
        }
        protected User assignId(User u, Long id) { return new User(id, u.name(), u.email(), u.role()); }
    }

    // Service layer depends on interface (not implementation)
    static class UserService {
        private final Repository<User> repo;
        UserService(Repository<User> repo) { this.repo = repo; }

        public User register(String name, String email) {
            User u = new User(null, name, email, "user");
            return repo.save(u);
        }

        public Optional<User> getUser(Long id) { return repo.findById(id); }

        public List<User> getAdmins() { return repo.findWhere(u -> "admin".equals(u.role())); }
    }

    public static void main(String[] args) {
        UserRepository repo = new UserRepository();
        UserService service = new UserService(repo);

        User alice = service.register("Alice", "alice@example.com");
        User bob   = service.register("Bob",   "bob@example.com");
        repo.save(new User(null, "Charlie", "charlie@example.com", "admin"));

        System.out.println(service.getUser(alice.getId()));  // Optional[User[id=1,...]]  
        System.out.println(repo.findByEmail("bob@example.com").map(User::name));  // Optional[Bob]
        System.out.println(service.getAdmins()); // [User[id=3, name=Charlie, role=admin]]
        System.out.println("Total users: " + repo.count()); // 3
    }
}
