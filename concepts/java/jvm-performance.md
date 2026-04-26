# JVM Performance Tuning

## Memory Areas
```
Heap:
  Young Gen (Eden + Survivor S0/S1)
    Most objects die here — collected by Minor GC
  Old Gen (Tenured)
    Long-lived objects — collected by Major GC

Non-Heap:
  Metaspace (class metadata) — unbounded by default
  Code Cache (JIT compiled native)
  Thread stacks

Key flags:
  -Xms512m               → initial heap
  -Xmx4g                 → max heap
  -Xss256k               → thread stack size
  -XX:MaxMetaspaceSize=256m
```

## Garbage Collectors
```
Serial GC:   Single-threaded, for small heaps (<100MB)
Parallel GC: Multi-threaded, throughput-focused (batch jobs)
G1 GC:       Default since Java 9, balanced latency/throughput
  Goal: -XX:MaxGCPauseMillis=200
ZGC:         Ultra-low latency (<1ms pauses), Java 15+
  Use for: latency-sensitive (trading, APIs)
Shenandoah: Similar to ZGC, RedHat implementation

Choose:
  Batch/offline → Parallel GC (max throughput)
  Web servers   → G1 GC (balanced, default)
  Real-time     → ZGC or Shenandoah
```

## GC Tuning
```bash
# G1 tuning
-XX:+UseG1GC
-XX:MaxGCPauseMillis=200      # target pause time
-XX:G1HeapRegionSize=16m      # larger for big heaps
-XX:InitiatingHeapOccupancyPercent=45

# ZGC
-XX:+UseZGC
-XX:SoftMaxHeapSize=4g        # soft limit, allow burst

# Heap dump on OOM
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=/var/dumps/heap.hprof

# GC logging (Java 9+)
-Xlog:gc*:file=/var/log/gc.log:time,uptime:filecount=5,filesize=20m
```

## JIT Compilation
```
Levels:
  0: Interpreted
  1-3: C1 (quick compiler, limited opt)
  4: C2 (aggressive optimizer)

C2 kicks in after ~10,000 invocations (HotSpot threshold)
Optimizations: inline, escape analysis, loop unrolling, SIMD

Tools:
  jit-watch: visualize JIT compilation decisions
  -XX:+PrintCompilation: log each compilation
  -XX:CICompilerCount=4: more JIT compiler threads
```

## Profiling Tools
```
jstack <pid>          → thread dump (deadlocks, blocked threads)
jmap -histo <pid>     → object histogram (memory leaks)
jmap -dump:format=b,file=heap.hprof <pid>
jstat -gcutil <pid> 1000   → GC stats per second
VisualVM / JConsole    → GUI profiler
AsyncProfiler          → low-overhead CPU/allocation profiler
IntelliJ Profiler      → integrated

Common Patterns:
  High CPU → JIT warmup, regex, string concat in loops
  OOM      → memory leak (check listeners, caches, ThreadLocal)
  High GC  → object churn, large objects, tune heap ratio
```

## Virtual Threads (Java 21)
```java
// Before: 1 OS thread per request → ~10K max concurrency
ExecutorService pool = Executors.newFixedThreadPool(200);

// After: Virtual threads → millions of concurrent tasks
ExecutorService vt = Executors.newVirtualThreadPerTaskExecutor();
vt.submit(() -> {
    // Blocking I/O now uses virtual thread mount/unmount
    // Does NOT block OS thread
    Thread.sleep(1000); // cheap with virtual threads!
});

// Spring Boot:
// spring.threads.virtual.enabled=true  (Spring Boot 3.2+)
```


# JVM Performance Tuning

## Memory Areas
```
Heap:
  Young Gen (Eden + Survivor S0/S1)
    Most objects die here — collected by Minor GC
  Old Gen (Tenured)
    Long-lived objects — collected by Major GC

Non-Heap:
  Metaspace (class metadata) — unbounded by default
  Code Cache (JIT compiled native)
  Thread stacks

Key flags:
  -Xms512m               → initial heap
  -Xmx4g                 → max heap
  -Xss256k               → thread stack size
  -XX:MaxMetaspaceSize=256m
```

## Garbage Collectors
```
Serial GC:   Single-threaded, for small heaps (<100MB)
Parallel GC: Multi-threaded, throughput-focused (batch jobs)
G1 GC:       Default since Java 9, balanced latency/throughput
  Goal: -XX:MaxGCPauseMillis=200
ZGC:         Ultra-low latency (<1ms pauses), Java 15+
  Use for: latency-sensitive (trading, APIs)
Shenandoah: Similar to ZGC, RedHat implementation

Choose:
  Batch/offline → Parallel GC (max throughput)
  Web servers   → G1 GC (balanced, default)
  Real-time     → ZGC or Shenandoah
```

## GC Tuning
```bash
# G1 tuning
-XX:+UseG1GC
-XX:MaxGCPauseMillis=200      # target pause time
-XX:G1HeapRegionSize=16m      # larger for big heaps
-XX:InitiatingHeapOccupancyPercent=45

# ZGC
-XX:+UseZGC
-XX:SoftMaxHeapSize=4g        # soft limit, allow burst

# Heap dump on OOM
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=/var/dumps/heap.hprof

# GC logging (Java 9+)
-Xlog:gc*:file=/var/log/gc.log:time,uptime:filecount=5,filesize=20m
```

## JIT Compilation
```
Levels:
  0: Interpreted
  1-3: C1 (quick compiler, limited opt)
  4: C2 (aggressive optimizer)

C2 kicks in after ~10,000 invocations (HotSpot threshold)
Optimizations: inline, escape analysis, loop unrolling, SIMD

Tools:
  jit-watch: visualize JIT compilation decisions
  -XX:+PrintCompilation: log each compilation
  -XX:CICompilerCount=4: more JIT compiler threads
```

## Profiling Tools
```
jstack <pid>          → thread dump (deadlocks, blocked threads)
jmap -histo <pid>     → object histogram (memory leaks)
jmap -dump:format=b,file=heap.hprof <pid>
jstat -gcutil <pid> 1000   → GC stats per second
VisualVM / JConsole    → GUI profiler
AsyncProfiler          → low-overhead CPU/allocation profiler
IntelliJ Profiler      → integrated

Common Patterns:
  High CPU → JIT warmup, regex, string concat in loops
  OOM      → memory leak (check listeners, caches, ThreadLocal)
  High GC  → object churn, large objects, tune heap ratio
```

## Virtual Threads (Java 21)
```java
// Before: 1 OS thread per request → ~10K max concurrency
ExecutorService pool = Executors.newFixedThreadPool(200);

// After: Virtual threads → millions of concurrent tasks
ExecutorService vt = Executors.newVirtualThreadPerTaskExecutor();
vt.submit(() -> {
    // Blocking I/O now uses virtual thread mount/unmount
    // Does NOT block OS thread
    Thread.sleep(1000); // cheap with virtual threads!
});

// Spring Boot:
// spring.threads.virtual.enabled=true  (Spring Boot 3.2+)
```
