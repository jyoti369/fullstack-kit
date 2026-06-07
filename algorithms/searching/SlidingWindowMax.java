package algorithms.searching;

import java.util.*;

/**
 * Sliding Window Maximum (LeetCode #239)
 * Time: O(n) | Space: O(k)
 * Uses a monotonic deque — front is always the current window maximum.
 */
public class SlidingWindowMax {

    // 1. Max sliding window
    public static int[] maxSlidingWindow(int[] nums, int k) {
        int n = nums.length;
        int[] result = new int[n - k + 1];
        Deque<Integer> dq = new ArrayDeque<>(); // stores indices
        for (int i = 0; i < n; i++) {
            // Remove indices outside window
            while (!dq.isEmpty() && dq.peekFirst() < i - k + 1) dq.pollFirst();
            // Remove smaller elements from back
            while (!dq.isEmpty() && nums[dq.peekLast()] < nums[i]) dq.pollLast();
            dq.offerLast(i);
            if (i >= k - 1) result[i - k + 1] = nums[dq.peekFirst()];
        }
        return result;
    }

    // 2. Min sliding window (flip comparison)
    public static int[] minSlidingWindow(int[] nums, int k) {
        int n = nums.length;
        int[] result = new int[n - k + 1];
        Deque<Integer> dq = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            while (!dq.isEmpty() && dq.peekFirst() < i - k + 1) dq.pollFirst();
            while (!dq.isEmpty() && nums[dq.peekLast()] > nums[i]) dq.pollLast();
            dq.offerLast(i);
            if (i >= k - 1) result[i - k + 1] = nums[dq.peekFirst()];
        }
        return result;
    }

    // 3. Sliding window average
    public static double[] movingAverage(int[] nums, int k) {
        double[] result = new double[nums.length - k + 1];
        double sum = 0;
        for (int i = 0; i < k; i++) sum += nums[i];
        result[0] = sum / k;
        for (int i = k; i < nums.length; i++) {
            sum += nums[i] - nums[i - k];
            result[i - k + 1] = sum / k;
        }
        return result;
    }

    // 4. Longest substring without repeating characters (classic sliding window)
    public static int lengthOfLongestSubstring(String s) {
        Map<Character, Integer> map = new HashMap<>();
        int max = 0, left = 0;
        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            if (map.containsKey(c) && map.get(c) >= left) left = map.get(c) + 1;
            map.put(c, right);
            max = Math.max(max, right - left + 1);
        }
        return max;
    }

    // 5. Minimum window substring
    public static String minWindow(String s, String t) {
        int[] need = new int[128];
        for (char c : t.toCharArray()) need[c]++;
        int missing = t.length(), left = 0, start = 0, end = 0;
        String result = "";
        for (int right = 0; right < s.length(); right++) {
            if (need[s.charAt(right)]-- > 0) missing--;
            while (missing == 0) {
                int len = right - left + 1;
                if (result.isEmpty() || len < result.length()) result = s.substring(left, right + 1);
                if (need[s.charAt(left++)]++ == 0) missing++;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(maxSlidingWindow(new int[]{1,3,-1,-3,5,3,6,7}, 3))); // [3,3,5,5,6,7]
        System.out.println(Arrays.toString(movingAverage(new int[]{1,3,5,7,9}, 3))); // [3.0,5.0,7.0]
        System.out.println(lengthOfLongestSubstring("abcabcbb")); // 3
        System.out.println(minWindow("ADOBECODEBANC", "ABC")); // BANC
    }
}
