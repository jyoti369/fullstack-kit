package snippets.java.modern;

import java.util.*;
import java.util.stream.*;

/**
 * Modern Java Features (Java 16-21)
 */
public class ModernJavaFeatures {

    // ---- Records (Java 16) ----
    record Point(double x, double y) {
        // Compact constructor for validation
        Point {
            if (Double.isNaN(x) || Double.isNaN(y)) throw new IllegalArgumentException("NaN not allowed");
        }
        // Custom method
        double distanceTo(Point other) {
            return Math.sqrt((x-other.x)*(x-other.x) + (y-other.y)*(y-other.y));
        }
    }

    record PageRequest(int page, int size, String sortBy) {
        // Default constructor built-in canonic constructor
        // Auto-generated: equals, hashCode, toString, getters
    }

    // ---- Sealed Classes (Java 17) ----
    sealed interface Shape permits Circle, Rectangle, Triangle {}
    record Circle(double radius) implements Shape { double area() { return Math.PI*radius*radius; } }
    record Rectangle(double w, double h) implements Shape { double area() { return w*h; } }
    record Triangle(double base, double height) implements Shape { double area() { return 0.5*base*height; } }

    static double getArea(Shape shape) {
        return switch (shape) { // Pattern matching (Java 21 — full)
            case Circle c    -> c.area();
            case Rectangle r -> r.area();
            case Triangle t  -> t.area();
        };
    }

    // ---- Result Type (sealed classes for error handling) ----
    sealed interface Result<T> permits Result.OK, Result.Err {
        record OK<T>(T value) implements Result<T> {}
        record Err<T>(String error, int code) implements Result<T> {}
    }

    static Result<String> divide(int a, int b) {
        if (b == 0) return new Result.Err<>("Division by zero", 400);
        return new Result.OK<>("" + (a / b));
    }

    // ---- Text Blocks (Java 15) ----
    static final String JSON = """
        {
            "name": "Alice",
            "role": "engineer"
        }
        """;

    static final String SQL = """
        SELECT u.name, COUNT(o.id) as order_count
        FROM users u
        LEFT JOIN orders o ON u.id = o.user_id
        GROUP BY u.name
        HAVING order_count > 5
        """;

    // ---- Pattern Matching instanceof (Java 16) ----
    static String describe(Object obj) {
        if (obj instanceof String s && !s.isEmpty()) return "Non-empty string: " + s.toUpperCase();
        if (obj instanceof Integer i && i > 0) return "Positive int: " + i;
        if (obj instanceof List<?> list) return "List of size: " + list.size();
        return "Unknown: " + obj;
    }

    // ---- SequencedCollections (Java 21) ----
    static void sequencedDemo() {
        List<String> list = new ArrayList<>(List.of("a","b","c"));
        String first = list.getFirst();  // Java 21
        String last  = list.getLast();
        list.addFirst("x");
        list.addLast("z");
        List<String> reversed = list.reversed();
        System.out.println(reversed);
    }

    public static void main(String[] args) {
        Point p1 = new Point(0, 0), p2 = new Point(3, 4);
        System.out.println(p1.distanceTo(p2)); // 5.0

        Shape[] shapes = {new Circle(5), new Rectangle(3,4), new Triangle(6,8)};
        for (Shape s : shapes) System.out.printf("Area: %.2f%n", getArea(s));

        var result = divide(10, 2);
        switch (result) {
            case Result.OK<String> ok  -> System.out.println("Answer: " + ok.value());
            case Result.Err<String> e  -> System.err.println("Error " + e.code() + ": " + e.error());
        }

        System.out.println(describe("hello"));  // Non-empty string: HELLO
        System.out.println(describe(42));        // Positive int: 42
    }
}
