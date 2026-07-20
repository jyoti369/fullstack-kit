package snippets.java.functional;

import java.util.Optional;

/**
 * Optional — Null-safe Java Patterns
 */
public class OptionalPatterns {

    record Address(String city, String country) {}
    record User(String name, Address address) {}

    // BAD: null-check hell
    static String getCityBad(User user) {
        if (user != null) {
            Address addr = user.address();
            if (addr != null) return addr.city();
        }
        return "Unknown";
    }

    // GOOD: Optional chains
    static Optional<String> getCity(User user) {
        return Optional.ofNullable(user)
            .map(User::address)
            .map(Address::city);
    }

    // Core API
    static void coreApi() {
        Optional<String> name = Optional.of("Alice");
        Optional<String> empty = Optional.empty();
        Optional<String> nullable = Optional.ofNullable(null);

        // Extract value
        name.get();                      // "Alice" (throws if empty)
        name.orElse("Default");          // "Alice"
        empty.orElse("Default");         // "Default"
        empty.orElseGet(() -> computeDefault());  // lazy
        empty.orElseThrow(() -> new IllegalStateException("Missing"));

        // Transform
        name.map(String::toUpperCase);          // Optional["ALICE"]
        name.flatMap(v -> Optional.of(v + "!"));
        name.filter(v -> v.length() > 3);       // Optional["Alice"]

        // Check
        name.isPresent();  // true
        name.isEmpty();    // false (Java 11+)
        name.ifPresent(v -> System.out.println("Name: " + v));
        name.ifPresentOrElse(    // Java 9+
            v -> System.out.println("Found: " + v),
            () -> System.out.println("Not found")
        );

        // Fallback chain (Java 9+)
        Optional<String> result = empty
            .or(() -> Optional.of("fallback"));  // Optional["fallback"]
    }

    // Repository pattern example
    static Optional<User> findUser(Long id) {
        // Simulate DB lookup
        return id == 1L ? Optional.of(new User("Alice", new Address("London", "UK")))
                        : Optional.empty();
    }

    private static String computeDefault() { return "computed"; }

    public static void main(String[] args) {
        // Usage
        User alice  = new User("Alice", new Address("London", "UK"));
        User noAddr = new User("Bob", null);

        System.out.println(getCity(alice).orElse("Unknown"));  // London
        System.out.println(getCity(noAddr).orElse("Unknown")); // Unknown
        System.out.println(getCity(null).orElse("Unknown"));   // Unknown

        // Chain with service call
        findUser(1L)
            .map(User::address)
            .map(Address::city)
            .ifPresentOrElse(
                city -> System.out.println("City: " + city),
                () -> System.out.println("User not found")
            );
    }
}


package snippets.java.functional;

import java.util.Optional;

/**
 * Optional — Null-safe Java Patterns
 */
public class OptionalPatterns {

    record Address(String city, String country) {}
    record User(String name, Address address) {}

    // BAD: null-check hell
    static String getCityBad(User user) {
        if (user != null) {
            Address addr = user.address();
            if (addr != null) return addr.city();
        }
        return "Unknown";
    }

    // GOOD: Optional chains
    static Optional<String> getCity(User user) {
        return Optional.ofNullable(user)
            .map(User::address)
            .map(Address::city);
    }

    // Core API
    static void coreApi() {
        Optional<String> name = Optional.of("Alice");
        Optional<String> empty = Optional.empty();
        Optional<String> nullable = Optional.ofNullable(null);

        // Extract value
        name.get();                      // "Alice" (throws if empty)
        name.orElse("Default");          // "Alice"
        empty.orElse("Default");         // "Default"
        empty.orElseGet(() -> computeDefault());  // lazy
        empty.orElseThrow(() -> new IllegalStateException("Missing"));

        // Transform
        name.map(String::toUpperCase);          // Optional["ALICE"]
        name.flatMap(v -> Optional.of(v + "!"));
        name.filter(v -> v.length() > 3);       // Optional["Alice"]

        // Check
        name.isPresent();  // true
        name.isEmpty();    // false (Java 11+)
        name.ifPresent(v -> System.out.println("Name: " + v));
        name.ifPresentOrElse(    // Java 9+
            v -> System.out.println("Found: " + v),
            () -> System.out.println("Not found")
        );

        // Fallback chain (Java 9+)
        Optional<String> result = empty
            .or(() -> Optional.of("fallback"));  // Optional["fallback"]
    }

    // Repository pattern example
    static Optional<User> findUser(Long id) {
        // Simulate DB lookup
        return id == 1L ? Optional.of(new User("Alice", new Address("London", "UK")))
                        : Optional.empty();
    }

    private static String computeDefault() { return "computed"; }

    public static void main(String[] args) {
        // Usage
        User alice  = new User("Alice", new Address("London", "UK"));
        User noAddr = new User("Bob", null);

        System.out.println(getCity(alice).orElse("Unknown"));  // London
        System.out.println(getCity(noAddr).orElse("Unknown")); // Unknown
        System.out.println(getCity(null).orElse("Unknown"));   // Unknown

        // Chain with service call
        findUser(1L)
            .map(User::address)
            .map(Address::city)
            .ifPresentOrElse(
                city -> System.out.println("City: " + city),
                () -> System.out.println("User not found")
            );
    }
}
