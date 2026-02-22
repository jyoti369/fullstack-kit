package snippets.java.generics;

import java.util.*;
import java.util.function.*;

/**
 * Java Generics — Advanced Patterns
 */
public class AdvancedGenerics {

    // 1. Bounded wildcards
    public static double sumList(List<? extends Number> list) {
        return list.stream().mapToDouble(Number::doubleValue).sum();
    }
    public static void addNumbers(List<? super Integer> list, int count) {
        for (int i = 0; i < count; i++) list.add(i);
    }

    // 2. Generic methods
    public static <T extends Comparable<T>> T max(List<T> list) {
        return list.stream().max(Comparator.naturalOrder()).orElseThrow();
    }

    public static <A, B> List<B> mapList(List<A> list, Function<A, B> mapper) {
        List<B> result = new ArrayList<>(list.size());
        for (A item : list) result.add(mapper.apply(item));
        return result;
    }

    // 3. Generic pair
    record Pair<A, B>(A first, B second) {
        public Pair<B, A> swap() { return new Pair<>(second, first); }
        public <C> Pair<A, C> mapSecond(Function<B, C> fn) { return new Pair<>(first, fn.apply(second)); }
    }

    // 4. Generic stack
    static class Stack<T> {
        private final Deque<T> deque = new ArrayDeque<>();
        public void push(T item) { deque.push(item); }
        public T pop() { if (deque.isEmpty()) throw new EmptyStackException(); return deque.pop(); }
        public T peek() { return deque.peek(); }
        public boolean isEmpty() { return deque.isEmpty(); }
        public int size() { return deque.size(); }
    }

    // 5. Generic Result type
    sealed interface Result<T> permits Result.Success, Result.Failure {
        record Success<T>(T value) implements Result<T> {}
        record Failure<T>(Throwable cause) implements Result<T> {}

        static <T> Result<T> of(Supplier<T> supplier) {
            try { return new Success<>(supplier.get()); }
            catch (Throwable e) { return new Failure<>(e); }
        }

        default <U> Result<U> map(Function<T, U> fn) {
            return switch (this) {
                case Success<T> s -> Result.of(() -> fn.apply(s.value()));
                case Failure<T> f -> new Result.Failure<>(f.cause());
            };
        }

        default T orElse(T defaultValue) {
            return switch (this) {
                case Success<T> s -> s.value();
                case Failure<T> _ -> defaultValue;
            };
        }
    }

    // 6. Type-safe heterogeneous container (Bloch's pattern)
    static class TypeSafeMap {
        private final Map<Class<?>, Object> map = new HashMap<>();
        public <T> void put(Class<T> type, T instance) { map.put(Objects.requireNonNull(type), instance); }
        public <T> T get(Class<T> type) { return type.cast(map.get(type)); }
    }

    // 7. Recursive generics (Builder pattern)
    abstract static class AbstractBuilder<T, SELF extends AbstractBuilder<T, SELF>> {
        protected String name;
        @SuppressWarnings("unchecked")
        public SELF name(String n) { this.name = n; return (SELF) this; }
        public abstract T build();
    }
    static class PersonBuilder extends AbstractBuilder<String, PersonBuilder> {
        private int age;
        public PersonBuilder age(int a) { this.age = a; return this; }
        public String build() { return name + " (" + age + ")"; }
    }

    public static void main(String[] args) {
        // Wildcards
        System.out.println(sumList(List.of(1, 2.5, 3L))); // 6.5

        // Pair
        Pair<String, Integer> p = new Pair<>("Alice", 30);
        System.out.println(p.swap()); // Pair[first=30, second=Alice]
        System.out.println(p.mapSecond(age -> age + 1)); // Pair[first=Alice, second=31]

        // Result
        Result<Integer> r1 = Result.of(() -> Integer.parseInt("42"));
        Result<Integer> r2 = Result.of(() -> Integer.parseInt("abc"));
        System.out.println(r1.map(n -> n * 2).orElse(-1));  // 84
        System.out.println(r2.map(n -> n * 2).orElse(-1));  // -1

        // Builder
        String person = new PersonBuilder().name("Alice").age(30).build();
        System.out.println(person); // Alice (30)
    }
}
