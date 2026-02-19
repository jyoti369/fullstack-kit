# REST API Design — Best Practices

## URL Structure
```
GET    /api/v1/users          — List users
GET    /api/v1/users/:id      — Get single user
POST   /api/v1/users          — Create user
PATCH  /api/v1/users/:id      — Partial update
PUT    /api/v1/users/:id      — Full replace
DELETE /api/v1/users/:id      — Delete user
```

## Key Principles

### 1. Use Nouns, Not Verbs
- ✅ `GET /users` 
- ❌ `GET /getUsers`

### 2. Consistent Status Codes
| Code | Meaning | When |
|------|---------|------|
| 200 | OK | Successful GET/PATCH |
| 201 | Created | Successful POST |
| 204 | No Content | Successful DELETE |
| 400 | Bad Request | Validation error |
| 401 | Unauthorized | Not authenticated |
| 403 | Forbidden | Not authorized |
| 404 | Not Found | Resource missing |
| 409 | Conflict | Duplicate resource |
| 429 | Too Many Requests | Rate limit hit |
| 500 | Server Error | Unexpected failure |

### 3. Pagination
```json
{
  "data": [...],
  "pagination": {
    "page": 2,
    "limit": 20,
    "total": 150,
    "totalPages": 8
  }
}
```

### 4. Filtering & Sorting
```
GET /users?role=admin&sort=-created_at&fields=name,email
```

### 5. Error Response Format
```json
{
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "Email is required",
    "details": [
      { "field": "email", "message": "must be a valid email" }
    ]
  }
}
```

### 6. Versioning
- URL: `/api/v1/users` (most common)
- Header: `Accept: application/vnd.api.v1+json`
