package algorithms.dynamic_programming;

import java.util.*;

/**
 * Classic Sequence DP Problems in Java
 */
public class SequenceDP {

    // 1. Longest Increasing Subsequence — O(n log n)
    public static int lengthOfLIS(int[] nums) {
        List<Integer> sub = new ArrayList<>();
        for (int num : nums) {
            int pos = Collections.binarySearch(sub, num);
            if (pos < 0) pos = -(pos + 1);
            if (pos == sub.size()) sub.add(num);
            else sub.set(pos, num);
        }
        return sub.size();
    }

    // 2. LCS — Longest Common Subsequence
    public static int lcs(String s1, String s2) {
        int m = s1.length(), n = s2.length();
        int[] dp = new int[n + 1];
        for (int i = 1; i <= m; i++) {
            int prev = 0;
            for (int j = 1; j <= n; j++) {
                int temp = dp[j];
                dp[j] = s1.charAt(i-1) == s2.charAt(j-1)
                    ? prev + 1 : Math.max(dp[j], dp[j-1]);
                prev = temp;
            }
        }
        return dp[n];
    }

    // 3. Edit Distance
    public static int minDistance(String word1, String word2) {
        int m = word1.length(), n = word2.length();
        int[] dp = new int[n + 1];
        for (int j = 0; j <= n; j++) dp[j] = j;
        for (int i = 1; i <= m; i++) {
            int prev = dp[0]; dp[0] = i;
            for (int j = 1; j <= n; j++) {
                int temp = dp[j];
                dp[j] = word1.charAt(i-1) == word2.charAt(j-1)
                    ? prev
                    : 1 + Math.min(prev, Math.min(dp[j], dp[j-1]));
                prev = temp;
            }
        }
        return dp[n];
    }

    // 4. Distinct Subsequences
    public static int numDistinct(String s, String t) {
        int m = s.length(), n = t.length();
        int[] dp = new int[n + 1];
        dp[0] = 1;
        for (int i = 1; i <= m; i++)
            for (int j = n; j >= 1; j--)
                if (s.charAt(i-1) == t.charAt(j-1)) dp[j] += dp[j-1];
        return dp[n];
    }

    // 5. Russian Doll Envelopes (2D LIS)
    public static int maxEnvelopes(int[][] envelopes) {
        Arrays.sort(envelopes, (a,b) -> a[0]==b[0] ? b[1]-a[1] : a[0]-b[0]);
        int[] heights = Arrays.stream(envelopes).mapToInt(e -> e[1]).toArray();
        return lengthOfLIS(heights);
    }

    // 6. Regular Expression Matching
    public static boolean isMatch(String s, String p) {
        int m = s.length(), n = p.length();
        boolean[][] dp = new boolean[m+1][n+1];
        dp[0][0] = true;
        for (int j = 1; j <= n; j++) if (p.charAt(j-1)=='*') dp[0][j] = dp[0][j-2];
        for (int i = 1; i <= m; i++)
            for (int j = 1; j <= n; j++) {
                if (p.charAt(j-1)=='*') {
                    dp[i][j] = dp[i][j-2] // zero occurrences
                        || (dp[i-1][j] && (p.charAt(j-2)=='.' || p.charAt(j-2)==s.charAt(i-1)));
                } else {
                    dp[i][j] = dp[i-1][j-1]
                        && (p.charAt(j-1)=='.' || p.charAt(j-1)==s.charAt(i-1));
                }
            }
        return dp[m][n];
    }

    public static void main(String[] args) {
        System.out.println(lengthOfLIS(new int[]{10,9,2,5,3,7,101,18})); // 4
        System.out.println(lcs("ABCBDAB", "BDCAB")); // 4
        System.out.println(minDistance("horse", "ros")); // 3
        System.out.println(numDistinct("rabbbit", "rabbit")); // 3
        System.out.println(maxEnvelopes(new int[][]{{5,4},{6,4},{6,7},{2,3}})); // 3
        System.out.println(isMatch("aa", "a*")); // true
    }
}


