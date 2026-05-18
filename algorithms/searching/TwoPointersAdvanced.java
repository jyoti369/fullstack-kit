package algorithms.searching;

import java.util.*;

/**
 * Advanced Two Pointer Problems in Java
 */
public class TwoPointersAdvanced {

    // 1. Subarray Product Less Than K
    public static int numSubarrayProductLessThanK(int[] nums, int k) {
        if (k <= 1) return 0;
        int product = 1, count = 0, left = 0;
        for (int right = 0; right < nums.length; right++) {
            product *= nums[right];
            while (product >= k) product /= nums[left++];
            count += right - left + 1;
        }
        return count;
    }

    // 2. Boats to Save People
    public static int numRescueBoats(int[] people, int limit) {
        Arrays.sort(people);
        int left = 0, right = people.length - 1, boats = 0;
        while (left <= right) {
            if (people[left] + people[right] <= limit) left++;
            right--; boats++;
        }
        return boats;
    }

    // 3. Minimum Difference Between Largest and Smallest in 3 Moves
    public static int minDifference(int[] nums) {
        if (nums.length <= 4) return 0;
        Arrays.sort(nums);
        int n = nums.length;
        int min = Integer.MAX_VALUE;
        for (int left = 0; left <= 3; left++)
            min = Math.min(min, nums[n-1-(3-left)] - nums[left]);
        return min;
    }

    // 4. Longest Mountain in Array
    public static int longestMountain(int[] arr) {
        int n = arr.length, ans = 0;
        for (int i = 1; i < n - 1; ) {
            if (arr[i-1] < arr[i] && arr[i] > arr[i+1]) {
                int left = i - 1, right = i + 1;
                while (left > 0 && arr[left-1] < arr[left]) left--;
                while (right < n-1 && arr[right] > arr[right+1]) right++;
                ans = Math.max(ans, right - left + 1);
                i = right;
            } else i++;
        }
        return ans;
    }

    // 5. Maximum Points You Can Obtain From Cards
    public static int maxScore(int[] cardPoints, int k) {
        int n = cardPoints.length;
        int total = 0;
        for (int i = 0; i < k; i++) total += cardPoints[i];
        int maxScore = total;
        for (int i = 0; i < k; i++) {
            total += cardPoints[n - 1 - i] - cardPoints[k - 1 - i];
            maxScore = Math.max(maxScore, total);
        }
        return maxScore;
    }

    public static void main(String[] args) {
        System.out.println(numSubarrayProductLessThanK(new int[]{10,5,2,6}, 100)); // 8
        System.out.println(numRescueBoats(new int[]{3,2,2,1}, 3));  // 3
        System.out.println(longestMountain(new int[]{2,1,4,7,3,2,5})); // 5
        System.out.println(maxScore(new int[]{1,2,3,4,5,6,1}, 3));  // 12
    }
}


package algorithms.searching;

import java.util.*;

/**
 * Advanced Two Pointer Problems in Java
 */
public class TwoPointersAdvanced {

    // 1. Subarray Product Less Than K
    public static int numSubarrayProductLessThanK(int[] nums, int k) {
        if (k <= 1) return 0;
        int product = 1, count = 0, left = 0;
        for (int right = 0; right < nums.length; right++) {
            product *= nums[right];
            while (product >= k) product /= nums[left++];
            count += right - left + 1;
        }
        return count;
    }

    // 2. Boats to Save People
    public static int numRescueBoats(int[] people, int limit) {
        Arrays.sort(people);
        int left = 0, right = people.length - 1, boats = 0;
        while (left <= right) {
            if (people[left] + people[right] <= limit) left++;
            right--; boats++;
        }
        return boats;
    }

    // 3. Minimum Difference Between Largest and Smallest in 3 Moves
    public static int minDifference(int[] nums) {
        if (nums.length <= 4) return 0;
        Arrays.sort(nums);
        int n = nums.length;
        int min = Integer.MAX_VALUE;
        for (int left = 0; left <= 3; left++)
            min = Math.min(min, nums[n-1-(3-left)] - nums[left]);
        return min;
    }

    // 4. Longest Mountain in Array
    public static int longestMountain(int[] arr) {
        int n = arr.length, ans = 0;
        for (int i = 1; i < n - 1; ) {
            if (arr[i-1] < arr[i] && arr[i] > arr[i+1]) {
                int left = i - 1, right = i + 1;
                while (left > 0 && arr[left-1] < arr[left]) left--;
                while (right < n-1 && arr[right] > arr[right+1]) right++;
                ans = Math.max(ans, right - left + 1);
                i = right;
            } else i++;
        }
        return ans;
    }

    // 5. Maximum Points You Can Obtain From Cards
    public static int maxScore(int[] cardPoints, int k) {
        int n = cardPoints.length;
        int total = 0;
        for (int i = 0; i < k; i++) total += cardPoints[i];
        int maxScore = total;
        for (int i = 0; i < k; i++) {
            total += cardPoints[n - 1 - i] - cardPoints[k - 1 - i];
            maxScore = Math.max(maxScore, total);
        }
        return maxScore;
    }

    public static void main(String[] args) {
        System.out.println(numSubarrayProductLessThanK(new int[]{10,5,2,6}, 100)); // 8
        System.out.println(numRescueBoats(new int[]{3,2,2,1}, 3));  // 3
        System.out.println(longestMountain(new int[]{2,1,4,7,3,2,5})); // 5
        System.out.println(maxScore(new int[]{1,2,3,4,5,6,1}, 3));  // 12
    }
}
