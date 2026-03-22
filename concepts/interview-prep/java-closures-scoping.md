# Java Closures and Functional Scoping

## What is a Closure in Java?
A lambda or anonymous class that captures variables from its enclosing scope.

```java
// Java lambda captures `factor` from enclosing scope
int factor = 3;
Function<Integer, Integer> multiply = n -> n * factor; // closure!
System.out.println(multiply.apply(5)); // 15

// Restriction: captured variables must be effectively final
int x = 10;
x = 20; // compile error if x is captured in lambda below!
Supplier<Integer> s = () -> x; // x must not change
```

## Closure State — Counter Pattern
```java
// In Java, closures can't modify captured primitives
// Solution 1: Use AtomicInteger (thread-safe)
AtomicInteger count = new AtomicInteger(0);
Runnable increment = () -> count.incrementAndGet();
increment.run(); increment.run();
System.out.println(count.get()); // 2

// Solution 2: Use an array (reference is final, value is mutable)
int[] state = {0};
Runnable inc = () -> state[0]++;
inc.run(); inc.run();
System.out.println(state[0]); // 2
```

## Currying and Partial Application
```java
// Currying: transform BiFunction<A,B,C> into Function<A, Function<B,C>>
BiFunction<Integer, Integer, Integer> add = (a, b) -> a + b;
Function<Integer, Function<Integer, Integer>> curriedAdd = a -> b -> a + b;

Function<Integer, Integer> add5 = curriedAdd.apply(5);
System.out.println(add5.apply(3));  // 8
System.out.println(add5.apply(10)); // 15

// Partial application via method reference
Predicate<String> longString = s -> s.length() > 5;
List<String> filter(List<String> list, Predicate<String> pred) {
    return list.stream().filter(pred).toList();
}
// filter(words, longString);
```

## Factory Closures (like module pattern)
```java
public static Runnable createTimer(String name) {
    long start = System.currentTimeMillis(); // captured!
    return () -> {
        long elapsed = System.currentTimeMillis() - start;
        System.out.println(name + " took " + elapsed + "ms");
    };
}

// Each call creates a new closure with its own `start` and `name`
Runnable t1 = createTimer("Task A");
Thread.sleep(100);
t1.run(); // Task A took ~100ms
```

## Lazy Evaluation with Suppliers
```java
// Defer expensive computation:
Supplier<Connection> conn = () -> DriverManager.getConnection(url); // not called yet

// Only called when needed:
if (needsDb) {
    try (Connection c = conn.get()) { /* use c */ }
}

// Memoized supplier (compute once)
public static <T> Supplier<T> memoize(Supplier<T> supplier) {
    AtomicReference<T> cache = new AtomicReference<>();
    return () -> cache.updateAndGet(v -> v != null ? v : supplier.get());
}
Supplier<ExpensiveObject> lazy = memoize(() -> new ExpensiveObject());
```
