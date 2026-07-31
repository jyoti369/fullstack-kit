package snippets.java.concurrent;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.*;

/**
 * Advanced Java Concurrency Patterns
 */
public class AdvancedConcurrency {

    // 1. ReadWriteLock — multiple readers OR one writer
    static class ReadWriteCache<K, V> {
        private final ConcurrentHashMap<K, V> map = new ConcurrentHashMap<>();
        private final ReadWriteLock lock = new ReentrantReadWriteLock();
        private final Lock readLock  = lock.readLock();
        private final Lock writeLock = lock.writeLock();

        public V get(K key) {
            readLock.lock();
            try { return map.get(key); }
            finally { readLock.unlock(); }
        }

        public void put(K key, V value) {
            writeLock.lock();
            try { map.put(key, value); }
            finally { writeLock.unlock(); }
        }
    }

    // 2. StampedLock — optimistic reads (Java 8+)
    static class StampedCache {
        private final StampedLock lock = new StampedLock();
        private double value = 0;

        public double read() {
            long stamp = lock.tryOptimisticRead();  // no lock!
            double v = value;
            if (!lock.validate(stamp)) {            // check no write happened
                stamp = lock.readLock();
                try { v = value; }
                finally { lock.unlockRead(stamp); }
            }
            return v;
        }

        public void write(double newValue) {
            long stamp = lock.writeLock();
            try { value = newValue; } finally { lock.unlockWrite(stamp); }
        }
    }

    // 3. Atomic operations
    static class AtomicCounter {
        private final AtomicLong counter = new AtomicLong(0);
        private final AtomicReference<String> state = new AtomicReference<>("IDLE");

        public long increment() { return counter.incrementAndGet(); }
        public long addAndGet(long delta) { return counter.addAndGet(delta); }

        // CAS — Compare-And-Swap
        public boolean transitionState(String expected, String next) {
            return state.compareAndSet(expected, next);
        }

        // getAndUpdate with lambda
        public long updateConditionally(long minVal) {
            return counter.updateAndGet(v -> Math.max(v, minVal));
        }
    }

    // 4. Phaser (flexible synchronization barrier)
    static void phaserExample() throws InterruptedException {
        Phaser phaser = new Phaser(3); // 3 parties
        for (int i = 0; i < 3; i++) {
            final int id = i;
            new Thread(() -> {
                System.out.println(id + " preparing, phase " + phaser.getPhase());
                phaser.arriveAndAwaitAdvance();  // wait for all
                System.out.println(id + " executing, phase " + phaser.getPhase());
                phaser.arriveAndAwaitAdvance();  // wait for all again
                System.out.println(id + " done");
                phaser.arriveAndDeregister();    // deregister
            }).start();
        }
    }

    // 5. BlockingQueue patterns (producer-consumer)
    static class PipelineStage<T> {
        private final BlockingQueue<T> queue = new LinkedBlockingQueue<>(100);

        public void produce(T item) throws InterruptedException {
            queue.put(item); // blocks if full
        }

        public T consume() throws InterruptedException {
            return queue.take(); // blocks if empty
        }

        public T pollWithTimeout(long ms) throws InterruptedException {
            return queue.poll(ms, TimeUnit.MILLISECONDS);
        }
    }

    // 6. CompletableFuture orchestration
    static CompletableFuture<String> callServiceA() { return CompletableFuture.supplyAsync(() -> "A"); }
    static CompletableFuture<String> callServiceB() { return CompletableFuture.supplyAsync(() -> "B"); }
    static CompletableFuture<String> callServiceC(String aResult) { return CompletableFuture.supplyAsync(() -> aResult + "-C"); }

    static void orchestrate() {
        // A and B in parallel, then C depends on A, then combine all
        CompletableFuture<String> aFuture = callServiceA();
        CompletableFuture<String> bFuture = callServiceB();
        CompletableFuture<String> cFuture = aFuture.thenCompose(AdvancedConcurrency::callServiceC);

        CompletableFuture.allOf(aFuture, bFuture, cFuture).thenRun(() -> {
            String a = aFuture.join(), b = bFuture.join(), c = cFuture.join();
            System.out.println(a + " | " + b + " | " + c); // A | B | A-C
        }).join();
    }

    public static void main(String[] args) throws Exception {
        AtomicCounter counter = new AtomicCounter();
        System.out.println(counter.increment()); // 1
        System.out.println(counter.transitionState("IDLE", "RUNNING")); // true
        orchestrate();
    }
}
