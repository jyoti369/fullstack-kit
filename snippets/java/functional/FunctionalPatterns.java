package snippets.java.functional;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * Java Functional Programming Patterns
 */
public class FunctionalPatterns {

    // 1. Function composition
    public static <A,B,C> Function<A,C> compose(Function<A,B> f, Function<B,C> g) {
        return f.andThen(g);
    }

    // 2. Currying
    public static <A,B,C> Function<A, Function<B,C>> curry(BiFunction<A,B,C> f) {
        return a -> b -> f.apply(a, b);
    }

    // 3. Memoization
    public static <K,V> Function<K,V> memoize(Function<K,V> fn) {
        Map<K,V> cache = new HashMap<>();
        return key -> cache.computeIfAbsent(key, fn);
    }

    // 4. Retry decorator
    public static <T> Supplier<T> withRetry(Supplier<T> supplier, int maxAttempts) {
        return () -> {
            RuntimeException last = null;
            for (int i = 0; i < maxAttempts; i++) {
                try { return supplier.get(); }
                catch (RuntimeException e) { last = e; }
            }
            throw last;
        };
    }

    // 5. Pipeline builder using Function
    static class Pipeline<T> {
        private final Function<T, T> steps;
        private Pipeline(Function<T,T> fn) { this.steps = fn; }
        public static <T> Pipeline<T> start() { return new Pipeline<>(Function.identity()); }
        public Pipeline<T> then(Function<T,T> step) { return new Pipeline<>(steps.andThen(step)); }
        public T execute(T input) { return steps.apply(input); }
    }

    public static void main(String[] args) {
        // Function composition
        Function<String, String>  trim  = String::trim;
        Function<String, String>  upper = String::toUpperCase;
        Function<String, Integer> len   = String::length;
        Function<String, Integer> pipeline = trim.andThen(upper).andThen(len);
        System.out.println(pipeline.apply("  hello  ")); // 5

        // Currying
        Function<Integer, Function<Integer, Integer>> add = curry(Integer::sum);
        Function<Integer, Integer> add5 = add.apply(5);
        System.out.println(add5.apply(3));  // 8
        System.out.println(add5.apply(10)); // 15

        // Memoization
        Function<Integer, Long> fib = memoize(n -> {
            if (n <= 1) return (long) n;
            // Note: can't self-reference here without externalized memo
            return (long) n; // simplified
        });

        // Pipeline
        String result = Pipeline.<String>start()
            .then(String::trim)
            .then(String::toLowerCase)
            .then(s -> s.replace(" ", "_"))
            .execute("  Hello World  ");
        System.out.println(result); // hello_world

        // Predicate composition
        Predicate<Integer> isEven = n -> n % 2 == 0;
        Predicate<Integer> isPos  = n -> n > 0;
        List<Integer> nums = List.of(-4,-1,0,2,3,6,8);
        List<Integer> evenPositive = nums.stream()
            .filter(isEven.and(isPos))
            .collect(Collectors.toList());
        System.out.println(evenPositive); // [2, 6, 8]
    }
}


package snippets.java.functional;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * Java Functional Programming Patterns
 */
public class FunctionalPatterns {

    // 1. Function composition
    public static <A,B,C> Function<A,C> compose(Function<A,B> f, Function<B,C> g) {
        return f.andThen(g);
    }

    // 2. Currying
    public static <A,B,C> Function<A, Function<B,C>> curry(BiFunction<A,B,C> f) {
        return a -> b -> f.apply(a, b);
    }

    // 3. Memoization
    public static <K,V> Function<K,V> memoize(Function<K,V> fn) {
        Map<K,V> cache = new HashMap<>();
        return key -> cache.computeIfAbsent(key, fn);
    }

    // 4. Retry decorator
    public static <T> Supplier<T> withRetry(Supplier<T> supplier, int maxAttempts) {
        return () -> {
            RuntimeException last = null;
            for (int i = 0; i < maxAttempts; i++) {
                try { return supplier.get(); }
                catch (RuntimeException e) { last = e; }
            }
            throw last;
        };
    }

    // 5. Pipeline builder using Function
    static class Pipeline<T> {
        private final Function<T, T> steps;
        private Pipeline(Function<T,T> fn) { this.steps = fn; }
        public static <T> Pipeline<T> start() { return new Pipeline<>(Function.identity()); }
        public Pipeline<T> then(Function<T,T> step) { return new Pipeline<>(steps.andThen(step)); }
        public T execute(T input) { return steps.apply(input); }
    }

    public static void main(String[] args) {
        // Function composition
        Function<String, String>  trim  = String::trim;
        Function<String, String>  upper = String::toUpperCase;
        Function<String, Integer> len   = String::length;
        Function<String, Integer> pipeline = trim.andThen(upper).andThen(len);
        System.out.println(pipeline.apply("  hello  ")); // 5

        // Currying
        Function<Integer, Function<Integer, Integer>> add = curry(Integer::sum);
        Function<Integer, Integer> add5 = add.apply(5);
        System.out.println(add5.apply(3));  // 8
        System.out.println(add5.apply(10)); // 15

        // Memoization
        Function<Integer, Long> fib = memoize(n -> {
            if (n <= 1) return (long) n;
            // Note: can't self-reference here without externalized memo
            return (long) n; // simplified
        });

        // Pipeline
        String result = Pipeline.<String>start()
            .then(String::trim)
            .then(String::toLowerCase)
            .then(s -> s.replace(" ", "_"))
            .execute("  Hello World  ");
        System.out.println(result); // hello_world

        // Predicate composition
        Predicate<Integer> isEven = n -> n % 2 == 0;
        Predicate<Integer> isPos  = n -> n > 0;
        List<Integer> nums = List.of(-4,-1,0,2,3,6,8);
        List<Integer> evenPositive = nums.stream()
            .filter(isEven.and(isPos))
            .collect(Collectors.toList());
        System.out.println(evenPositive); // [2, 6, 8]
    }
}