package algorithms.dynamic_programming;

import java.util.*;

/**
 * Classic Sequence DP Problems in Java
 */
public class SequenceDP {

    // 1. Longest Increasing Subsequence — O(n log n)
    public static int lengthOfLIS(int[] nums) {
        List<Integer> sub = new ArrayList<>();
        for (int num : nums) {
            int pos = Collections.binarySearch(sub, num);
            if (pos < 0) pos = -(pos + 1);
            if (pos == sub.size()) sub.add(num);
            else sub.set(pos, num);
        }
        return sub.size();
    }

    // 2. LCS — Longest Common Subsequence
    public static int lcs(String s1, String s2) {
        int m = s1.length(), n = s2.length();
        int[] dp = new int[n + 1];
        for (int i = 1; i <= m; i++) {
            int prev = 0;
            for (int j = 1; j <= n; j++) {
                int temp = dp[j];
                dp[j] = s1.charAt(i-1) == s2.charAt(j-1)
                    ? prev + 1 : Math.max(dp[j], dp[j-1]);
                prev = temp;
            }
        }
        return dp[n];
    }

    // 3. Edit Distance
    public static int minDistance(String word1, String word2) {
        int m = word1.length(), n = word2.length();
        int[] dp = new int[n + 1];
        for (int j = 0; j <= n; j++) dp[j] = j;
        for (int i = 1; i <= m; i++) {
            int prev = dp[0]; dp[0] = i;
            for (int j = 1; j <= n; j++) {
                int temp = dp[j];
                dp[j] = word1.charAt(i-1) == word2.charAt(j-1)
                    ? prev
                    : 1 + Math.min(prev, Math.min(dp[j], dp[j-1]));
                prev = temp;
            }
        }
        return dp[n];
    }

    // 4. Distinct Subsequences
    public static int numDistinct(String s, String t) {
        int m = s.length(), n = t.length();
        int[] dp = new int[n + 1];
        dp[0] = 1;
        for (int i = 1; i <= m; i++)
            for (int j = n; j >= 1; j--)
                if (s.charAt(i-1) == t.charAt(j-1)) dp[j] += dp[j-1];
        return dp[n];
    }

    // 5. Russian Doll Envelopes (2D LIS)
    public static int maxEnvelopes(int[][] envelopes) {
        Arrays.sort(envelopes, (a,b) -> a[0]==b[0] ? b[1]-a[1] : a[0]-b[0]);
        int[] heights = Arrays.stream(envelopes).mapToInt(e -> e[1]).toArray();
        return lengthOfLIS(heights);
    }

    // 6. Regular Expression Matching
    public static boolean isMatch(String s, String p) {
        int m = s.length(), n = p.length();
        boolean[][] dp = new boolean[m+1][n+1];
        dp[0][0] = true;
        for (int j = 1; j <= n; j++) if (p.charAt(j-1)=='*') dp[0][j] = dp[0][j-2];
        for (int i = 1; i <= m; i++)
            for (int j = 1; j <= n; j++) {
                if (p.charAt(j-1)=='*') {
                    dp[i][j] = dp[i][j-2] // zero occurrences
                        || (dp[i-1][j] && (p.charAt(j-2)=='.' || p.charAt(j-2)==s.charAt(i-1)));
                } else {
                    dp[i][j] = dp[i-1][j-1]
                        && (p.charAt(j-1)=='.' || p.charAt(j-1)==s.charAt(i-1));
                }
            }
        return dp[m][n];
    }

    public static void main(String[] args) {
        System.out.println(lengthOfLIS(new int[]{10,9,2,5,3,7,101,18})); // 4
        System.out.println(lcs("ABCBDAB", "BDCAB")); // 4
        System.out.println(minDistance("horse", "ros")); // 3
        System.out.println(numDistinct("rabbbit", "rabbit")); // 3
        System.out.println(maxEnvelopes(new int[][]{{5,4},{6,4},{6,7},{2,3}})); // 3
        System.out.println(isMatch("aa", "a*")); // true
    }
}


