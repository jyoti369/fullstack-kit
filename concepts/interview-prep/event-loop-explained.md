# JavaScript Event Loop — Interview Deep Dive

## The Execution Model

JavaScript is **single-threaded** but **non-blocking** thanks to the event loop.

```
┌─────────────────────┐
│    Call Stack        │  ← Executes synchronous code
└─────────┬───────────┘
          │
          ▼
┌─────────────────────┐
│   Web APIs / Node   │  ← setTimeout, fetch, I/O
│   (runs in background) │
└─────────┬───────────┘
          │ callback ready
          ▼
┌─────────────────────┐
│  Microtask Queue    │  ← Promises, queueMicrotask
│  (higher priority)  │
└─────────┬───────────┘
          │
┌─────────────────────┐
│  Macrotask Queue    │  ← setTimeout, setInterval, I/O
│  (lower priority)   │
└─────────────────────┘
```

## Execution Order
1. Execute all synchronous code in the call stack
2. Drain the **microtask queue** (Promises, queueMicrotask)
3. Execute ONE **macrotask** (setTimeout, setInterval)
4. Drain microtask queue again
5. Repeat

## Classic Interview Question
```javascript
console.log('1');                          // sync
setTimeout(() => console.log('2'), 0);     // macrotask
Promise.resolve().then(() => console.log('3')); // microtask
console.log('4');                          // sync

// Output: 1, 4, 3, 2
```

## Key Takeaways
- **Microtasks always run before macrotasks**
- `async/await` is syntactic sugar over Promises (microtasks)
- `process.nextTick()` in Node.js runs before Promise microtasks
- Infinite microtask loop will block the event loop
