# System Design: YouTube

## Scale
```
2.7B monthly active users
500 hours of video uploaded per minute
1B hours of video watched per day
```

## Video Upload Flow
```
1. Client uploads raw video → upload servers (chunked)
2. Original stored in blob storage (S3)
3. Message Queue (Kafka) → Video Processing Service
4. Transcoding: 1080p, 720p, 480p, 360p, 144p
5. Thumbnail extraction (multiple frames)
6. CDN distribution (CloudFront, Akamai)
7. Metadata stored in DB
8. Search index updated
```

## Video Processing
```
Transcoding Pipeline:
  Raw video → decode → transcode → encode → upload to CDN

Parallel processing:
  Split video into segments (GOP-aligned, ~2 min each)
  Process segments in parallel
  Reassemble after transcoding

Formats:
  Container: MP4 (H.264/H.265), WebM (VP9)
  HLS: playlist (.m3u8) + segments (.ts)
  DASH: MPD manifest + segments

Adaptive Bitrate Streaming (ABR):
  Player switches quality based on bandwidth
  CDN edge serves nearest available quality
```

## Video Streaming Architecture
```
Client → DNS → Nearest CDN PoP
             → CDN cache hit: serve segment
             → CDN cache miss: origin (S3) → cache → client

CDN strategy:
  Static content (thumbnails, JS): global CDN
  Video segments: regional CDNs (reduce origin load)
  Live content: custom CDN (lower TTL)
```

## Database Design
```sql
-- Videos table
CREATE TABLE videos (
  id          VARCHAR(11) PRIMARY KEY,  -- YouTube ID format
  channel_id  BIGINT,
  title       VARCHAR(100),
  description TEXT,
  duration    INT,       -- seconds
  view_count  BIGINT,    -- async updated
  like_count  INT,
  status      ENUM('uploading','processing','published','deleted'),
  created_at  TIMESTAMP
);

-- View count update (async to avoid write hotspot)
-- Batch: aggregate in Redis, flush to DB every 5 min
INCRBY video:views:abc123 1
```

## Recommendation System
```
Inputs:
  - Watch history + completion rate
  - Search history
  - Likes/dislikes
  - Similar users (collaborative filtering)
  - Video metadata (tags, title, description)

Architecture:
  Offline: train ML model (TensorFlow)
  Serving: inference service → candidate generation → ranking
  Cache: pre-compute top-N recommendations per user (hourly)
```


# System Design: YouTube

## Scale
```
2.7B monthly active users
500 hours of video uploaded per minute
1B hours of video watched per day
```

## Video Upload Flow
```
1. Client uploads raw video → upload servers (chunked)
2. Original stored in blob storage (S3)
3. Message Queue (Kafka) → Video Processing Service
4. Transcoding: 1080p, 720p, 480p, 360p, 144p
5. Thumbnail extraction (multiple frames)
6. CDN distribution (CloudFront, Akamai)
7. Metadata stored in DB
8. Search index updated
```

## Video Processing
```
Transcoding Pipeline:
  Raw video → decode → transcode → encode → upload to CDN

Parallel processing:
  Split video into segments (GOP-aligned, ~2 min each)
  Process segments in parallel
  Reassemble after transcoding

Formats:
  Container: MP4 (H.264/H.265), WebM (VP9)
  HLS: playlist (.m3u8) + segments (.ts)
  DASH: MPD manifest + segments

Adaptive Bitrate Streaming (ABR):
  Player switches quality based on bandwidth
  CDN edge serves nearest available quality
```

## Video Streaming Architecture
```
Client → DNS → Nearest CDN PoP
             → CDN cache hit: serve segment
             → CDN cache miss: origin (S3) → cache → client

CDN strategy:
  Static content (thumbnails, JS): global CDN
  Video segments: regional CDNs (reduce origin load)
  Live content: custom CDN (lower TTL)
```

## Database Design
```sql
-- Videos table
CREATE TABLE videos (
  id          VARCHAR(11) PRIMARY KEY,  -- YouTube ID format
  channel_id  BIGINT,
  title       VARCHAR(100),
  description TEXT,
  duration    INT,       -- seconds
  view_count  BIGINT,    -- async updated
  like_count  INT,
  status      ENUM('uploading','processing','published','deleted'),
  created_at  TIMESTAMP
);

-- View count update (async to avoid write hotspot)
-- Batch: aggregate in Redis, flush to DB every 5 min
INCRBY video:views:abc123 1
```

## Recommendation System
```
Inputs:
  - Watch history + completion rate
  - Search history
  - Likes/dislikes
  - Similar users (collaborative filtering)
  - Video metadata (tags, title, description)

Architecture:
  Offline: train ML model (TensorFlow)
  Serving: inference service → candidate generation → ranking
  Cache: pre-compute top-N recommendations per user (hourly)
```
