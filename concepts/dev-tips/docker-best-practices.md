# Docker — Best Practices for Production

## Dockerfile Optimization

### Layer Caching Order
```dockerfile
# BAD: code changes invalidate npm install
FROM node:20-alpine
COPY . .
RUN npm install    # runs every time code changes!

# GOOD: copy package.json first, code last
FROM node:20-alpine
COPY package*.json ./
RUN npm install    # cached unless package.json changes
COPY . .
CMD ["node", "server.js"]
```

### Multi-Stage Builds (reduce image size)
```dockerfile
# Stage 1: Build
FROM node:20-alpine AS builder
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build

# Stage 2: Production (no devDeps, no source)
FROM node:20-alpine AS production
WORKDIR /app
COPY package*.json ./
RUN npm ci --only=production
COPY --from=builder /app/dist ./dist

# Non-root user
RUN adduser -D appuser && chown -R appuser /app
USER appuser

EXPOSE 3000
HEALTHCHECK CMD wget -qO- http://localhost:3000/health || exit 1
CMD ["node", "dist/server.js"]
```

### .dockerignore
```
node_modules
.git
.env
*.log
dist
.DS_Store
README.md
```

## Docker Compose for Dev
```yaml
version: '3.9'
services:
  app:
    build: .
    ports:
      - '3000:3000'
    environment:
      NODE_ENV: development
      DB_URL: postgresql://postgres:pass@db:5432/mydb
    volumes:
      - .:/app              # hot reload
      - /app/node_modules   # don't override node_modules
    depends_on:
      db:
        condition: service_healthy

  db:
    image: postgres:16-alpine
    environment:
      POSTGRES_PASSWORD: pass
      POSTGRES_DB: mydb
    volumes:
      - postgres_data:/var/lib/postgresql/data
    healthcheck:
      test: ["CMD", "pg_isready", "-U", "postgres"]
      interval: 5s
      retries: 5

  redis:
    image: redis:7-alpine
    command: redis-server --appendonly yes
    volumes:
      - redis_data:/data

volumes:
  postgres_data:
  redis_data:
```

## Security Checklist
```
✅ Use specific image tags (not :latest)
✅ Non-root user in container
✅ Read-only filesystem where possible
✅ No secrets in Dockerfile or image layers
✅ Scan images (docker scout, trivy, snyk)
✅ Use COPY over ADD (ADD can fetch URLs)
✅ Minimal base image (alpine > ubuntu)
✅ Health checks defined
✅ Resource limits set (CPU + memory)
```
