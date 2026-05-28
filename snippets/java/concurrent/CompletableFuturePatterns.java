package snippets.java.concurrent;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * CompletableFuture — Async Programming in Java
 */
public class CompletableFuturePatterns {

    private static final ExecutorService executor =
        Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    // 1. Basic async computation
    public static CompletableFuture<String> fetchUser(Long userId) {
        return CompletableFuture.supplyAsync(() -> {
            // simulate DB call
            return "User:" + userId;
        }, executor);
    }

    // 2. Chain (thenApply = map, thenCompose = flatMap)
    public static CompletableFuture<String> buildProfile(Long userId) {
        return fetchUser(userId)
            .thenApply(user -> user.toUpperCase())           // sync transform
            .thenCompose(user -> fetchOrders(user))          // async chain
            .thenApply(orders -> "Profile: " + orders);
    }

    private static CompletableFuture<String> fetchOrders(String user) {
        return CompletableFuture.supplyAsync(() -> user + "_ORDERS");
    }

    // 3. allOf — wait for all futures
    public static void fetchAll(List<Long> userIds) {
        List<CompletableFuture<String>> futures = userIds.stream()
            .map(CompletableFuturePatterns::fetchUser)
            .collect(Collectors.toList());

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
            .thenRun(() -> {
                List<String> results = futures.stream()
                    .map(CompletableFuture::join)
                    .collect(Collectors.toList());
                System.out.println("All done: " + results);
            });
    }

    // 4. anyOf — first to complete wins
    public static CompletableFuture<Object> race(List<Long> userIds) {
        List<CompletableFuture<String>> futures = userIds.stream()
            .map(CompletableFuturePatterns::fetchUser)
            .collect(Collectors.toList());
        return CompletableFuture.anyOf(futures.toArray(new CompletableFuture[0]));
    }

    // 5. Error handling
    public static CompletableFuture<String> robustFetch(Long userId) {
        return fetchUser(userId)
            .exceptionally(ex -> "Default User")              // fallback on error
            .whenComplete((result, ex) -> {                   // always runs
                if (ex != null) System.err.println("Error: " + ex.getMessage());
                else System.out.println("Fetched: " + result);
            });
    }

    // 6. Timeout
    public static CompletableFuture<String> withTimeout(Long userId) {
        return fetchUser(userId)
            .orTimeout(5, TimeUnit.SECONDS)                   // Java 9+
            .exceptionally(ex -> "Timed out");
    }

    // 7. thenCombine — merge two futures
    public static CompletableFuture<String> fetchAndCombine(Long userId, Long orderId) {
        CompletableFuture<String> userFuture  = fetchUser(userId);
        CompletableFuture<String> orderFuture = fetchUser(orderId); // reusing for demo
        return userFuture.thenCombine(orderFuture,
            (user, order) -> user + " | Order: " + order);
    }

    public static void main(String[] args) throws Exception {
        // Run chain
        String profile = buildProfile(42L).get(5, TimeUnit.SECONDS);
        System.out.println(profile);

        // Fetch all
        fetchAll(List.of(1L, 2L, 3L));
        Thread.sleep(500);

        executor.shutdown();
    }
}
