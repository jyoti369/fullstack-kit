package algorithms.dynamic_programming;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Dynamic Programming — Core Patterns in Java
 * Memoization (top-down) vs Tabulation (bottom-up)
 */
public class DPFundamentals {

    // 1. Fibonacci — memoization
    private static Map<Integer, Long> memo = new HashMap<>();
    public static long fib(int n) {
        if (n <= 1) return n;
        if (memo.containsKey(n)) return memo.get(n);
        long result = fib(n-1) + fib(n-2);
        memo.put(n, result);
        return result;
    }

    // 2. Climbing Stairs
    public static int climbStairs(int n) {
        if (n <= 2) return n;
        int a = 1, b = 2;
        for (int i = 3; i <= n; i++) { int c = a + b; a = b; b = c; }
        return b;
    }

    // 3. House Robber
    public static int rob(int[] nums) {
        if (nums.length == 0) return 0;
        if (nums.length == 1) return nums[0];
        int prev2 = nums[0], prev1 = Math.max(nums[0], nums[1]);
        for (int i = 2; i < nums.length; i++) {
            int curr = Math.max(prev1, prev2 + nums[i]);
            prev2 = prev1; prev1 = curr;
        }
        return prev1;
    }

    // 4. Longest Common Subsequence
    public static int lcs(String s1, String s2) {
        int m = s1.length(), n = s2.length();
        int[][] dp = new int[m+1][n+1];
        for (int i = 1; i <= m; i++)
            for (int j = 1; j <= n; j++)
                dp[i][j] = s1.charAt(i-1) == s2.charAt(j-1)
                    ? dp[i-1][j-1] + 1
                    : Math.max(dp[i-1][j], dp[i][j-1]);
        return dp[m][n];
    }

    // 5. 0/1 Knapsack
    public static int knapsack(int[] weights, int[] values, int capacity) {
        int n = weights.length;
        int[] dp = new int[capacity + 1];
        for (int i = 0; i < n; i++)
            for (int w = capacity; w >= weights[i]; w--)
                dp[w] = Math.max(dp[w], dp[w - weights[i]] + values[i]);
        return dp[capacity];
    }

    // 6. Coin Change
    public static int coinChange(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1);
        dp[0] = 0;
        for (int coin : coins)
            for (int a = coin; a <= amount; a++)
                dp[a] = Math.min(dp[a], dp[a - coin] + 1);
        return dp[amount] > amount ? -1 : dp[amount];
    }

    // 7. Unique Paths
    public static int uniquePaths(int m, int n) {
        int[] dp = new int[n];
        Arrays.fill(dp, 1);
        for (int i = 1; i < m; i++)
            for (int j = 1; j < n; j++)
                dp[j] += dp[j-1];
        return dp[n-1];
    }

    // 8. Word Break
    public static boolean wordBreak(String s, java.util.List<String> wordDict) {
        java.util.Set<String> dict = new java.util.HashSet<>(wordDict);
        boolean[] dp = new boolean[s.length() + 1];
        dp[0] = true;
        for (int i = 1; i <= s.length(); i++)
            for (int j = 0; j < i; j++)
                if (dp[j] && dict.contains(s.substring(j, i))) { dp[i] = true; break; }
        return dp[s.length()];
    }

    public static void main(String[] args) {
        System.out.println(fib(10));                          // 55
        System.out.println(climbStairs(5));                   // 8
        System.out.println(rob(new int[]{2,7,9,3,1}));       // 12
        System.out.println(lcs("ABCBDAB", "BDCAB"));         // 4
        System.out.println(coinChange(new int[]{1,5,11}, 15)); // 3
        System.out.println(uniquePaths(3, 7));                // 28
    }
}


package algorithms.dynamic_programming;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Dynamic Programming — Core Patterns in Java
 * Memoization (top-down) vs Tabulation (bottom-up)
 */
public class DPFundamentals {

    // 1. Fibonacci — memoization
    private static Map<Integer, Long> memo = new HashMap<>();
    public static long fib(int n) {
        if (n <= 1) return n;
        if (memo.containsKey(n)) return memo.get(n);
        long result = fib(n-1) + fib(n-2);
        memo.put(n, result);
        return result;
    }

    // 2. Climbing Stairs
    public static int climbStairs(int n) {
        if (n <= 2) return n;
        int a = 1, b = 2;
        for (int i = 3; i <= n; i++) { int c = a + b; a = b; b = c; }
        return b;
    }

    // 3. House Robber
    public static int rob(int[] nums) {
        if (nums.length == 0) return 0;
        if (nums.length == 1) return nums[0];
        int prev2 = nums[0], prev1 = Math.max(nums[0], nums[1]);
        for (int i = 2; i < nums.length; i++) {
            int curr = Math.max(prev1, prev2 + nums[i]);
            prev2 = prev1; prev1 = curr;
        }
        return prev1;
    }

