# Service Mesh & Observability

## Service Mesh
Infrastructure layer that handles service-to-service communication.

### Components
```
Data Plane: Sidecar proxies (Envoy) alongside each service
  - Handle: TLS, retries, circuit breaking, load balancing
  - Collect: metrics, traces, logs

Control Plane: Istio, Linkerd
  - Configure proxies
  - Service discovery
  - Policy enforcement
```

### Key Features
```yaml
# Istio VirtualService — traffic management
apiVersion: networking.istio.io/v1alpha3
kind: VirtualService
spec:
  http:
  - route:
    - destination:
        host: my-service
        subset: v1
      weight: 80
    - destination:
        host: my-service
        subset: v2    # canary: 20% traffic to new version
      weight: 20
```

## The Three Pillars of Observability

### 1. Metrics
Numeric measurements over time.
```
RED Method (for services):
  R - Rate (requests/sec)
  E - Errors (error rate %)
  D - Duration (latency percentiles: p50, p95, p99)

USE Method (for resources):
  U - Utilization (CPU %)
  S - Saturation (queue depth)
  E - Errors (disk errors/sec)

Tools: Prometheus + Grafana
```

### 2. Logs
Immutable record of events.
```python
# Structured logging (JSON)
logger.info('payment_processed', extra={
    'user_id': 123,
    'amount': 49.99,
    'currency': 'USD',
    'trace_id': '4bf92f3577b34da6',
    'duration_ms': 142
})

# Log levels: DEBUG < INFO < WARN < ERROR < FATAL
# Tools: ELK Stack, Datadog, Splunk
```

### 3. Distributed Tracing
Track a request across multiple services.
```
Request ID: 4bf92f3577b34da6
  ├── API Gateway        [0ms - 245ms]
  │     ├── Auth Service [5ms - 15ms]
  │     ├── User Service [20ms - 80ms]
  │     └── Order Service[85ms - 240ms]
  │           ├── DB Query[90ms - 150ms]
  │           └── Cache   [155ms - 160ms]

Tools: Jaeger, Zipkin, Datadog APM, AWS X-Ray

# Propagate trace context:
X-B3-TraceId: 4bf92f3577b34da6
X-B3-SpanId: a2fb4a1d1a96d312
```

## Alerting Best Practices
```
1. Alert on symptoms, not causes
   ✅ "Error rate > 1% for 5 minutes"
   ❌ "CPU > 90%" (doesn't mean users are affected)

2. Have meaningful SLOs
   99.9% availability = 8.7 hrs downtime/year
   99.99% = 52 min/year

3. Runbooks for every alert
   What fired → Why it matters → How to fix
```


# Service Mesh & Observability

## Service Mesh
Infrastructure layer that handles service-to-service communication.

### Components
```
Data Plane: Sidecar proxies (Envoy) alongside each service
  - Handle: TLS, retries, circuit breaking, load balancing
  - Collect: metrics, traces, logs

Control Plane: Istio, Linkerd
  - Configure proxies
  - Service discovery
  - Policy enforcement
```

### Key Features
```yaml
# Istio VirtualService — traffic management
apiVersion: networking.istio.io/v1alpha3
kind: VirtualService
spec:
  http:
  - route:
    - destination:
        host: my-service
        subset: v1
      weight: 80
    - destination:
        host: my-service
        subset: v2    # canary: 20% traffic to new version
      weight: 20
```

## The Three Pillars of Observability

### 1. Metrics
Numeric measurements over time.
```
RED Method (for services):
  R - Rate (requests/sec)
  E - Errors (error rate %)
  D - Duration (latency percentiles: p50, p95, p99)

USE Method (for resources):
  U - Utilization (CPU %)
  S - Saturation (queue depth)
  E - Errors (disk errors/sec)

Tools: Prometheus + Grafana
```

