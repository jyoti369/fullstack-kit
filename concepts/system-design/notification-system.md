# System Design: Notification System

## Notification Types
- **Push**: Mobile (APNs/FCM), Browser
- **Email**: SMTP via SendGrid/SES
- **SMS**: Twilio, SNS
- **In-App**: WebSocket / SSE

## High-Level Flow
```
Trigger (event/API call)
    ↓
Notification Service API
    ↓
Message Queue (Kafka/SQS)
    ↓ (fan-out by type)
┌───────────────────────────┐
│  Push Worker │ Email Worker │ SMS Worker │
└───────────────────────────┘
    ↓              ↓           ↓
 APNs/FCM    SendGrid/SES   Twilio
```

## Reliability Patterns

### Retry with Exponential Backoff
```python
import time
def send_with_retry(send_fn, max_retries=3):
    for attempt in range(max_retries):
        try:
            return send_fn()
        except Exception as e:
            if attempt == max_retries - 1: raise
            time.sleep(2 ** attempt)  # 1s, 2s, 4s
```

### Dead Letter Queue (DLQ)
Messages that fail after all retries go to DLQ for:
- Manual inspection
- Alerting
- Optional replay

### Idempotency Key
Prevent duplicate notifications:
```sql
INSERT INTO notification_log (idempotency_key, user_id, type, sent_at)
VALUES ($1, $2, $3, NOW())
ON CONFLICT (idempotency_key) DO NOTHING;
-- Only send if INSERT succeeded (affected rows > 0)
```

## User Preferences
```json
{
  "user_id": 123,
  "preferences": {
    "push": { "enabled": true, "quiet_hours": "22:00-08:00" },
    "email": { "enabled": true, "digest": "daily" },
    "sms": { "enabled": false }
  }
}
```

## Rate Limiting per User
- Max 5 push notifications per minute per user
- Batch low-priority notifications into digest
- Group similar notifications ("5 new likes" vs 5 separate)

## Scale Numbers
```
Facebook: 1B+ notifications/day
  → 11,574/sec average
  → Multiple Kafka partitions
  → Regional delivery (send from nearest DC)
```


# System Design: Notification System

## Notification Types
- **Push**: Mobile (APNs/FCM), Browser
- **Email**: SMTP via SendGrid/SES
- **SMS**: Twilio, SNS
- **In-App**: WebSocket / SSE

## High-Level Flow
```
Trigger (event/API call)
    ↓
Notification Service API
    ↓
Message Queue (Kafka/SQS)
    ↓ (fan-out by type)
┌───────────────────────────┐
│  Push Worker │ Email Worker │ SMS Worker │
└───────────────────────────┘
    ↓              ↓           ↓
 APNs/FCM    SendGrid/SES   Twilio
```

## Reliability Patterns

### Retry with Exponential Backoff
```python
import time
def send_with_retry(send_fn, max_retries=3):
    for attempt in range(max_retries):
        try:
            return send_fn()
        except Exception as e:
            if attempt == max_retries - 1: raise
            time.sleep(2 ** attempt)  # 1s, 2s, 4s
```

### Dead Letter Queue (DLQ)
Messages that fail after all retries go to DLQ for:
- Manual inspection
- Alerting
- Optional replay

### Idempotency Key
Prevent duplicate notifications:
```sql
INSERT INTO notification_log (idempotency_key, user_id, type, sent_at)
VALUES ($1, $2, $3, NOW())
ON CONFLICT (idempotency_key) DO NOTHING;
-- Only send if INSERT succeeded (affected rows > 0)
```

## User Preferences
```json
{
  "user_id": 123,
  "preferences": {
    "push": { "enabled": true, "quiet_hours": "22:00-08:00" },
    "email": { "enabled": true, "digest": "daily" },
    "sms": { "enabled": false }
  }
}
```

## Rate Limiting per User
- Max 5 push notifications per minute per user
- Batch low-priority notifications into digest
- Group similar notifications ("5 new likes" vs 5 separate)

## Scale Numbers
```
Facebook: 1B+ notifications/day
  → 11,574/sec average
  → Multiple Kafka partitions
  → Regional delivery (send from nearest DC)
```
