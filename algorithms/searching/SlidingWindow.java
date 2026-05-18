package algorithms.searching;

import java.util.*;

/**
 * Sliding Window Patterns — Fixed and Variable Size
 * O(n) solutions for subarray/substring problems.
 */
public class SlidingWindow {

    // 1. Max sum subarray of size k
    public static int maxSumSubarray(int[] arr, int k) {
        int windowSum = 0, maxSum = 0;
        for (int i = 0; i < k; i++) windowSum += arr[i];
        maxSum = windowSum;
        for (int i = k; i < arr.length; i++) {
            windowSum += arr[i] - arr[i - k];
            maxSum = Math.max(maxSum, windowSum);
        }
        return maxSum;
    }

    // 2. Longest substring without repeating chars
    public static int lengthOfLongestSubstring(String s) {
        Map<Character, Integer> charIndex = new HashMap<>();
        int maxLen = 0, left = 0;
        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            if (charIndex.containsKey(c) && charIndex.get(c) >= left)
                left = charIndex.get(c) + 1;
            charIndex.put(c, right);
            maxLen = Math.max(maxLen, right - left + 1);
        }
        return maxLen;
    }

    // 3. Minimum window substring
    public static String minWindow(String s, String t) {
        Map<Character, Integer> need = new HashMap<>();
        for (char c : t.toCharArray()) need.merge(c, 1, Integer::sum);
        int missing = t.length(), left = 0, start = 0, minLen = Integer.MAX_VALUE;
        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            if (need.getOrDefault(c, 0) > 0) missing--;
            need.merge(c, -1, Integer::sum);
            if (missing == 0) {
                while (need.getOrDefault(s.charAt(left), 0) < 0)
                    need.merge(s.charAt(left++), 1, Integer::sum);
                if (right - left + 1 < minLen) { minLen = right - left + 1; start = left; }
                need.merge(s.charAt(left++), 1, Integer::sum);
                missing++;
            }
        }
        return minLen == Integer.MAX_VALUE ? "" : s.substring(start, start + minLen);
    }

    // 4. Longest repeating character replacement
    public static int characterReplacement(String s, int k) {
        int[] freq = new int[26];
        int left = 0, maxFreq = 0, maxLen = 0;
        for (int right = 0; right < s.length(); right++) {
            freq[s.charAt(right) - 'A']++;
            maxFreq = Math.max(maxFreq, freq[s.charAt(right) - 'A']);
            if ((right - left + 1) - maxFreq > k)
                freq[s.charAt(left++) - 'A']--;
            maxLen = Math.max(maxLen, right - left + 1);
        }
        return maxLen;
    }

    // 5. Find all anagrams in string
    public static List<Integer> findAnagrams(String s, String p) {
        List<Integer> result = new ArrayList<>();
        int[] pCount = new int[26], wCount = new int[26];
        for (char c : p.toCharArray()) pCount[c - 'a']++;
        for (int i = 0; i < s.length(); i++) {
            wCount[s.charAt(i) - 'a']++;
            if (i >= p.length()) wCount[s.charAt(i - p.length()) - 'a']--;
            if (Arrays.equals(pCount, wCount)) result.add(i - p.length() + 1);
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(maxSumSubarray(new int[]{2,1,5,1,3,2}, 3));  // 9
        System.out.println(lengthOfLongestSubstring("abcabcbb"));         // 3
        System.out.println(minWindow("ADOBECODEBANC", "ABC"));            // BANC
        System.out.println(characterReplacement("AABABBA", 1));           // 4
        System.out.println(findAnagrams("cbaebabacd", "abc"));            // [0, 6]
    }
}


package algorithms.searching;

import java.util.*;

/**
 * Sliding Window Patterns — Fixed and Variable Size
 * O(n) solutions for subarray/substring problems.
 */
public class SlidingWindow {

    // 1. Max sum subarray of size k
    public static int maxSumSubarray(int[] arr, int k) {
        int windowSum = 0, maxSum = 0;
        for (int i = 0; i < k; i++) windowSum += arr[i];
        maxSum = windowSum;
        for (int i = k; i < arr.length; i++) {
            windowSum += arr[i] - arr[i - k];
            maxSum = Math.max(maxSum, windowSum);
        }
        return maxSum;
    }

    // 2. Longest substring without repeating chars
    public static int lengthOfLongestSubstring(String s) {
        Map<Character, Integer> charIndex = new HashMap<>();
        int maxLen = 0, left = 0;
        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            if (charIndex.containsKey(c) && charIndex.get(c) >= left)
                left = charIndex.get(c) + 1;
            charIndex.put(c, right);
            maxLen = Math.max(maxLen, right - left + 1);
        }
        return maxLen;
    }

    // 3. Minimum window substring
    public static String minWindow(String s, String t) {
        Map<Character, Integer> need = new HashMap<>();
        for (char c : t.toCharArray()) need.merge(c, 1, Integer::sum);
        int missing = t.length(), left = 0, start = 0, minLen = Integer.MAX_VALUE;
        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            if (need.getOrDefault(c, 0) > 0) missing--;
            need.merge(c, -1, Integer::sum);
            if (missing == 0) {
                while (need.getOrDefault(s.charAt(left), 0) < 0)
                    need.merge(s.charAt(left++), 1, Integer::sum);
                if (right - left + 1 < minLen) { minLen = right - left + 1; start = left; }
                need.merge(s.charAt(left++), 1, Integer::sum);
                missing++;
            }
        }
        return minLen == Integer.MAX_VALUE ? "" : s.substring(start, start + minLen);
    }

    // 4. Longest repeating character replacement
    public static int characterReplacement(String s, int k) {
        int[] freq = new int[26];
        int left = 0, maxFreq = 0, maxLen = 0;
        for (int right = 0; right < s.length(); right++) {
            freq[s.charAt(right) - 'A']++;
            maxFreq = Math.max(maxFreq, freq[s.charAt(right) - 'A']);
            if ((right - left + 1) - maxFreq > k)
                freq[s.charAt(left++) - 'A']--;
            maxLen = Math.max(maxLen, right - left + 1);
        }
        return maxLen;
    }

    // 5. Find all anagrams in string
    public static List<Integer> findAnagrams(String s, String p) {
        List<Integer> result = new ArrayList<>();
        int[] pCount = new int[26], wCount = new int[26];
        for (char c : p.toCharArray()) pCount[c - 'a']++;
        for (int i = 0; i < s.length(); i++) {
            wCount[s.charAt(i) - 'a']++;
            if (i >= p.length()) wCount[s.charAt(i - p.length()) - 'a']--;
            if (Arrays.equals(pCount, wCount)) result.add(i - p.length() + 1);
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(maxSumSubarray(new int[]{2,1,5,1,3,2}, 3));  // 9
        System.out.println(lengthOfLongestSubstring("abcabcbb"));         // 3
        System.out.println(minWindow("ADOBECODEBANC", "ABC"));            // BANC
        System.out.println(characterReplacement("AABABBA", 1));           // 4
        System.out.println(findAnagrams("cbaebabacd", "abc"));            // [0, 6]
    }
}
