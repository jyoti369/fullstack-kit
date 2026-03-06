package algorithms.searching;

import java.util.*;

/**
 * K-Way Merge Patterns — Java
 */
public class KWayMerge {

    // 1. Merge K sorted lists
    static class ListNode { int val; ListNode next; ListNode(int v){val=v;} }

    public static ListNode mergeKLists(ListNode[] lists) {
        PriorityQueue<ListNode> heap = new PriorityQueue<>(Comparator.comparingInt(n -> n.val));
        for (ListNode list : lists) if (list != null) heap.offer(list);
        ListNode dummy = new ListNode(0), curr = dummy;
        while (!heap.isEmpty()) {
            curr.next = heap.poll();
            curr = curr.next;
            if (curr.next != null) heap.offer(curr.next);
        }
        return dummy.next;
    }

    // 2. Find K-th smallest in N sorted arrays
    public static int kthSmallest(int[][] arrays, int k) {
        // {value, arrayIdx, elemIdx}
        PriorityQueue<int[]> heap = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        for (int i = 0; i < arrays.length; i++)
            if (arrays[i].length > 0) heap.offer(new int[]{arrays[i][0], i, 0});
        int count = 0, result = 0;
        while (!heap.isEmpty()) {
            int[] curr = heap.poll();
            result = curr[0]; count++;
            if (count == k) return result;
            int arrIdx = curr[1], elemIdx = curr[2] + 1;
            if (elemIdx < arrays[arrIdx].length)
                heap.offer(new int[]{arrays[arrIdx][elemIdx], arrIdx, elemIdx});
        }
        return result;
    }

    // 3. Kth smallest in sorted matrix
    public static int kthSmallestMatrix(int[][] matrix, int k) {
        int n = matrix.length;
        PriorityQueue<int[]> heap = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        for (int i = 0; i < n; i++) heap.offer(new int[]{matrix[i][0], i, 0});
        int result = 0;
        for (int count = 0; count < k; count++) {
            int[] curr = heap.poll(); result = curr[0];
            int row = curr[1], col = curr[2] + 1;
            if (col < n) heap.offer(new int[]{matrix[row][col], row, col});
        }
        return result;
    }

    // 4. Smallest Range Covering Elements from K Lists
    public static int[] smallestRange(List<List<Integer>> nums) {
        PriorityQueue<int[]> heap = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < nums.size(); i++) {
            heap.offer(new int[]{nums.get(i).get(0), i, 0});
            max = Math.max(max, nums.get(i).get(0));
        }
        int rangeStart = 0, rangeEnd = Integer.MAX_VALUE;
        while (heap.size() == nums.size()) {
            int[] curr = heap.poll();
            if (max - curr[0] < rangeEnd - rangeStart) { rangeStart = curr[0]; rangeEnd = max; }
            int nextIdx = curr[2] + 1;
            if (nextIdx < nums.get(curr[1]).size()) {
                int next = nums.get(curr[1]).get(nextIdx);
                heap.offer(new int[]{next, curr[1], nextIdx});
                max = Math.max(max, next);
            }
        }
        return new int[]{rangeStart, rangeEnd};
    }

    // 5. Meeting Rooms III (K rooms, assign meetings)
    public static int mostBooked(int n, int[][] meetings) {
        Arrays.sort(meetings, Comparator.comparingInt(a -> a[0]));
        PriorityQueue<Long> freeRooms = new PriorityQueue<>();
        PriorityQueue<long[]> busyRooms = new PriorityQueue<>(Comparator.comparingLong(a -> a[0]));
        for (int i = 0; i < n; i++) freeRooms.offer((long)i);
        int[] count = new int[n];
        for (int[] m : meetings) {
            while (!busyRooms.isEmpty() && busyRooms.peek()[0] <= m[0])
                freeRooms.offer(busyRooms.poll()[1]);
            long room, end;
            if (!freeRooms.isEmpty()) { room = freeRooms.poll(); end = m[1]; }
            else { long[] earliest = busyRooms.poll(); end = earliest[0] + m[1]-m[0]; room = earliest[1]; }
            busyRooms.offer(new long[]{end, room}); count[(int)room]++;
        }
        int max = 0, ans = 0;
        for (int i = 0; i < n; i++) if (count[i] > max) { max = count[i]; ans = i; }
        return ans;
    }

    public static void main(String[] args) {
        System.out.println(kthSmallest(new int[][]{{1,5,7},{2,3,8},{4,6,9}}, 5)); // 5
        System.out.println(kthSmallestMatrix(new int[][]{{1,5,9},{10,11,13},{12,13,15}}, 8)); // 13
    }
}
