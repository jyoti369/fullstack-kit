package algorithms.searching;

import java.util.*;

/**
 * Interval Manipulation Problems in Java
 */
public class IntervalProblems {

    // 1. Merge Intervals
    public static int[][] merge(int[][] intervals) {
        Arrays.sort(intervals, Comparator.comparingInt(a -> a[0]));
        List<int[]> merged = new ArrayList<>();
        for (int[] curr : intervals) {
            if (merged.isEmpty() || merged.get(merged.size()-1)[1] < curr[0])
                merged.add(curr);
            else merged.get(merged.size()-1)[1] = Math.max(merged.get(merged.size()-1)[1], curr[1]);
        }
        return merged.toArray(new int[0][]);
    }

    // 2. Insert Interval
    public static int[][] insert(int[][] intervals, int[] newInterval) {
        List<int[]> result = new ArrayList<>();
        int i = 0, n = intervals.length;
        while (i < n && intervals[i][1] < newInterval[0]) result.add(intervals[i++]);
        while (i < n && intervals[i][0] <= newInterval[1]) {
            newInterval[0] = Math.min(newInterval[0], intervals[i][0]);
            newInterval[1] = Math.max(newInterval[1], intervals[i][1]);
            i++;
        }
        result.add(newInterval);
        while (i < n) result.add(intervals[i++]);
        return result.toArray(new int[0][]);
    }

    // 3. Non-overlapping Intervals (min removals)
    public static int eraseOverlapIntervals(int[][] intervals) {
        if (intervals.length == 0) return 0;
        Arrays.sort(intervals, Comparator.comparingInt(a -> a[1]));
        int count = 0, prevEnd = intervals[0][1];
        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] < prevEnd) count++;
            else prevEnd = intervals[i][1];
        }
        return count;
    }

    // 4. Meeting Rooms II (min rooms needed)
    public static int minMeetingRooms(int[][] intervals) {
        int n = intervals.length;
        int[] starts = new int[n], ends = new int[n];
        for (int i = 0; i < n; i++) { starts[i] = intervals[i][0]; ends[i] = intervals[i][1]; }
        Arrays.sort(starts); Arrays.sort(ends);
        int rooms = 0, ptr = 0;
        for (int start : starts) {
            if (start < ends[ptr]) rooms++;
            else ptr++;
        }
        return rooms;
    }

    // 5. Employee Free Time (merged gaps between all intervals)
    public static List<int[]> employeeFreeTime(List<List<int[]>> schedule) {
        List<int[]> all = new ArrayList<>();
        for (List<int[]> emp : schedule) all.addAll(emp);
        all.sort(Comparator.comparingInt(a -> a[0]));
        List<int[]> result = new ArrayList<>();
        int[] prev = all.get(0);
        for (int[] curr : all) {
            if (curr[0] > prev[1]) { result.add(new int[]{prev[1], curr[0]}); prev = curr; }
            else prev[1] = Math.max(prev[1], curr[1]);
        }
        return result;
    }

    // 6. Interval List Intersections
    public static int[][] intervalIntersection(int[][] A, int[][] B) {
        List<int[]> result = new ArrayList<>();
        int i = 0, j = 0;
        while (i < A.length && j < B.length) {
            int lo = Math.max(A[i][0], B[j][0]);
            int hi = Math.min(A[i][1], B[j][1]);
            if (lo <= hi) result.add(new int[]{lo, hi});
            if (A[i][1] < B[j][1]) i++; else j++;
        }
        return result.toArray(new int[0][]);
    }

    public static void main(String[] args) {
        System.out.println(Arrays.deepToString(merge(new int[][]{{1,3},{2,6},{8,10},{15,18}})));
        System.out.println(eraseOverlapIntervals(new int[][]{{1,2},{2,3},{3,4},{1,3}})); // 1
        System.out.println(minMeetingRooms(new int[][]{{0,30},{5,10},{15,20}})); // 2
    }
}


