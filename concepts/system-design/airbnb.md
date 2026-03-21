# System Design: Airbnb

## Core Features
- Search listings by location, dates, price, type
- Double-booking prevention (critical!)
- Payment processing
- Host and guest messaging

## Search System
```
Search request: location, check_in, check_out, guests, filters

Naive approach (won't scale):
  SELECT * FROM listings
  WHERE NOT EXISTS (
    SELECT 1 FROM reservations
    WHERE listing_id = listings.id
    AND overlaps(check_in, check_out)
  )

Better: Pre-compute availability calendar
  Table: listing_availability(listing_id, date, status)
  Index: (listing_id, date)
  Update: when booking occurs, mark dates unavailable
```

## Geosearch
```
PostGIS for location-based search:
  SELECT *, ST_Distance(location, ST_Point(lng, lat)) as dist
  FROM listings
  WHERE ST_DWithin(location, ST_Point(lng, lat), radius_meters)
  AND id IN (available listing IDs)
  ORDER BY dist, price
  LIMIT 50;

Index:
  CREATE INDEX idx_geo ON listings USING GIST(location);

Scaling:
  Elasticsearch with geo_point fields
  → near-instant geo queries at Airbnb scale
```

## Preventing Double Booking (The Hard Part)
```sql
-- Two guests book the same dates simultaneously?
-- Approach 1: DB-level unique constraint (per day)
CREATE TABLE reservations (
  id          BIGINT PRIMARY KEY,
  listing_id  BIGINT NOT NULL,
  guest_id    BIGINT NOT NULL,
  check_in    DATE NOT NULL,
  check_out   DATE NOT NULL,
  status      VARCHAR(20)
);

CREATE UNIQUE INDEX no_overlap ON listing_availability
  (listing_id, date) WHERE status = 'blocked';

-- Approach 2: SELECT FOR UPDATE (pessimistic locking)
BEGIN;
  SELECT * FROM listing_availability
  WHERE listing_id = $1
  AND date BETWEEN $check_in AND $check_out
  FOR UPDATE;  -- lock these rows

  -- If all available:
  INSERT INTO reservations ...;
  UPDATE listing_availability SET status='blocked' WHERE ...;
COMMIT;

-- Approach 3: Optimistic locking with version
UPDATE listings
SET version = version + 1
WHERE id = $1 AND version = $expected_version;
-- Check affected rows > 0
```

## Pricing Engine
```
Base price + dynamic adjustments:
  Weekends: +20%
  Holidays: +40%
  High demand (nearby events): +15-50%
  Last minute (3 days): -10%
  Long stay (7+ nights): -15%

Orchestration:
  Nightly batch job → calculate prices for 12 months ahead
  Store in Redis (listing_id:date → price)
  Real-time override for surge events
```
