package algorithms.searching;

import java.util.*;

/**
 * Greedy Algorithm Patterns in Java
 */
public class GreedyAlgorithms {

    // 1. Activity Selection (max non-overlapping events)
    public static int activitySelection(int[] start, int[] end) {
        int n = start.length;
        Integer[] idx = new Integer[n];
        for (int i=0;i<n;i++) idx[i]=i;
        Arrays.sort(idx, Comparator.comparingInt(i -> end[i]));
        int count = 1, lastEnd = end[idx[0]];
        for (int i = 1; i < n; i++) {
            if (start[idx[i]] >= lastEnd) { count++; lastEnd = end[idx[i]]; }
        }
        return count;
    }

    // 2. Fractional Knapsack
    public static double fractionalKnapsack(int[] weights, int[] values, int capacity) {
        int n = weights.length;
        double[][] items = new double[n][2];
        for (int i=0;i<n;i++) { items[i][0]=values[i]; items[i][1]=weights[i]; }
        Arrays.sort(items, (a,b) -> Double.compare(b[0]/b[1], a[0]/a[1]));
        double total = 0;
        for (double[] item : items) {
            if (capacity >= item[1]) { total += item[0]; capacity -= item[1]; }
            else { total += item[0] * (capacity / item[1]); break; }
        }
        return total;
    }

    // 3. Jump Game
    public static boolean canJump(int[] nums) {
        int maxReach = 0;
        for (int i = 0; i < nums.length; i++) {
            if (i > maxReach) return false;
            maxReach = Math.max(maxReach, i + nums[i]);
        }
        return true;
    }

    // 4. Gas Station
    public static int canCompleteCircuit(int[] gas, int[] cost) {
        int totalGas = 0, totalCost = 0, tank = 0, start = 0;
        for (int i = 0; i < gas.length; i++) {
            totalGas += gas[i]; totalCost += cost[i];
            tank += gas[i] - cost[i];
            if (tank < 0) { start = i + 1; tank = 0; }
        }
        return totalGas >= totalCost ? start : -1;
    }

    // 5. Minimum number of arrows to burst balloons
    public static int findMinArrowShots(int[][] points) {
        Arrays.sort(points, Comparator.comparingInt(a -> a[1]));
        int arrows = 1, end = points[0][1];
        for (int[] p : points) {
            if (p[0] > end) { arrows++; end = p[1]; }
        }
        return arrows;
    }

    // 6. Assign Cookies
    public static int findContentChildren(int[] g, int[] s) {
        Arrays.sort(g); Arrays.sort(s);
        int child = 0, cookie = 0;
        while (child < g.length && cookie < s.length) {
            if (s[cookie] >= g[child]) child++;
            cookie++;
        }
        return child;
    }

    // 7. Reorganize String (no same adjacent)
    public static String reorganizeString(String s) {
        int[] freq = new int[26];
        for (char c : s.toCharArray()) freq[c-'a']++;
        PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> b[1] - a[1]);
        for (int i = 0; i < 26; i++) if (freq[i] > 0) heap.offer(new int[]{i, freq[i]});
        StringBuilder sb = new StringBuilder();
        while (heap.size() >= 2) {
            int[] a = heap.poll(), b = heap.poll();
            sb.append((char)('a'+a[0])).append((char)('a'+b[0]));
            if (--a[1] > 0) heap.offer(a);
            if (--b[1] > 0) heap.offer(b);
        }
        if (!heap.isEmpty()) {
            int[] last = heap.poll();
            if (last[1] > 1) return ""; // impossible
            sb.append((char)('a'+last[0]));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(activitySelection(new int[]{1,3,0,5,8,5}, new int[]{2,4,6,7,9,9})); // 4
        System.out.println(canJump(new int[]{2,3,1,1,4}));  // true
        System.out.println(findMinArrowShots(new int[][]{{10,16},{2,8},{1,6},{7,12}})); // 2
        System.out.println(reorganizeString("aab")); // aba
    }
}


