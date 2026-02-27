# Java Concurrency — Patterns & Best Practices

## Core Concepts

### Thread vs Virtual Thread (Java 21+)
```java
// Platform thread (heavy, ~1MB stack)
Thread.ofPlatform().start(() -> doWork());

// Virtual thread (lightweight, ~KB, millions possible)
Thread.ofVirtual().start(() -> doWork());
Executors.newVirtualThreadPerTaskExecutor(); // best for I/O-heavy
```

## Synchronization

### synchronized (Intrinsic Lock)
```java
public synchronized void increment() { count++; }

// Block-level (more granular)
synchronized(this.lock) { ... }
```

### ReentrantLock (More Control)
```java
Lock lock = new ReentrantLock();
lock.lock();
try {
    // critical section
} finally {
    lock.unlock();  // always release!
}

// Try-lock with timeout
if (lock.tryLock(100, TimeUnit.MILLISECONDS)) { ... }
```

### ReadWriteLock
```java
ReadWriteLock rwLock = new ReentrantReadWriteLock();
// Multiple readers, exclusive writer
rwLock.readLock().lock();   // many threads can hold this
rwLock.writeLock().lock();  // exclusive
```

## Atomic Variables
```java
AtomicInteger counter = new AtomicInteger(0);
counter.incrementAndGet();
counter.compareAndSet(expected, update); // CAS operation

AtomicReference<Node> head = new AtomicReference<>();
```

## Concurrent Collections
```java
ConcurrentHashMap<K,V> map = new ConcurrentHashMap<>();
CopyOnWriteArrayList<E> list = new CopyOnWriteArrayList<>();
ConcurrentLinkedQueue<E> queue = new ConcurrentLinkedQueue<>();
BlockingQueue<E> bq = new LinkedBlockingQueue<>(capacity);
```

## CompletableFuture (Async Composition)
```java
CompletableFuture
  .supplyAsync(() -> fetchUser(userId))           // runs in ForkJoinPool
  .thenApplyAsync(user -> fetchOrders(user.id))  // chain
  .thenCombine(fetchRecommendations(), (orders, recs) ->  // parallel
      new Dashboard(orders, recs))
  .exceptionally(ex -> Dashboard.empty())         // error handling
  .thenAccept(System.out::println);
```

## Common Pitfalls
```java
// ❌ Double-checked locking (broken without volatile)
if (instance == null) {
    synchronized(this) {
        if (instance == null) instance = new Singleton();
    }
}

// ✅ With volatile (Java 5+)
private volatile Singleton instance;

// ❌ Calling external method while holding lock (deadlock risk)
synchronized(lock) { listener.onEvent(data); } // don't!

// ✅ Copy then call
Listener l;
synchronized(lock) { l = this.listener; }
l.onEvent(data); // outside lock
```
