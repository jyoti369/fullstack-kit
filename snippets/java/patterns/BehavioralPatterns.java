package snippets.java.patterns;

import java.util.*;
import java.util.function.*;

/**
 * Behavioral Design Patterns in Java
 */
public class BehavioralPatterns {

    // ---- 1. Observer Pattern ----
    interface EventListener<T> { void onEvent(T event); }

    static class EventBus<T> {
        private final Map<String, List<EventListener<T>>> listeners = new HashMap<>();
        public void subscribe(String event, EventListener<T> listener) {
            listeners.computeIfAbsent(event, k -> new ArrayList<>()).add(listener);
        }
        public void publish(String event, T data) {
            listeners.getOrDefault(event, List.of()).forEach(l -> l.onEvent(data));
        }
    }

    // ---- 2. Strategy Pattern ----
    interface SortStrategy { void sort(int[] arr); }

    static class Sorter {
        private SortStrategy strategy;
        Sorter(SortStrategy s) { this.strategy = s; }
        void setStrategy(SortStrategy s) { this.strategy = s; }
        void sort(int[] arr) { strategy.sort(arr); }
    }

    // ---- 3. Command Pattern ----
    interface Command { void execute(); void undo(); }

    static class TextEditor {
        private StringBuilder text = new StringBuilder();
        private final Deque<Command> history = new ArrayDeque<>();

        public void executeCommand(Command cmd) { cmd.execute(); history.push(cmd); }
        public void undo() { if (!history.isEmpty()) history.pop().undo(); }
        public String getText() { return text.toString(); }

        class InsertCommand implements Command {
            private final String toInsert; private final int pos;
            InsertCommand(String s, int p) { toInsert=s; pos=p; }
            public void execute() { text.insert(pos, toInsert); }
            public void undo() { text.delete(pos, pos + toInsert.length()); }
        }
    }

    // ---- 4. Chain of Responsibility ----
    abstract static class Handler {
        protected Handler next;
        Handler setNext(Handler h) { this.next = h; return h; }
        abstract boolean handle(int request);
    }

    static class AuthHandler extends Handler {
        public boolean handle(int req) {
            if (req < 1) { System.out.println("Auth failed"); return false; }
            return next == null || next.handle(req);
        }
    }
    static class RateLimitHandler extends Handler {
        public boolean handle(int req) {
            if (req > 1000) { System.out.println("Rate limit exceeded"); return false; }
            return next == null || next.handle(req);
        }
    }
    static class BusinessHandler extends Handler {
        public boolean handle(int req) { System.out.println("Processing: " + req); return true; }
    }

    // ---- 5. Template Method ----
    abstract static class DataProcessor {
        // Template method defines algorithm skeleton
        public final void process() {
            readData();      // step 1
            processData();   // step 2 — abstract, subclass defines
            writeResults();  // step 3
        }
        protected void readData() { System.out.println("Reading data"); }
        protected abstract void processData();
        protected void writeResults() { System.out.println("Writing results"); }
    }

    static class CsvProcessor extends DataProcessor {
        protected void processData() { System.out.println("Processing CSV"); }
    }

    // ---- 6. State Pattern ----
    interface OrderState { void next(OrderContext ctx); String status(); }
    static class OrderContext {
        private OrderState state = new PendingState();
        public void setState(OrderState s) { this.state = s; }
        public void next() { state.next(this); }
        public String getStatus() { return state.status(); }
    }
    static class PendingState  implements OrderState { public void next(OrderContext c){c.setState(new ProcessingState());} public String status(){return "PENDING";} }
    static class ProcessingState implements OrderState { public void next(OrderContext c){c.setState(new ShippedState());} public String status(){return "PROCESSING";} }
    static class ShippedState   implements OrderState { public void next(OrderContext c){c.setState(new DeliveredState());} public String status(){return "SHIPPED";} }
    static class DeliveredState implements OrderState { public void next(OrderContext c){} public String status(){return "DELIVERED";} }

    public static void main(String[] args) {
        // Observer
        EventBus<String> bus = new EventBus<>();
        bus.subscribe("login", e -> System.out.println("User logged in: " + e));
        bus.publish("login", "Alice");

        // Chain of Responsibility
        Handler auth = new AuthHandler();
        auth.setNext(new RateLimitHandler()).setNext(new BusinessHandler());
        auth.handle(42);   // processes
        auth.handle(-1);   // auth fails

        // State
        OrderContext order = new OrderContext();
        System.out.println(order.getStatus()); // PENDING
        order.next(); System.out.println(order.getStatus()); // PROCESSING
        order.next(); System.out.println(order.getStatus()); // SHIPPED

        // Template method
        new CsvProcessor().process();
    }
}