### 2. Logs
Immutable record of events.
```python
# Structured logging (JSON)
logger.info('payment_processed', extra={
    'user_id': 123,
    'amount': 49.99,
    'currency': 'USD',
    'trace_id': '4bf92f3577b34da6',
    'duration_ms': 142
})

# Log levels: DEBUG < INFO < WARN < ERROR < FATAL
# Tools: ELK Stack, Datadog, Splunk
```

### 3. Distributed Tracing
Track a request across multiple services.
```
Request ID: 4bf92f3577b34da6
  ├── API Gateway        [0ms - 245ms]
  │     ├── Auth Service [5ms - 15ms]
  │     ├── User Service [20ms - 80ms]
  │     └── Order Service[85ms - 240ms]
  │           ├── DB Query[90ms - 150ms]
  │           └── Cache   [155ms - 160ms]

Tools: Jaeger, Zipkin, Datadog APM, AWS X-Ray

# Propagate trace context:
X-B3-TraceId: 4bf92f3577b34da6
X-B3-SpanId: a2fb4a1d1a96d312
```

## Alerting Best Practices
```
1. Alert on symptoms, not causes
   ✅ "Error rate > 1% for 5 minutes"
   ❌ "CPU > 90%" (doesn't mean users are affected)

2. Have meaningful SLOs
   99.9% availability = 8.7 hrs downtime/year
   99.99% = 52 min/year

3. Runbooks for every alert
   What fired → Why it matters → How to fix
```


# Service Mesh & Observability

## Service Mesh
Infrastructure layer that handles service-to-service communication.

### Components
```
Data Plane: Sidecar proxies (Envoy) alongside each service
  - Handle: TLS, retries, circuit breaking, load balancing
  - Collect: metrics, traces, logs

Control Plane: Istio, Linkerd
  - Configure proxies
  - Service discovery
  - Policy enforcement
```

### Key Features
```yaml
# Istio VirtualService — traffic management
apiVersion: networking.istio.io/v1alpha3
kind: VirtualService
spec:
  http:
  - route:
    - destination:
        host: my-service
        subset: v1
      weight: 80
    - destination:
        host: my-service
        subset: v2    # canary: 20% traffic to new version
      weight: 20
```

## The Three Pillars of Observability

### 1. Metrics
Numeric measurements over time.
```
RED Method (for services):
  R - Rate (requests/sec)
  E - Errors (error rate %)
  D - Duration (latency percentiles: p50, p95, p99)

USE Method (for resources):
  U - Utilization (CPU %)
  S - Saturation (queue depth)
  E - Errors (disk errors/sec)

Tools: Prometheus + Grafana
```

### 2. Logs
Immutable record of events.
```python
# Structured logging (JSON)
logger.info('payment_processed', extra={
    'user_id': 123,
    'amount': 49.99,
    'currency': 'USD',
    'trace_id': '4bf92f3577b34da6',
    'duration_ms': 142
})

# Log levels: DEBUG < INFO < WARN < ERROR < FATAL
# Tools: ELK Stack, Datadog, Splunk
```

### 3. Distributed Tracing
Track a request across multiple services.
```
Request ID: 4bf92f3577b34da6
  ├── API Gateway        [0ms - 245ms]
  │     ├── Auth Service [5ms - 15ms]
  │     ├── User Service [20ms - 80ms]
  │     └── Order Service[85ms - 240ms]
  │           ├── DB Query[90ms - 150ms]
  │           └── Cache   [155ms - 160ms]

Tools: Jaeger, Zipkin, Datadog APM, AWS X-Ray

# Propagate trace context:
X-B3-TraceId: 4bf92f3577b34da6
X-B3-SpanId: a2fb4a1d1a96d312
```

## Alerting Best Practices
```
1. Alert on symptoms, not causes
   ✅ "Error rate > 1% for 5 minutes"
   ❌ "CPU > 90%" (doesn't mean users are affected)

2. Have meaningful SLOs
   99.9% availability = 8.7 hrs downtime/year
   99.99% = 52 min/year

3. Runbooks for every alert
   What fired → Why it matters → How to fix
```