package algorithms.searching;

import java.util.*;

/**
 * Interval Manipulation Problems in Java
 */
public class IntervalProblems {

    // 1. Merge Intervals
    public static int[][] merge(int[][] intervals) {
        Arrays.sort(intervals, Comparator.comparingInt(a -> a[0]));
        List<int[]> merged = new ArrayList<>();
        for (int[] curr : intervals) {
            if (merged.isEmpty() || merged.get(merged.size()-1)[1] < curr[0])
                merged.add(curr);
            else merged.get(merged.size()-1)[1] = Math.max(merged.get(merged.size()-1)[1], curr[1]);
        }
        return merged.toArray(new int[0][]);
    }

    // 2. Insert Interval
    public static int[][] insert(int[][] intervals, int[] newInterval) {
        List<int[]> result = new ArrayList<>();
        int i = 0, n = intervals.length;
        while (i < n && intervals[i][1] < newInterval[0]) result.add(intervals[i++]);
        while (i < n && intervals[i][0] <= newInterval[1]) {
            newInterval[0] = Math.min(newInterval[0], intervals[i][0]);
            newInterval[1] = Math.max(newInterval[1], intervals[i][1]);
            i++;
        }
        result.add(newInterval);
        while (i < n) result.add(intervals[i++]);
        return result.toArray(new int[0][]);
    }

    // 3. Non-overlapping Intervals (min removals)
    public static int eraseOverlapIntervals(int[][] intervals) {
        if (intervals.length == 0) return 0;
        Arrays.sort(intervals, Comparator.comparingInt(a -> a[1]));
        int count = 0, prevEnd = intervals[0][1];
        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] < prevEnd) count++;
            else prevEnd = intervals[i][1];
        }
        return count;
    }

    // 4. Meeting Rooms II (min rooms needed)
    public static int minMeetingRooms(int[][] intervals) {
        int n = intervals.length;
        int[] starts = new int[n], ends = new int[n];
        for (int i = 0; i < n; i++) { starts[i] = intervals[i][0]; ends[i] = intervals[i][1]; }
        Arrays.sort(starts); Arrays.sort(ends);
        int rooms = 0, ptr = 0;
        for (int start : starts) {
            if (start < ends[ptr]) rooms++;
            else ptr++;
        }
        return rooms;
    }

    // 5. Employee Free Time (merged gaps between all intervals)
    public static List<int[]> employeeFreeTime(List<List<int[]>> schedule) {
        List<int[]> all = new ArrayList<>();
        for (List<int[]> emp : schedule) all.addAll(emp);
        all.sort(Comparator.comparingInt(a -> a[0]));
        List<int[]> result = new ArrayList<>();
        int[] prev = all.get(0);
        for (int[] curr : all) {
            if (curr[0] > prev[1]) { result.add(new int[]{prev[1], curr[0]}); prev = curr; }
            else prev[1] = Math.max(prev[1], curr[1]);
        }
        return result;
    }

    // 6. Interval List Intersections
    public static int[][] intervalIntersection(int[][] A, int[][] B) {
        List<int[]> result = new ArrayList<>();
        int i = 0, j = 0;
        while (i < A.length && j < B.length) {
            int lo = Math.max(A[i][0], B[j][0]);
            int hi = Math.min(A[i][1], B[j][1]);
            if (lo <= hi) result.add(new int[]{lo, hi});
            if (A[i][1] < B[j][1]) i++; else j++;
        }
        return result.toArray(new int[0][]);
    }

    public static void main(String[] args) {
        System.out.println(Arrays.deepToString(merge(new int[][]{{1,3},{2,6},{8,10},{15,18}})));
        System.out.println(eraseOverlapIntervals(new int[][]{{1,2},{2,3},{3,4},{1,3}})); // 1
        System.out.println(minMeetingRooms(new int[][]{{0,30},{5,10},{15,20}})); // 2
    }
}
