package algorithms.strings;

import java.util.*;

/**
 * String Algorithms in Java
 * KMP, Rabin-Karp, Z-Algorithm
 */
public class StringAlgorithms {

    // ---- KMP (Knuth-Morris-Pratt) ----
    public static List<Integer> kmpSearch(String text, String pattern) {
        List<Integer> result = new ArrayList<>();
        int n = text.length(), m = pattern.length();
        int[] lps = buildLPS(pattern);
        int i = 0, j = 0;
        while (i < n) {
            if (text.charAt(i) == pattern.charAt(j)) { i++; j++; }
            if (j == m) { result.add(i - m); j = lps[j - 1]; }
            else if (i < n && text.charAt(i) != pattern.charAt(j)) {
                if (j != 0) j = lps[j - 1]; else i++;
            }
        }
        return result;
    }

    private static int[] buildLPS(String pattern) {
        int m = pattern.length();
        int[] lps = new int[m];
        int len = 0, i = 1;
        while (i < m) {
            if (pattern.charAt(i) == pattern.charAt(len)) { lps[i++] = ++len; }
            else { if (len != 0) len = lps[len - 1]; else lps[i++] = 0; }
        }
        return lps;
    }

    // ---- Rabin-Karp ----
    public static List<Integer> rabinKarp(String text, String pattern) {
        List<Integer> result = new ArrayList<>();
        int n = text.length(), m = pattern.length();
        final int BASE = 31, MOD = 1_000_000_007;
        long patHash = 0, winHash = 0, power = 1;
        for (int i = 0; i < m; i++) {
            patHash = (patHash * BASE + (pattern.charAt(i) - 'a' + 1)) % MOD;
            winHash = (winHash * BASE + (text.charAt(i) - 'a' + 1)) % MOD;
            if (i > 0) power = (power * BASE) % MOD;
        }
        for (int i = m; i <= n; i++) {
            if (winHash == patHash && text.substring(i-m, i).equals(pattern)) result.add(i-m);
            if (i < n) {
                winHash = (winHash - (text.charAt(i-m) - 'a' + 1) * power % MOD + MOD) % MOD;
                winHash = (winHash * BASE + (text.charAt(i) - 'a' + 1)) % MOD;
            }
        }
        return result;
    }

    // ---- Z-Algorithm ----
    public static int[] zFunction(String s) {
        int n = s.length();
        int[] z = new int[n];
        int l = 0, r = 0;
        for (int i = 1; i < n; i++) {
            if (i < r) z[i] = Math.min(r - i, z[i - l]);
            while (i + z[i] < n && s.charAt(z[i]) == s.charAt(i + z[i])) z[i]++;
            if (i + z[i] > r) { l = i; r = i + z[i]; }
        }
        return z;
    }

    // ---- Longest Palindromic Substring (Manacher) ----
    public static String manacher(String s) {
        String t = "#" + String.join("#", s.split("")) + "#";
        int n = t.length();
        int[] p = new int[n];
        int center = 0, right = 0;
        int bestCenter = 0, bestLen = 0;
        for (int i = 0; i < n; i++) {
            if (i < right) p[i] = Math.min(right - i, p[2 * center - i]);
            while (i-p[i]-1 >= 0 && i+p[i]+1 < n && t.charAt(i-p[i]-1) == t.charAt(i+p[i]+1)) p[i]++;
            if (i + p[i] > right) { center = i; right = i + p[i]; }
            if (p[i] > bestLen) { bestLen = p[i]; bestCenter = i; }
        }
        return s.substring((bestCenter - bestLen) / 2, (bestCenter + bestLen) / 2);
    }

    // ---- Anagram check ----
    public static boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) return false;
        int[] count = new int[26];
        for (char c : s.toCharArray()) count[c-'a']++;
        for (char c : t.toCharArray()) if (--count[c-'a'] < 0) return false;
        return true;
    }

    public static void main(String[] args) {
        System.out.println(kmpSearch("AABAACAADAABAABA", "AABA")); // [0, 9, 12]
        System.out.println(manacher("babad"));  // bab or aba
        System.out.println(isAnagram("anagram", "nagaram")); // true
    }
}
