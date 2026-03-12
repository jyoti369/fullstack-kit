# System Design: Search Autocomplete (Google Suggest)

## Requirements
- Return top 5 suggestions for any prefix
- Low latency: <100ms
- Update suggestions based on search frequency
- Scale: Google processes 8.5B searches/day

## Data Collection
```
User searches → aggregation pipeline
  1. Log all searches with timestamps
  2. MapReduce / Spark job (every hour)
  3. Compute frequency for each query-prefix pair
  4. Update trie / suggestion store
```

## Storage: Trie in Database
```
Each trie node stored as a row:
  prefix    | top_5_suggestions (JSON)
------------|------------------------
  'a'       | ['amazon','apple','airbnb',...]
  'am'      | ['amazon','amc','american',...]
  'ama'     | ['amazon','amaz',...]
  'amaz'    | ['amazon','amazing',...]
  'amazon'  | ['amazon shopping','amazon prime',...]

Storage: ~200GB for 5B prefixes
All fits in Redis cluster!
```

## Read Path (Hot Path — Must be fast)
```
Client types 'ama'
  → Check browser cache (0ms)
  → CDN (geographic cache)
  → Load Balancer
  → Autocomplete Service
  → Redis (prefix → suggestions)
  → Return in <50ms

# Redis command:
GET autocomplete:ama  → '["amazon","amaz",...]'
```

## Write Path (Batch — Can be slow)
```
Search logs → Kafka → Aggregation Service
                              ↓
                       Batch compute (hourly)
                              ↓
                       Update DB + Redis
                              ↓
                    Cache invalidation for updated prefixes
```

## Handling Typos (Trie + Edit Distance)
```python
# If no results for 'amzon', try:
1. Exact match: 'amzon' → nothing
2. Delete 1: 'mzon','azon','amon','amzn','amzo' → check each
3. Replace 1: try all single char replacements
4. Fuzzy match limited to top 1000 frequent terms

# Better: store common typo→correction mappings
typo_map = {'amzon': 'amazon', 'gogle': 'google'}
```

## Filtering (Safe Search, Personalization)
```
Filters applied at serving time (not stored):
  - Remove adult content based on user settings
  - Boost personalized results (user's past searches)
  - A/B test different ranking algorithms
  - Geo-filter: local businesses for location queries
```
