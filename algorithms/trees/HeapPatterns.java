package algorithms.trees;

import java.util.*;

/**
 * Heap / PriorityQueue Patterns
 */
public class HeapPatterns {

    // 1. Kth largest element
    public static int findKthLargest(int[] nums, int k) {
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        for (int num : nums) {
            minHeap.offer(num);
            if (minHeap.size() > k) minHeap.poll();
        }
        return minHeap.peek();
    }

    // 2. K closest points to origin
    public static int[][] kClosest(int[][] points, int k) {
        PriorityQueue<int[]> maxHeap = new PriorityQueue<>(
            (a, b) -> (b[0]*b[0]+b[1]*b[1]) - (a[0]*a[0]+a[1]*a[1]));
        for (int[] p : points) {
            maxHeap.offer(p);
            if (maxHeap.size() > k) maxHeap.poll();
        }
        return maxHeap.toArray(new int[0][]);
    }

    // 3. Merge K sorted lists
    public static int[] mergeKSortedArrays(int[][] arrays) {
        PriorityQueue<int[]> heap = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        int totalLen = 0;
        for (int i = 0; i < arrays.length; i++) {
            if (arrays[i].length > 0) { heap.offer(new int[]{arrays[i][0], i, 0}); totalLen += arrays[i].length; }
        }
        int[] result = new int[totalLen];
        int idx = 0;
        while (!heap.isEmpty()) {
            int[] curr = heap.poll();
            result[idx++] = curr[0];
            int arrIdx = curr[1], elemIdx = curr[2];
            if (elemIdx + 1 < arrays[arrIdx].length)
                heap.offer(new int[]{arrays[arrIdx][elemIdx+1], arrIdx, elemIdx+1});
        }
        return result;
    }

    // 4. Find median from data stream
    static class MedianFinder {
        private PriorityQueue<Integer> lo = new PriorityQueue<>(Collections.reverseOrder()); // max heap
        private PriorityQueue<Integer> hi = new PriorityQueue<>(); // min heap

        public void addNum(int num) {
            lo.offer(num);
            hi.offer(lo.poll());
            if (lo.size() < hi.size()) lo.offer(hi.poll());
        }

        public double findMedian() {
            return lo.size() > hi.size() ? lo.peek() : (lo.peek() + hi.peek()) / 2.0;
        }
    }

    // 5. Task Scheduler
    public static int leastInterval(char[] tasks, int n) {
        int[] freq = new int[26];
        for (char t : tasks) freq[t - 'A']++;
        Arrays.sort(freq);
        int maxCount = freq[25];
        int idleSlots = (maxCount - 1) * n;
        for (int i = 24; i >= 0; i--) idleSlots -= Math.min(freq[i], maxCount - 1);
        return tasks.length + Math.max(0, idleSlots);
    }

    public static void main(String[] args) {
        System.out.println(findKthLargest(new int[]{3,2,1,5,6,4}, 2));  // 5
        System.out.println(Arrays.deepToString(kClosest(new int[][]{{1,3},{-2,2}}, 1))); // [[-2,2]]
        System.out.println(Arrays.toString(mergeKSortedArrays(new int[][]{{1,4,7},{2,5,8},{3,6,9}})));
        MedianFinder mf = new MedianFinder();
        mf.addNum(1); mf.addNum(2); System.out.println(mf.findMedian()); // 1.5
        mf.addNum(3); System.out.println(mf.findMedian()); // 2.0
    }
}


package algorithms.trees;

import java.util.*;

/**
 * Heap / PriorityQueue Patterns
 */
public class HeapPatterns {

    // 1. Kth largest element
    public static int findKthLargest(int[] nums, int k) {
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        for (int num : nums) {
            minHeap.offer(num);
            if (minHeap.size() > k) minHeap.poll();
        }
        return minHeap.peek();
    }

    // 2. K closest points to origin
    public static int[][] kClosest(int[][] points, int k) {
        PriorityQueue<int[]> maxHeap = new PriorityQueue<>(
            (a, b) -> (b[0]*b[0]+b[1]*b[1]) - (a[0]*a[0]+a[1]*a[1]));
        for (int[] p : points) {
            maxHeap.offer(p);
            if (maxHeap.size() > k) maxHeap.poll();
        }
        return maxHeap.toArray(new int[0][]);
    }

    // 3. Merge K sorted lists
    public static int[] mergeKSortedArrays(int[][] arrays) {
        PriorityQueue<int[]> heap = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        int totalLen = 0;
        for (int i = 0; i < arrays.length; i++) {
            if (arrays[i].length > 0) { heap.offer(new int[]{arrays[i][0], i, 0}); totalLen += arrays[i].length; }
        }
        int[] result = new int[totalLen];
        int idx = 0;
        while (!heap.isEmpty()) {
            int[] curr = heap.poll();
            result[idx++] = curr[0];
            int arrIdx = curr[1], elemIdx = curr[2];
            if (elemIdx + 1 < arrays[arrIdx].length)
                heap.offer(new int[]{arrays[arrIdx][elemIdx+1], arrIdx, elemIdx+1});
        }
        return result;
    }

    // 4. Find median from data stream
    static class MedianFinder {
        private PriorityQueue<Integer> lo = new PriorityQueue<>(Collections.reverseOrder()); // max heap
        private PriorityQueue<Integer> hi = new PriorityQueue<>(); // min heap

        public void addNum(int num) {
            lo.offer(num);
            hi.offer(lo.poll());
            if (lo.size() < hi.size()) lo.offer(hi.poll());
        }

        public double findMedian() {
            return lo.size() > hi.size() ? lo.peek() : (lo.peek() + hi.peek()) / 2.0;
        }
    }

    // 5. Task Scheduler
    public static int leastInterval(char[] tasks, int n) {
        int[] freq = new int[26];
        for (char t : tasks) freq[t - 'A']++;
        Arrays.sort(freq);
        int maxCount = freq[25];
        int idleSlots = (maxCount - 1) * n;
        for (int i = 24; i >= 0; i--) idleSlots -= Math.min(freq[i], maxCount - 1);
        return tasks.length + Math.max(0, idleSlots);
    }

    public static void main(String[] args) {
        System.out.println(findKthLargest(new int[]{3,2,1,5,6,4}, 2));  // 5
        System.out.println(Arrays.deepToString(kClosest(new int[][]{{1,3},{-2,2}}, 1))); // [[-2,2]]
        System.out.println(Arrays.toString(mergeKSortedArrays(new int[][]{{1,4,7},{2,5,8},{3,6,9}})));
        MedianFinder mf = new MedianFinder();
        mf.addNum(1); mf.addNum(2); System.out.println(mf.findMedian()); // 1.5
        mf.addNum(3); System.out.println(mf.findMedian()); // 2.0
    }
}
