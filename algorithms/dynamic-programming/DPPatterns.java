package algorithms.dynamic_programming;

import java.util.*;

/**
 * Dynamic Programming Patterns — Java
 */
public class DPPatterns {

    // 1. Longest Palindromic Substring
    public static String longestPalindrome(String s) {
        String result = "";
        for (int i = 0; i < s.length(); i++) {
            for (String sub : new String[]{expand(s, i, i), expand(s, i, i+1)}) {
                if (sub.length() > result.length()) result = sub;
            }
        }
        return result;
    }
    private static String expand(String s, int l, int r) {
        while (l >= 0 && r < s.length() && s.charAt(l) == s.charAt(r)) { l--; r++; }
        return s.substring(l+1, r);
    }

    // 2. Decode Ways
    public static int numDecodings(String s) {
        if (s.isEmpty() || s.charAt(0) == '0') return 0;
        int n = s.length();
        int[] dp = new int[n+1];
        dp[0] = 1; dp[1] = 1;
        for (int i = 2; i <= n; i++) {
            int one = Integer.parseInt(s.substring(i-1, i));
            int two = Integer.parseInt(s.substring(i-2, i));
            if (one >= 1) dp[i] += dp[i-1];
            if (two >= 10 && two <= 26) dp[i] += dp[i-2];
        }
        return dp[n];
    }

    // 3. Maximum Product Subarray
    public static int maxProduct(int[] nums) {
        int maxProd = nums[0], minProd = nums[0], result = nums[0];
        for (int i = 1; i < nums.length; i++) {
            int tmp = maxProd;
            maxProd = Math.max(nums[i], Math.max(maxProd * nums[i], minProd * nums[i]));
            minProd = Math.min(nums[i], Math.min(tmp * nums[i], minProd * nums[i]));
            result = Math.max(result, maxProd);
        }
        return result;
    }

    // 4. Jump Game II
    public static int jump(int[] nums) {
        int jumps = 0, currEnd = 0, farthest = 0;
        for (int i = 0; i < nums.length - 1; i++) {
            farthest = Math.max(farthest, i + nums[i]);
            if (i == currEnd) { jumps++; currEnd = farthest; }
        }
        return jumps;
    }

    // 5. Matrix Chain Multiplication (Interval DP)
    public static int matrixChain(int[] dims) {
        int n = dims.length - 1;
        int[][] dp = new int[n][n];
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;
                dp[i][j] = Integer.MAX_VALUE;
                for (int k = i; k < j; k++)
                    dp[i][j] = Math.min(dp[i][j], dp[i][k] + dp[k+1][j] + dims[i]*dims[k+1]*dims[j+1]);
            }
        }
        return dp[0][n-1];
    }

    // 6. Burst Balloons
    public static int maxCoins(int[] nums) {
        int n = nums.length;
        int[] arr = new int[n+2];
        arr[0] = arr[n+1] = 1;
        for (int i = 0; i < n; i++) arr[i+1] = nums[i];
        int[][] dp = new int[n+2][n+2];
        for (int len = 1; len <= n; len++) {
            for (int left = 1; left <= n - len + 1; left++) {
                int right = left + len - 1;
                for (int k = left; k <= right; k++) {
                    dp[left][right] = Math.max(dp[left][right],
                        dp[left][k-1] + arr[left-1]*arr[k]*arr[right+1] + dp[k+1][right]);
                }
            }
        }
        return dp[1][n];
    }

    public static void main(String[] args) {
        System.out.println(longestPalindrome("babad")); // bab
        System.out.println(numDecodings("226"));          // 3
        System.out.println(maxProduct(new int[]{2,3,-2,4})); // 6
        System.out.println(jump(new int[]{2,3,1,1,4}));  // 2
        System.out.println(maxCoins(new int[]{3,1,5,8})); // 167
    }
}


package algorithms.dynamic_programming;

import java.util.*;

/**
 * Dynamic Programming Patterns — Java
 */
public class DPPatterns {

    // 1. Longest Palindromic Substring
    public static String longestPalindrome(String s) {
        String result = "";
        for (int i = 0; i < s.length(); i++) {
            for (String sub : new String[]{expand(s, i, i), expand(s, i, i+1)}) {
                if (sub.length() > result.length()) result = sub;
            }
        }
        return result;
    }
    private static String expand(String s, int l, int r) {
        while (l >= 0 && r < s.length() && s.charAt(l) == s.charAt(r)) { l--; r++; }
        return s.substring(l+1, r);
    }

    // 2. Decode Ways
    public static int numDecodings(String s) {
        if (s.isEmpty() || s.charAt(0) == '0') return 0;
        int n = s.length();
        int[] dp = new int[n+1];
        dp[0] = 1; dp[1] = 1;
        for (int i = 2; i <= n; i++) {
            int one = Integer.parseInt(s.substring(i-1, i));
            int two = Integer.parseInt(s.substring(i-2, i));
            if (one >= 1) dp[i] += dp[i-1];
            if (two >= 10 && two <= 26) dp[i] += dp[i-2];
        }
        return dp[n];
    }

    // 3. Maximum Product Subarray
    public static int maxProduct(int[] nums) {
        int maxProd = nums[0], minProd = nums[0], result = nums[0];
        for (int i = 1; i < nums.length; i++) {
            int tmp = maxProd;
            maxProd = Math.max(nums[i], Math.max(maxProd * nums[i], minProd * nums[i]));
            minProd = Math.min(nums[i], Math.min(tmp * nums[i], minProd * nums[i]));
            result = Math.max(result, maxProd);
        }
        return result;
    }

    // 4. Jump Game II
    public static int jump(int[] nums) {
        int jumps = 0, currEnd = 0, farthest = 0;
        for (int i = 0; i < nums.length - 1; i++) {
            farthest = Math.max(farthest, i + nums[i]);
            if (i == currEnd) { jumps++; currEnd = farthest; }
        }
        return jumps;
    }

    // 5. Matrix Chain Multiplication (Interval DP)
    public static int matrixChain(int[] dims) {
        int n = dims.length - 1;
        int[][] dp = new int[n][n];
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;
                dp[i][j] = Integer.MAX_VALUE;
                for (int k = i; k < j; k++)
                    dp[i][j] = Math.min(dp[i][j], dp[i][k] + dp[k+1][j] + dims[i]*dims[k+1]*dims[j+1]);
            }
        }
        return dp[0][n-1];
    }

    // 6. Burst Balloons
    public static int maxCoins(int[] nums) {
        int n = nums.length;
        int[] arr = new int[n+2];
        arr[0] = arr[n+1] = 1;
        for (int i = 0; i < n; i++) arr[i+1] = nums[i];
        int[][] dp = new int[n+2][n+2];
        for (int len = 1; len <= n; len++) {
            for (int left = 1; left <= n - len + 1; left++) {
                int right = left + len - 1;
                for (int k = left; k <= right; k++) {
                    dp[left][right] = Math.max(dp[left][right],
                        dp[left][k-1] + arr[left-1]*arr[k]*arr[right+1] + dp[k+1][right]);
                }
            }
        }
        return dp[1][n];
    }

    public static void main(String[] args) {
        System.out.println(longestPalindrome("babad")); // bab
        System.out.println(numDecodings("226"));          // 3
        System.out.println(maxProduct(new int[]{2,3,-2,4})); // 6
        System.out.println(jump(new int[]{2,3,1,1,4}));  // 2
        System.out.println(maxCoins(new int[]{3,1,5,8})); // 167
    }
}
