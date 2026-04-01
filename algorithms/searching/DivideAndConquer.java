package algorithms.searching;

import java.util.*;

/**
 * Divide and Conquer / Advanced Recursion — Java
 */
public class DivideAndConquer {

    // 1. Power function (fast exponentiation)
    public static double myPow(double x, int n) {
        long N = n;
        if (N < 0) { x = 1/x; N = -N; }
        double result = 1;
        while (N > 0) {
            if ((N & 1) == 1) result *= x;
            x *= x; N >>= 1;
        }
        return result;
    }

    // 2. Find Kth largest (QuickSelect) — O(n) average
    public static int findKthLargest(int[] nums, int k) {
        return quickSelect(nums, 0, nums.length-1, nums.length - k);
    }
    private static int quickSelect(int[] nums, int l, int r, int k) {
        int pivot = nums[r], p = l;
        for (int i = l; i < r; i++)
            if (nums[i] <= pivot) { int t=nums[p]; nums[p]=nums[i]; nums[i]=t; p++; }
        int t = nums[p]; nums[p] = nums[r]; nums[r] = t;
        if (p == k) return nums[p];
        return p < k ? quickSelect(nums, p+1, r, k) : quickSelect(nums, l, p-1, k);
    }

    // 3. Count inversions (merge sort)
    public static long countInversions(int[] arr) {
        return mergeSort(arr, 0, arr.length - 1);
    }
    private static long mergeSort(int[] arr, int left, int right) {
        if (left >= right) return 0;
        int mid = (left + right) / 2;
        long count = mergeSort(arr, left, mid) + mergeSort(arr, mid+1, right);
        int[] temp = new int[right - left + 1];
        int i = left, j = mid+1, k = 0;
        while (i <= mid && j <= right) {
            if (arr[i] <= arr[j]) temp[k++] = arr[i++];
            else { count += mid - i + 1; temp[k++] = arr[j++]; }
        }
        while (i <= mid) temp[k++] = arr[i++];
        while (j <= right) temp[k++] = arr[j++];
        System.arraycopy(temp, 0, arr, left, temp.length);
        return count;
    }

    // 4. Closest Pair of Points
    public static double closestPair(int[][] points) {
        Arrays.sort(points, Comparator.comparingInt(p -> p[0]));
        return closestPairRec(points, 0, points.length - 1);
    }
    private static double closestPairRec(int[][] pts, int l, int r) {
        if (r - l < 3) {
            double min = Double.MAX_VALUE;
            for (int i = l; i <= r; i++)
                for (int j = i+1; j <= r; j++) min = Math.min(min, dist(pts[i], pts[j]));
            return min;
        }
        int mid = (l + r) / 2;
        double d = Math.min(closestPairRec(pts, l, mid), closestPairRec(pts, mid+1, r));
        // Check strip
        List<int[]> strip = new ArrayList<>();
        double mx = pts[mid][0];
        for (int i = l; i <= r; i++) if (Math.abs(pts[i][0] - mx) < d) strip.add(pts[i]);
        strip.sort(Comparator.comparingInt(p -> p[1]));
        for (int i = 0; i < strip.size(); i++)
            for (int j = i+1; j < strip.size() && strip.get(j)[1]-strip.get(i)[1] < d; j++)
                d = Math.min(d, dist(strip.get(i), strip.get(j)));
        return d;
    }
    private static double dist(int[] a, int[] b) {
        return Math.sqrt((long)(a[0]-b[0])*(a[0]-b[0]) + (long)(a[1]-b[1])*(a[1]-b[1]));
    }

    // 5. Majority Element (Boyer-Moore)
    public static int majorityElement(int[] nums) {
        int count = 0, candidate = 0;
        for (int n : nums) {
            if (count == 0) candidate = n;
            count += n == candidate ? 1 : -1;
        }
        return candidate;
    }

    public static void main(String[] args) {
        System.out.println(myPow(2.0, 10));   // 1024.0
        System.out.println(myPow(2.0, -2));   // 0.25
        System.out.println(findKthLargest(new int[]{3,2,1,5,6,4}, 2)); // 5
        System.out.println(countInversions(new int[]{8,4,2,1})); // 6
        System.out.println(majorityElement(new int[]{3,2,3})); // 3
    }
}