    // 4. Longest Common Subsequence
    public static int lcs(String s1, String s2) {
        int m = s1.length(), n = s2.length();
        int[][] dp = new int[m+1][n+1];
        for (int i = 1; i <= m; i++)
            for (int j = 1; j <= n; j++)
                dp[i][j] = s1.charAt(i-1) == s2.charAt(j-1)
                    ? dp[i-1][j-1] + 1
                    : Math.max(dp[i-1][j], dp[i][j-1]);
        return dp[m][n];
    }

    // 5. 0/1 Knapsack
    public static int knapsack(int[] weights, int[] values, int capacity) {
        int n = weights.length;
        int[] dp = new int[capacity + 1];
        for (int i = 0; i < n; i++)
            for (int w = capacity; w >= weights[i]; w--)
                dp[w] = Math.max(dp[w], dp[w - weights[i]] + values[i]);
        return dp[capacity];
    }

    // 6. Coin Change
    public static int coinChange(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1);
        dp[0] = 0;
        for (int coin : coins)
            for (int a = coin; a <= amount; a++)
                dp[a] = Math.min(dp[a], dp[a - coin] + 1);
        return dp[amount] > amount ? -1 : dp[amount];
    }

    // 7. Unique Paths
    public static int uniquePaths(int m, int n) {
        int[] dp = new int[n];
        Arrays.fill(dp, 1);
        for (int i = 1; i < m; i++)
            for (int j = 1; j < n; j++)
                dp[j] += dp[j-1];
        return dp[n-1];
    }

    // 8. Word Break
    public static boolean wordBreak(String s, java.util.List<String> wordDict) {
        java.util.Set<String> dict = new java.util.HashSet<>(wordDict);
        boolean[] dp = new boolean[s.length() + 1];
        dp[0] = true;
        for (int i = 1; i <= s.length(); i++)
            for (int j = 0; j < i; j++)
                if (dp[j] && dict.contains(s.substring(j, i))) { dp[i] = true; break; }
        return dp[s.length()];
    }

    public static void main(String[] args) {
        System.out.println(fib(10));                          // 55
        System.out.println(climbStairs(5));                   // 8
        System.out.println(rob(new int[]{2,7,9,3,1}));       // 12
        System.out.println(lcs("ABCBDAB", "BDCAB"));         // 4
        System.out.println(coinChange(new int[]{1,5,11}, 15)); // 3
        System.out.println(uniquePaths(3, 7));                // 28
    }
}


package algorithms.dynamic_programming;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Dynamic Programming — Core Patterns in Java
 * Memoization (top-down) vs Tabulation (bottom-up)
 */
public class DPFundamentals {

    // 1. Fibonacci — memoization
    private static Map<Integer, Long> memo = new HashMap<>();
    public static long fib(int n) {
        if (n <= 1) return n;
        if (memo.containsKey(n)) return memo.get(n);
        long result = fib(n-1) + fib(n-2);
        memo.put(n, result);
        return result;
    }

    // 2. Climbing Stairs
    public static int climbStairs(int n) {
        if (n <= 2) return n;
        int a = 1, b = 2;
        for (int i = 3; i <= n; i++) { int c = a + b; a = b; b = c; }
        return b;
    }

    // 3. House Robber
    public static int rob(int[] nums) {
        if (nums.length == 0) return 0;
        if (nums.length == 1) return nums[0];
        int prev2 = nums[0], prev1 = Math.max(nums[0], nums[1]);
        for (int i = 2; i < nums.length; i++) {
            int curr = Math.max(prev1, prev2 + nums[i]);
            prev2 = prev1; prev1 = curr;
        }
        return prev1;
    }

    // 4. Longest Common Subsequence
    public static int lcs(String s1, String s2) {
        int m = s1.length(), n = s2.length();
        int[][] dp = new int[m+1][n+1];
        for (int i = 1; i <= m; i++)
            for (int j = 1; j <= n; j++)
                dp[i][j] = s1.charAt(i-1) == s2.charAt(j-1)
                    ? dp[i-1][j-1] + 1
                    : Math.max(dp[i-1][j], dp[i][j-1]);
        return dp[m][n];
    }

    // 5. 0/1 Knapsack
    public static int knapsack(int[] weights, int[] values, int capacity) {
        int n = weights.length;
        int[] dp = new int[capacity + 1];
        for (int i = 0; i < n; i++)
            for (int w = capacity; w >= weights[i]; w--)
                dp[w] = Math.max(dp[w], dp[w - weights[i]] + values[i]);
        return dp[capacity];
    }

