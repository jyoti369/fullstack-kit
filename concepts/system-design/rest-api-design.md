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

### 2. Status Codes
```
200 OK             — Successful GET/PATCH
201 Created        — Successful POST
204 No Content     — Successful DELETE
400 Bad Request    — Validation error
401 Unauthorized   — Not authenticated
403 Forbidden      — Not authorized
404 Not Found      — Resource missing
409 Conflict       — Duplicate resource
429 Too Many Reqs  — Rate limit hit
500 Server Error   — Unexpected failure
```

### 3. Standard Error Response (Spring Boot)
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(ResourceNotFoundException ex) {
        return new ErrorResponse("NOT_FOUND", ex.getMessage());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidation(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
            .stream().map(fe -> fe.getField() + ": " + fe.getDefaultMessage()).toList();
        return new ErrorResponse("VALIDATION_FAILED", String.join(", ", errors));
    }
}
```

### 4. Pagination (Spring)
```java
@GetMapping
public Page<UserDto> getUsers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size,
        @RequestParam(defaultValue = "id") String sortBy) {
    return userService.findAll(PageRequest.of(page, size, Sort.by(sortBy)));
}
// Response: { data: [...], page: 0, size: 20, totalElements: 150, totalPages: 8 }
```

### 5. HATEOAS (optional, Level 3 REST)
```java
@GetMapping("/{id}")
public EntityModel<UserDto> getUser(@PathVariable Long id) {
    UserDto dto = userService.findById(id);
    return EntityModel.of(dto,
        linkTo(methodOn(UserController.class).getUser(id)).withSelfRel(),
        linkTo(methodOn(UserController.class).getOrders(id)).withRel("orders"));
}
```