package snippets.java.patterns;

import java.util.*;
import java.util.function.*;

/**
 * Behavioral Design Patterns in Java
 */
public class BehavioralPatterns {

    // ---- 1. Observer Pattern ----
    interface EventListener<T> { void onEvent(T event); }

    static class EventBus<T> {
        private final Map<String, List<EventListener<T>>> listeners = new HashMap<>();
        public void subscribe(String event, EventListener<T> listener) {
            listeners.computeIfAbsent(event, k -> new ArrayList<>()).add(listener);
        }
        public void publish(String event, T data) {
            listeners.getOrDefault(event, List.of()).forEach(l -> l.onEvent(data));
        }
    }

    // ---- 2. Strategy Pattern ----
    interface SortStrategy { void sort(int[] arr); }

    static class Sorter {
        private SortStrategy strategy;
        Sorter(SortStrategy s) { this.strategy = s; }
        void setStrategy(SortStrategy s) { this.strategy = s; }
        void sort(int[] arr) { strategy.sort(arr); }
    }

    // ---- 3. Command Pattern ----
    interface Command { void execute(); void undo(); }

    static class TextEditor {
        private StringBuilder text = new StringBuilder();
        private final Deque<Command> history = new ArrayDeque<>();

        public void executeCommand(Command cmd) { cmd.execute(); history.push(cmd); }
        public void undo() { if (!history.isEmpty()) history.pop().undo(); }
        public String getText() { return text.toString(); }

        class InsertCommand implements Command {
            private final String toInsert; private final int pos;
            InsertCommand(String s, int p) { toInsert=s; pos=p; }
            public void execute() { text.insert(pos, toInsert); }
            public void undo() { text.delete(pos, pos + toInsert.length()); }
        }
    }

    // ---- 4. Chain of Responsibility ----
    abstract static class Handler {
        protected Handler next;
        Handler setNext(Handler h) { this.next = h; return h; }
        abstract boolean handle(int request);
    }

    static class AuthHandler extends Handler {
        public boolean handle(int req) {
            if (req < 1) { System.out.println("Auth failed"); return false; }
            return next == null || next.handle(req);
        }
    }
    static class RateLimitHandler extends Handler {
        public boolean handle(int req) {
            if (req > 1000) { System.out.println("Rate limit exceeded"); return false; }
            return next == null || next.handle(req);
        }
    }
    static class BusinessHandler extends Handler {
        public boolean handle(int req) { System.out.println("Processing: " + req); return true; }
    }

    // ---- 5. Template Method ----
    abstract static class DataProcessor {
        // Template method defines algorithm skeleton
        public final void process() {
            readData();      // step 1
            processData();   // step 2 — abstract, subclass defines
            writeResults();  // step 3
        }
        protected void readData() { System.out.println("Reading data"); }
        protected abstract void processData();
        protected void writeResults() { System.out.println("Writing results"); }
    }

    static class CsvProcessor extends DataProcessor {
        protected void processData() { System.out.println("Processing CSV"); }
    }

    // ---- 6. State Pattern ----
    interface OrderState { void next(OrderContext ctx); String status(); }
    static class OrderContext {
        private OrderState state = new PendingState();
        public void setState(OrderState s) { this.state = s; }
        public void next() { state.next(this); }
        public String getStatus() { return state.status(); }
    }
    static class PendingState  implements OrderState { public void next(OrderContext c){c.setState(new ProcessingState());} public String status(){return "PENDING";} }
    static class ProcessingState implements OrderState { public void next(OrderContext c){c.setState(new ShippedState());} public String status(){return "PROCESSING";} }
    static class ShippedState   implements OrderState { public void next(OrderContext c){c.setState(new DeliveredState());} public String status(){return "SHIPPED";} }
    static class DeliveredState implements OrderState { public void next(OrderContext c){} public String status(){return "DELIVERED";} }

    public static void main(String[] args) {
        // Observer
        EventBus<String> bus = new EventBus<>();
        bus.subscribe("login", e -> System.out.println("User logged in: " + e));
        bus.publish("login", "Alice");

        // Chain of Responsibility
        Handler auth = new AuthHandler();
        auth.setNext(new RateLimitHandler()).setNext(new BusinessHandler());
        auth.handle(42);   // processes
        auth.handle(-1);   // auth fails

        // State
        OrderContext order = new OrderContext();
        System.out.println(order.getStatus()); // PENDING
        order.next(); System.out.println(order.getStatus()); // PROCESSING
        order.next(); System.out.println(order.getStatus()); // SHIPPED

        // Template method
        new CsvProcessor().process();
    }
}
