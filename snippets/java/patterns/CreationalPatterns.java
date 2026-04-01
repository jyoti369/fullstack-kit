package snippets.java.patterns;

import java.util.HashMap;
import java.util.Map;

/**
 * Creational Design Patterns in Java
 */
public class CreationalPatterns {

    // ---- 1. Singleton (Thread-safe, Lazy) ----
    static class Singleton {
        private static volatile Singleton instance;
        private int value;
        private Singleton() { value = 42; }
        public static Singleton getInstance() {
            if (instance == null) {
                synchronized (Singleton.class) {
                    if (instance == null) instance = new Singleton(); // DCL
                }
            }
            return instance;
        }
        // Or better: Holder pattern (JVM-level thread-safety)
        private static class Holder { static final Singleton INSTANCE = new Singleton(); }
        public static Singleton holderInstance() { return Holder.INSTANCE; }
    }

    // ---- 2. Builder ----
    static class HttpRequest {
        private final String url;
        private final String method;
        private final Map<String, String> headers;
        private final String body;
        private final int timeoutMs;

        private HttpRequest(Builder b) {
            url = b.url; method = b.method; headers = b.headers; body = b.body; timeoutMs = b.timeoutMs;
        }

        static class Builder {
            private final String url;
            private String method = "GET";
            private Map<String, String> headers = new HashMap<>();
            private String body;
            private int timeoutMs = 5000;
            Builder(String url) { this.url = url; }
            Builder method(String m) { this.method = m; return this; }
            Builder header(String k, String v) { headers.put(k, v); return this; }
            Builder body(String b) { this.body = b; return this; }
            Builder timeout(int ms) { this.timeoutMs = ms; return this; }
            HttpRequest build() {
                if (url == null || url.isBlank()) throw new IllegalStateException("URL required");
                return new HttpRequest(this);
            }
        }
        @Override public String toString() { return method + " " + url; }
    }

    // ---- 3. Factory Method ----
    interface Notification { void send(String message); }
    static class EmailNotification implements Notification { public void send(String m){ System.out.println("Email: " + m); } }
    static class SMSNotification   implements Notification { public void send(String m){ System.out.println("SMS: " + m); } }
    static class PushNotification   implements Notification { public void send(String m){ System.out.println("Push: " + m); } }

    static Notification notificationFactory(String type) {
        return switch (type.toLowerCase()) {
            case "email" -> new EmailNotification();
            case "sms"   -> new SMSNotification();
            case "push"  -> new PushNotification();
            default -> throw new IllegalArgumentException("Unknown type: " + type);
        };
    }

    // ---- 4. Abstract Factory ----
    interface Button { void render(); }
    interface Checkbox { void render(); }
    interface UIFactory { Button createButton(); Checkbox createCheckbox(); }

    static class WindowsButton implements Button { public void render(){ System.out.println("Windows Button"); } }
    static class WindowsCheckbox implements Checkbox { public void render(){ System.out.println("Windows Checkbox"); } }
    static class MacButton implements Button { public void render(){ System.out.println("Mac Button"); } }
    static class MacCheckbox implements Checkbox { public void render(){ System.out.println("Mac Checkbox"); } }

    static class WindowsFactory implements UIFactory {
        public Button createButton() { return new WindowsButton(); }
        public Checkbox createCheckbox() { return new WindowsCheckbox(); }
    }
    static class MacFactory implements UIFactory {
        public Button createButton() { return new MacButton(); }
        public Checkbox createCheckbox() { return new MacCheckbox(); }
    }

    // ---- 5. Prototype ----
    static class Config implements Cloneable {
        Map<String, String> settings;
        Config(Map<String, String> s) { settings = new HashMap<>(s); }
        @Override public Config clone() {
            try { Config copy = (Config) super.clone(); copy.settings = new HashMap<>(settings); return copy; }
            catch (CloneNotSupportedException e) { throw new RuntimeException(e); }
        }
    }

    public static void main(String[] args) {
        // Singleton
        Singleton s1 = Singleton.getInstance();
        Singleton s2 = Singleton.getInstance();
        System.out.println(s1 == s2); // true

        // Builder
        HttpRequest req = new HttpRequest.Builder("https://api.example.com/users")
            .method("POST")
            .header("Content-Type", "application/json")
            .body("{\"name\":\"Alice\"}")
            .timeout(3000)
            .build();
        System.out.println(req);

        // Factory
        notificationFactory("email").send("Hello!");
        notificationFactory("sms").send("Hello!");

        // Abstract Factory
        UIFactory factory = new MacFactory();
        factory.createButton().render();
        factory.createCheckbox().render();
    }
}
