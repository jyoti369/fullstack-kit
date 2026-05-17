package snippets.java.concurrent;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

/**
 * CompletableFuture — Java's Answer to Promises
 * Equivalent to JS Promise.then/catch/finally/all/race/allSettled
 */
public class PromisePatterns {

    static CompletableFuture<String> fetchUser(int id) {
        return CompletableFuture.supplyAsync(() -> {
            if (id <= 0) throw new IllegalArgumentException("Invalid id");
            return "User-" + id;
        });
    }

    // 1. Chaining (.thenApply, .thenCompose, .thenAccept)
    public static void chainingDemo() {
        fetchUser(1)
            .thenApply(u -> u.toUpperCase())             // transform result
            .thenCompose(u -> CompletableFuture.supplyAsync(() -> u + "_ORDERS")) // flat-map
            .thenAccept(result -> System.out.println("Final: " + result))
            .exceptionally(ex -> { System.err.println("Error: " + ex.getMessage()); return null; })
            .join();
    }

    // 2. allOf — like Promise.all (wait for ALL)
    public static List<String> fetchAll(List<Integer> ids) {
        List<CompletableFuture<String>> futures = ids.stream().map(PromisePatterns::fetchUser).toList();
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
            .thenApply(v -> futures.stream().map(CompletableFuture::join).toList())
            .join();
    }

    // 3. anyOf — like Promise.race (first to complete wins)
    public static String race(List<CompletableFuture<String>> futures) {
        return (String) CompletableFuture.anyOf(futures.toArray(new CompletableFuture[0])).join();
    }

    // 4. allSettled — collect results AND errors
    public static List<String> allSettled(List<Integer> ids) {
        List<CompletableFuture<String>> futures = ids.stream()
            .map(id -> fetchUser(id)
                .exceptionally(ex -> "ERROR: " + ex.getMessage()))
            .toList();
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
            .thenApply(v -> futures.stream().map(CompletableFuture::join).toList())
            .join();
    }

    // 5. Retry with exponential backoff
    public static CompletableFuture<String> retry(int id, int maxAttempts, long baseDelayMs) {
        return fetchUser(id).exceptionallyCompose(ex -> {
            if (maxAttempts <= 0) return CompletableFuture.failedFuture(ex);
            System.out.printf("Retry in %dms (%d attempts left)%n", baseDelayMs, maxAttempts);
            return CompletableFuture.supplyAsync(() -> null,
                    CompletableFuture.delayedExecutor(baseDelayMs, TimeUnit.MILLISECONDS))
                .thenCompose(v -> retry(id, maxAttempts - 1, baseDelayMs * 2));
        });
    }

    // 6. Timeout
    public static CompletableFuture<String> withTimeout(CompletableFuture<String> future, long ms) {
        return future.orTimeout(ms, TimeUnit.MILLISECONDS) // Java 9+
            .exceptionally(ex -> ex instanceof TimeoutException ? "DEFAULT" : "ERROR");
    }

    // 7. Debounce (using ScheduledExecutorService)
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static ScheduledFuture<?> debounceTask;

    public static void debounce(Runnable action, long delayMs) {
        if (debounceTask != null && !debounceTask.isDone()) debounceTask.cancel(false);
        debounceTask = scheduler.schedule(action, delayMs, TimeUnit.MILLISECONDS);
    }

    public static void main(String[] args) throws Exception {
        chainingDemo();
        System.out.println(fetchAll(List.of(1, 2, 3)));             // [User-1, User-2, User-3]
        System.out.println(allSettled(List.of(1, -1, 3)));          // [User-1, ERROR:..., User-3]
        System.out.println(retry(1, 3, 100).get(5, TimeUnit.SECONDS)); // User-1
        scheduler.shutdown();
    }
}


package snippets.java.concurrent;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

/**
 * CompletableFuture — Java's Answer to Promises
 * Equivalent to JS Promise.then/catch/finally/all/race/allSettled
 */
public class PromisePatterns {

    static CompletableFuture<String> fetchUser(int id) {
        return CompletableFuture.supplyAsync(() -> {
            if (id <= 0) throw new IllegalArgumentException("Invalid id");
            return "User-" + id;
        });
    }

    // 1. Chaining (.thenApply, .thenCompose, .thenAccept)
    public static void chainingDemo() {
        fetchUser(1)
            .thenApply(u -> u.toUpperCase())             // transform result
            .thenCompose(u -> CompletableFuture.supplyAsync(() -> u + "_ORDERS")) // flat-map
            .thenAccept(result -> System.out.println("Final: " + result))
            .exceptionally(ex -> { System.err.println("Error: " + ex.getMessage()); return null; })
            .join();
    }

    // 2. allOf — like Promise.all (wait for ALL)
    public static List<String> fetchAll(List<Integer> ids) {
        List<CompletableFuture<String>> futures = ids.stream().map(PromisePatterns::fetchUser).toList();
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
            .thenApply(v -> futures.stream().map(CompletableFuture::join).toList())
            .join();
    }

    // 3. anyOf — like Promise.race (first to complete wins)
    public static String race(List<CompletableFuture<String>> futures) {
        return (String) CompletableFuture.anyOf(futures.toArray(new CompletableFuture[0])).join();
    }

    // 4. allSettled — collect results AND errors
    public static List<String> allSettled(List<Integer> ids) {
        List<CompletableFuture<String>> futures = ids.stream()
            .map(id -> fetchUser(id)
                .exceptionally(ex -> "ERROR: " + ex.getMessage()))
            .toList();
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
            .thenApply(v -> futures.stream().map(CompletableFuture::join).toList())
            .join();
    }

    // 5. Retry with exponential backoff
    public static CompletableFuture<String> retry(int id, int maxAttempts, long baseDelayMs) {
        return fetchUser(id).exceptionallyCompose(ex -> {
            if (maxAttempts <= 0) return CompletableFuture.failedFuture(ex);
            System.out.printf("Retry in %dms (%d attempts left)%n", baseDelayMs, maxAttempts);
            return CompletableFuture.supplyAsync(() -> null,
                    CompletableFuture.delayedExecutor(baseDelayMs, TimeUnit.MILLISECONDS))
                .thenCompose(v -> retry(id, maxAttempts - 1, baseDelayMs * 2));
        });
    }

    // 6. Timeout
    public static CompletableFuture<String> withTimeout(CompletableFuture<String> future, long ms) {
        return future.orTimeout(ms, TimeUnit.MILLISECONDS) // Java 9+
            .exceptionally(ex -> ex instanceof TimeoutException ? "DEFAULT" : "ERROR");
    }

    // 7. Debounce (using ScheduledExecutorService)
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static ScheduledFuture<?> debounceTask;

    public static void debounce(Runnable action, long delayMs) {
        if (debounceTask != null && !debounceTask.isDone()) debounceTask.cancel(false);
        debounceTask = scheduler.schedule(action, delayMs, TimeUnit.MILLISECONDS);
    }

    public static void main(String[] args) throws Exception {
        chainingDemo();
        System.out.println(fetchAll(List.of(1, 2, 3)));             // [User-1, User-2, User-3]
        System.out.println(allSettled(List.of(1, -1, 3)));          // [User-1, ERROR:..., User-3]
        System.out.println(retry(1, 3, 100).get(5, TimeUnit.SECONDS)); // User-1
        scheduler.shutdown();
    }
}
