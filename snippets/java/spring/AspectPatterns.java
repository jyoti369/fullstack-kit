package snippets.java.spring;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import java.lang.annotation.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Spring AOP — Java's Answer to Decorators/Middleware
 */
@Aspect
@Component
public class AspectPatterns {

    // ---- Custom Annotation ----
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface Loggable { String level() default "INFO"; }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface Cacheable { int ttlSeconds() default 60; }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface RateLimit { int rpm() default 100; }

    // ---- Logging Aspect ----
    @Around("@annotation(snippets.java.spring.AspectPatterns.Loggable)")
    public Object logExecution(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        String method = pjp.getSignature().toShortString();
        System.out.println("→ Entering: " + method);
        try {
            Object result = pjp.proceed();
            System.out.printf("← Exiting: %s (took %dms)%n", method, System.currentTimeMillis()-start);
            return result;
        } catch (Throwable ex) {
            System.out.println("✗ Exception in " + method + ": " + ex.getMessage());
            throw ex;
        }
    }

    // ---- Caching Aspect ----
    private final ConcurrentHashMap<String, Object> cache = new ConcurrentHashMap<>();

    @Around("@annotation(snippets.java.spring.AspectPatterns.Cacheable)")
    public Object cacheResult(ProceedingJoinPoint pjp) throws Throwable {
        String key = pjp.getSignature().toLongString() + java.util.Arrays.toString(pjp.getArgs());
        if (cache.containsKey(key)) {
            System.out.println("Cache hit: " + key);
            return cache.get(key);
        }
        Object result = pjp.proceed();
        cache.put(key, result);
        return result;
    }

    // ---- Transaction Aspect (simplified) ----
    @Around("@annotation(org.springframework.transaction.annotation.Transactional)")
    public Object manageTransaction(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("BEGIN TRANSACTION");
        try {
            Object result = pjp.proceed();
            System.out.println("COMMIT");
            return result;
        } catch (Exception e) {
            System.out.println("ROLLBACK");
            throw e;
        }
    }

    // ---- @Before, @After, @AfterThrowing ----
    @Before("execution(* snippets.java.spring..*Service.*(..))")  // all service methods
    public void beforeServiceCall(org.aspectj.lang.JoinPoint jp) {
        System.out.println("Security check before: " + jp.getSignature().getName());
    }

    @AfterThrowing(pointcut = "execution(* snippets.java.spring..*(..))", throwing = "ex")
    public void afterException(Exception ex) {
        System.err.println("Exception caught by aspect: " + ex.getMessage());
    }

    // ---- Usage Example ----
    @Component
    static class UserService {
        @Loggable
        @Cacheable(ttlSeconds = 300)
        public String getUser(Long id) {
            // method body — automatically logged + cached by aspects
            return "User-" + id;
        }
    }
}
