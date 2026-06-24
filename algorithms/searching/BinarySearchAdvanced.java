package algorithms.searching;

/**
 * Binary Search on Answer (meta-binary search)
 * When you can't find the answer directly but can CHECK if an answer is valid.
 */
public class BinarySearchAdvanced {

    // 1. Koko Eating Bananas — find min speed
    public static int minEatingSpeed(int[] piles, int h) {
        int lo = 1, hi = 0;
        for (int p : piles) hi = Math.max(hi, p);
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (canFinish(piles, mid, h)) hi = mid;
            else lo = mid + 1;
        }
        return lo;
    }
    private static boolean canFinish(int[] piles, int speed, int h) {
        int hours = 0;
        for (int p : piles) hours += (p + speed - 1) / speed;
        return hours <= h;
    }

    // 2. Minimum Days to Make m Bouquets
    public static int minDays(int[] bloomDay, int m, int k) {
        if ((long)m * k > bloomDay.length) return -1;
        int lo = 1, hi = 0;
        for (int d : bloomDay) hi = Math.max(hi, d);
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (canMake(bloomDay, m, k, mid)) hi = mid;
            else lo = mid + 1;
        }
        return lo;
    }
    private static boolean canMake(int[] bd, int m, int k, int day) {
        int bouquets = 0, consecutive = 0;
        for (int d : bd) {
            if (d <= day) { if (++consecutive == k) { bouquets++; consecutive = 0; }}
            else consecutive = 0;
        }
        return bouquets >= m;
    }

    // 3. Sqrt (integer)
    public static int mySqrt(int x) {
        long lo = 1, hi = x;
        while (lo < hi) {
            long mid = lo + (hi - lo + 1) / 2;
            if (mid * mid <= x) lo = mid;
            else hi = mid - 1;
        }
        return (int) lo;
    }

    // 4. Split Array Largest Sum
    public static int splitArray(int[] nums, int k) {
        int lo = 0, hi = 0;
        for (int n : nums) { lo = Math.max(lo, n); hi += n; }
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (canSplit(nums, k, mid)) hi = mid;
            else lo = mid + 1;
        }
        return lo;
    }
    private static boolean canSplit(int[] nums, int k, int maxSum) {
        int count = 1, currSum = 0;
        for (int n : nums) {
            if (currSum + n > maxSum) { count++; currSum = 0; }
            currSum += n;
        }
        return count <= k;
    }

    // 5. Find element in 2D sorted matrix
    public static boolean searchMatrix(int[][] matrix, int target) {
        int m = matrix.length, n = matrix[0].length;
        int lo = 0, hi = m * n - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            int val = matrix[mid / n][mid % n];
            if (val == target) return true;
            if (val < target) lo = mid + 1;
            else hi = mid - 1;
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println(minEatingSpeed(new int[]{3,6,7,11}, 8));  // 4
        System.out.println(mySqrt(8));  // 2
        System.out.println(splitArray(new int[]{7,2,5,10,8}, 2));    // 18
        System.out.println(searchMatrix(new int[][]{{1,3,5,7},{10,11,16,20},{23,30,34,60}}, 3)); // true
    }
}


package algorithms.searching;

/**
 * Binary Search on Answer (meta-binary search)
 * When you can't find the answer directly but can CHECK if an answer is valid.
 */
public class BinarySearchAdvanced {

    // 1. Koko Eating Bananas — find min speed
    public static int minEatingSpeed(int[] piles, int h) {
        int lo = 1, hi = 0;
        for (int p : piles) hi = Math.max(hi, p);
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (canFinish(piles, mid, h)) hi = mid;
            else lo = mid + 1;
        }
        return lo;
    }
    private static boolean canFinish(int[] piles, int speed, int h) {
        int hours = 0;
        for (int p : piles) hours += (p + speed - 1) / speed;
        return hours <= h;
    }

    // 2. Minimum Days to Make m Bouquets
    public static int minDays(int[] bloomDay, int m, int k) {
        if ((long)m * k > bloomDay.length) return -1;
        int lo = 1, hi = 0;
        for (int d : bloomDay) hi = Math.max(hi, d);
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (canMake(bloomDay, m, k, mid)) hi = mid;
            else lo = mid + 1;
        }
        return lo;
    }
    private static boolean canMake(int[] bd, int m, int k, int day) {
        int bouquets = 0, consecutive = 0;
        for (int d : bd) {
            if (d <= day) { if (++consecutive == k) { bouquets++; consecutive = 0; }}
            else consecutive = 0;
        }
        return bouquets >= m;
    }

    // 3. Sqrt (integer)
    public static int mySqrt(int x) {
        long lo = 1, hi = x;
        while (lo < hi) {
            long mid = lo + (hi - lo + 1) / 2;
            if (mid * mid <= x) lo = mid;
            else hi = mid - 1;
        }
        return (int) lo;
    }

    // 4. Split Array Largest Sum
    public static int splitArray(int[] nums, int k) {
        int lo = 0, hi = 0;
        for (int n : nums) { lo = Math.max(lo, n); hi += n; }
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (canSplit(nums, k, mid)) hi = mid;
            else lo = mid + 1;
        }
        return lo;
    }
    private static boolean canSplit(int[] nums, int k, int maxSum) {
        int count = 1, currSum = 0;
        for (int n : nums) {
            if (currSum + n > maxSum) { count++; currSum = 0; }
            currSum += n;
        }
        return count <= k;
    }

    // 5. Find element in 2D sorted matrix
    public static boolean searchMatrix(int[][] matrix, int target) {
        int m = matrix.length, n = matrix[0].length;
        int lo = 0, hi = m * n - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            int val = matrix[mid / n][mid % n];
            if (val == target) return true;
            if (val < target) lo = mid + 1;
            else hi = mid - 1;
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println(minEatingSpeed(new int[]{3,6,7,11}, 8));  // 4
        System.out.println(mySqrt(8));  // 2
        System.out.println(splitArray(new int[]{7,2,5,10,8}, 2));    // 18
        System.out.println(searchMatrix(new int[][]{{1,3,5,7},{10,11,16,20},{23,30,34,60}}, 3)); // true
    }
}