package algorithms.dynamic_programming;

import java.util.*;

/**
 * Classic Sequence DP Problems in Java
 */
public class SequenceDP {

    // 1. Longest Increasing Subsequence — O(n log n)
    public static int lengthOfLIS(int[] nums) {
        List<Integer> sub = new ArrayList<>();
        for (int num : nums) {
            int pos = Collections.binarySearch(sub, num);
            if (pos < 0) pos = -(pos + 1);
            if (pos == sub.size()) sub.add(num);
            else sub.set(pos, num);
        }
        return sub.size();
    }

    // 2. LCS — Longest Common Subsequence
    public static int lcs(String s1, String s2) {
        int m = s1.length(), n = s2.length();
        int[] dp = new int[n + 1];
        for (int i = 1; i <= m; i++) {
            int prev = 0;
            for (int j = 1; j <= n; j++) {
                int temp = dp[j];
                dp[j] = s1.charAt(i-1) == s2.charAt(j-1)
                    ? prev + 1 : Math.max(dp[j], dp[j-1]);
                prev = temp;
            }
        }
        return dp[n];
    }

    // 3. Edit Distance
    public static int minDistance(String word1, String word2) {
        int m = word1.length(), n = word2.length();
        int[] dp = new int[n + 1];
        for (int j = 0; j <= n; j++) dp[j] = j;
        for (int i = 1; i <= m; i++) {
            int prev = dp[0]; dp[0] = i;
            for (int j = 1; j <= n; j++) {
                int temp = dp[j];
                dp[j] = word1.charAt(i-1) == word2.charAt(j-1)
                    ? prev
                    : 1 + Math.min(prev, Math.min(dp[j], dp[j-1]));
                prev = temp;
            }
        }
        return dp[n];
    }

    // 4. Distinct Subsequences
    public static int numDistinct(String s, String t) {
        int m = s.length(), n = t.length();
        int[] dp = new int[n + 1];
        dp[0] = 1;
        for (int i = 1; i <= m; i++)
            for (int j = n; j >= 1; j--)
                if (s.charAt(i-1) == t.charAt(j-1)) dp[j] += dp[j-1];
        return dp[n];
    }

    // 5. Russian Doll Envelopes (2D LIS)
    public static int maxEnvelopes(int[][] envelopes) {
        Arrays.sort(envelopes, (a,b) -> a[0]==b[0] ? b[1]-a[1] : a[0]-b[0]);
        int[] heights = Arrays.stream(envelopes).mapToInt(e -> e[1]).toArray();
        return lengthOfLIS(heights);
    }

    // 6. Regular Expression Matching
    public static boolean isMatch(String s, String p) {
        int m = s.length(), n = p.length();
        boolean[][] dp = new boolean[m+1][n+1];
        dp[0][0] = true;
        for (int j = 1; j <= n; j++) if (p.charAt(j-1)=='*') dp[0][j] = dp[0][j-2];
        for (int i = 1; i <= m; i++)
            for (int j = 1; j <= n; j++) {
                if (p.charAt(j-1)=='*') {
                    dp[i][j] = dp[i][j-2] // zero occurrences
                        || (dp[i-1][j] && (p.charAt(j-2)=='.' || p.charAt(j-2)==s.charAt(i-1)));
                } else {
                    dp[i][j] = dp[i-1][j-1]
                        && (p.charAt(j-1)=='.' || p.charAt(j-1)==s.charAt(i-1));
                }
            }
        return dp[m][n];
    }

    public static void main(String[] args) {
        System.out.println(lengthOfLIS(new int[]{10,9,2,5,3,7,101,18})); // 4
        System.out.println(lcs("ABCBDAB", "BDCAB")); // 4
        System.out.println(minDistance("horse", "ros")); // 3
        System.out.println(numDistinct("rabbbit", "rabbit")); // 3
        System.out.println(maxEnvelopes(new int[][]{{5,4},{6,4},{6,7},{2,3}})); // 3
        System.out.println(isMatch("aa", "a*")); // true
    }
}
