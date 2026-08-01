package algorithms.searching;

/**
 * Binary Search Variants
 * All O(log n) — works on sorted arrays.
 */
public class BinarySearch {

    // 1. Standard binary search
    public static int search(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2; // avoid overflow
            if (nums[mid] == target) return mid;
            if (nums[mid] < target) left = mid + 1;
            else right = mid - 1;
        }
        return -1;
    }

    // 2. Find leftmost (first occurrence)
    public static int searchLeft(int[] nums, int target) {
        int left = 0, right = nums.length - 1, result = -1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] == target) { result = mid; right = mid - 1; }
            else if (nums[mid] < target) left = mid + 1;
            else right = mid - 1;
        }
        return result;
    }

    // 3. Find rightmost (last occurrence)
    public static int searchRight(int[] nums, int target) {
        int left = 0, right = nums.length - 1, result = -1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] == target) { result = mid; left = mid + 1; }
            else if (nums[mid] < target) left = mid + 1;
            else right = mid - 1;
        }
        return result;
    }

    // 4. Search in rotated sorted array
    public static int searchRotated(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] == target) return mid;
            if (nums[left] <= nums[mid]) { // left half sorted
                if (target >= nums[left] && target < nums[mid]) right = mid - 1;
                else left = mid + 1;
            } else { // right half sorted
                if (target > nums[mid] && target <= nums[right]) left = mid + 1;
                else right = mid - 1;
            }
        }
        return -1;
    }

    // 5. Find minimum in rotated sorted array
    public static int findMin(int[] nums) {
        int left = 0, right = nums.length - 1;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] > nums[right]) left = mid + 1;
            else right = mid;
        }
        return nums[left];
    }

    // 6. First bad version
    public static int firstBadVersion(int n) {
        int left = 1, right = n;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (isBadVersion(mid)) right = mid;
            else left = mid + 1;
        }
        return left;
    }

    private static boolean isBadVersion(int v) { return v >= 4; } // mock

    // 7. Peak element
    public static int findPeakElement(int[] nums) {
        int left = 0, right = nums.length - 1;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] > nums[mid + 1]) right = mid;
            else left = mid + 1;
        }
        return left;
    }

    public static void main(String[] args) {
        int[] arr = {1, 3, 3, 5, 7, 9};
        System.out.println(search(arr, 3));      // 1 or 2
        System.out.println(searchLeft(arr, 3));  // 1
        System.out.println(searchRight(arr, 3)); // 2
        int[] rotated = {4,5,6,7,0,1,2};
        System.out.println(searchRotated(rotated, 0)); // 4
        System.out.println(findMin(rotated));           // 0
    }
}
