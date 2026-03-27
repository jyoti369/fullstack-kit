# System Design: URL Shortener (bit.ly)

## Requirements
**Functional:**
- Shorten a URL → return short code
- Redirect short URL to original
- Custom aliases (optional)
- Link expiry (optional)

**Non-Functional:**
- 100M URLs created/day
- 10B redirects/day (100:1 read:write)
- Low latency (<10ms redirect)
- High availability

## Estimation
```
Writes: 100M/day = ~1,200/sec
Reads:  10B/day  = ~115,000/sec

Storage (5 years):
  100M/day × 365 × 5 = 182.5B URLs
  Each URL ~500 bytes → ~91 TB

Cache (20% hot URLs = 80% traffic):
  115K reads/sec, 500B avg → ~115 KB/sec → trivial
```

## Short Code Generation

### Option 1: MD5/SHA256 + truncate
```python
import hashlib, base64
def shorten(url):
    hash = hashlib.md5(url.encode()).digest()
    return base64.urlsafe_b64encode(hash)[:7].decode()
# Problem: collisions, same URL gives same hash
```

### Option 2: Auto-increment ID + Base62 ⭐
```python
CHARSET = '0-9A-Za-z'  # 62 chars
def to_base62(n):
    result = ''
    while n:
        result = CHARSET[n % 62] + result
        n //= 62
    return result or '0'
# 7 chars = 62^7 = 3.5 trillion URLs
# Predictable → use distributed ID generator (Snowflake)
```

### Option 3: Pre-generate random codes
- Generate random 7-char base62 codes offline
- Store in `available_codes` table
- Claim one when needed (atomic)

## Architecture
```
Client → CDN → Load Balancer
                    │
         ┌──────────┤
    API Servers (stateless, auto-scale)
         │               │
    [Write Path]    [Read Path]
    DB write        Cache (Redis)
    Cache warm      → DB on miss
         │
    MySQL (primary)
         │
    MySQL (replicas)
```

## Database Schema
```sql
CREATE TABLE urls (
  id         BIGINT PRIMARY KEY AUTO_INCREMENT,
  short_code VARCHAR(10) UNIQUE NOT NULL,
  long_url   TEXT NOT NULL,
  user_id    BIGINT,
  expires_at TIMESTAMP,
  created_at TIMESTAMP DEFAULT NOW()
);
CREATE INDEX idx_short_code ON urls(short_code);
```

## Redirect Flow
```
1. GET /abc1234
2. Check Redis: O(1)
3. Cache hit → 301/302 redirect
4. Cache miss → query DB → cache → redirect

301 (Permanent) → browser caches, no future server hit
302 (Temporary) → always hits server → better analytics
```
