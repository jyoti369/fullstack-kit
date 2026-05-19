# Observer Pattern — Java Implementations

## Overview
Defines a one-to-many dependency between objects so that when one object changes state, all its dependents are notified and updated automatically.

## Use Cases
- Event systems (Spring ApplicationEvent, Java EventBus)
- Real-time notifications (WebSockets)
- State management
- Kafka consumer patterns

## Implementation 1: Java's Built-in (Java 9+ Flow API)
```java
import java.util.concurrent.Flow.*;
import java.util.concurrent.SubmissionPublisher;

// Publisher
var publisher = new SubmissionPublisher<String>();

// Subscriber
publisher.subscribe(new Subscriber<>() {
    Subscription subscription;
    public void onSubscribe(Subscription s) { subscription = s; s.request(Long.MAX_VALUE); }
    public void onNext(String item) { System.out.println("Received: " + item); }
    public void onError(Throwable t) { t.printStackTrace(); }
    public void onComplete() { System.out.println("Done"); }
});

publisher.submit("Hello");
publisher.submit("World");
publisher.close();
```

## Implementation 2: Custom EventBus (Spring-style)
```java
public class EventBus {
    private final Map<Class<?>, List<Consumer<Object>>> listeners = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public <T> void subscribe(Class<T> eventType, Consumer<T> handler) {
        listeners.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>())
            .add(event -> handler.accept((T) event));
    }

    public void publish(Object event) {
        listeners.getOrDefault(event.getClass(), List.of())
            .forEach(handler -> handler.accept(event));
    }
}

// Usage
record UserLoggedInEvent(String userId, Instant timestamp) {}

EventBus bus = new EventBus();
bus.subscribe(UserLoggedInEvent.class, e -> System.out.println("Welcome: " + e.userId()));
bus.publish(new UserLoggedInEvent("alice", Instant.now()));
```

## Implementation 3: Spring ApplicationEvent
```java
// Event
record OrderPlacedEvent(Order order) {}

// Publisher service
@Service @RequiredArgsConstructor
class OrderService {
    private final ApplicationEventPublisher eventPublisher;
    public Order placeOrder(OrderRequest req) {
        Order order = orderRepository.save(new Order(req));
        eventPublisher.publishEvent(new OrderPlacedEvent(order)); // async if @Async
        return order;
    }
}

// Listener
@Component
class NotificationListener {
    @EventListener
    @Async // handle in background thread
    public void onOrderPlaced(OrderPlacedEvent event) {
        emailService.sendOrderConfirmation(event.order());
    }
}
```


# Observer Pattern — Java Implementations

## Overview
Defines a one-to-many dependency between objects so that when one object changes state, all its dependents are notified and updated automatically.

## Use Cases
- Event systems (Spring ApplicationEvent, Java EventBus)
- Real-time notifications (WebSockets)
- State management
- Kafka consumer patterns

## Implementation 1: Java's Built-in (Java 9+ Flow API)
```java
import java.util.concurrent.Flow.*;
import java.util.concurrent.SubmissionPublisher;

// Publisher
var publisher = new SubmissionPublisher<String>();

// Subscriber
publisher.subscribe(new Subscriber<>() {
    Subscription subscription;
    public void onSubscribe(Subscription s) { subscription = s; s.request(Long.MAX_VALUE); }
    public void onNext(String item) { System.out.println("Received: " + item); }
    public void onError(Throwable t) { t.printStackTrace(); }
    public void onComplete() { System.out.println("Done"); }
});

publisher.submit("Hello");
publisher.submit("World");
publisher.close();
```

## Implementation 2: Custom EventBus (Spring-style)
```java
public class EventBus {
    private final Map<Class<?>, List<Consumer<Object>>> listeners = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public <T> void subscribe(Class<T> eventType, Consumer<T> handler) {
        listeners.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>())
            .add(event -> handler.accept((T) event));
    }

    public void publish(Object event) {
        listeners.getOrDefault(event.getClass(), List.of())
            .forEach(handler -> handler.accept(event));
    }
}

// Usage
record UserLoggedInEvent(String userId, Instant timestamp) {}

EventBus bus = new EventBus();
bus.subscribe(UserLoggedInEvent.class, e -> System.out.println("Welcome: " + e.userId()));
bus.publish(new UserLoggedInEvent("alice", Instant.now()));
```

## Implementation 3: Spring ApplicationEvent
```java
// Event
record OrderPlacedEvent(Order order) {}

// Publisher service
@Service @RequiredArgsConstructor
class OrderService {
    private final ApplicationEventPublisher eventPublisher;
    public Order placeOrder(OrderRequest req) {
        Order order = orderRepository.save(new Order(req));
        eventPublisher.publishEvent(new OrderPlacedEvent(order)); // async if @Async
        return order;
    }
}

// Listener
@Component
class NotificationListener {
    @EventListener
    @Async // handle in background thread
    public void onOrderPlaced(OrderPlacedEvent event) {
        emailService.sendOrderConfirmation(event.order());
    }
}
```


# Observer Pattern — Java Implementations

## Overview
Defines a one-to-many dependency between objects so that when one object changes state, all its dependents are notified and updated automatically.

## Use Cases
- Event systems (Spring ApplicationEvent, Java EventBus)
- Real-time notifications (WebSockets)
- State management
- Kafka consumer patterns

## Implementation 1: Java's Built-in (Java 9+ Flow API)
```java
import java.util.concurrent.Flow.*;
import java.util.concurrent.SubmissionPublisher;

// Publisher
var publisher = new SubmissionPublisher<String>();

// Subscriber
publisher.subscribe(new Subscriber<>() {
    Subscription subscription;
    public void onSubscribe(Subscription s) { subscription = s; s.request(Long.MAX_VALUE); }
    public void onNext(String item) { System.out.println("Received: " + item); }
    public void onError(Throwable t) { t.printStackTrace(); }
    public void onComplete() { System.out.println("Done"); }
});

publisher.submit("Hello");
publisher.submit("World");
publisher.close();
```

## Implementation 2: Custom EventBus (Spring-style)
```java
public class EventBus {
    private final Map<Class<?>, List<Consumer<Object>>> listeners = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public <T> void subscribe(Class<T> eventType, Consumer<T> handler) {
        listeners.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>())
            .add(event -> handler.accept((T) event));
    }

    public void publish(Object event) {
        listeners.getOrDefault(event.getClass(), List.of())
            .forEach(handler -> handler.accept(event));
    }
}

// Usage
record UserLoggedInEvent(String userId, Instant timestamp) {}

EventBus bus = new EventBus();
bus.subscribe(UserLoggedInEvent.class, e -> System.out.println("Welcome: " + e.userId()));
bus.publish(new UserLoggedInEvent("alice", Instant.now()));
```

## Implementation 3: Spring ApplicationEvent
```java
// Event
record OrderPlacedEvent(Order order) {}

// Publisher service
@Service @RequiredArgsConstructor
class OrderService {
    private final ApplicationEventPublisher eventPublisher;
    public Order placeOrder(OrderRequest req) {
        Order order = orderRepository.save(new Order(req));
        eventPublisher.publishEvent(new OrderPlacedEvent(order)); // async if @Async
        return order;
    }
}

// Listener
@Component
class NotificationListener {
    @EventListener
    @Async // handle in background thread
    public void onOrderPlaced(OrderPlacedEvent event) {
        emailService.sendOrderConfirmation(event.order());
    }
}
```
