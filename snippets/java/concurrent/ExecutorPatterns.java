package snippets.java.concurrent;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Java Executor Service — Thread Pool Patterns
 */
public class ExecutorPatterns {

    // 1. Fixed thread pool
    public static void fixedPool() throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 10; i++) {
            final int taskId = i;
            pool.submit(() -> {
                System.out.println("Task " + taskId + " on " + Thread.currentThread().getName());
            });
        }
        pool.shutdown();
        pool.awaitTermination(10, TimeUnit.SECONDS);
    }

    // 2. Custom thread pool with rejection policy
    public static ThreadPoolExecutor customPool() {
        return new ThreadPoolExecutor(
            2,              // corePoolSize
            10,             // maximumPoolSize
            60, TimeUnit.SECONDS,    // keepAliveTime
            new LinkedBlockingQueue<>(100),          // work queue
            new ThreadFactory() {                    // custom thread naming
                AtomicInteger count = new AtomicInteger();
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(r, "worker-" + count.incrementAndGet());
                    t.setDaemon(true);
                    return t;
                }
            },
            new ThreadPoolExecutor.CallerRunsPolicy() // on full queue: caller executes
        );
    }

    // 3. ScheduledExecutorService
    public static void scheduledTasks() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

        // Run after 1s delay
        scheduler.schedule(() -> System.out.println("Delayed task"), 1, TimeUnit.SECONDS);

        // Run every 5s (fixed rate — regardless of execution time)
        scheduler.scheduleAtFixedRate(
            () -> System.out.println("Fixed rate: " + System.currentTimeMillis()),
            0, 5, TimeUnit.SECONDS);

        // Run 5s AFTER previous finishes (fixed delay)
        scheduler.scheduleWithFixedDelay(
            () -> System.out.println("Fixed delay"),
            0, 5, TimeUnit.SECONDS);
    }

    // 4. Callable + Future
    public static void futureExample() throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(2);
        Future<Integer> future = pool.submit(() -> {
            Thread.sleep(1000);
            return 42;
        });
        System.out.println("Working while future runs...");
        int result = future.get(5, TimeUnit.SECONDS); // blocks until done
        System.out.println("Result: " + result);
        pool.shutdown();
    }

    // 5. Semaphore — limit concurrent access
    public static void semaphoreExample() {
        Semaphore semaphore = new Semaphore(3); // max 3 concurrent
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName() + " running");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    semaphore.release();
                }
            }).start();
        }
    }

    // 6. CountDownLatch and CyclicBarrier
    public static void latchExample() throws InterruptedException {
        int N = 3;
        CountDownLatch latch = new CountDownLatch(N);
        for (int i = 0; i < N; i++) {
            new Thread(() -> { latch.countDown(); System.out.println("Worker done"); }).start();
        }
        latch.await(); // main waits until all workers done
        System.out.println("All workers finished");
    }

    public static void main(String[] args) throws Exception {
        fixedPool();
        futureExample();
    }
}
