# REST API Design — Best Practices

## URL Structure
```
# Resources are nouns (not verbs)
GET    /users              → list users
POST   /users              → create user
GET    /users/{id}         → get user
PATCH  /users/{id}         → partial update
DELETE /users/{id}         → delete user
GET    /users/{id}/orders  → user's orders

# Bad:
POST /createUser          ← verb in URL
GET  /getUserOrders       ← verb + mixed case

# Use query params for filtering, sorting, pagination:
GET /orders?status=pending&sort=created_at:desc&page=2&limit=20
```

## HTTP Status Codes
```
200 OK              — successful GET/PUT
201 Created         — successful POST (include Location header)
204 No Content      — successful DELETE
400 Bad Request     — invalid input (include error details)
401 Unauthorized    — not authenticated
403 Forbidden       — authenticated but not authorized
404 Not Found       — resource doesn't exist
409 Conflict        — duplicate resource
422 Unprocessable   — validation failed
429 Too Many Requests — rate limited (include Retry-After)
500 Internal Error  — server bug (don't leak stack traces)
```

## Versioning
```
# URL versioning (most common)
GET /v1/users
GET /v2/users

# Header versioning
GET /users
Accept: application/vnd.api+json; version=2

# Never break existing clients — always support v(n-1)
```

## Error Response Format
```json
{
  "error": {
    "code": "VALIDATION_FAILED",
    "message": "Request validation failed",
    "details": [
      { "field": "email", "message": "Invalid email format" },
      { "field": "age", "message": "Must be >= 18" }
    ],
    "trace_id": "4bf92f3577b34da6"
  }
}
```

## Pagination Response
```json
{
  "data": [...],
  "pagination": {
    "cursor": "eyJpZCI6MTAwfQ==",
    "has_next": true,
    "total": 1234
  }
}
```

## HATEOAS (Discoverable APIs)
```json
{
  "id": 123,
  "status": "pending",
  "_links": {
    "self": { "href": "/orders/123" },
    "cancel": { "href": "/orders/123/cancel", "method": "POST" },
    "payment": { "href": "/orders/123/payment" }
  }
}
```

## Idempotency
```
GET, HEAD, OPTIONS: always idempotent (safe)
PUT, DELETE: idempotent (same result if called multiple times)
POST: NOT idempotent by default

# Make POST idempotent with Idempotency-Key header:
POST /payments
Idempotency-Key: a8098c1a-f86e-11da-bd1a-00112444be1e

→ Server stores result, returns same response on retry
```


# REST API Design — Best Practices

## URL Structure
```
# Resources are nouns (not verbs)
GET    /users              → list users
POST   /users              → create user
GET    /users/{id}         → get user
PATCH  /users/{id}         → partial update
DELETE /users/{id}         → delete user
GET    /users/{id}/orders  → user's orders

# Bad:
POST /createUser          ← verb in URL
GET  /getUserOrders       ← verb + mixed case

# Use query params for filtering, sorting, pagination:
GET /orders?status=pending&sort=created_at:desc&page=2&limit=20
```

## HTTP Status Codes
```
200 OK              — successful GET/PUT
201 Created         — successful POST (include Location header)
204 No Content      — successful DELETE
400 Bad Request     — invalid input (include error details)
401 Unauthorized    — not authenticated
403 Forbidden       — authenticated but not authorized
404 Not Found       — resource doesn't exist
409 Conflict        — duplicate resource
422 Unprocessable   — validation failed
429 Too Many Requests — rate limited (include Retry-After)
500 Internal Error  — server bug (don't leak stack traces)
```

## Versioning
```
# URL versioning (most common)
GET /v1/users
GET /v2/users

# Header versioning
GET /users
Accept: application/vnd.api+json; version=2

# Never break existing clients — always support v(n-1)
```

## Error Response Format
```json
{
  "error": {
    "code": "VALIDATION_FAILED",
    "message": "Request validation failed",
    "details": [
      { "field": "email", "message": "Invalid email format" },
      { "field": "age", "message": "Must be >= 18" }
    ],
    "trace_id": "4bf92f3577b34da6"
  }
}
```

## Pagination Response
```json
{
  "data": [...],
  "pagination": {
    "cursor": "eyJpZCI6MTAwfQ==",
    "has_next": true,
    "total": 1234
  }
}
```

## HATEOAS (Discoverable APIs)
```json
{
  "id": 123,
  "status": "pending",
  "_links": {
    "self": { "href": "/orders/123" },
    "cancel": { "href": "/orders/123/cancel", "method": "POST" },
    "payment": { "href": "/orders/123/payment" }
  }
}
```

## Idempotency
```
GET, HEAD, OPTIONS: always idempotent (safe)
PUT, DELETE: idempotent (same result if called multiple times)
POST: NOT idempotent by default

# Make POST idempotent with Idempotency-Key header:
POST /payments
Idempotency-Key: a8098c1a-f86e-11da-bd1a-00112444be1e

→ Server stores result, returns same response on retry
```


# REST API Design — Best Practices

## URL Structure
```
# Resources are nouns (not verbs)
GET    /users              → list users
POST   /users              → create user
GET    /users/{id}         → get user
PATCH  /users/{id}         → partial update
DELETE /users/{id}         → delete user
GET    /users/{id}/orders  → user's orders

# Bad:
POST /createUser          ← verb in URL
GET  /getUserOrders       ← verb + mixed case

# Use query params for filtering, sorting, pagination:
GET /orders?status=pending&sort=created_at:desc&page=2&limit=20
```

## HTTP Status Codes
```
200 OK              — successful GET/PUT
201 Created         — successful POST (include Location header)
204 No Content      — successful DELETE
400 Bad Request     — invalid input (include error details)
401 Unauthorized    — not authenticated
403 Forbidden       — authenticated but not authorized
404 Not Found       — resource doesn't exist
409 Conflict        — duplicate resource
422 Unprocessable   — validation failed
429 Too Many Requests — rate limited (include Retry-After)
500 Internal Error  — server bug (don't leak stack traces)
```

## Versioning
```
# URL versioning (most common)
GET /v1/users
GET /v2/users

# Header versioning
GET /users
Accept: application/vnd.api+json; version=2

# Never break existing clients — always support v(n-1)
```

## Error Response Format
```json
{
  "error": {
    "code": "VALIDATION_FAILED",
    "message": "Request validation failed",
    "details": [
      { "field": "email", "message": "Invalid email format" },
      { "field": "age", "message": "Must be >= 18" }
    ],
    "trace_id": "4bf92f3577b34da6"
  }
}
```

## Pagination Response
```json
{
  "data": [...],
  "pagination": {
    "cursor": "eyJpZCI6MTAwfQ==",
    "has_next": true,
    "total": 1234
  }
}
```

## HATEOAS (Discoverable APIs)
```json
{
  "id": 123,
  "status": "pending",
  "_links": {
    "self": { "href": "/orders/123" },
    "cancel": { "href": "/orders/123/cancel", "method": "POST" },
    "payment": { "href": "/orders/123/payment" }
  }
}
```

## Idempotency
```
GET, HEAD, OPTIONS: always idempotent (safe)
PUT, DELETE: idempotent (same result if called multiple times)
POST: NOT idempotent by default

# Make POST idempotent with Idempotency-Key header:
POST /payments
Idempotency-Key: a8098c1a-f86e-11da-bd1a-00112444be1e

→ Server stores result, returns same response on retry
```
