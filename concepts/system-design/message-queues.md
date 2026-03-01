# Message Queues & Event Streaming

## Message Queue vs Event Streaming

| | Message Queue | Event Streaming |
|--|--|--|
| Model | Point-to-point | Pub/Sub |
| Retention | Message deleted after consume | Retained (configurable) |
| Consumers | One consumer per message | Many consumers, each reads all |
| Replay | ❌ | ✅ |
| Examples | RabbitMQ, SQS, ActiveMQ | Kafka, Kinesis, Pulsar |

## When to Use Each

**Use Message Queue when:**
- Task distribution (job workers)
- Each task done by exactly one worker
- Email sending, image processing, notifications

**Use Event Streaming when:**
- Event sourcing / audit logs
- Multiple consumers need same event (fan-out)
- Stream processing (real-time analytics)
- Replay capability needed

## Kafka Deep Dive
```
Producer → [Topic: orders] → Consumer Group A (billing)
                           → Consumer Group B (inventory)
                           → Consumer Group C (analytics)

Topic partitioned for parallelism:
Partition 0: order_id % 3 == 0
Partition 1: order_id % 3 == 1
Partition 2: order_id % 3 == 2
```

**Key Guarantees:**
- `acks=0`: No guarantee (fastest)
- `acks=1`: Leader acknowledgment
- `acks=all`: All replicas acknowledge (slowest, safest)

**Consumer Groups:**
- Each partition consumed by exactly one consumer in a group
- Add consumers to scale up to number of partitions

## Delivery Guarantees
| Guarantee | Description | Trade-off |
|-----------|-------------|----------|
| At-most-once | May lose messages | Fastest |
| At-least-once | May duplicate | Safe for idempotent ops |
| Exactly-once | No loss, no dupe | Slowest, hardest |

## Handling at-least-once with Idempotency
```sql
-- Store processed message IDs
INSERT INTO payments (id, order_id, amount)
VALUES ($1, $2, $3)
ON CONFLICT (id) DO NOTHING;  -- Idempotent!
```
