# Java Thread Model — Interview Deep Dive

## Thread Lifecycle
```
NEW → RUNNABLE → (BLOCKED / WAITING / TIMED_WAITING) → TERMINATED

NEW:            Thread created, not yet started
RUNNABLE:       Running or ready to run
BLOCKED:        Waiting for monitor lock (synchronized)
WAITING:        Waiting for notify() — Object.wait(), LockSupport.park()
TIMED_WAITING:  Thread.sleep(ms), Object.wait(ms), join(ms)
TERMINATED:     Finished execution
```

## happens-before Relationship (Java Memory Model)
```
Guarantees thread B sees thread A's writes IF:
  1. Synchronized block: A.unlock happens-before B.lock on same monitor
  2. volatile write: A writes volatile → B reads same volatile
  3. Thread.start(): actions before start() seen by started thread
  4. Thread.join():  actions in thread seen after join() returns
  5. static initialization: clinit completes before first class access
```

## Classic Interview Questions
```java
// 1. What's wrong with this singleton?
class Singleton {
    private static Singleton instance;  // not volatile!
    public static Singleton getInstance() {
        if (instance == null) {         // Thread A and B both enter
            synchronized(Singleton.class) {
                if (instance == null)
                    instance = new Singleton(); // partially constructed!
            }
        }
        return instance;
    }
    // Fix: add volatile to instance field
    // Or: use enum Singleton (guaranteed thread-safe by JVM)
}

// 2. Why is i++ not atomic?
volatile int i = 0;
void increment() { i++; }  // read → modify → write: 3 operations!
// Fix: AtomicInteger.incrementAndGet()

// 3. ThreadLocal misuse in thread pools
static ThreadLocal<User> currentUser = new ThreadLocal<>();
// PROBLEM: thread pool reuses threads → stale ThreadLocal from previous request!
// FIX: always call remove() in finally block
try {
    currentUser.set(user);
    processRequest();
} finally {
    currentUser.remove(); // CRITICAL
}
```

## Virtual Threads (Java 21)
```java
// Platform thread: 1-to-1 with OS thread, ~1MB stack
// Virtual thread: M-to-N with OS threads, ~few KB stack, millions possible

// Old way (OS thread per request, limited to ~10K concurrent)
ExecutorService pool = Executors.newFixedThreadPool(200);

// New way (virtual thread per task, millions concurrent)
ExecutorService vtp = Executors.newVirtualThreadPerTaskExecutor();
vtp.submit(() -> {
    // Blocking calls (JDBC, REST) unmount the virtual thread,
    // freeing the carrier OS thread to do other work
    ResultSet rs = jdbcTemplate.queryForList("..."); // no longer blocks OS thread
});

// Spring Boot 3.2: spring.threads.virtual.enabled=true
```

## Common Pitfalls
```
1. Forgetting volatile → visibility bugs (cached in CPU register)
2. Using synchronized on local variable (useless — no sharing)
3. synchronized(this) in public class → clients can lock your monitor
4. Calling Thread.stop() — deprecated, corrupts state
5. Catching InterruptedException without restoring interrupt flag
   → Thread.currentThread().interrupt(); // restore!
6. Long GC pauses releasing locks sporadically — use StampedLock/ReadWriteLock
```
