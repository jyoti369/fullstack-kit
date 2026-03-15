# System Design: Ride-Sharing (Uber/Lyft)

## Core Features
- Rider requests ride → matches nearest driver
- Real-time location tracking
- ETA calculation
- Surge pricing
- Payment processing

## Scale
```
Uber: 15M trips/day, 3-4M drivers, 100M users
Peak QPS: ~100K location updates/sec from drivers
```

## Location Service (Critical)
```
Driver app → Location Update every 4s → Location Service
                                           ↓
                                     Redis GeoHash
                                     (stores lat/lng)

# Redis GeoAdd
GEOADD drivers 77.5946 12.9716 "driver:123"

# Find drivers within 5km
GEORADIUS drivers 77.5946 12.9716 5 km ASC COUNT 10
```

### GeoHash Sharding
```
GeoHash divides Earth into grid cells.
7-char precision = 153m × 153m cells

Shard by GeoHash prefix:
  Shard 1: GeoHash starts with 'a'-'h'
  Shard 2: GeoHash starts with 'j'-'n'
  ...

Query nearby = query current cell + 8 neighbors
```

## Trip Matching
```
Rider requests → MatchingService
  1. Query driving radius (1km, then 3km, then 5km)
  2. Filter: available drivers, vehicle type
  3. Score: distance + driver rating
  4. Send offer to best driver (10s timeout)
  5. If rejected → next driver

Avoiding double-booking:
  Redis SET NX (atomic set if not exists)
  SET driver:123:status BUSY EX 30 NX
  → only one rider can "claim" a driver
```

## Real-time Tracking (WebSocket)
```
Rider app ←WebSocket→ Tracking Service ←Driver updates

# Driver publishes location:
Publish to: location/{trip_id}

# Rider subscribes to:
Subscribe: location/{trip_id}

Scaling WebSocket:
  - Sticky sessions (route same user to same server)
  - Or: pub/sub layer (Kafka/Redis PubSub) between servers
```

## Surge Pricing
```
Surge = f(demand, supply) in a region

Algorithm:
  supply = available drivers in zone
  demand = pending requests in zone
  surge_multiplier = demand / supply  (capped at 5x)

Zones: GeoHash cells (updated every 30s)
Stored in: Redis (fast read for pricing)
```

## Database Design
```sql
CREATE TABLE trips (
  id         UUID PRIMARY KEY,
  rider_id   BIGINT,
  driver_id  BIGINT,
  status     ENUM('requested','matched','in_progress','completed','cancelled'),
  pickup_lat DECIMAL(9,6),
  pickup_lng DECIMAL(9,6),
  dropoff_lat DECIMAL(9,6),
  dropoff_lng DECIMAL(9,6),
  fare        DECIMAL(10,2),
  start_time  TIMESTAMP,
  end_time    TIMESTAMP
);
-- Shard by rider_id for user history queries
-- Keep current trips in Redis for fast status updates
```
