# System Design: Payment System

## Core Requirements
- Process payments reliably (no double charges)
- Exactly-once semantics
- Reconciliation with banks
- Fraud detection
- PCI DSS compliance

## Exactly-Once Payment Processing
```
Client generates idempotency key (UUID)
  → store with request
  → retry safe: same key = check if already processed

Payment flow:
1. Client: POST /payments { idempotency_key: "uuid", amount: 100 }
2. API Server: check Redis for idempotency_key
   - If exists: return cached response
   - If not: lock (SET NX), process, cache response
3. Call payment processor (Stripe/Braintree)
4. Store result in DB atomically
5. Release lock
```

## Database Schema
```sql
CREATE TABLE payments (
  id               UUID PRIMARY KEY,
  idempotency_key  UUID UNIQUE NOT NULL,
  user_id          BIGINT,
  amount           DECIMAL(10,2),
  currency         CHAR(3),
  status           ENUM('pending','processing','completed','failed','refunded'),
  processor        VARCHAR(50),  -- 'stripe', 'braintree'
  processor_txn_id VARCHAR(100), -- external transaction ID
  created_at       TIMESTAMP,
  updated_at       TIMESTAMP
);

CREATE TABLE ledger (
  id               BIGSERIAL PRIMARY KEY,
  payment_id       UUID REFERENCES payments(id),
  account_id       BIGINT,
  amount           DECIMAL(10,2),
  direction        ENUM('debit','credit'),
  created_at       TIMESTAMP
);
-- Double-entry bookkeeping: every payment creates 2 ledger entries
```

## Reconciliation
```
Daily batch:
1. Download statement from bank/processor
2. Match each transaction against our DB
3. Flag mismatches (in DB but not in bank, or vice versa)
4. Alert finance team for manual review

Automated checks:
  - Total debits == total credits (ledger balance)
  - Every payment.status=completed has matching bank record
  - No orphaned processor_txn_ids
```

## Fraud Detection
```
Rule-based (fast, O(1)):
  - Amount > $10,000 → manual review
  - 5+ transactions in 1 minute from same IP → flag
  - Card from country X, shipping to country Y
  - Known bad IPs/cards (blocklist)

ML-based (slower, runs async):
  Features: transaction velocity, geolocation, device fingerprint,
            spending pattern deviation, merchants category
  Model: XGBoost, trained on historical fraud labels
  Threshold: score > 0.7 → decline, 0.4-0.7 → 3DS auth
```

## PCI DSS Compliance
```
Never store:
  - Full card number (store only last 4 digits)
  - CVV (never)
  - PIN

Use tokenization:
  Raw card → Stripe → token
  Store only token → use for future charges
  Token useless if stolen

Infrastructure:
  - Separate network segment for payment servers
  - All communication encrypted (TLS 1.2+)
  - Access logs, audit trails
  - Annual PCI audit
```


# System Design: Payment System

## Core Requirements
- Process payments reliably (no double charges)
- Exactly-once semantics
- Reconciliation with banks
- Fraud detection
- PCI DSS compliance

## Exactly-Once Payment Processing
```
Client generates idempotency key (UUID)
  → store with request
  → retry safe: same key = check if already processed

Payment flow:
1. Client: POST /payments { idempotency_key: "uuid", amount: 100 }
2. API Server: check Redis for idempotency_key
   - If exists: return cached response
   - If not: lock (SET NX), process, cache response
3. Call payment processor (Stripe/Braintree)
4. Store result in DB atomically
5. Release lock
```

## Database Schema
```sql
CREATE TABLE payments (
  id               UUID PRIMARY KEY,
  idempotency_key  UUID UNIQUE NOT NULL,
  user_id          BIGINT,
  amount           DECIMAL(10,2),
  currency         CHAR(3),
  status           ENUM('pending','processing','completed','failed','refunded'),
  processor        VARCHAR(50),  -- 'stripe', 'braintree'
  processor_txn_id VARCHAR(100), -- external transaction ID
  created_at       TIMESTAMP,
  updated_at       TIMESTAMP
);

CREATE TABLE ledger (
  id               BIGSERIAL PRIMARY KEY,
  payment_id       UUID REFERENCES payments(id),
  account_id       BIGINT,
  amount           DECIMAL(10,2),
  direction        ENUM('debit','credit'),
  created_at       TIMESTAMP
);
-- Double-entry bookkeeping: every payment creates 2 ledger entries
```

## Reconciliation
```
Daily batch:
1. Download statement from bank/processor
2. Match each transaction against our DB
3. Flag mismatches (in DB but not in bank, or vice versa)
4. Alert finance team for manual review

Automated checks:
  - Total debits == total credits (ledger balance)
  - Every payment.status=completed has matching bank record
  - No orphaned processor_txn_ids
```

## Fraud Detection
```
Rule-based (fast, O(1)):
  - Amount > $10,000 → manual review
  - 5+ transactions in 1 minute from same IP → flag
  - Card from country X, shipping to country Y
  - Known bad IPs/cards (blocklist)

ML-based (slower, runs async):
  Features: transaction velocity, geolocation, device fingerprint,
            spending pattern deviation, merchants category
  Model: XGBoost, trained on historical fraud labels
  Threshold: score > 0.7 → decline, 0.4-0.7 → 3DS auth
```

## PCI DSS Compliance
```
Never store:
  - Full card number (store only last 4 digits)
  - CVV (never)
  - PIN

Use tokenization:
  Raw card → Stripe → token
  Store only token → use for future charges
  Token useless if stolen

Infrastructure:
  - Separate network segment for payment servers
  - All communication encrypted (TLS 1.2+)
  - Access logs, audit trails
  - Annual PCI audit
```
