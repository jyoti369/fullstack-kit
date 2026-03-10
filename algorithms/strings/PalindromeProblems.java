package algorithms.strings;

import java.util.*;

/**
 * Palindrome Problems in Java
 */
public class PalindromeProblems {

    // 1. Valid Palindrome
    public static boolean isPalindrome(String s) {
        int l = 0, r = s.length() - 1;
        while (l < r) {
            while (l < r && !Character.isLetterOrDigit(s.charAt(l))) l++;
            while (l < r && !Character.isLetterOrDigit(s.charAt(r))) r--;
            if (Character.toLowerCase(s.charAt(l)) != Character.toLowerCase(s.charAt(r))) return false;
            l++; r--;
        }
        return true;
    }

    // 2. Longest Palindromic Substring — Expand Around Center
    public static String longestPalindrome(String s) {
        int start = 0, maxLen = 1;
        for (int i = 0; i < s.length(); i++) {
            for (int[] range : new int[][]{{i,i},{i,i+1}}) {
                int l = range[0], r = range[1];
                while (l >= 0 && r < s.length() && s.charAt(l) == s.charAt(r)) { l--; r++; }
                if (r-l-1 > maxLen) { maxLen = r-l-1; start = l+1; }
            }
        }
        return s.substring(start, start + maxLen);
    }

    // 3. Palindromic Substrings — Count
    public static int countSubstrings(String s) {
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            count += expand(s, i, i) + expand(s, i, i+1);
        }
        return count;
    }
    private static int expand(String s, int l, int r) {
        int cnt = 0;
        while (l >= 0 && r < s.length() && s.charAt(l--) == s.charAt(r++)) cnt++;
        return cnt;
    }

    // 4. Minimum Insertions to Make Palindrome
    public static int minInsertions(String s) {
        String rev = new StringBuilder(s).reverse().toString();
        // LCS of s and reverse(s) → min insertions = n - LCS
        int n = s.length();
        int[] dp = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            int prev = 0;
            for (int j = 1; j <= n; j++) {
                int temp = dp[j];
                dp[j] = s.charAt(i-1) == rev.charAt(j-1) ? prev+1 : Math.max(dp[j], dp[j-1]);
                prev = temp;
            }
        }
        return n - dp[n];
    }

    // 5. Partition string into palindromes (min cuts)
    public static int minCut(String s) {
        int n = s.length();
        boolean[][] isPalin = new boolean[n][n];
        for (int i = n-1; i >= 0; i--)
            for (int j = i; j < n; j++)
                isPalin[i][j] = s.charAt(i)==s.charAt(j) && (j-i<=2 || isPalin[i+1][j-1]);
        int[] dp = new int[n];
        Arrays.fill(dp, Integer.MAX_VALUE);
        for (int i = 0; i < n; i++) {
            if (isPalin[0][i]) { dp[i] = 0; continue; }
            for (int j = 1; j <= i; j++)
                if (isPalin[j][i]) dp[i] = Math.min(dp[i], dp[j-1]+1);
        }
        return dp[n-1];
    }

    // 6. Palindrome Pairs (in word list)
    public static List<List<Integer>> palindromePairs(String[] words) {
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < words.length; i++) map.put(words[i], i);
        List<List<Integer>> result = new ArrayList<>();
        for (int i = 0; i < words.length; i++) {
            for (int j = 0; j <= words[i].length(); j++) {
                String prefix = words[i].substring(0, j);
                String suffix = words[i].substring(j);
                String revPrefix = new StringBuilder(prefix).reverse().toString();
                String revSuffix = new StringBuilder(suffix).reverse().toString();
                if (isPalinCheck(suffix) && map.containsKey(revPrefix) && map.get(revPrefix) != i)
                    result.add(List.of(i, map.get(revPrefix)));
                if (j != words[i].length() && isPalinCheck(prefix) && map.containsKey(revSuffix) && map.get(revSuffix) != i)
                    result.add(List.of(map.get(revSuffix), i));
            }
        }
        return result;
    }
    private static boolean isPalinCheck(String s) {
        int l=0,r=s.length()-1; while(l<r) if(s.charAt(l++)!=s.charAt(r--)) return false; return true;
    }

    public static void main(String[] args) {
        System.out.println(isPalindrome("A man, a plan, a canal: Panama")); // true
        System.out.println(longestPalindrome("babad")); // bab
        System.out.println(countSubstrings("aaa")); // 6
        System.out.println(minInsertions("zzazz")); // 0
        System.out.println(minCut("aab")); // 1
    }
}
