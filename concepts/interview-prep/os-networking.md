# OS & Networking — Java Backend Engineer Essentials

## Process vs Thread vs Virtual Thread
```
Process:        isolated memory, ~1ms context switch
Thread (OS):    shared heap, ~10μs switch, ~1MB stack
Virtual Thread: user-space, ~1KB stack, millions possible
                Introduced in Java 21 (Project Loom)
```

## Java Memory Model (JMM)
```
Happens-before guarantee:
  Thread A writes x → Thread B reads x
  B is guaranteed to see A's write IF:
    1. A.write happens-before B.read
    2. Enforced by: synchronized, volatile, final,
                   Lock.unlock/lock, Thread.start/join

volatile:
  - Visibility: all threads see latest value (no CPU caching)
  - Prevents reordering around volatile read/write
  - NOT atomic for compound ops (i++ is not safe!)
  - Use AtomicInteger for increments

synchronized:
  - Mutual exclusion + visibility
  - Block: synchronized(this) { ... }
  - Method: synchronized void method() { ... }
```

## TCP Deep Dive
```
Three-way handshake:
  Client → SYN
  Server → SYN-ACK
  Client → ACK
  (Now data flows)

Four-way teardown:
  initiator → FIN
  other → ACK
  other → FIN
  initiator → ACK  (2MSL wait = TIME_WAIT)

TCP Flow Control: receiver advertises window size
TCP Congestion Control: slow start, AIMD
Nagle's Algorithm: batch small packets
  → disable for low-latency apps: TCP_NODELAY

Connection pool sizing:
  Too few → queuing, high latency
  Too many → DB exhaustion, thread overhead
  Ideal = latency × throughput (Little's Law)
  e.g., 100 ms latency, 1000 req/sec → 100 connections
```

## HTTP/2 vs HTTP/3 (QUIC)
```
HTTP/1.1:
  One request at a time per connection
  Head-of-line blocking

HTTP/2:
  Multiplexing (many streams per TCP connection)
  Header compression (HPACK)
  Still TCP → HOL blocking at layer 4

HTTP/3 (QUIC):
  UDP-based, reliability in userspace
  Per-stream delivery — no HOL blocking
  0-RTT connection resumption
  Java 21 HttpClient has HTTP/3 experimental support
```

## File System and I/O
```
Buffered vs Unbuffered:
  BufferedWriter writes to 8KB buffer → single syscall
  Raw FileWriter → syscall per write (slow)

Java NIO (non-blocking):
  FileChannel, SocketChannel, Selector
  Direct buffer: ByteBuffer.allocateDirect() → off-heap
  Memory-mapped: MappedByteBuffer → OS maps file to virtual memory

Zero-copy:
  FileChannel.transferTo() → sendfile syscall
  Data moves kernel buffer → socket without user space copy
  Used by Kafka, Netty

inode:
  Contains: permissions, size, timestamps, data block pointers
  NOT the filename (directory maps name → inode number)
```
