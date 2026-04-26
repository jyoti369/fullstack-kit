# Microservices Design Patterns — Java/Spring Focus

## 1. API Gateway Pattern
```
 Client → API Gateway → [Auth Service]
                      → [User Service]
                      → [Order Service]
                      → [Product Service]

Spring Cloud Gateway example:
  @Bean
  RouteLocator routes(RouteLocatorBuilder b) {
    return b.routes()
      .route("user-service", r -> r.path("/api/users/**")
        .filters(f -> f.stripPrefix(1)
                        .circuitBreaker(c -> c.setName("user-cb")))
        .uri("lb://USER-SERVICE"))
      .build();
  }
```

## 2. Circuit Breaker (Resilience4j)
```java
@CircuitBreaker(name = "orderService", fallbackMethod = "fallbackOrders")
public List<Order> getOrders(Long userId) {
    return orderClient.getOrdersForUser(userId); // may fail
}

public List<Order> fallbackOrders(Long userId, Exception ex) {
    log.warn("Orders unavailable for user {}: {}", userId, ex.getMessage());
    return Collections.emptyList(); // graceful degradation
}

// States: CLOSED → normal, OPEN → fast-fail, HALF_OPEN → testing recovery
// application.yml:
// resilience4j.circuitbreaker.instances.orderService:
//   failure-rate-threshold: 50
//   wait-duration-in-open-state: 10s
```

## 3. Saga Pattern (Distributed Transactions)
```
ORCHESTRATION (via Spring State Machine or Temporal):
  SagaOrchestrator → PaymentService → InventoryService → ShippingService
  On failure → compensating transactions (undo each step)

CHOREOGRAPHY (Kafka events):
  OrderCreated event → PaymentService listens → PaymentDone event
                     → InventoryService listens → InventoryReserved event
```

## 4. Event Sourcing + CQRS
```java
// Command side — writes events to event store
@Aggregate
public class Order {
    @CommandHandler
    public Order(CreateOrderCommand cmd) {
        apply(new OrderCreatedEvent(cmd.orderId(), cmd.items()));
    }
    @EventSourcingHandler
    public void on(OrderCreatedEvent e) { this.id = e.orderId(); }
}
// Axon Framework handles event sourcing/CQRS boilerplate
```

## 5. Service Mesh (Istio / Linkerd)
```
Sidecar proxy (Envoy) injected alongside each pod:
  - mTLS between services
  - Traffic shaping, retries, timeouts
  - Distributed tracing (Jaeger)
  - Metrics (Prometheus)

Developers write no networking code —
the mesh handles it transparently.
```

## 6. When to Use Microservices
```
✅ Large teams (Conway's Law)
✅ Independent deployment/scaling needed
✅ Polyglot requirements

❌ Small team < 10 engineers
❌ Early startup — start with a monolith
❌ If bounded contexts unclear

Rule of thumb:
  Start monolith → define clear domain boundaries
  → extract service when team/scaling pain is *real*
```


# Microservices Design Patterns — Java/Spring Focus

## 1. API Gateway Pattern
```
 Client → API Gateway → [Auth Service]
                      → [User Service]
                      → [Order Service]
                      → [Product Service]

Spring Cloud Gateway example:
  @Bean
  RouteLocator routes(RouteLocatorBuilder b) {
    return b.routes()
      .route("user-service", r -> r.path("/api/users/**")
        .filters(f -> f.stripPrefix(1)
                        .circuitBreaker(c -> c.setName("user-cb")))
        .uri("lb://USER-SERVICE"))
      .build();
  }
```

## 2. Circuit Breaker (Resilience4j)
```java
@CircuitBreaker(name = "orderService", fallbackMethod = "fallbackOrders")
public List<Order> getOrders(Long userId) {
    return orderClient.getOrdersForUser(userId); // may fail
}

public List<Order> fallbackOrders(Long userId, Exception ex) {
    log.warn("Orders unavailable for user {}: {}", userId, ex.getMessage());
    return Collections.emptyList(); // graceful degradation
}

// States: CLOSED → normal, OPEN → fast-fail, HALF_OPEN → testing recovery
// application.yml:
// resilience4j.circuitbreaker.instances.orderService:
//   failure-rate-threshold: 50
//   wait-duration-in-open-state: 10s
```

## 3. Saga Pattern (Distributed Transactions)
```
ORCHESTRATION (via Spring State Machine or Temporal):
  SagaOrchestrator → PaymentService → InventoryService → ShippingService
  On failure → compensating transactions (undo each step)

CHOREOGRAPHY (Kafka events):
  OrderCreated event → PaymentService listens → PaymentDone event
                     → InventoryService listens → InventoryReserved event
```

## 4. Event Sourcing + CQRS
```java
// Command side — writes events to event store
@Aggregate
public class Order {
    @CommandHandler
    public Order(CreateOrderCommand cmd) {
        apply(new OrderCreatedEvent(cmd.orderId(), cmd.items()));
    }
    @EventSourcingHandler
    public void on(OrderCreatedEvent e) { this.id = e.orderId(); }
}
// Axon Framework handles event sourcing/CQRS boilerplate
```

## 5. Service Mesh (Istio / Linkerd)
```
Sidecar proxy (Envoy) injected alongside each pod:
  - mTLS between services
  - Traffic shaping, retries, timeouts
  - Distributed tracing (Jaeger)
  - Metrics (Prometheus)

Developers write no networking code —
the mesh handles it transparently.
```

## 6. When to Use Microservices
```
✅ Large teams (Conway's Law)
✅ Independent deployment/scaling needed
✅ Polyglot requirements

❌ Small team < 10 engineers
❌ Early startup — start with a monolith
❌ If bounded contexts unclear

Rule of thumb:
  Start monolith → define clear domain boundaries
  → extract service when team/scaling pain is *real*
```
