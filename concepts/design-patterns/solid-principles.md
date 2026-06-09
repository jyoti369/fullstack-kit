# SOLID Principles — With Code Examples

## S — Single Responsibility Principle
> A class should have one, and only one, reason to change.

```javascript
// ❌ Bad — handles both user logic AND email sending
class UserService {
  createUser(data) { /* ... */ }
  sendWelcomeEmail(user) { /* ... */ }
}

// ✅ Good — separated concerns
class UserService {
  createUser(data) { /* ... */ }
}
class EmailService {
  sendWelcomeEmail(user) { /* ... */ }
}
```

## O — Open/Closed Principle
> Open for extension, closed for modification.

```javascript
// ✅ Use strategy pattern instead of if/else chains
const discountStrategies = {
  regular: (price) => price,
  premium: (price) => price * 0.9,
  vip: (price) => price * 0.8,
};

function calculatePrice(price, customerType) {
  return discountStrategies[customerType](price);
}
```

## L — Liskov Substitution
> Subtypes must be substitutable for their base types.

## I — Interface Segregation
> Don't force clients to depend on methods they don't use.

```typescript
// ❌ Fat interface
interface Worker {
  work(): void;
  eat(): void;    // Robots don't eat!
  sleep(): void;  // Robots don't sleep!
}

// ✅ Segregated interfaces
interface Workable { work(): void; }
interface Feedable { eat(): void; }
interface Restable { sleep(): void; }
```

## D — Dependency Inversion
> Depend on abstractions, not concretions.

```javascript
// ❌ Direct dependency
class OrderService {
  constructor() { this.db = new MySQLDatabase(); }
}

// ✅ Injected dependency
class OrderService {
  constructor(database) { this.db = database; }
}
```


# SOLID Principles — Java Examples

## S — Single Responsibility Principle
> A class should have one, and only one, reason to change.

```java
// ❌ Bad — handles user logic AND email
class UserService {
    public void createUser(User u) { /*...*/ }
    public void sendWelcomeEmail(User u) { /*...*/ }
}

// ✅ Good — separated concerns
class UserService { public void createUser(User u) { /*...*/ } }
class EmailService { public void sendWelcomeEmail(User u) { /*...*/ } }
```

## O — Open/Closed Principle
> Open for extension, closed for modification.

```java
// ❌ Bad — modify this class every time a new discount type is added
if (type.equals("premium")) price *= 0.9;
else if (type.equals("vip")) price *= 0.8;

// ✅ Good — add new strategy without touching existing code
interface DiscountStrategy { double apply(double price); }
class PremiumDiscount implements DiscountStrategy { public double apply(double p) { return p * 0.9; } }
class VipDiscount     implements DiscountStrategy { public double apply(double p) { return p * 0.8; } }
class PriceCalculator {
    private final DiscountStrategy strategy;
    PriceCalculator(DiscountStrategy s) { this.strategy = s; }
    double calculate(double price) { return strategy.apply(price); }
}
```

## L — Liskov Substitution Principle
```java
// Subtypes must be substitutable for base types
class Rectangle { protected int w, h; void setWidth(int w){this.w=w;} }
class Square extends Rectangle {
    // ❌ Bad: Square.setWidth overrides and breaks Rectangle's contract
    void setWidth(int w) { this.w = this.h = w; } // now height also changes!
}
// Fix: don't inherit — use interface Shape instead
```

## I — Interface Segregation
```java
// ❌ Fat interface — Robot forced to implement eat()/sleep()
interface Worker { void work(); void eat(); void sleep(); }

// ✅ Segregated
interface Workable { void work(); }
interface Feedable  { void eat(); }
interface Restable  { void sleep(); }
class Robot   implements Workable { public void work() { /*...*/ } }
class Employee implements Workable, Feedable, Restable { /*...*/ }
```

## D — Dependency Inversion
```java
// ❌ Direct dependency on concrete class
class OrderService { private MySQLDatabase db = new MySQLDatabase(); }

// ✅ Depend on abstraction
interface OrderRepository { Order findById(Long id); }
class OrderService {
    private final OrderRepository repo; // injected
    OrderService(OrderRepository repo) { this.repo = repo; }
}
// Spring does this automatically via @Autowired / constructor injection
```