    // 6. Coin Change
    public static int coinChange(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1);
        dp[0] = 0;
        for (int coin : coins)
            for (int a = coin; a <= amount; a++)
                dp[a] = Math.min(dp[a], dp[a - coin] + 1);
        return dp[amount] > amount ? -1 : dp[amount];
    }

    // 7. Unique Paths
    public static int uniquePaths(int m, int n) {
        int[] dp = new int[n];
        Arrays.fill(dp, 1);
        for (int i = 1; i < m; i++)
            for (int j = 1; j < n; j++)
                dp[j] += dp[j-1];
        return dp[n-1];
    }

    // 8. Word Break
    public static boolean wordBreak(String s, java.util.List<String> wordDict) {
        java.util.Set<String> dict = new java.util.HashSet<>(wordDict);
        boolean[] dp = new boolean[s.length() + 1];
        dp[0] = true;
        for (int i = 1; i <= s.length(); i++)
            for (int j = 0; j < i; j++)
                if (dp[j] && dict.contains(s.substring(j, i))) { dp[i] = true; break; }
        return dp[s.length()];
    }

    public static void main(String[] args) {
        System.out.println(fib(10));                          // 55
        System.out.println(climbStairs(5));                   // 8
        System.out.println(rob(new int[]{2,7,9,3,1}));       // 12
        System.out.println(lcs("ABCBDAB", "BDCAB"));         // 4
        System.out.println(coinChange(new int[]{1,5,11}, 15)); // 3
        System.out.println(uniquePaths(3, 7));                // 28
    }
}


package algorithms.dynamic_programming;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Dynamic Programming — Core Patterns in Java
 * Memoization (top-down) vs Tabulation (bottom-up)
 */
public class DPFundamentals {

    // 1. Fibonacci — memoization
    private static Map<Integer, Long> memo = new HashMap<>();
    public static long fib(int n) {
        if (n <= 1) return n;
        if (memo.containsKey(n)) return memo.get(n);
        long result = fib(n-1) + fib(n-2);
        memo.put(n, result);
        return result;
    }

    // 2. Climbing Stairs
    public static int climbStairs(int n) {
        if (n <= 2) return n;
        int a = 1, b = 2;
        for (int i = 3; i <= n; i++) { int c = a + b; a = b; b = c; }
        return b;
    }

    // 3. House Robber
    public static int rob(int[] nums) {
        if (nums.length == 0) return 0;
        if (nums.length == 1) return nums[0];
        int prev2 = nums[0], prev1 = Math.max(nums[0], nums[1]);
        for (int i = 2; i < nums.length; i++) {
            int curr = Math.max(prev1, prev2 + nums[i]);
            prev2 = prev1; prev1 = curr;
        }
        return prev1;
    }

    // 4. Longest Common Subsequence
    public static int lcs(String s1, String s2) {
        int m = s1.length(), n = s2.length();
        int[][] dp = new int[m+1][n+1];
        for (int i = 1; i <= m; i++)
            for (int j = 1; j <= n; j++)
                dp[i][j] = s1.charAt(i-1) == s2.charAt(j-1)
                    ? dp[i-1][j-1] + 1
                    : Math.max(dp[i-1][j], dp[i][j-1]);
        return dp[m][n];
    }

    // 5. 0/1 Knapsack
    public static int knapsack(int[] weights, int[] values, int capacity) {
        int n = weights.length;
        int[] dp = new int[capacity + 1];
        for (int i = 0; i < n; i++)
            for (int w = capacity; w >= weights[i]; w--)
                dp[w] = Math.max(dp[w], dp[w - weights[i]] + values[i]);
        return dp[capacity];
    }

    // 6. Coin Change
    public static int coinChange(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1);
        dp[0] = 0;
        for (int coin : coins)
            for (int a = coin; a <= amount; a++)
                dp[a] = Math.min(dp[a], dp[a - coin] + 1);
        return dp[amount] > amount ? -1 : dp[amount];
    }

    // 7. Unique Paths
    public static int uniquePaths(int m, int n) {
        int[] dp = new int[n];
        Arrays.fill(dp, 1);
        for (int i = 1; i < m; i++)
            for (int j = 1; j < n; j++)
                dp[j] += dp[j-1];
        return dp[n-1];
    }

    // 8. Word Break
    public static boolean wordBreak(String s, java.util.List<String> wordDict) {
        java.util.Set<String> dict = new java.util.HashSet<>(wordDict);
        boolean[] dp = new boolean[s.length() + 1];
        dp[0] = true;
        for (int i = 1; i <= s.length(); i++)
            for (int j = 0; j < i; j++)
                if (dp[j] && dict.contains(s.substring(j, i))) { dp[i] = true; break; }
        return dp[s.length()];
    }

    public static void main(String[] args) {
        System.out.println(fib(10));                          // 55
        System.out.println(climbStairs(5));                   // 8
        System.out.println(rob(new int[]{2,7,9,3,1}));       // 12
        System.out.println(lcs("ABCBDAB", "BDCAB"));         // 4
        System.out.println(coinChange(new int[]{1,5,11}, 15)); // 3
        System.out.println(uniquePaths(3, 7));                // 28
    }
}
