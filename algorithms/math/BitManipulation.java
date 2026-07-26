package algorithms.math;

import java.util.*;

/**
 * Bit Manipulation Tricks in Java
 */
public class BitManipulation {

    // ---- Core tricks ----
    public static void basics() {
        int n = 42;  // 0b101010
        System.out.println(n & 1);         // check odd: 0
        System.out.println(n & (n-1));     // clear lowest set bit: 40
        System.out.println(n & (-n));      // isolate lowest set bit: 2
        System.out.println(n | (1<<5));    // set bit 5: 42
        System.out.println(n ^ (1<<1));    // flip bit 1: 40
        System.out.println(n >> 1);        // divide by 2: 21
        System.out.println(n << 1);        // multiply by 2: 84
        System.out.println(Integer.bitCount(n)); // count set bits: 3
        System.out.println(Integer.highestOneBit(n)); // 32
        System.out.println(Integer.numberOfTrailingZeros(n)); // 1
    }

    // 1. Single Number (XOR trick)
    public static int singleNumber(int[] nums) {
        int result = 0;
        for (int n : nums) result ^= n;
        return result;
    }

    // 2. Single Number III (two distinct singles)
    public static int[] singleNumberIII(int[] nums) {
        int xor = 0;
        for (int n : nums) xor ^= n;
        int diff = xor & (-xor);  // rightmost set bit
        int a = 0;
        for (int n : nums) if ((n & diff) != 0) a ^= n;
        return new int[]{a, xor ^ a};
    }

    // 3. Count Bits 0..n
    public static int[] countBits(int n) {
        int[] dp = new int[n + 1];
        for (int i = 1; i <= n; i++) dp[i] = dp[i >> 1] + (i & 1);
        return dp;
    }

    // 4. Reverse Bits
    public static int reverseBits(int n) {
        int result = 0;
        for (int i = 0; i < 32; i++) { result = (result << 1) | (n & 1); n >>= 1; }
        return result;
    }

    // 5. Generate all subsets using bits
    public static List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        int total = 1 << nums.length;
        for (int mask = 0; mask < total; mask++) {
            List<Integer> subset = new ArrayList<>();
            for (int i = 0; i < nums.length; i++)
                if ((mask & (1 << i)) != 0) subset.add(nums[i]);
            result.add(subset);
        }
        return result;
    }

    // 6. Maximum XOR of Two Numbers
    public static int findMaximumXOR(int[] nums) {
        int max = 0, mask = 0;
        for (int i = 31; i >= 0; i--) {
            mask |= (1 << i);
            Set<Integer> prefixes = new HashSet<>();
            for (int n : nums) prefixes.add(n & mask);
            int candidate = max | (1 << i);
            for (int prefix : prefixes)
                if (prefixes.contains(prefix ^ candidate)) { max = candidate; break; }
        }
        return max;
    }

    public static void main(String[] args) {
        System.out.println(singleNumber(new int[]{4,1,2,1,2}));  // 4
        System.out.println(Arrays.toString(singleNumberIII(new int[]{1,2,1,3,2,5}))); // [3,5]
        System.out.println(Arrays.toString(countBits(5)));  // [0,1,1,2,1,2]
        System.out.println(Integer.toBinaryString(reverseBits(0b00000010100101000001111010011100)));
        System.out.println(findMaximumXOR(new int[]{3,10,5,25,2,8})); // 28
    }
}


package algorithms.math;

import java.util.*;

/**
 * Bit Manipulation Tricks in Java
 */
public class BitManipulation {

    // ---- Core tricks ----
    public static void basics() {
        int n = 42;  // 0b101010
        System.out.println(n & 1);         // check odd: 0
        System.out.println(n & (n-1));     // clear lowest set bit: 40
        System.out.println(n & (-n));      // isolate lowest set bit: 2
        System.out.println(n | (1<<5));    // set bit 5: 42
        System.out.println(n ^ (1<<1));    // flip bit 1: 40
        System.out.println(n >> 1);        // divide by 2: 21
        System.out.println(n << 1);        // multiply by 2: 84
        System.out.println(Integer.bitCount(n)); // count set bits: 3
        System.out.println(Integer.highestOneBit(n)); // 32
        System.out.println(Integer.numberOfTrailingZeros(n)); // 1
    }

    // 1. Single Number (XOR trick)
    public static int singleNumber(int[] nums) {
        int result = 0;
        for (int n : nums) result ^= n;
        return result;
    }

    // 2. Single Number III (two distinct singles)
    public static int[] singleNumberIII(int[] nums) {
        int xor = 0;
        for (int n : nums) xor ^= n;
        int diff = xor & (-xor);  // rightmost set bit
        int a = 0;
        for (int n : nums) if ((n & diff) != 0) a ^= n;
        return new int[]{a, xor ^ a};
    }

    // 3. Count Bits 0..n
    public static int[] countBits(int n) {
        int[] dp = new int[n + 1];
        for (int i = 1; i <= n; i++) dp[i] = dp[i >> 1] + (i & 1);
        return dp;
    }

    // 4. Reverse Bits
    public static int reverseBits(int n) {
        int result = 0;
        for (int i = 0; i < 32; i++) { result = (result << 1) | (n & 1); n >>= 1; }
        return result;
    }

    // 5. Generate all subsets using bits
    public static List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        int total = 1 << nums.length;
        for (int mask = 0; mask < total; mask++) {
            List<Integer> subset = new ArrayList<>();
            for (int i = 0; i < nums.length; i++)
                if ((mask & (1 << i)) != 0) subset.add(nums[i]);
            result.add(subset);
        }
        return result;
    }

    // 6. Maximum XOR of Two Numbers
    public static int findMaximumXOR(int[] nums) {
        int max = 0, mask = 0;
        for (int i = 31; i >= 0; i--) {
            mask |= (1 << i);
            Set<Integer> prefixes = new HashSet<>();
            for (int n : nums) prefixes.add(n & mask);
            int candidate = max | (1 << i);
            for (int prefix : prefixes)
                if (prefixes.contains(prefix ^ candidate)) { max = candidate; break; }
        }
        return max;
    }

    public static void main(String[] args) {
        System.out.println(singleNumber(new int[]{4,1,2,1,2}));  // 4
        System.out.println(Arrays.toString(singleNumberIII(new int[]{1,2,1,3,2,5}))); // [3,5]
        System.out.println(Arrays.toString(countBits(5)));  // [0,1,1,2,1,2]
        System.out.println(Integer.toBinaryString(reverseBits(0b00000010100101000001111010011100)));
        System.out.println(findMaximumXOR(new int[]{3,10,5,25,2,8})); // 28
    }
}
