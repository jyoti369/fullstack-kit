/**
 * Min Heap / Priority Queue Implementation
 * insert: O(log n) | extractMin: O(log n) | peek: O(1)
 *
 * A complete binary tree where parent <= children.
 * Used in: Dijkstra's, Huffman coding, task scheduling.
 */

class MinHeap {
  constructor() {
    this.heap = [];
  }

  get size() { return this.heap.length; }
  peek() { return this.heap[0]; }
  isEmpty() { return this.heap.length === 0; }

  insert(val) {
    this.heap.push(val);
    this._bubbleUp(this.heap.length - 1);
  }

  extractMin() {
    if (this.isEmpty()) return undefined;
    const min = this.heap[0];
    const last = this.heap.pop();
    if (!this.isEmpty()) {
      this.heap[0] = last;
      this._sinkDown(0);
    }
    return min;
  }

  _bubbleUp(idx) {
    while (idx > 0) {
      const parent = Math.floor((idx - 1) / 2);
      if (this.heap[parent] <= this.heap[idx]) break;
      [this.heap[parent], this.heap[idx]] = [this.heap[idx], this.heap[parent]];
      idx = parent;
    }
  }

  _sinkDown(idx) {
    const n = this.heap.length;
    while (true) {
      let smallest = idx;
      const left = 2 * idx + 1;
      const right = 2 * idx + 2;
      if (left < n && this.heap[left] < this.heap[smallest]) smallest = left;
      if (right < n && this.heap[right] < this.heap[smallest]) smallest = right;
      if (smallest === idx) break;
      [this.heap[smallest], this.heap[idx]] = [this.heap[idx], this.heap[smallest]];
      idx = smallest;
    }
  }
}

// --- Example ---
const pq = new MinHeap();
[5, 3, 8, 1, 2, 7].forEach(v => pq.insert(v));
console.log(pq.extractMin()); // 1
console.log(pq.extractMin()); // 2
console.log(pq.peek());       // 3
console.log(pq.size);         // 4

module.exports = { MinHeap };
