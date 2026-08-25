# Kubernetes — Essential Concepts for Developers

## Core Objects

### Pod — smallest deployable unit
```yaml
apiVersion: v1
kind: Pod
metadata:
  name: my-app
spec:
  containers:
  - name: app
    image: my-app:1.0
    ports:
    - containerPort: 3000
    resources:
      requests: { memory: '64Mi', cpu: '250m' }  # reserved
      limits:   { memory: '128Mi', cpu: '500m' }  # max allowed
    env:
    - name: DB_URL
      valueFrom:
        secretKeyRef:
          name: db-secret
          key: url
```

### Deployment — manages ReplicaSets
```yaml
apiVersion: apps/v1
kind: Deployment
spec:
  replicas: 3
  strategy:
    type: RollingUpdate
    rollingUpdate:
      maxSurge: 1         # allow 1 extra pod during update
      maxUnavailable: 0   # keep all pods available
  selector:
    matchLabels:
      app: my-app
  template:
    # ... pod spec here
```

### Service — stable network endpoint
```yaml
kind: Service
spec:
  type: ClusterIP   # or NodePort, LoadBalancer
  selector:
    app: my-app
  ports:
  - port: 80
    targetPort: 3000
```

## Essential kubectl Commands
```bash
# View resources
kubectl get pods -o wide
kubectl describe pod my-pod
kubectl logs my-pod --tail=100 -f

# Exec into pod
kubectl exec -it my-pod -- /bin/sh

# Scale
kubectl scale deployment my-app --replicas=5

# Rolling update
kubectl set image deployment/my-app app=my-app:2.0
kubectl rollout status deployment/my-app
kubectl rollout undo deployment/my-app  # rollback

# Port forward for debugging
kubectl port-forward pod/my-pod 8080:3000

# View resource usage
kubectl top pods
kubectl top nodes
```

## HorizontalPodAutoscaler
```yaml
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
spec:
  scaleTargetRef:
    kind: Deployment
    name: my-app
  minReplicas: 2
  maxReplicas: 20
  metrics:
  - type: Resource
    resource:
      name: cpu
      target:
        type: Utilization
        averageUtilization: 70   # scale up if avg CPU > 70%
```

## Probes (Health Checks)
```yaml
livenessProbe:   # restart pod if fails
  httpGet:
    path: /health
    port: 3000
  initialDelaySeconds: 30
  periodSeconds: 10

readinessProbe:  # stop sending traffic if fails
  httpGet:
    path: /ready
    port: 3000
  initialDelaySeconds: 5
  periodSeconds: 5
```


# Kubernetes — Essential Concepts for Developers

## Core Objects

### Pod — smallest deployable unit
```yaml
apiVersion: v1
kind: Pod
metadata:
  name: my-app
spec:
  containers:
  - name: app
    image: my-app:1.0
    ports:
    - containerPort: 3000
    resources:
      requests: { memory: '64Mi', cpu: '250m' }  # reserved
      limits:   { memory: '128Mi', cpu: '500m' }  # max allowed
    env:
    - name: DB_URL
      valueFrom:
        secretKeyRef:
          name: db-secret
          key: url
```

### Deployment — manages ReplicaSets
```yaml
apiVersion: apps/v1
kind: Deployment
spec:
  replicas: 3
  strategy:
    type: RollingUpdate
    rollingUpdate:
      maxSurge: 1         # allow 1 extra pod during update
      maxUnavailable: 0   # keep all pods available
  selector:
    matchLabels:
      app: my-app
  template:
    # ... pod spec here
```

### Service — stable network endpoint
```yaml
kind: Service
spec:
  type: ClusterIP   # or NodePort, LoadBalancer
  selector:
    app: my-app
  ports:
  - port: 80
    targetPort: 3000
```

## Essential kubectl Commands
```bash
# View resources
kubectl get pods -o wide
kubectl describe pod my-pod
kubectl logs my-pod --tail=100 -f

# Exec into pod
kubectl exec -it my-pod -- /bin/sh

# Scale
kubectl scale deployment my-app --replicas=5

# Rolling update
kubectl set image deployment/my-app app=my-app:2.0
kubectl rollout status deployment/my-app
kubectl rollout undo deployment/my-app  # rollback

# Port forward for debugging
kubectl port-forward pod/my-pod 8080:3000

# View resource usage
kubectl top pods
kubectl top nodes
```

## HorizontalPodAutoscaler
```yaml
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
spec:
  scaleTargetRef:
    kind: Deployment
    name: my-app
  minReplicas: 2
  maxReplicas: 20
  metrics:
  - type: Resource
    resource:
      name: cpu
      target:
        type: Utilization
        averageUtilization: 70   # scale up if avg CPU > 70%
```

## Probes (Health Checks)
```yaml
livenessProbe:   # restart pod if fails
  httpGet:
    path: /health
    port: 3000
  initialDelaySeconds: 30
  periodSeconds: 10

readinessProbe:  # stop sending traffic if fails
  httpGet:
    path: /ready
    port: 3000
  initialDelaySeconds: 5
  periodSeconds: 5
```


# Kubernetes — Essential Concepts for Developers

## Core Objects

### Pod — smallest deployable unit
```yaml
apiVersion: v1
kind: Pod
metadata:
  name: my-app
spec:
  containers:
  - name: app
    image: my-app:1.0
    ports:
    - containerPort: 3000
    resources:
      requests: { memory: '64Mi', cpu: '250m' }  # reserved
      limits:   { memory: '128Mi', cpu: '500m' }  # max allowed
    env:
    - name: DB_URL
      valueFrom:
        secretKeyRef:
          name: db-secret
          key: url
```

### Deployment — manages ReplicaSets
```yaml
apiVersion: apps/v1
kind: Deployment
spec:
  replicas: 3
  strategy:
    type: RollingUpdate
    rollingUpdate:
      maxSurge: 1         # allow 1 extra pod during update
      maxUnavailable: 0   # keep all pods available
  selector:
    matchLabels:
      app: my-app
  template:
    # ... pod spec here
```

### Service — stable network endpoint
```yaml
kind: Service
spec:
  type: ClusterIP   # or NodePort, LoadBalancer
  selector:
    app: my-app
  ports:
  - port: 80
    targetPort: 3000
```

## Essential kubectl Commands
```bash
# View resources
kubectl get pods -o wide
kubectl describe pod my-pod
kubectl logs my-pod --tail=100 -f

# Exec into pod
kubectl exec -it my-pod -- /bin/sh

# Scale
kubectl scale deployment my-app --replicas=5

# Rolling update
kubectl set image deployment/my-app app=my-app:2.0
kubectl rollout status deployment/my-app
kubectl rollout undo deployment/my-app  # rollback

# Port forward for debugging
kubectl port-forward pod/my-pod 8080:3000

# View resource usage
kubectl top pods
kubectl top nodes
```

## HorizontalPodAutoscaler
```yaml
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
spec:
  scaleTargetRef:
    kind: Deployment
    name: my-app
  minReplicas: 2
  maxReplicas: 20
  metrics:
  - type: Resource
    resource:
      name: cpu
      target:
        type: Utilization
        averageUtilization: 70   # scale up if avg CPU > 70%
```

## Probes (Health Checks)
```yaml
livenessProbe:   # restart pod if fails
  httpGet:
    path: /health
    port: 3000
  initialDelaySeconds: 30
  periodSeconds: 10

readinessProbe:  # stop sending traffic if fails
  httpGet:
    path: /ready
    port: 3000
  initialDelaySeconds: 5
  periodSeconds: 5
```
