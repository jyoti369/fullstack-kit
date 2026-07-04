# System Design: Search Engine (Google at high level)

## Components
1. **Crawler** — discover and download web pages
2. **Indexer** — process and index page content
3. **Query Engine** — handle search queries
4. **Ranking** — score and order results

## Web Crawler
```
Seed URLs → URL Frontier (priority queue)
  ↓
Fetcher (HTTP GET, respect robots.txt)
  ↓
HTML Parser → extract text + links
  ↓
├── Store HTML → blob storage
├── Extract links → URL Frontier (deduplicated)
└── Queue for indexing

Scale:
  1B pages, 10KB avg = 10TB storage
  Crawl rate: 1000 pages/sec
  Politeness: max 1 req/sec per domain, respect Crawl-Delay

Deduplication:
  URL seen? → Bloom filter (fast, tiny memory, allow false positives)
  Content duplicate? → SimHash (near-duplicate detection)
```

## Inverted Index
```
Document: "Python is fast and Python is powerful"

Inverted Index:
  python    → [doc1:0, doc1:4]  ← positions for phrase search
  is        → [doc1:1, doc1:5]
  fast      → [doc1:2]
  powerful  → [doc1:6]

Storage: Lucene format (used by Elasticsearch)
  - Posting list compressed (delta encoding)
  - Stored on SSD, sharded by term hash

Index update: near-real-time (NRT)
  - Write to in-memory segment
  - Flush every 1s to disk segment
  - Background merge keeps segment count low
```

## Query Processing
```
"python web framework" →
  1. Query parsing & spelling correction
  2. Expansion: synonyms, related terms
  3. Retrieve posting lists for each term
  4. Intersect (AND) or union (OR)
  5. Score top-K candidates
  6. Apply domain/freshness/personalization boosts
  7. Return top-10

TF-IDF:
  TF = term frequency in doc
  IDF = log(total docs / docs containing term)
  score = TF × IDF  (high = rare term, appears often in doc)

BM25 (better version):
  Accounts for document length normalization
```

## PageRank
```
Idea: a page is important if important pages link to it.

PageRank(A) = (1-d) + d × Σ(PageRank(B) / OutLinks(B))
              for all B linking to A

d = damping factor (0.85)

Computed iteratively (MapReduce, 20-50 iterations)
Force-multiplied by link structure knowledge graph
```


# System Design: Search Engine (Google at high level)

## Components
1. **Crawler** — discover and download web pages
2. **Indexer** — process and index page content
3. **Query Engine** — handle search queries
4. **Ranking** — score and order results

## Web Crawler
```
Seed URLs → URL Frontier (priority queue)
  ↓
Fetcher (HTTP GET, respect robots.txt)
  ↓
HTML Parser → extract text + links
  ↓
├── Store HTML → blob storage
├── Extract links → URL Frontier (deduplicated)
└── Queue for indexing

Scale:
  1B pages, 10KB avg = 10TB storage
  Crawl rate: 1000 pages/sec
  Politeness: max 1 req/sec per domain, respect Crawl-Delay

Deduplication:
  URL seen? → Bloom filter (fast, tiny memory, allow false positives)
  Content duplicate? → SimHash (near-duplicate detection)
```

## Inverted Index
```
Document: "Python is fast and Python is powerful"

Inverted Index:
  python    → [doc1:0, doc1:4]  ← positions for phrase search
  is        → [doc1:1, doc1:5]
  fast      → [doc1:2]
  powerful  → [doc1:6]

Storage: Lucene format (used by Elasticsearch)
  - Posting list compressed (delta encoding)
  - Stored on SSD, sharded by term hash

Index update: near-real-time (NRT)
  - Write to in-memory segment
  - Flush every 1s to disk segment
  - Background merge keeps segment count low
```

## Query Processing
```
"python web framework" →
  1. Query parsing & spelling correction
  2. Expansion: synonyms, related terms
  3. Retrieve posting lists for each term
  4. Intersect (AND) or union (OR)
  5. Score top-K candidates
  6. Apply domain/freshness/personalization boosts
  7. Return top-10

TF-IDF:
  TF = term frequency in doc
  IDF = log(total docs / docs containing term)
  score = TF × IDF  (high = rare term, appears often in doc)

BM25 (better version):
  Accounts for document length normalization
```

## PageRank
```
Idea: a page is important if important pages link to it.

PageRank(A) = (1-d) + d × Σ(PageRank(B) / OutLinks(B))
              for all B linking to A

d = damping factor (0.85)

Computed iteratively (MapReduce, 20-50 iterations)
Force-multiplied by link structure knowledge graph
```
