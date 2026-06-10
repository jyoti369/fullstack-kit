package algorithms.searching;

import java.util.*;

/**
 * Monotonic Stack Problems — Java
 */
public class MonotonicStack {

    // 1. Next Greater Element
    public static int[] nextGreaterElement(int[] nums) {
        int n = nums.length;
        int[] result = new int[n];
        Arrays.fill(result, -1);
        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && nums[i] > nums[stack.peek()])
                result[stack.pop()] = nums[i];
            stack.push(i);
        }
        return result;
    }

    // 2. Next Greater Element — circular
    public static int[] nextGreaterElementCircular(int[] nums) {
        int n = nums.length;
        int[] result = new int[n];
        Arrays.fill(result, -1);
        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = 0; i < 2 * n; i++) {
            while (!stack.isEmpty() && nums[i % n] > nums[stack.peek()])
                result[stack.pop()] = nums[i % n];
            if (i < n) stack.push(i);
        }
        return result;
    }

    // 3. Sum of Subarray Minimums
    public static int sumSubarrayMins(int[] arr) {
        final int MOD = 1_000_000_007;
        int n = arr.length;
        long result = 0;
        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = 0; i <= n; i++) {
            while (!stack.isEmpty() && (i == n || arr[i] <= arr[stack.peek()])) {
                int j = stack.pop();
                int left = stack.isEmpty() ? j + 1 : j - stack.peek();
                int right = i - j;
                result = (result + (long) arr[j] * left * right) % MOD;
            }
            stack.push(i);
        }
        return (int) result;
    }

    // 4. Maximal Rectangle in Binary Matrix
    public static int maximalRectangle(char[][] matrix) {
        if (matrix.length == 0) return 0;
        int n = matrix[0].length;
        int[] heights = new int[n];
        int max = 0;
        for (char[] row : matrix) {
            for (int j = 0; j < n; j++)
                heights[j] = row[j] == '1' ? heights[j] + 1 : 0;
            max = Math.max(max, largestRectangleInHistogram(heights));
        }
        return max;
    }

    private static int largestRectangleInHistogram(int[] heights) {
        Deque<Integer> stack = new ArrayDeque<>();
        int max = 0;
        for (int i = 0; i <= heights.length; i++) {
            int h = i == heights.length ? 0 : heights[i];
            while (!stack.isEmpty() && h < heights[stack.peek()]) {
                int height = heights[stack.pop()];
                int width = stack.isEmpty() ? i : i - stack.peek() - 1;
                max = Math.max(max, height * width);
            }
            stack.push(i);
        }
        return max;
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(nextGreaterElement(new int[]{2,1,2,4,3}))); // [4,2,4,-1,-1]
        System.out.println(sumSubarrayMins(new int[]{3,1,2,4})); // 17
        char[][] mat = {{"1","0","1","0","0"},{"1","0","1","1","1"},{"1","1","1","1","1"},{"1","0","0","1","0"}}
            .length == 0 ? new char[0][] :
            new char[][]{{"1","0","1"[0]+'0',"0"[0]+'0',"0"[0]+'0'}}; // simplified demo
        System.out.println(maximalRectangle(new char[][]{{"1","0","1","0","0"},{"1","0","1","1","1"},{"1","1","1","1","1"}}.length == 0 ? new char[0][] : new char[][]{
            {'1','0','1','0','0'},
            {'1','0','1','1','1'},
            {'1','1','1','1','1'},
            {'1','0','0','1','0'}}));
    }
}


package algorithms.searching;

import java.util.*;

/**
 * Monotonic Stack Problems — Java
 */
public class MonotonicStack {

    // 1. Next Greater Element
    public static int[] nextGreaterElement(int[] nums) {
        int n = nums.length;
        int[] result = new int[n];
        Arrays.fill(result, -1);
        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && nums[i] > nums[stack.peek()])
                result[stack.pop()] = nums[i];
            stack.push(i);
        }
        return result;
    }

    // 2. Next Greater Element — circular
    public static int[] nextGreaterElementCircular(int[] nums) {
        int n = nums.length;
        int[] result = new int[n];
        Arrays.fill(result, -1);
        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = 0; i < 2 * n; i++) {
            while (!stack.isEmpty() && nums[i % n] > nums[stack.peek()])
                result[stack.pop()] = nums[i % n];
            if (i < n) stack.push(i);
        }
        return result;
    }

    // 3. Sum of Subarray Minimums
    public static int sumSubarrayMins(int[] arr) {
        final int MOD = 1_000_000_007;
        int n = arr.length;
        long result = 0;
        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = 0; i <= n; i++) {
            while (!stack.isEmpty() && (i == n || arr[i] <= arr[stack.peek()])) {
                int j = stack.pop();
                int left = stack.isEmpty() ? j + 1 : j - stack.peek();
                int right = i - j;
                result = (result + (long) arr[j] * left * right) % MOD;
            }
            stack.push(i);
        }
        return (int) result;
    }

    // 4. Maximal Rectangle in Binary Matrix
    public static int maximalRectangle(char[][] matrix) {
        if (matrix.length == 0) return 0;
        int n = matrix[0].length;
        int[] heights = new int[n];
        int max = 0;
        for (char[] row : matrix) {
            for (int j = 0; j < n; j++)
                heights[j] = row[j] == '1' ? heights[j] + 1 : 0;
            max = Math.max(max, largestRectangleInHistogram(heights));
        }
        return max;
    }

    private static int largestRectangleInHistogram(int[] heights) {
        Deque<Integer> stack = new ArrayDeque<>();
        int max = 0;
        for (int i = 0; i <= heights.length; i++) {
            int h = i == heights.length ? 0 : heights[i];
            while (!stack.isEmpty() && h < heights[stack.peek()]) {
                int height = heights[stack.pop()];
                int width = stack.isEmpty() ? i : i - stack.peek() - 1;
                max = Math.max(max, height * width);
            }
            stack.push(i);
        }
        return max;
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(nextGreaterElement(new int[]{2,1,2,4,3}))); // [4,2,4,-1,-1]
        System.out.println(sumSubarrayMins(new int[]{3,1,2,4})); // 17
        char[][] mat = {{"1","0","1","0","0"},{"1","0","1","1","1"},{"1","1","1","1","1"},{"1","0","0","1","0"}}
            .length == 0 ? new char[0][] :
            new char[][]{{"1","0","1"[0]+'0',"0"[0]+'0',"0"[0]+'0'}}; // simplified demo
        System.out.println(maximalRectangle(new char[][]{{"1","0","1","0","0"},{"1","0","1","1","1"},{"1","1","1","1","1"}}.length == 0 ? new char[0][] : new char[][]{
            {'1','0','1','0','0'},
            {'1','0','1','1','1'},
            {'1','1','1','1','1'},
            {'1','0','0','1','0'}}));
    }
}
