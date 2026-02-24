# System Design: Photo Sharing (Instagram)

## Scale
```
1B monthly users, 100M photos/day uploaded
4.2B likes/day, 2B stories/day
```

## Photo Upload Flow
```
Client → API Server
  1. Generate unique photo_id (Snowflake)
  2. Return pre-signed S3 URL to client
  3. Client uploads directly to S3 (avoids server bottleneck)
  4. S3 triggers event → Lambda → update DB
  5. Queue thumbnail generation job (async)
```

## Media Storage
```
Original photos: S3
Thumbnails: S3 (multiple sizes: 150px, 640px, 1080px)
CDN: CloudFront serves thumbnails globally

Photo URL pattern:
  cdn.instagram.com/{user_id}/{photo_id}/{size}.jpg
  → maps to: s3://bucket/photos/{user_id}/{photo_id}/{size}.jpg
```

## Database Schema
```sql
-- Photos table (sharded by user_id)
CREATE TABLE photos (
  id          BIGINT PRIMARY KEY,  -- Snowflake ID
  user_id     BIGINT NOT NULL,
  caption     TEXT,
  location    POINT,
  s3_key      VARCHAR(256),
  like_count  INT DEFAULT 0,
  created_at  TIMESTAMP
);

-- Likes (separate table, sharded by photo_id)
CREATE TABLE likes (
  photo_id    BIGINT,
  user_id     BIGINT,
  created_at  TIMESTAMP,
  PRIMARY KEY (photo_id, user_id)
);

-- Use counter sharding for like_count:
-- Store in Redis, sync to DB every 5 minutes
```

## Feed Generation (Instagram's Approach)
```
Fans-out on write for normal users:
  User posts → push photo_id to all followers' feed cache

Celebrities (>10M followers):
  Fan-out on read: merge into feed at read time

Feed Cache (Redis sorted set):
  Key: feed:{user_id}
  Score: timestamp
  Member: photo_id
  Max size: 300 feed items
```

## Stories (24-hour expiry)
```
Stories table:
  story_id, user_id, media_url, expires_at, created_at

Who viewed:
  Redis Set: story_views:{story_id} → {viewer_ids}
  TTL: 24 hours (auto-expire)

Query "stories of people I follow":
  For each followee → check if they have active story
  → Redis pipeline for bulk lookup
  → Sort by: unseen first, most recent
```
