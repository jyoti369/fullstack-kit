package snippets.java.spring;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.*;
import java.time.Duration;
import java.util.Map;

/**
 * Spring WebFlux — Non-blocking Reactive REST Controller
 * Replaces Spring MVC when you need high concurrency with fewer threads.
 */
@RestController
@RequestMapping("/api/users")
public class WebFluxController {

    private final UserReactiveService userService;

    WebFluxController(UserReactiveService userService) { this.userService = userService; }

    // Returns Mono<T> — single async result
    @GetMapping("/{id}")
    public Mono<ResponseEntity<User>> getUser(@PathVariable Long id) {
        return userService.findById(id)
            .map(user -> ResponseEntity.ok(user))
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // Returns Flux<T> — streaming multiple results
    @GetMapping(produces = "application/json")
    public Flux<User> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return userService.findAll()
            .skip((long) page * size)
            .take(size)
            .delayElements(Duration.ofMillis(1)); // back-pressure demo
    }

    // Server-Sent Events (SSE) — real-time streaming to browser
    @GetMapping(value = "/stream", produces = "text/event-stream")
    public Flux<Map<String,Object>> liveStream() {
        return Flux.interval(Duration.ofSeconds(1))
            .map(tick -> Map.of("tick", tick, "ts", System.currentTimeMillis()));
    }

    @PostMapping
    public Mono<ResponseEntity<User>> createUser(@RequestBody Mono<CreateUserRequest> request) {
        return request
            .flatMap(userService::create)
            .map(u -> ResponseEntity.status(201).body(u));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUser(@PathVariable Long id) {
        return userService.delete(id)
            .then(Mono.just(ResponseEntity.<Void>noContent().build()));
    }

    // Combine multiple service calls (parallel execution)
    @GetMapping("/{id}/dashboard")
    public Mono<Map<String,Object>> dashboard(@PathVariable Long id) {
        Mono<User> user = userService.findById(id);
        Mono<Integer> orderCount = userService.getOrderCount(id);
        Mono<String> status = userService.getStatus(id);
        return Mono.zip(user, orderCount, status)
            .map(t -> Map.of("user", t.getT1(), "orders", t.getT2(), "status", t.getT3()));
    }

    // Placeholder types
    record User(Long id, String name) {}
    record CreateUserRequest(String name, String email) {}
    interface UserReactiveService {
        Mono<User> findById(Long id);
        Flux<User> findAll();
        Mono<User> create(CreateUserRequest req);
        Mono<Void> delete(Long id);
        Mono<Integer> getOrderCount(Long id);
        Mono<String> getStatus(Long id);
    }
}


package snippets.java.spring;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.*;
import java.time.Duration;
import java.util.Map;

/**
 * Spring WebFlux — Non-blocking Reactive REST Controller
 * Replaces Spring MVC when you need high concurrency with fewer threads.
 */
@RestController
@RequestMapping("/api/users")
public class WebFluxController {

    private final UserReactiveService userService;

    WebFluxController(UserReactiveService userService) { this.userService = userService; }

    // Returns Mono<T> — single async result
    @GetMapping("/{id}")
    public Mono<ResponseEntity<User>> getUser(@PathVariable Long id) {
        return userService.findById(id)
            .map(user -> ResponseEntity.ok(user))
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // Returns Flux<T> — streaming multiple results
    @GetMapping(produces = "application/json")
    public Flux<User> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return userService.findAll()
            .skip((long) page * size)
            .take(size)
            .delayElements(Duration.ofMillis(1)); // back-pressure demo
    }

    // Server-Sent Events (SSE) — real-time streaming to browser
    @GetMapping(value = "/stream", produces = "text/event-stream")
    public Flux<Map<String,Object>> liveStream() {
        return Flux.interval(Duration.ofSeconds(1))
            .map(tick -> Map.of("tick", tick, "ts", System.currentTimeMillis()));
    }

    @PostMapping
    public Mono<ResponseEntity<User>> createUser(@RequestBody Mono<CreateUserRequest> request) {
        return request
            .flatMap(userService::create)
            .map(u -> ResponseEntity.status(201).body(u));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUser(@PathVariable Long id) {
        return userService.delete(id)
            .then(Mono.just(ResponseEntity.<Void>noContent().build()));
    }

    // Combine multiple service calls (parallel execution)
    @GetMapping("/{id}/dashboard")
    public Mono<Map<String,Object>> dashboard(@PathVariable Long id) {
        Mono<User> user = userService.findById(id);
        Mono<Integer> orderCount = userService.getOrderCount(id);
        Mono<String> status = userService.getStatus(id);
        return Mono.zip(user, orderCount, status)
            .map(t -> Map.of("user", t.getT1(), "orders", t.getT2(), "status", t.getT3()));
    }

    // Placeholder types
    record User(Long id, String name) {}
    record CreateUserRequest(String name, String email) {}
    interface UserReactiveService {
        Mono<User> findById(Long id);
        Flux<User> findAll();
        Mono<User> create(CreateUserRequest req);
        Mono<Void> delete(Long id);
        Mono<Integer> getOrderCount(Long id);
        Mono<String> getStatus(Long id);
    }
}
