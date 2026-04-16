package algorithms.dynamic_programming;

import java.util.*;

/**
 * Coin Change, DP Classics and LRU Cache — Java
 */
public class CoinChangeLRU {

    // ---- Coin Change — min coins ----
    public static int coinChange(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1);
        dp[0] = 0;
        for (int coin : coins)
            for (int a = coin; a <= amount; a++)
                dp[a] = Math.min(dp[a], dp[a - coin] + 1);
        return dp[amount] > amount ? -1 : dp[amount];
    }

    // ---- Coin Change II — count ways ----
    public static int coinChangeWays(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        dp[0] = 1;
        for (int coin : coins)
            for (int a = coin; a <= amount; a++)
                dp[a] += dp[a - coin];
        return dp[amount];
    }

    // ---- Word Break ----
    public static boolean wordBreak(String s, List<String> wordDict) {
        Set<String> dict = new HashSet<>(wordDict);
        boolean[] dp = new boolean[s.length() + 1];
        dp[0] = true;
        for (int i = 1; i <= s.length(); i++)
            for (int j = 0; j < i; j++)
                if (dp[j] && dict.contains(s.substring(j, i))) { dp[i] = true; break; }
        return dp[s.length()];
    }

    // ---- LRU Cache — O(1) get/put ----
    static class LRUCache {
        private final int cap;
        private final Map<Integer, int[]> map; // key → [val, prev, next] simulated via Java LinkedHashMap
        private final LinkedHashMap<Integer, Integer> lhm;

        LRUCache(int capacity) {
            this.cap = capacity;
            this.lhm = new LinkedHashMap<>(capacity, 0.75f, true) {
                protected boolean removeEldestEntry(Map.Entry<Integer,Integer> e) { return size() > cap; }
            };
            this.map = null;
        }

        public int get(int key) { return lhm.getOrDefault(key, -1); }
        public void put(int key, int val) { lhm.put(key, val); }
    }

    // ---- Fibonacci (multiple approaches) ----
    public static long fibDP(int n) {
        if (n <= 1) return n;
        long a = 0, b = 1;
        for (int i = 2; i <= n; i++) { long c = a + b; a = b; b = c; }
        return b;
    }

    public static long fibMatrix(int n) {
        // O(log n) via matrix exponentiation
        if (n <= 1) return n;
        long[][] result = {{1,0},{0,1}}, m = {{1,1},{1,0}};
        n--;
        while (n > 0) {
            if ((n & 1) == 1) result = multiply(result, m);
            m = multiply(m, m);
            n >>= 1;
        }
        return result[0][0];
    }
    private static long[][] multiply(long[][] a, long[][] b) {
        return new long[][]{
            {a[0][0]*b[0][0]+a[0][1]*b[1][0], a[0][0]*b[0][1]+a[0][1]*b[1][1]},
            {a[1][0]*b[0][0]+a[1][1]*b[1][0], a[1][0]*b[0][1]+a[1][1]*b[1][1]}
        };
    }

    public static void main(String[] args) {
        System.out.println(coinChange(new int[]{1,5,10,25}, 36)); // 3 (25+10+1)
        System.out.println(coinChange(new int[]{2}, 3));           // -1
        System.out.println(coinChangeWays(new int[]{1,2,5}, 5));   // 4
        System.out.println(wordBreak("leetcode", List.of("leet","code"))); // true
        System.out.println(fibDP(10));     // 55
        System.out.println(fibMatrix(50)); // 12586269025

        LRUCache cache = new LRUCache(2);
        cache.put(1,1); cache.put(2,2);
        System.out.println(cache.get(1)); // 1
        cache.put(3,3); // evicts 2
        System.out.println(cache.get(2)); // -1
    }
}


package algorithms.dynamic_programming;

import java.util.*;

/**
 * Coin Change, DP Classics and LRU Cache — Java
 */
public class CoinChangeLRU {

    // ---- Coin Change — min coins ----
    public static int coinChange(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1);
        dp[0] = 0;
        for (int coin : coins)
            for (int a = coin; a <= amount; a++)
                dp[a] = Math.min(dp[a], dp[a - coin] + 1);
        return dp[amount] > amount ? -1 : dp[amount];
    }

    // ---- Coin Change II — count ways ----
    public static int coinChangeWays(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        dp[0] = 1;
        for (int coin : coins)
            for (int a = coin; a <= amount; a++)
                dp[a] += dp[a - coin];
        return dp[amount];
    }

    // ---- Word Break ----
    public static boolean wordBreak(String s, List<String> wordDict) {
        Set<String> dict = new HashSet<>(wordDict);
        boolean[] dp = new boolean[s.length() + 1];
        dp[0] = true;
        for (int i = 1; i <= s.length(); i++)
            for (int j = 0; j < i; j++)
                if (dp[j] && dict.contains(s.substring(j, i))) { dp[i] = true; break; }
        return dp[s.length()];
    }

    // ---- LRU Cache — O(1) get/put ----
    static class LRUCache {
        private final int cap;
        private final Map<Integer, int[]> map; // key → [val, prev, next] simulated via Java LinkedHashMap
        private final LinkedHashMap<Integer, Integer> lhm;

        LRUCache(int capacity) {
            this.cap = capacity;
            this.lhm = new LinkedHashMap<>(capacity, 0.75f, true) {
                protected boolean removeEldestEntry(Map.Entry<Integer,Integer> e) { return size() > cap; }
            };
            this.map = null;
        }

        public int get(int key) { return lhm.getOrDefault(key, -1); }
        public void put(int key, int val) { lhm.put(key, val); }
    }

    // ---- Fibonacci (multiple approaches) ----
    public static long fibDP(int n) {
        if (n <= 1) return n;
        long a = 0, b = 1;
        for (int i = 2; i <= n; i++) { long c = a + b; a = b; b = c; }
        return b;
    }

    public static long fibMatrix(int n) {
        // O(log n) via matrix exponentiation
        if (n <= 1) return n;
        long[][] result = {{1,0},{0,1}}, m = {{1,1},{1,0}};
        n--;
        while (n > 0) {
            if ((n & 1) == 1) result = multiply(result, m);
            m = multiply(m, m);
            n >>= 1;
        }
        return result[0][0];
    }
    private static long[][] multiply(long[][] a, long[][] b) {
        return new long[][]{
            {a[0][0]*b[0][0]+a[0][1]*b[1][0], a[0][0]*b[0][1]+a[0][1]*b[1][1]},
            {a[1][0]*b[0][0]+a[1][1]*b[1][0], a[1][0]*b[0][1]+a[1][1]*b[1][1]}
        };
    }

    public static void main(String[] args) {
        System.out.println(coinChange(new int[]{1,5,10,25}, 36)); // 3 (25+10+1)
        System.out.println(coinChange(new int[]{2}, 3));           // -1
        System.out.println(coinChangeWays(new int[]{1,2,5}, 5));   // 4
        System.out.println(wordBreak("leetcode", List.of("leet","code"))); // true
        System.out.println(fibDP(10));     // 55
        System.out.println(fibMatrix(50)); // 12586269025

        LRUCache cache = new LRUCache(2);
        cache.put(1,1); cache.put(2,2);
        System.out.println(cache.get(1)); // 1
        cache.put(3,3); // evicts 2
        System.out.println(cache.get(2)); // -1
    }
}
