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