package algorithms.searching;

import java.util.*;

/**
 * Greedy Algorithm Patterns in Java
 */
public class GreedyAlgorithms {

    // 1. Activity Selection (max non-overlapping events)
    public static int activitySelection(int[] start, int[] end) {
        int n = start.length;
        Integer[] idx = new Integer[n];
        for (int i=0;i<n;i++) idx[i]=i;
        Arrays.sort(idx, Comparator.comparingInt(i -> end[i]));
        int count = 1, lastEnd = end[idx[0]];
        for (int i = 1; i < n; i++) {
            if (start[idx[i]] >= lastEnd) { count++; lastEnd = end[idx[i]]; }
        }
        return count;
    }

    // 2. Fractional Knapsack
    public static double fractionalKnapsack(int[] weights, int[] values, int capacity) {
        int n = weights.length;
        double[][] items = new double[n][2];
        for (int i=0;i<n;i++) { items[i][0]=values[i]; items[i][1]=weights[i]; }
        Arrays.sort(items, (a,b) -> Double.compare(b[0]/b[1], a[0]/a[1]));
        double total = 0;
        for (double[] item : items) {
            if (capacity >= item[1]) { total += item[0]; capacity -= item[1]; }
            else { total += item[0] * (capacity / item[1]); break; }
        }
        return total;
    }

    // 3. Jump Game
    public static boolean canJump(int[] nums) {
        int maxReach = 0;
        for (int i = 0; i < nums.length; i++) {
            if (i > maxReach) return false;
            maxReach = Math.max(maxReach, i + nums[i]);
        }
        return true;
    }

    // 4. Gas Station
    public static int canCompleteCircuit(int[] gas, int[] cost) {
        int totalGas = 0, totalCost = 0, tank = 0, start = 0;
        for (int i = 0; i < gas.length; i++) {
            totalGas += gas[i]; totalCost += cost[i];
            tank += gas[i] - cost[i];
            if (tank < 0) { start = i + 1; tank = 0; }
        }
        return totalGas >= totalCost ? start : -1;
    }

    // 5. Minimum number of arrows to burst balloons
    public static int findMinArrowShots(int[][] points) {
        Arrays.sort(points, Comparator.comparingInt(a -> a[1]));
        int arrows = 1, end = points[0][1];
        for (int[] p : points) {
            if (p[0] > end) { arrows++; end = p[1]; }
        }
        return arrows;
    }

    // 6. Assign Cookies
    public static int findContentChildren(int[] g, int[] s) {
        Arrays.sort(g); Arrays.sort(s);
        int child = 0, cookie = 0;
        while (child < g.length && cookie < s.length) {
            if (s[cookie] >= g[child]) child++;
            cookie++;
        }
        return child;
    }

    // 7. Reorganize String (no same adjacent)
    public static String reorganizeString(String s) {
        int[] freq = new int[26];
        for (char c : s.toCharArray()) freq[c-'a']++;
        PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> b[1] - a[1]);
        for (int i = 0; i < 26; i++) if (freq[i] > 0) heap.offer(new int[]{i, freq[i]});
        StringBuilder sb = new StringBuilder();
        while (heap.size() >= 2) {
            int[] a = heap.poll(), b = heap.poll();
            sb.append((char)('a'+a[0])).append((char)('a'+b[0]));
            if (--a[1] > 0) heap.offer(a);
            if (--b[1] > 0) heap.offer(b);
        }
        if (!heap.isEmpty()) {
            int[] last = heap.poll();
            if (last[1] > 1) return ""; // impossible
            sb.append((char)('a'+last[0]));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(activitySelection(new int[]{1,3,0,5,8,5}, new int[]{2,4,6,7,9,9})); // 4
        System.out.println(canJump(new int[]{2,3,1,1,4}));  // true
        System.out.println(findMinArrowShots(new int[][]{{10,16},{2,8},{1,6},{7,12}})); // 2
        System.out.println(reorganizeString("aab")); // aba
    }
}
