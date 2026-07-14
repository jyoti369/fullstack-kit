# System Design: Twitter/X News Feed

## Scale
```
330M monthly active users
500M tweets/day (~5,800/sec)
```

## Core Challenge: Fan-out
When a user tweets, how do all followers see it?

### Option 1: Fan-out on Write (Push)
```
User A tweets → immediately write to all followers' feeds

✅ Fast reads (pre-computed feed)
❌ Slow writes for celebrities (MrBeast: 200M followers → 200M writes)
❌ Wasted work for inactive followers
```

### Option 2: Fan-out on Read (Pull)
```
User B opens feed → pull latest tweets from all followees

✅ Fast writes
❌ Slow reads (imagine following 1000 people)
❌ Heavy DB load
```

### Option 3: Hybrid (Twitter's Actual Approach)
```
Normal users (<1M followers) → fan-out on write
Celebrities / heavy hitters   → fan-out on read

When feed loads:
  1. Load precomputed feed from Redis
  2. Fetch last few tweets from celebrities you follow
  3. Merge and sort by timestamp
```

## Feed Generation
```
Feed table in Redis (sorted set, score = timestamp):
  Key: feed:{user_id}
  Members: tweet_ids
  Limit to last 800 tweets per user

On tweet:
  1. Save tweet to DB
  2. Push tweet_id to followers' feed sets

On read:
  1. ZRANGEBYSCORE feed:123 offset limit → tweet_ids
  2. Fetch tweet data from tweet cache
  3. Merge celebrity tweets
```

## Database Design
```sql
CREATE TABLE tweets (
  id         BIGINT PRIMARY KEY,  -- Snowflake ID
  user_id    BIGINT NOT NULL,
  content    VARCHAR(280),
  media_urls TEXT[],
  like_count INT DEFAULT 0,
  created_at TIMESTAMP
);

CREATE TABLE follows (
  follower_id  BIGINT,
  following_id BIGINT,
  PRIMARY KEY (follower_id, following_id)
);
```

## Read Path
```
Client → CDN (static assets)
       → API Gateway
       → Feed Service
             → Redis (feed cache)
             → Tweet Service (tweet data)
             → User Service (avatar, name)
       ← assembled feed JSON
```


# System Design: Twitter/X News Feed

## Scale
```
330M monthly active users
500M tweets/day (~5,800/sec)
```

## Core Challenge: Fan-out
When a user tweets, how do all followers see it?

### Option 1: Fan-out on Write (Push)
```
User A tweets → immediately write to all followers' feeds

✅ Fast reads (pre-computed feed)
❌ Slow writes for celebrities (MrBeast: 200M followers → 200M writes)
❌ Wasted work for inactive followers
```

### Option 2: Fan-out on Read (Pull)
```
User B opens feed → pull latest tweets from all followees

✅ Fast writes
❌ Slow reads (imagine following 1000 people)
❌ Heavy DB load
```

### Option 3: Hybrid (Twitter's Actual Approach)
```
Normal users (<1M followers) → fan-out on write
Celebrities / heavy hitters   → fan-out on read

When feed loads:
  1. Load precomputed feed from Redis
  2. Fetch last few tweets from celebrities you follow
  3. Merge and sort by timestamp
```

## Feed Generation
```
Feed table in Redis (sorted set, score = timestamp):
  Key: feed:{user_id}
  Members: tweet_ids
  Limit to last 800 tweets per user

On tweet:
  1. Save tweet to DB
  2. Push tweet_id to followers' feed sets

On read:
  1. ZRANGEBYSCORE feed:123 offset limit → tweet_ids
  2. Fetch tweet data from tweet cache
  3. Merge celebrity tweets
```

## Database Design
```sql
CREATE TABLE tweets (
  id         BIGINT PRIMARY KEY,  -- Snowflake ID
  user_id    BIGINT NOT NULL,
  content    VARCHAR(280),
  media_urls TEXT[],
  like_count INT DEFAULT 0,
  created_at TIMESTAMP
);

CREATE TABLE follows (
  follower_id  BIGINT,
  following_id BIGINT,
  PRIMARY KEY (follower_id, following_id)
);
```

## Read Path
```
Client → CDN (static assets)
       → API Gateway
       → Feed Service
             → Redis (feed cache)
             → Tweet Service (tweet data)
             → User Service (avatar, name)
       ← assembled feed JSON
```


# System Design: Twitter/X News Feed

## Scale
```
330M monthly active users
500M tweets/day (~5,800/sec)
```

## Core Challenge: Fan-out
When a user tweets, how do all followers see it?

### Option 1: Fan-out on Write (Push)
```
User A tweets → immediately write to all followers' feeds

✅ Fast reads (pre-computed feed)
❌ Slow writes for celebrities (MrBeast: 200M followers → 200M writes)
❌ Wasted work for inactive followers
```

### Option 2: Fan-out on Read (Pull)
```
User B opens feed → pull latest tweets from all followees

✅ Fast writes
❌ Slow reads (imagine following 1000 people)
❌ Heavy DB load
```

### Option 3: Hybrid (Twitter's Actual Approach)
```
Normal users (<1M followers) → fan-out on write
Celebrities / heavy hitters   → fan-out on read

When feed loads:
  1. Load precomputed feed from Redis
  2. Fetch last few tweets from celebrities you follow
  3. Merge and sort by timestamp
```

## Feed Generation
```
Feed table in Redis (sorted set, score = timestamp):
  Key: feed:{user_id}
  Members: tweet_ids
  Limit to last 800 tweets per user

On tweet:
  1. Save tweet to DB
  2. Push tweet_id to followers' feed sets

On read:
  1. ZRANGEBYSCORE feed:123 offset limit → tweet_ids
  2. Fetch tweet data from tweet cache
  3. Merge celebrity tweets
```

## Database Design
```sql
CREATE TABLE tweets (
  id         BIGINT PRIMARY KEY,  -- Snowflake ID
  user_id    BIGINT NOT NULL,
  content    VARCHAR(280),
  media_urls TEXT[],
  like_count INT DEFAULT 0,
  created_at TIMESTAMP
);

CREATE TABLE follows (
  follower_id  BIGINT,
  following_id BIGINT,
  PRIMARY KEY (follower_id, following_id)
);
```

## Read Path
```
Client → CDN (static assets)
       → API Gateway
       → Feed Service
             → Redis (feed cache)
             → Tweet Service (tweet data)
             → User Service (avatar, name)
       ← assembled feed JSON
```
