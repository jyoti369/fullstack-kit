# Behavioral Design Patterns

## 1. Observer Pattern
```typescript
interface Observer {
  update(data: any): void;
}

class EventEmitter {
  private listeners = new Map<string, Observer[]>();

  on(event: string, observer: Observer) {
    if (!this.listeners.has(event)) this.listeners.set(event, []);
    this.listeners.get(event)!.push(observer);
  }

  emit(event: string, data: any) {
    this.listeners.get(event)?.forEach(o => o.update(data));
  }

  off(event: string, observer: Observer) {
    const list = this.listeners.get(event) ?? [];
    this.listeners.set(event, list.filter(o => o !== observer));
  }
}
```

## 2. Strategy Pattern
```typescript
interface SortStrategy {
  sort(data: number[]): number[];
}

class QuickSort implements SortStrategy {
  sort(data: number[]) { return [...data].sort((a,b)=>a-b); }
}

class MergeSort implements SortStrategy {
  sort(data: number[]) { /* merge sort impl */ return data; }
}

class Sorter {
  constructor(private strategy: SortStrategy) {}
  setStrategy(strategy: SortStrategy) { this.strategy = strategy; }
  sort(data: number[]) { return this.strategy.sort(data); }
}

// Switch algorithms at runtime
const sorter = new Sorter(new QuickSort());
sorter.sort([3,1,4]);
sorter.setStrategy(new MergeSort());
sorter.sort([3,1,4]);
```

## 3. Command Pattern
```typescript
interface Command {
  execute(): void;
  undo(): void;
}

class CommandHistory {
  private history: Command[] = [];

  execute(cmd: Command) {
    cmd.execute();
    this.history.push(cmd);
  }

  undo() {
    this.history.pop()?.undo();
  }
}

// Text editor example
class InsertTextCommand implements Command {
  constructor(private editor: Editor, private text: string, private pos: number) {}
  execute() { this.editor.insert(this.text, this.pos); }
  undo() { this.editor.delete(this.pos, this.text.length); }
}
```

## 4. Iterator Pattern
```typescript
class Range {
  constructor(private start: number, private end: number, private step = 1) {}

  [Symbol.iterator]() {
    let current = this.start;
    const { end, step } = this;
    return {
      next() {
        if (current < end) {
          const value = current;
          current += step;
          return { value, done: false };
        }
        return { value: undefined, done: true };
      }
    };
  }
}

// Usage
for (const n of new Range(0, 10, 2)) console.log(n); // 0,2,4,6,8
const nums = [...new Range(1, 6)]; // [1,2,3,4,5]
```

## 5. Template Method
```typescript
abstract class DataProcessor {
  // Template method — defines skeleton
  process(data: any[]) {
    const filtered = this.filter(data);
    const transformed = this.transform(filtered);
    this.save(transformed);
  }

  filter(data: any[]) { return data; }  // default: no filter
  abstract transform(data: any[]): any[];
  abstract save(data: any[]): void;
}

class CSVProcessor extends DataProcessor {
  transform(data: any[]) { return data.map(d => Object.values(d).join(',')); }
  save(data: any[]) { /* write to file */ }
}
```


# Behavioral Design Patterns

## 1. Observer Pattern
```typescript
interface Observer {
  update(data: any): void;
}

class EventEmitter {
  private listeners = new Map<string, Observer[]>();

  on(event: string, observer: Observer) {
    if (!this.listeners.has(event)) this.listeners.set(event, []);
    this.listeners.get(event)!.push(observer);
  }

  emit(event: string, data: any) {
    this.listeners.get(event)?.forEach(o => o.update(data));
  }

  off(event: string, observer: Observer) {
    const list = this.listeners.get(event) ?? [];
    this.listeners.set(event, list.filter(o => o !== observer));
  }
}
```

## 2. Strategy Pattern
```typescript
interface SortStrategy {
  sort(data: number[]): number[];
}

class QuickSort implements SortStrategy {
  sort(data: number[]) { return [...data].sort((a,b)=>a-b); }
}

class MergeSort implements SortStrategy {
  sort(data: number[]) { /* merge sort impl */ return data; }
}

class Sorter {
  constructor(private strategy: SortStrategy) {}
  setStrategy(strategy: SortStrategy) { this.strategy = strategy; }
  sort(data: number[]) { return this.strategy.sort(data); }
}

// Switch algorithms at runtime
const sorter = new Sorter(new QuickSort());
sorter.sort([3,1,4]);
sorter.setStrategy(new MergeSort());
sorter.sort([3,1,4]);
```

## 3. Command Pattern
```typescript
interface Command {
  execute(): void;
  undo(): void;
}

class CommandHistory {
  private history: Command[] = [];

  execute(cmd: Command) {
    cmd.execute();
    this.history.push(cmd);
  }

  undo() {
    this.history.pop()?.undo();
  }
}

// Text editor example
class InsertTextCommand implements Command {
  constructor(private editor: Editor, private text: string, private pos: number) {}
  execute() { this.editor.insert(this.text, this.pos); }
  undo() { this.editor.delete(this.pos, this.text.length); }
}
```

## 4. Iterator Pattern
```typescript
class Range {
  constructor(private start: number, private end: number, private step = 1) {}

  [Symbol.iterator]() {
    let current = this.start;
    const { end, step } = this;
    return {
      next() {
        if (current < end) {
          const value = current;
          current += step;
          return { value, done: false };
        }
        return { value: undefined, done: true };
      }
    };
  }
}

// Usage
for (const n of new Range(0, 10, 2)) console.log(n); // 0,2,4,6,8
const nums = [...new Range(1, 6)]; // [1,2,3,4,5]
```

## 5. Template Method
```typescript
abstract class DataProcessor {
  // Template method — defines skeleton
  process(data: any[]) {
    const filtered = this.filter(data);
    const transformed = this.transform(filtered);
    this.save(transformed);
  }

  filter(data: any[]) { return data; }  // default: no filter
  abstract transform(data: any[]): any[];
  abstract save(data: any[]): void;
}

class CSVProcessor extends DataProcessor {
  transform(data: any[]) { return data.map(d => Object.values(d).join(',')); }
  save(data: any[]) { /* write to file */ }
}
```
