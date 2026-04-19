package algorithms.dynamic_programming;

import java.util.*;

/**
 * Knapsack Variants and Custom Heap Operations — Java
 */
public class KnapsackAndHeap {

    // ---- 0/1 Knapsack (full table) ----
    public static int knapsack(int[] weights, int[] values, int W) {
        int n = weights.length;
        // 1D optimization — iterate backwards
        int[] dp = new int[W + 1];
        for (int i = 0; i < n; i++)
            for (int w = W; w >= weights[i]; w--)
                dp[w] = Math.max(dp[w], dp[w - weights[i]] + values[i]);
        return dp[W];
    }

    // Reconstruct items in the knapsack
    public static List<Integer> knapsackItems(int[] weights, int[] values, int W) {
        int n = weights.length;
        int[][] dp = new int[n+1][W+1];
        for (int i = 1; i <= n; i++)
            for (int w = 0; w <= W; w++) {
                dp[i][w] = dp[i-1][w];
                if (weights[i-1] <= w) dp[i][w] = Math.max(dp[i][w], dp[i-1][w-weights[i-1]] + values[i-1]);
            }
        List<Integer> result = new ArrayList<>();
        int w = W;
        for (int i = n; i >= 1; i--)
            if (dp[i][w] != dp[i-1][w]) { result.add(i-1); w -= weights[i-1]; }
        return result;
    }

    // ---- Custom MinHeap ----
    static class MinHeap {
        private final int[] heap;
        private int size;
        MinHeap(int capacity) { heap = new int[capacity + 1]; }

        public void insert(int val) {
            heap[++size] = val;
            bubbleUp(size);
        }
        public int extractMin() {
            int min = heap[1];
            heap[1] = heap[size--];
            sinkDown(1);
            return min;
        }
        public int peek() { return heap[1]; }
        public boolean isEmpty() { return size == 0; }
        public int size() { return size; }

        private void bubbleUp(int i) {
            while (i > 1 && heap[i] < heap[i/2]) {
                int t = heap[i]; heap[i] = heap[i/2]; heap[i/2] = t;
                i /= 2;
            }
        }
        private void sinkDown(int i) {
            while (2*i <= size) {
                int child = 2*i;
                if (child < size && heap[child+1] < heap[child]) child++;
                if (heap[i] <= heap[child]) break;
                int t = heap[i]; heap[i] = heap[child]; heap[child] = t;
                i = child;
            }
        }
    }

    // ---- Top K elements using Java's PriorityQueue ----
    public static int[] topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> freq = new HashMap<>();
        for (int n : nums) freq.merge(n, 1, Integer::sum);
        // Min-heap of size k
        PriorityQueue<int[]> heap = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        for (var e : freq.entrySet()) {
            heap.offer(new int[]{e.getKey(), e.getValue()});
            if (heap.size() > k) heap.poll();
        }
        int[] result = new int[k];
        for (int i = k-1; i >= 0; i--) result[i] = heap.poll()[0];
        return result;
    }

    public static void main(String[] args) {
        int[] weights = {2, 3, 4, 5};
        int[] values  = {3, 4, 5, 6};
        System.out.println(knapsack(weights, values, 8));  // 10
        System.out.println(knapsackItems(weights, values, 8)); // [2, 1] → items 2 and 1 (0-indexed)

        MinHeap heap = new MinHeap(10);
        for (int v : new int[]{5,3,8,1,2,7}) heap.insert(v);
        System.out.println(heap.extractMin()); // 1
        System.out.println(heap.extractMin()); // 2
        System.out.println(heap.peek());       // 3

        System.out.println(Arrays.toString(topKFrequent(new int[]{1,1,1,2,2,3}, 2))); // [1,2]
    }
}


package algorithms.dynamic_programming;

import java.util.*;

/**
 * Knapsack Variants and Custom Heap Operations — Java
 */
public class KnapsackAndHeap {

    // ---- 0/1 Knapsack (full table) ----
    public static int knapsack(int[] weights, int[] values, int W) {
        int n = weights.length;
        // 1D optimization — iterate backwards
        int[] dp = new int[W + 1];
        for (int i = 0; i < n; i++)
            for (int w = W; w >= weights[i]; w--)
                dp[w] = Math.max(dp[w], dp[w - weights[i]] + values[i]);
        return dp[W];
    }

    // Reconstruct items in the knapsack
    public static List<Integer> knapsackItems(int[] weights, int[] values, int W) {
        int n = weights.length;
        int[][] dp = new int[n+1][W+1];
        for (int i = 1; i <= n; i++)
            for (int w = 0; w <= W; w++) {
                dp[i][w] = dp[i-1][w];
                if (weights[i-1] <= w) dp[i][w] = Math.max(dp[i][w], dp[i-1][w-weights[i-1]] + values[i-1]);
            }
        List<Integer> result = new ArrayList<>();
        int w = W;
        for (int i = n; i >= 1; i--)
            if (dp[i][w] != dp[i-1][w]) { result.add(i-1); w -= weights[i-1]; }
        return result;
    }

    // ---- Custom MinHeap ----
    static class MinHeap {
        private final int[] heap;
        private int size;
        MinHeap(int capacity) { heap = new int[capacity + 1]; }

        public void insert(int val) {
            heap[++size] = val;
            bubbleUp(size);
        }
        public int extractMin() {
            int min = heap[1];
            heap[1] = heap[size--];
            sinkDown(1);
            return min;
        }
        public int peek() { return heap[1]; }
        public boolean isEmpty() { return size == 0; }
        public int size() { return size; }

        private void bubbleUp(int i) {
            while (i > 1 && heap[i] < heap[i/2]) {
                int t = heap[i]; heap[i] = heap[i/2]; heap[i/2] = t;
                i /= 2;
            }
        }
        private void sinkDown(int i) {
            while (2*i <= size) {
                int child = 2*i;
                if (child < size && heap[child+1] < heap[child]) child++;
                if (heap[i] <= heap[child]) break;
                int t = heap[i]; heap[i] = heap[child]; heap[child] = t;
                i = child;
            }
        }
    }

    // ---- Top K elements using Java's PriorityQueue ----
    public static int[] topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> freq = new HashMap<>();
        for (int n : nums) freq.merge(n, 1, Integer::sum);
        // Min-heap of size k
        PriorityQueue<int[]> heap = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        for (var e : freq.entrySet()) {
            heap.offer(new int[]{e.getKey(), e.getValue()});
            if (heap.size() > k) heap.poll();
        }
        int[] result = new int[k];
        for (int i = k-1; i >= 0; i--) result[i] = heap.poll()[0];
        return result;
    }

    public static void main(String[] args) {
        int[] weights = {2, 3, 4, 5};
        int[] values  = {3, 4, 5, 6};
        System.out.println(knapsack(weights, values, 8));  // 10
        System.out.println(knapsackItems(weights, values, 8)); // [2, 1] → items 2 and 1 (0-indexed)

        MinHeap heap = new MinHeap(10);
        for (int v : new int[]{5,3,8,1,2,7}) heap.insert(v);
        System.out.println(heap.extractMin()); // 1
        System.out.println(heap.extractMin()); // 2
        System.out.println(heap.peek());       // 3

        System.out.println(Arrays.toString(topKFrequent(new int[]{1,1,1,2,2,3}, 2))); // [1,2]
    }
}
