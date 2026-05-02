package snippets.java.logging;

import org.slf4j.*;
import net.logstash.logback.argument.StructuredArguments;
import net.logstash.logback.marker.Markers;
import java.util.Map;

/**
 * Structured Logging in Java — SLF4J + Logback
 * JSON output for log aggregation (ELK, Datadog, Splunk)
 */
public class StructuredLogging {

    // Using LogstashEncoder for JSON output in logback.xml:
    // <appender name="JSON" class="ch.qos.logback.core.ConsoleAppender">
    //   <encoder class="net.logstash.logback.encoder.LogstashEncoder"/>
    // </appender>

    private static final Logger log = LoggerFactory.getLogger(StructuredLogging.class);

    // 1. Basic structured fields using StructuredArguments
    public static void basicStructured() {
        log.info("User logged in",
            StructuredArguments.kv("userId", 42),
            StructuredArguments.kv("email", "alice@example.com"),
            StructuredArguments.kv("duration_ms", 120)
        );
        // JSON output: {"message":"User logged in","userId":42,"email":"alice@example.com","duration_ms":120}
    }

    // 2. MDC (Mapped Diagnostic Context) — thread-local fields
    public static void withMDC(String requestId, String userId) {
        try (var mdc1 = org.slf4j.MDC.putCloseable("requestId", requestId);
             var mdc2 = org.slf4j.MDC.putCloseable("userId", userId)) {
            log.info("Processing request"); // requestId + userId auto-added to every log in this scope
            processOrder();
        } // MDC cleared automatically even on exception
    }
    private static void processOrder() {
        log.info("Order processed"); // also has requestId + userId in MDC
    }

    // 3. Structured markers (non-indexed fields, bulk JSON)
    public static void withMarkers(Map<String, Object> context) {
        log.info(Markers.appendEntries(context), "Request completed");
    }

    // 4. Error logging with exception details
    public static void logError(String operation, Exception ex, Map<String, String> ctx) {
        log.error("Operation failed: {}", operation,
            StructuredArguments.entries(ctx),
            ex // SLF4J renders full stack trace
        );
    }

    // 5. Log level guard (skip expensive serialization when off)
    public static void conditionalLog(Object expensiveObj) {
        if (log.isDebugEnabled()) {
            log.debug("State dump", StructuredArguments.kv("state", expensiveObj.toString()));
        }
    }

    // 6. Performance logging pattern
    public static <T> T measureAndLog(String operation, java.util.concurrent.Callable<T> task) throws Exception {
        long start = System.currentTimeMillis();
        try {
            T result = task.call();
            log.info("Operation succeeded",
                StructuredArguments.kv("operation", operation),
                StructuredArguments.kv("duration_ms", System.currentTimeMillis() - start));
            return result;
        } catch (Exception ex) {
            log.error("Operation failed",
                StructuredArguments.kv("operation", operation),
                StructuredArguments.kv("duration_ms", System.currentTimeMillis() - start), ex);
            throw ex;
        }
    }

    public static void main(String[] args) throws Exception {
        // Basic usage
        log.info("Server started",
            StructuredArguments.kv("port", 8080),
            StructuredArguments.kv("profile", "prod"));

        // MDC with request context
        withMDC("req-abc-123", "user-456");

        // Measure performance
        measureAndLog("db-query", () -> {
            Thread.sleep(50);
            return "result";
        });
    }
}
