# CI/CD Pipeline — Best Practices

## Pipeline Stages
```
Code Push → CI Pipeline → CD Pipeline → Production

CI (Continuous Integration):
  1. Lint & format check
  2. Unit tests
  3. Build
  4. Integration tests
  5. Security scan (SAST)
  6. Docker build & push

CD (Continuous Delivery/Deployment):
  7. Deploy to staging
  8. Smoke tests
  9. Manual approval gate (Delivery) or auto (Deployment)
  10. Deploy to production
  11. Health checks + monitoring
  12. Rollback if needed
```

## GitHub Actions Example
```yaml
name: CI/CD
on:
  push:
    branches: [main]
  pull_request:
    branches: [main]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-node@v4
        with:
          node-version: '20'
          cache: 'npm'
      - run: npm ci
      - run: npm run lint
      - run: npm test -- --coverage
      - uses: codecov/codecov-action@v4

  build:
    needs: test
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: docker/setup-buildx-action@v3
      - uses: docker/login-action@v3
        with:
          registry: ghcr.io
          username: ${{ github.actor }}
          password: ${{ secrets.GITHUB_TOKEN }}
      - uses: docker/build-push-action@v5
        with:
          push: ${{ github.ref == 'refs/heads/main' }}
          tags: ghcr.io/${{ github.repository }}:${{ github.sha }}
          cache-from: type=gha
          cache-to: type=gha,mode=max

  deploy:
    needs: build
    if: github.ref == 'refs/heads/main'
    runs-on: ubuntu-latest
    environment: production
    steps:
      - name: Deploy to production
        run: |
          ssh deploy@prod-server \
            "docker pull ghcr.io/${{ github.repository }}:${{ github.sha }} && \
             docker-compose up -d"
```

## Deployment Strategies
```
Recreate: shut down old, start new (downtime!)

Rolling: replace instances one at a time
  + No downtime, simple
  - Brief period with mixed versions

Blue/Green: two identical environments, switch traffic
  + Instant rollback (switch back)
  - Requires 2x resources

Canary: send 1-5% traffic to new version, monitor, ramp up
  + Catch issues on small blast radius
  - Complex routing, monitoring

Feature Flags: deploy code, enable feature separately
  + Decouple deploy from release
  + A/B testing, gradual rollout
  - Added complexity, need flag management
```

## Key Metrics
```
DORA Metrics:
  Deployment Frequency: how often can you deploy?
  Lead Time for Changes: commit → production time
  Change Failure Rate: % deployments causing incidents
  MTTR: Mean Time To Recovery

Elite teams: deploy multiple times/day, MTTR < 1 hour
```
