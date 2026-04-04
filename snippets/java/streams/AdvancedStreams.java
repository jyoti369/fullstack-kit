package snippets.java.streams;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * Java Streams — Advanced Patterns and Functional Programming
 */
public class AdvancedStreams {

    record Person(String name, int age, String city, double salary) {}

    // 1. Complex grouping and statistics
    public static void groupingDemo(List<Person> people) {
        // Group by city, count per city
        Map<String, Long> byCity = people.stream()
            .collect(Collectors.groupingBy(Person::city, Collectors.counting()));

        // Average salary per city
        Map<String, Double> avgSalary = people.stream()
            .collect(Collectors.groupingBy(Person::city, Collectors.averagingDouble(Person::salary)));

        // Partition by age > 30
        Map<Boolean, List<Person>> partition = people.stream()
            .collect(Collectors.partitioningBy(p -> p.age() > 30));

        // Multi-level grouping: city → age bracket → list
        Map<String, Map<String, List<Person>>> nested = people.stream()
            .collect(Collectors.groupingBy(Person::city,
                Collectors.groupingBy(p -> p.age() < 30 ? "junior" : "senior")));

        System.out.println(byCity);
        System.out.println(avgSalary);
    }

    // 2. Custom collectors
    public static <T> Collector<T, ?, Optional<T>> toSingleton() {
        return Collectors.collectingAndThen(Collectors.toList(), list -> {
            if (list.size() > 1) throw new IllegalStateException("Expected at most 1 element");
            return list.stream().findFirst();
        });
    }

    // 3. FlatMap — flatten nested collections
    public static void flatMapDemo(List<List<Integer>> matrix) {
        List<Integer> flat = matrix.stream().flatMap(Collection::stream).toList();
        System.out.println(flat);

        // Distinct words across multiple sentences
        List<String> sentences = List.of("hello world", "stream api", "hello java");
        Set<String> unique = sentences.stream()
            .flatMap(s -> Arrays.stream(s.split(" ")))
            .collect(Collectors.toSet());
        System.out.println(unique);
    }

    // 4. Parallel streams (use for CPU-bound, >= 10K elements)
    public static long parallelSum(List<Long> numbers) {
        return numbers.parallelStream()
            .mapToLong(Long::longValue)
            .sum();
    }

    // 5. Infinite streams with generate/iterate
    public static void infiniteStreams() {
        // Fibonacci sequence
        List<Long> fib = Stream.iterate(new long[]{0, 1}, f -> new long[]{f[1], f[0]+f[1]})
            .limit(10)
            .map(f -> f[0])
            .toList();
        System.out.println(fib); // [0, 1, 1, 2, 3, 5, 8, 13, 21, 34]

        // Random integers -- limit to 5 distinct evens
        List<Integer> evens = new Random().ints(1, 100)
            .filter(n -> n % 2 == 0)
            .distinct()
            .limit(5)
            .boxed()
            .toList();
        System.out.println(evens);
    }

    // 6. Reduce — custom aggregation
    public static void reduceDemo(List<Integer> nums) {
        // Sum
        int sum = nums.stream().reduce(0, Integer::sum);
        // Product
        Optional<Integer> product = nums.stream().reduce((a, b) -> a * b);
        // Max by custom logic
        Optional<Integer> secondMax = nums.stream().sorted(Comparator.reverseOrder()).skip(1).findFirst();
        System.out.println(sum + " " + product + " " + secondMax);
    }

    // 7. toMap with merge function (handle duplicates)
    public static void toMapDemo(List<Person> people) {
        // name → salary (keep higher salary on duplicate names)
        Map<String, Double> salaryMap = people.stream()
            .collect(Collectors.toMap(Person::name, Person::salary,
                (existing, replacement) -> Math.max(existing, replacement)));
        System.out.println(salaryMap);
    }

    public static void main(String[] args) {
        List<Person> people = List.of(
            new Person("Alice", 28, "NYC", 95000),
            new Person("Bob", 35, "LA", 88000),
            new Person("Charlie", 42, "NYC", 120000),
            new Person("Diana", 29, "LA", 75000)
        );
        groupingDemo(people);
        flatMapDemo(List.of(List.of(1,2,3), List.of(4,5,6)));
        infiniteStreams();
        reduceDemo(List.of(1,2,3,4,5));
    }
}
