package snippets.java.reactive;

import reactor.core.publisher.*;
import reactor.core.scheduler.Schedulers;
import java.time.Duration;
import java.util.List;

/**
 * Project Reactor — Reactive Programming in Java
 * Used by Spring WebFlux
 */
public class ReactorPatterns {

    record User(Long id, String name) {}

    // ---- Mono (0 or 1 element) ----
    public Mono<User> findUser(Long id) {
        return Mono.fromCallable(() -> new User(id, "Alice"))
            .subscribeOn(Schedulers.boundedElastic()) // offload blocking call
            .timeout(Duration.ofSeconds(5))
            .onErrorReturn(new User(-1L, "Unknown")); // fallback on error
    }

    // ---- Flux (0..N elements) ----
    public Flux<User> findAllUsers() {
        return Flux.fromIterable(List.of(new User(1L,"Alice"), new User(2L,"Bob")))
            .filter(u -> u.id() > 0)
            .map(u -> new User(u.id(), u.name().toUpperCase()))
            .take(10)  // limit
            .delayElements(Duration.ofMillis(10)); // back-pressure demo
    }

    // ---- Combining streams ----
    public Mono<String> mergeData(Long userId, Long orderId) {
        Mono<User>   userMono  = findUser(userId);
        Mono<String> orderMono = Mono.just("Order-" + orderId);
        return Mono.zip(userMono, orderMono,
            (user, order) -> user.name() + " | " + order); // run both in parallel
    }

    // ---- Backpressure ----
    public Flux<Integer> backPressureDemo() {
        return Flux.range(1, 1000)
            .onBackpressureBuffer(100)   // buffer 100 items if slow consumer
            .publishOn(Schedulers.single()) // switch to single thread
            .map(i -> i * 2);
    }

    // ---- Hot vs Cold ----
    // Cold: each subscriber gets full stream from start (default)
    // Hot : stream is ongoing, new subscribers get items from now on
    public static Flux<Long> hotStream() {
        return Flux.interval(Duration.ofSeconds(1)) // hot (interval-based)
            .share(); // multicast to all subscribers
    }

    // ---- Error handling ----
    public Flux<User> robustStream() {
        return Flux.range(1, 5)
            .map(i -> {
                if (i == 3) throw new RuntimeException("DB error");
                return new User((long)i, "User" + i);
            })
            .onErrorContinue((ex, obj) -> System.err.println("Skipping: " + obj))
            // OR: .onErrorResume(ex -> Flux.empty())
            // OR: .retry(3)
            .doOnNext(u -> System.out.println("Processing: " + u.name()))
            .doFinally(sig -> System.out.println("Stream completed: " + sig));
    }

    public static void main(String[] args) throws InterruptedException {
        ReactorPatterns r = new ReactorPatterns();
        r.findUser(1L).subscribe(u -> System.out.println("Found: " + u));
        r.findAllUsers().subscribe(u -> System.out.println(u.name()));
        r.robustStream().blockLast(); // block for demo only
    }
}
