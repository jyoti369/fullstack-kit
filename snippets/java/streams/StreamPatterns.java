package snippets.java.streams;

import java.util.*;
import java.util.stream.*;
import java.util.function.*;

/**
 * Java Stream API — Practical Patterns
 */
public class StreamPatterns {

    record Employee(String name, String dept, double salary) {}

    public static void main(String[] args) {
        List<Employee> employees = List.of(
            new Employee("Alice", "Engineering", 95000),
            new Employee("Bob",   "Engineering", 85000),
            new Employee("Carol", "Marketing",   75000),
            new Employee("Dave",  "Marketing",   80000),
            new Employee("Eve",   "Engineering", 120000)
        );

        // 1. Filter + Map + Collect
        List<String> highEarners = employees.stream()
            .filter(e -> e.salary() > 90000)
            .map(Employee::name)
            .sorted()
            .collect(Collectors.toList());
        System.out.println(highEarners); // [Alice, Eve]

        // 2. Group by department
        Map<String, List<Employee>> byDept = employees.stream()
            .collect(Collectors.groupingBy(Employee::dept));

        // 3. Average salary per department
        Map<String, Double> avgSalaryByDept = employees.stream()
            .collect(Collectors.groupingBy(Employee::dept,
                     Collectors.averagingDouble(Employee::salary)));
        System.out.println(avgSalaryByDept);

        // 4. Statistics
        DoubleSummaryStatistics stats = employees.stream()
            .mapToDouble(Employee::salary)
            .summaryStatistics();
        System.out.printf("Max: %.0f, Avg: %.0f%n", stats.getMax(), stats.getAverage());

        // 5. Partition by condition
        Map<Boolean, List<Employee>> partitioned = employees.stream()
            .collect(Collectors.partitioningBy(e -> e.salary() > 90000));

        // 6. FlatMap (flatten nested lists)
        List<List<Integer>> nested = List.of(List.of(1,2), List.of(3,4), List.of(5));
        List<Integer> flat = nested.stream().flatMap(Collection::stream).collect(Collectors.toList());
        System.out.println(flat); // [1,2,3,4,5]

        // 7. Reduce
        double totalSalary = employees.stream()
            .mapToDouble(Employee::salary)
            .reduce(0, Double::sum);

        // 8. Count by department
        Map<String, Long> countByDept = employees.stream()
            .collect(Collectors.groupingBy(Employee::dept, Collectors.counting()));

        // 9. String joining
        String names = employees.stream()
            .map(Employee::name)
            .collect(Collectors.joining(", ", "[", "]"));
        System.out.println(names); // [Alice, Bob, Carol, Dave, Eve]

        // 10. Custom Collector — top earner per dept
        Map<String, Optional<Employee>> topEarner = employees.stream()
            .collect(Collectors.groupingBy(Employee::dept,
                     Collectors.maxBy(Comparator.comparingDouble(Employee::salary))));
        topEarner.forEach((dept, emp) ->
            emp.ifPresent(e -> System.out.println(dept + ": " + e.name())));
    }
}


package snippets.java.streams;

import java.util.*;
import java.util.stream.*;
import java.util.function.*;

/**
 * Java Stream API — Practical Patterns
 */
public class StreamPatterns {

    record Employee(String name, String dept, double salary) {}

    public static void main(String[] args) {
        List<Employee> employees = List.of(
            new Employee("Alice", "Engineering", 95000),
            new Employee("Bob",   "Engineering", 85000),
            new Employee("Carol", "Marketing",   75000),
            new Employee("Dave",  "Marketing",   80000),
            new Employee("Eve",   "Engineering", 120000)
        );

        // 1. Filter + Map + Collect
        List<String> highEarners = employees.stream()
            .filter(e -> e.salary() > 90000)
            .map(Employee::name)
            .sorted()
            .collect(Collectors.toList());
        System.out.println(highEarners); // [Alice, Eve]

        // 2. Group by department
        Map<String, List<Employee>> byDept = employees.stream()
            .collect(Collectors.groupingBy(Employee::dept));

        // 3. Average salary per department
        Map<String, Double> avgSalaryByDept = employees.stream()
            .collect(Collectors.groupingBy(Employee::dept,
                     Collectors.averagingDouble(Employee::salary)));
        System.out.println(avgSalaryByDept);

        // 4. Statistics
        DoubleSummaryStatistics stats = employees.stream()
            .mapToDouble(Employee::salary)
            .summaryStatistics();
        System.out.printf("Max: %.0f, Avg: %.0f%n", stats.getMax(), stats.getAverage());

        // 5. Partition by condition
        Map<Boolean, List<Employee>> partitioned = employees.stream()
            .collect(Collectors.partitioningBy(e -> e.salary() > 90000));

        // 6. FlatMap (flatten nested lists)
        List<List<Integer>> nested = List.of(List.of(1,2), List.of(3,4), List.of(5));
        List<Integer> flat = nested.stream().flatMap(Collection::stream).collect(Collectors.toList());
        System.out.println(flat); // [1,2,3,4,5]

        // 7. Reduce
        double totalSalary = employees.stream()
            .mapToDouble(Employee::salary)
            .reduce(0, Double::sum);

        // 8. Count by department
        Map<String, Long> countByDept = employees.stream()
            .collect(Collectors.groupingBy(Employee::dept, Collectors.counting()));

        // 9. String joining
        String names = employees.stream()
            .map(Employee::name)
            .collect(Collectors.joining(", ", "[", "]"));
        System.out.println(names); // [Alice, Bob, Carol, Dave, Eve]

        // 10. Custom Collector — top earner per dept
        Map<String, Optional<Employee>> topEarner = employees.stream()
            .collect(Collectors.groupingBy(Employee::dept,
                     Collectors.maxBy(Comparator.comparingDouble(Employee::salary))));
        topEarner.forEach((dept, emp) ->
            emp.ifPresent(e -> System.out.println(dept + ": " + e.name())));
    }
}
