package algorithms.sorting;

import java.util.Arrays;

/**
 * Core Sorting Algorithms
 * QuickSort: O(n log n) avg, O(n²) worst
 * MergeSort: O(n log n) always, O(n) space
 * HeapSort:  O(n log n) always, O(1) space
 */
public class SortingAlgorithms {

    // ---- QuickSort ----
    public static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pivot = partition(arr, low, high);
            quickSort(arr, low, pivot - 1);
            quickSort(arr, pivot + 1, high);
        }
    }

    private static int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (arr[j] <= pivot) {
                i++;
                int tmp = arr[i]; arr[i] = arr[j]; arr[j] = tmp;
            }
        }
        int tmp = arr[i+1]; arr[i+1] = arr[high]; arr[high] = tmp;
        return i + 1;
    }

    // ---- MergeSort ----
    public static void mergeSort(int[] arr, int left, int right) {
        if (left >= right) return;
        int mid = (left + right) / 2;
        mergeSort(arr, left, mid);
        mergeSort(arr, mid + 1, right);
        merge(arr, left, mid, right);
    }

    private static void merge(int[] arr, int left, int mid, int right) {
        int[] temp = Arrays.copyOfRange(arr, left, right + 1);
        int i = 0, j = mid - left + 1, k = left;
        while (i <= mid - left && j <= right - left) {
            if (temp[i] <= temp[j]) arr[k++] = temp[i++];
            else arr[k++] = temp[j++];
        }
        while (i <= mid - left) arr[k++] = temp[i++];
        while (j <= right - left) arr[k++] = temp[j++];
    }

    // ---- HeapSort ----
    public static void heapSort(int[] arr) {
        int n = arr.length;
        for (int i = n / 2 - 1; i >= 0; i--) heapify(arr, n, i);
        for (int i = n - 1; i > 0; i--) {
            int tmp = arr[0]; arr[0] = arr[i]; arr[i] = tmp;
            heapify(arr, i, 0);
        }
    }

    private static void heapify(int[] arr, int n, int i) {
        int largest = i, left = 2*i+1, right = 2*i+2;
        if (left < n && arr[left] > arr[largest]) largest = left;
        if (right < n && arr[right] > arr[largest]) largest = right;
        if (largest != i) {
            int tmp = arr[i]; arr[i] = arr[largest]; arr[largest] = tmp;
            heapify(arr, n, largest);
        }
    }

    // ---- Counting Sort (for integers in range [0, k]) ----
    public static int[] countingSort(int[] arr, int k) {
        int[] count = new int[k + 1];
        for (int x : arr) count[x]++;
        for (int i = 1; i <= k; i++) count[i] += count[i-1];
        int[] out = new int[arr.length];
        for (int i = arr.length - 1; i >= 0; i--)
            out[--count[arr[i]]] = arr[i];
        return out;
    }

    public static void main(String[] args) {
        int[] arr1 = {10,7,8,9,1,5};
        quickSort(arr1, 0, arr1.length - 1);
        System.out.println(Arrays.toString(arr1)); // [1,5,7,8,9,10]

        int[] arr2 = {38,27,43,3,9,82,10};
        mergeSort(arr2, 0, arr2.length - 1);
        System.out.println(Arrays.toString(arr2)); // [3,9,10,27,38,43,82]

        int[] arr3 = {12,11,13,5,6,7};
        heapSort(arr3);
        System.out.println(Arrays.toString(arr3)); // [5,6,7,11,12,13]
    }
}


package algorithms.sorting;

import java.util.Arrays;

/**
 * Core Sorting Algorithms
 * QuickSort: O(n log n) avg, O(n²) worst
 * MergeSort: O(n log n) always, O(n) space
 * HeapSort:  O(n log n) always, O(1) space
 */
public class SortingAlgorithms {

    // ---- QuickSort ----
    public static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pivot = partition(arr, low, high);
            quickSort(arr, low, pivot - 1);
            quickSort(arr, pivot + 1, high);
        }
    }

    private static int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (arr[j] <= pivot) {
                i++;
                int tmp = arr[i]; arr[i] = arr[j]; arr[j] = tmp;
            }
        }
        int tmp = arr[i+1]; arr[i+1] = arr[high]; arr[high] = tmp;
        return i + 1;
    }

    // ---- MergeSort ----
    public static void mergeSort(int[] arr, int left, int right) {
        if (left >= right) return;
        int mid = (left + right) / 2;
        mergeSort(arr, left, mid);
        mergeSort(arr, mid + 1, right);
        merge(arr, left, mid, right);
    }

    private static void merge(int[] arr, int left, int mid, int right) {
        int[] temp = Arrays.copyOfRange(arr, left, right + 1);
        int i = 0, j = mid - left + 1, k = left;
        while (i <= mid - left && j <= right - left) {
            if (temp[i] <= temp[j]) arr[k++] = temp[i++];
            else arr[k++] = temp[j++];
        }
        while (i <= mid - left) arr[k++] = temp[i++];
        while (j <= right - left) arr[k++] = temp[j++];
    }

    // ---- HeapSort ----
    public static void heapSort(int[] arr) {
        int n = arr.length;
        for (int i = n / 2 - 1; i >= 0; i--) heapify(arr, n, i);
        for (int i = n - 1; i > 0; i--) {
            int tmp = arr[0]; arr[0] = arr[i]; arr[i] = tmp;
            heapify(arr, i, 0);
        }
    }

    private static void heapify(int[] arr, int n, int i) {
        int largest = i, left = 2*i+1, right = 2*i+2;
        if (left < n && arr[left] > arr[largest]) largest = left;
        if (right < n && arr[right] > arr[largest]) largest = right;
        if (largest != i) {
            int tmp = arr[i]; arr[i] = arr[largest]; arr[largest] = tmp;
            heapify(arr, n, largest);
        }
    }

    // ---- Counting Sort (for integers in range [0, k]) ----
    public static int[] countingSort(int[] arr, int k) {
        int[] count = new int[k + 1];
        for (int x : arr) count[x]++;
        for (int i = 1; i <= k; i++) count[i] += count[i-1];
        int[] out = new int[arr.length];
        for (int i = arr.length - 1; i >= 0; i--)
            out[--count[arr[i]]] = arr[i];
        return out;
    }

    public static void main(String[] args) {
        int[] arr1 = {10,7,8,9,1,5};
        quickSort(arr1, 0, arr1.length - 1);
        System.out.println(Arrays.toString(arr1)); // [1,5,7,8,9,10]

        int[] arr2 = {38,27,43,3,9,82,10};
        mergeSort(arr2, 0, arr2.length - 1);
        System.out.println(Arrays.toString(arr2)); // [3,9,10,27,38,43,82]

        int[] arr3 = {12,11,13,5,6,7};
        heapSort(arr3);
        System.out.println(Arrays.toString(arr3)); // [5,6,7,11,12,13]
    }
}
