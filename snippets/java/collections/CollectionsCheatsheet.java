package snippets.java.collections;

import java.util.*;

/**
 * Java Collections Framework — Common Operations
 */
public class CollectionsCheatsheet {

    public static void main(String[] args) {

        // ---- ArrayList vs LinkedList ----
        // ArrayList: O(1) get, O(n) insert/delete at middle
        // LinkedList: O(n) get, O(1) insert/delete at ends
        List<String> list = new ArrayList<>(List.of("a", "b", "c"));
        list.add("d");          // O(1) amortized
        list.remove(0);         // O(n) shift
        list.sort(Comparator.reverseOrder());

        // ---- HashMap ----
        // O(1) avg get/put, O(n) worst (hash collision)
        Map<String, Integer> freq = new HashMap<>();
        String[] words = {"the", "cat", "sat", "on", "the", "mat"};
        for (String w : words) freq.merge(w, 1, Integer::sum);
        freq.forEach((k, v) -> System.out.printf("%s: %d%n", k, v));
        int count = freq.getOrDefault("the", 0);  // 2
        freq.computeIfAbsent("dog", k -> 0);

        // ---- TreeMap (sorted by key) ----
        TreeMap<Integer, String> treeMap = new TreeMap<>();
        treeMap.put(3, "c"); treeMap.put(1, "a"); treeMap.put(2, "b");
        System.out.println(treeMap.firstKey());             // 1
        System.out.println(treeMap.floorKey(2));            // 2
        System.out.println(treeMap.ceilingKey(2));          // 2
        System.out.println(treeMap.subMap(1, 3));           // {1=a, 2=b}

        // ---- PriorityQueue (MinHeap by default) ----
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        PriorityQueue<int[]> customHeap = new PriorityQueue<>(
            Comparator.comparingInt((int[] a) -> a[0]).thenComparingInt(a -> a[1]));

        // ---- Deque (Stack + Queue) ----
        Deque<Integer> deque = new ArrayDeque<>();
        deque.push(1);    // stack push (addFirst)
        deque.peek();     // stack top (peekFirst)
        deque.pop();      // stack pop (removeFirst)
        deque.offer(2);   // queue add (addLast)
        deque.poll();     // queue remove (removeFirst)

        // ---- LinkedHashMap (insertion-order) ----
        // LRU Cache base structure
        Map<String, Integer> lru = new LinkedHashMap<>(16, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, Integer> eldest) {
                return size() > 3; // keeps last 3 accessed
            }
        };

        // ---- Collections utility ----
        List<Integer> nums = new ArrayList<>(List.of(3,1,4,1,5,9,2,6));
        Collections.sort(nums);                            // [1,1,2,3,4,5,6,9]
        Collections.shuffle(nums);
        int pos = Collections.binarySearch(nums, 5);       // sorted needed
        System.out.println(Collections.frequency(nums, 1)); // 2
        System.out.println(Collections.max(nums));          // 9

        // ---- Arrays utility ----
        int[] arr = {5, 2, 8, 1, 9};
        Arrays.sort(arr);                                  // [1,2,5,8,9]
        System.out.println(Arrays.binarySearch(arr, 5));   // 2
        System.out.println(Arrays.toString(arr));
        int[][] matrix = {{3,1},{2,4}};
        Arrays.sort(matrix, Comparator.comparingInt(r -> r[0])); // sort by first col
    }
}
