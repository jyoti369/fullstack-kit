# System Design: Real-Time Chat (WhatsApp/Slack)

## Core Requirements
- 1:1 messaging and group chats
- Message delivery: sent → delivered → read
- Presence (online/offline)
- Push notifications for offline users

## Message Flow
```
Sender → WebSocket → Chat Server A
                         ↓ (store to DB)
                     Message DB
                         ↓ (push to recipient)
                  Is recipient online?
             Yes: WebSocket on Chat Server B
             No:  Push Notification Service
```

## Connection Management
```
Client maintains persistent WebSocket connection.
On connect:
  1. Auth (JWT)
  2. Register: userID → serverID in Redis
     SET user:{user_id}:server {server_id} EX 300

When sending to user_id:
  1. Lookup: GET user:{user_id}:server
  2. Route to that server via internal gRPC
  3. Server pushes via WebSocket

Heartbeat every 30s to keep connection alive.
```

## Message Storage
```sql
CREATE TABLE messages (
  id           BIGINT PRIMARY KEY,  -- Snowflake
  conversation_id UUID NOT NULL,
  sender_id    BIGINT,
  content      TEXT,
  type         ENUM('text','image','video','file'),
  status       ENUM('sent','delivered','read'),
  created_at   TIMESTAMP
);
-- Sharded by conversation_id
-- Hot: last 7 days in Cassandra (fast writes)
-- Cold: older messages in S3
```

## Delivery Receipts
```
Sent:      message saved to DB
Delivered: recipient's device acknowledged receipt
Read:      recipient opened the conversation

Implementation:
  Recipient acks → sends delivery receipt WebSocket message
  → updates message.status in DB
  → notifies sender via WebSocket
```

## Group Chat
```
Group: {group_id, name, [member_ids], created_at}

Send to group:
  1. Store one copy of message
  2. Fan-out: add message to each member's inbox

For large groups (Slack channels, 10K+ members):
  Don't fan-out — each client pulls on load.
  Use cursor-based pagination.

Groups table:
  group_members(group_id, user_id, role, joined_at)
  Index: (user_id) → "all groups I'm in"
```

## Presence System
```
Online: WebSocket connected
Offline: connection dropped

Redis TTL approach:
  On connect:    SET presence:{user_id} 1 EX 60
  On heartbeat:  EXPIRE presence:{user_id} 60
  On disconnect: DEL presence:{user_id}
  Check online:  EXISTS presence:{user_id}

For group chats: only show presence for people you've chatted with recently.
(Facebook: 94% of users check <150 friends' presence)
```


# System Design: Real-Time Chat (WhatsApp/Slack)

## Core Requirements
- 1:1 messaging and group chats
- Message delivery: sent → delivered → read
- Presence (online/offline)
- Push notifications for offline users

## Message Flow
```
Sender → WebSocket → Chat Server A
                         ↓ (store to DB)
                     Message DB
                         ↓ (push to recipient)
                  Is recipient online?
             Yes: WebSocket on Chat Server B
             No:  Push Notification Service
```

## Connection Management
```
Client maintains persistent WebSocket connection.
On connect:
  1. Auth (JWT)
  2. Register: userID → serverID in Redis
     SET user:{user_id}:server {server_id} EX 300

When sending to user_id:
  1. Lookup: GET user:{user_id}:server
  2. Route to that server via internal gRPC
  3. Server pushes via WebSocket

Heartbeat every 30s to keep connection alive.
```

## Message Storage
```sql
CREATE TABLE messages (
  id           BIGINT PRIMARY KEY,  -- Snowflake
  conversation_id UUID NOT NULL,
  sender_id    BIGINT,
  content      TEXT,
  type         ENUM('text','image','video','file'),
  status       ENUM('sent','delivered','read'),
  created_at   TIMESTAMP
);
-- Sharded by conversation_id
-- Hot: last 7 days in Cassandra (fast writes)
-- Cold: older messages in S3
```

## Delivery Receipts
```
Sent:      message saved to DB
Delivered: recipient's device acknowledged receipt
Read:      recipient opened the conversation

Implementation:
  Recipient acks → sends delivery receipt WebSocket message
  → updates message.status in DB
  → notifies sender via WebSocket
```

## Group Chat
```
Group: {group_id, name, [member_ids], created_at}

Send to group:
  1. Store one copy of message
  2. Fan-out: add message to each member's inbox

For large groups (Slack channels, 10K+ members):
  Don't fan-out — each client pulls on load.
  Use cursor-based pagination.

Groups table:
  group_members(group_id, user_id, role, joined_at)
  Index: (user_id) → "all groups I'm in"
```

## Presence System
```
Online: WebSocket connected
Offline: connection dropped

Redis TTL approach:
  On connect:    SET presence:{user_id} 1 EX 60
  On heartbeat:  EXPIRE presence:{user_id} 60
  On disconnect: DEL presence:{user_id}
  Check online:  EXISTS presence:{user_id}

For group chats: only show presence for people you've chatted with recently.
(Facebook: 94% of users check <150 friends' presence)
```
