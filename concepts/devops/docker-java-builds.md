# Docker Multi-stage Builds — Java/Spring Boot

## Why Multi-stage?
- Build stage uses full JDK (large)
- Runtime image uses only JRE (small)
- Result: 1GB+ image → ~150MB

## Spring Boot Layered JAR (optimal caching)
```dockerfile
# Stage 1: Build
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /workspace
COPY mvnw pom.xml ./
COPY .mvn .mvn
RUN ./mvnw dependency:go-offline -q   # layer: deps rarely change
COPY src ./src
RUN ./mvnw package -DskipTests -q

# Stage 2: Layer extraction
FROM build AS layers
RUN java -Djarmode=layertools -jar target/*.jar extract

# Stage 3: Final image (JRE only)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
RUN addgroup -S javauser && adduser -S javauser -G javauser
USER javauser

# Layer order: stable → frequently-changing (for Docker cache)
COPY --from=layers /workspace/dependencies ./
COPY --from=layers /workspace/spring-boot-loader ./
COPY --from=layers /workspace/snapshot-dependencies ./
COPY --from=layers /workspace/application ./

EXPOSE 8080
ENTRYPOINT ["java",
  "-XX:+UseContainerSupport",
  "-XX:MaxRAMPercentage=75.0",
  "-Djava.security.egd=file:/dev/./urandom",
  "org.springframework.boot.loader.JarLauncher"]
```

## Native Image with GraalVM (fastest startup)
```dockerfile
FROM ghcr.io/graalvm/native-image:21 AS native-build
WORKDIR /app
COPY . .
RUN ./mvnw -Pnative native:compile -DskipTests

FROM debian:bookworm-slim
COPY --from=native-build /app/target/myapp /app/myapp
EXPOSE 8080
ENTRYPOINT ["/app/myapp"]
# Result: startup ~50ms vs ~3s for JVM, image ~80MB
```

## Health Check + Graceful Shutdown
```dockerfile
HEALTHCHECK --interval=10s --timeout=5s --retries=3 \
  CMD wget -q -O- http://localhost:8080/actuator/health || exit 1
```

```yaml
# application.yml
server:
  shutdown: graceful             # Wait for requests to finish
spring:
  lifecycle:
    timeout-per-shutdown-phase: 30s  # Max wait
```
