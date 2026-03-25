package snippets.java.cache;

import java.util.*;

/**
 * LRU Cache Implementations in Java
 */
public class LRUCache {

    // ---- Implementation 1: LinkedHashMap (simplest) ----
    static class LRULinkedHashMap<K, V> extends LinkedHashMap<K, V> {
        private final int capacity;
        LRULinkedHashMap(int capacity) { super(capacity, 0.75f, true); this.capacity = capacity; }
        @Override protected boolean removeEldestEntry(Map.Entry<K,V> eldest) { return size() > capacity; }
    }

    // ---- Implementation 2: Doubly Linked List + HashMap ----
    static class LRUCacheCustom {
        private static class Node {
            int key, val; Node prev, next;
            Node(int k, int v) { key=k; val=v; }
        }
        private final int cap;
        private final Map<Integer, Node> map;
        private final Node head, tail; // sentinels

        LRUCacheCustom(int capacity) {
            cap = capacity;
            map = new HashMap<>();
            head = new Node(0,0); tail = new Node(0,0);
            head.next = tail; tail.prev = head;
        }

        public int get(int key) {
            if (!map.containsKey(key)) return -1;
            Node node = map.get(key);
            remove(node); insertFront(node); // move to front
            return node.val;
        }

        public void put(int key, int value) {
            if (map.containsKey(key)) remove(map.get(key));
            if (map.size() == cap) remove(tail.prev); // evict LRU
            insertFront(new Node(key, value));
        }

        private void remove(Node n) {
            map.remove(n.key);
            n.prev.next = n.next; n.next.prev = n.prev;
        }

        private void insertFront(Node n) {
            map.put(n.key, n);
            n.next = head.next; n.prev = head;
            head.next.prev = n; head.next = n;
        }
    }

    // ---- LFU Cache (Least Frequently Used) ----
    static class LFUCache {
        private final int cap;
        private int minFreq;
        private final Map<Integer, Integer> keyVal, keyFreq;
        private final Map<Integer, LinkedHashSet<Integer>> freqKeys;

        LFUCache(int capacity) {
            cap = capacity; minFreq = 0;
            keyVal = new HashMap<>(); keyFreq = new HashMap<>(); freqKeys = new HashMap<>();
        }

        public int get(int key) {
            if (!keyVal.containsKey(key)) return -1;
            updateFreq(key); return keyVal.get(key);
        }

        public void put(int key, int value) {
            if (cap == 0) return;
            if (keyVal.containsKey(key)) { keyVal.put(key, value); updateFreq(key); return; }
            if (keyVal.size() == cap) {
                LinkedHashSet<Integer> minSet = freqKeys.get(minFreq);
                int evict = minSet.iterator().next(); minSet.remove(evict);
                keyVal.remove(evict); keyFreq.remove(evict);
            }
            keyVal.put(key, value); keyFreq.put(key, 1);
            freqKeys.computeIfAbsent(1, k -> new LinkedHashSet<>()).add(key);
            minFreq = 1;
        }

        private void updateFreq(int key) {
            int f = keyFreq.get(key);
            keyFreq.put(key, f+1);
            freqKeys.get(f).remove(key);
            if (freqKeys.get(f).isEmpty() && f == minFreq) minFreq++;
            freqKeys.computeIfAbsent(f+1, k -> new LinkedHashSet<>()).add(key);
        }
    }

    public static void main(String[] args) {
        // LinkedHashMap LRU
        LRULinkedHashMap<Integer, String> lhm = new LRULinkedHashMap<>(3);
        lhm.put(1,"a"); lhm.put(2,"b"); lhm.put(3,"c");
        lhm.get(1);   // access 1 → now MRU
        lhm.put(4,"d"); // 2 should be evicted
        System.out.println(lhm.containsKey(2)); // false

        // Custom LRU
        LRUCacheCustom cache = new LRUCacheCustom(2);
        cache.put(1,1); cache.put(2,2);
        System.out.println(cache.get(1));  // 1
        cache.put(3,3); // 2 evicted
        System.out.println(cache.get(2));  // -1
    }
}
