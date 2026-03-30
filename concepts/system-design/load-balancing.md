# Load Balancing — Strategies & Algorithms

## Why Load Balancing?
Distribute traffic across multiple servers to:
- Prevent single point of failure
- Maximize throughput
- Minimize latency
- Enable horizontal scaling

## Algorithms

### 1. Round Robin
Requests go to each server in order: 1→2→3→1→2→3
```
+ Simple, equal distribution
- Doesn't consider server load or request complexity
```

### 2. Weighted Round Robin
Servers get requests proportional to their weight.
```
Server A (weight=3): gets 3 out of every 5 requests
Server B (weight=2): gets 2 out of every 5 requests
+ Handles heterogeneous servers
```

### 3. Least Connections
Route to server with fewest active connections.
```
+ Better for long-lived connections (WebSockets, file uploads)
- More overhead to track connections
```

### 4. IP Hash / Consistent Hashing
Hash client IP to always route to same server.
```
+ Session persistence without sticky sessions
+ Useful for caching (same server = cache hit)
- Uneven distribution if few IPs
```

### 5. Least Response Time
Route to server with lowest latency + fewest connections.
```
+ Best user experience
- Requires active health monitoring
```

### 6. Random
Pick a server randomly.
```
+ Simple, surprisingly effective at scale (law of large numbers)
```

## Layer 4 vs Layer 7

| | Layer 4 (Transport) | Layer 7 (Application) |
|--|--|--|
| Sees | IP + Port | HTTP headers, cookies, URL |
| Speed | Faster | Slower |
| Routing | By IP/port | By content |
| Example | AWS NLB | AWS ALB, Nginx |

## Consistent Hashing
Used to minimize remapping when servers are added/removed.
```
Hash ring: 0 ─────────────────── 2³²
Servers:   ────A────────B────────C────
Keys:      ──k1──k2──────k3──k4────k5──

k1→A, k2→A, k3→B, k4→C, k5→C

Add server D between B and C:
Only k4 remaps to D. k1,k2,k3,k5 unaffected.
```
**Virtual nodes**: Each server has multiple positions on ring → better distribution.

## Health Checks
- **Passive**: Monitor real traffic errors
- **Active**: Send periodic pings to check liveness
- **Circuit Breaker**: Stop sending if failure rate too high
