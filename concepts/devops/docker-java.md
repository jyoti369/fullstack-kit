# Docker Best Practices for Java Applications

## Multi-Stage Build (minimize image size)
```dockerfile
# Stage 1: Build (JDK)
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app
COPY mvnw pom.xml ./
COPY .mvn .mvn
RUN ./mvnw dependency:go-offline -q   # cache dependencies layer
COPY src ./src
RUN ./mvnw package -DskipTests -q

# Stage 2: Extract layers (Spring Boot Layered JARs)
FROM builder AS layers
RUN java -Djarmode=layertools -jar target/*.jar extract

# Stage 3: Runtime (JRE only)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
RUN addgroup -S spring && adduser -S spring -G spring
USER spring

# Copy layers in dependency order (most stable first)
COPY --from=layers /app/dependencies ./
COPY --from=layers /app/spring-boot-loader ./
COPY --from=layers /app/snapshot-dependencies ./
COPY --from=layers /app/application ./

EXPOSE 8080
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "org.springframework.boot.loader.JarLauncher"]
```

## JVM Flags for Containers
```bash
# Respect container memory limits (Java 8u191+, Java 11+)
-XX:+UseContainerSupport         # detect cgroup limits
-XX:MaxRAMPercentage=75.0        # use 75% of container memory
-XX:InitialRAMPercentage=50.0

# Performance
-XX:+UseG1GC                     # G1 GC (default)
-XX:MaxGCPauseMillis=200
-Djava.security.egd=file:/dev/./urandom  # faster startup on Linux

# Observability
-Dmanagement.endpoints.web.exposure.include=health,info,metrics,prometheus
```

## docker-compose for local dev
```yaml
services:
  app:
    build: .
    ports: ["8080:8080"]
    environment:
      SPRING_PROFILES_ACTIVE: local
      SPRING_DATASOURCE_URL: jdbc:postgresql://db:5432/mydb
    depends_on:
      db:
        condition: service_healthy
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8080/actuator/health"]
      interval: 10s
      retries: 5

  db:
    image: postgres:15-alpine
    environment: { POSTGRES_DB: mydb, POSTGRES_USER: user, POSTGRES_PASSWORD: pass }
    volumes: ["pg_data:/var/lib/postgresql/data"]
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U user"]
      interval: 5s

  redis:
    image: redis:7-alpine
    command: redis-server --maxmemory 256mb --maxmemory-policy allkeys-lru

volumes:
  pg_data:
```

## Tips
```
□ Use eclipse-temurin (Eclipse Adoptium) or amazoncorretto for base images
□ Run as non-root user (security)
□ Set JAVA_TOOL_OPTIONS in environment for JVM flags (Kubernetes-friendly)
□ Enable Spring Boot layered JARs for better Docker layer caching
□ Use health checks for orchestration readiness
□ Never put secrets in Dockerfile — use env vars or secrets manager
```
