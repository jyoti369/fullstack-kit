package algorithms.searching;

import java.util.*;

/**
 * Prefix Sum and Difference Array Patterns
 */
public class PrefixSum {

    // 1. Range sum query
    static class NumArray {
        private int[] prefix;
        public NumArray(int[] nums) {
            prefix = new int[nums.length + 1];
            for (int i = 0; i < nums.length; i++)
                prefix[i+1] = prefix[i] + nums[i];
        }
        public int sumRange(int l, int r) { return prefix[r+1] - prefix[l]; }
    }

    // 2. Subarray sum equals K (using prefix sum)
    public static int subarraySum(int[] nums, int k) {
        Map<Integer, Integer> count = new HashMap<>();
        count.put(0, 1);
        int sum = 0, result = 0;
        for (int num : nums) {
            sum += num;
            result += count.getOrDefault(sum - k, 0);
            count.merge(sum, 1, Integer::sum);
        }
        return result;
    }

    // 3. Product of Array Except Self
    public static int[] productExceptSelf(int[] nums) {
        int n = nums.length;
        int[] result = new int[n];
        result[0] = 1;
        for (int i = 1; i < n; i++) result[i] = result[i-1] * nums[i-1];
        int right = 1;
        for (int i = n-1; i >= 0; i--) { result[i] *= right; right *= nums[i]; }
        return result;
    }

    // 4. Difference array (range update, point query)
    static class DifferenceArray {
        private int[] diff;
        public DifferenceArray(int n) { diff = new int[n + 1]; }
        // Add val to range [l, r]
        public void update(int l, int r, int val) { diff[l] += val; diff[r+1] -= val; }
        // Get final array after all updates
        public int[] result() {
            int[] res = new int[diff.length - 1];
            res[0] = diff[0];
            for (int i = 1; i < res.length; i++) res[i] = res[i-1] + diff[i];
            return res;
        }
    }

    // 5. Number of subarrays with bounded maximum
    public static int numSubarrayBoundedMax(int[] nums, int left, int right) {
        int result = 0, prev = -1, count = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] > right) { prev = i; count = 0; }
            else if (nums[i] >= left) { count = i - prev; }
            result += count;
        }
        return result;
    }

    public static void main(String[] args) {
        NumArray na = new NumArray(new int[]{-2,0,3,-5,2,-1});
        System.out.println(na.sumRange(0,2));  // 1
        System.out.println(na.sumRange(2,5));  // -1
        System.out.println(Arrays.toString(productExceptSelf(new int[]{1,2,3,4}))); // [24,12,8,6]
        System.out.println(subarraySum(new int[]{1,1,1}, 2)); // 2
    }
}
