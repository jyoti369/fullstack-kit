package algorithms.linkedlist;

/**
 * Linked List Core Operations — Java
 */
public class LinkedListOperations {

    static class ListNode {
        int val; ListNode next;
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

    // Reverse a linked list — O(n) time, O(1) space
    public static ListNode reverseList(ListNode head) {
        ListNode prev = null, curr = head;
        while (curr != null) {
            ListNode next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }
        return prev;
    }

    // Recursive reverse
    public static ListNode reverseRecursive(ListNode head) {
        if (head == null || head.next == null) return head;
        ListNode newHead = reverseRecursive(head.next);
        head.next.next = head;
        head.next = null;
        return newHead;
    }

    // Floyd's cycle detection — O(n) time, O(1) space
    public static boolean hasCycle(ListNode head) {
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) return true;
        }
        return false;
    }

    // Find middle node (slow/fast pointers)
    public static ListNode findMiddle(ListNode head) {
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

    // Merge two sorted linked lists — O(n+m)
    public static ListNode mergeTwoSorted(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(0), curr = dummy;
        while (l1 != null && l2 != null) {
            if (l1.val <= l2.val) { curr.next = l1; l1 = l1.next; }
            else                  { curr.next = l2; l2 = l2.next; }
            curr = curr.next;
        }
        curr.next = l1 != null ? l1 : l2;
        return dummy.next;
    }

    // Remove nth node from end — O(n) one-pass
    public static ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode fast = dummy, slow = dummy;
        for (int i = 0; i <= n; i++) fast = fast.next;
        while (fast != null) { slow = slow.next; fast = fast.next; }
        slow.next = slow.next.next;
        return dummy.next;
    }

    // Rotate list by k places
    public static ListNode rotateRight(ListNode head, int k) {
        if (head == null || head.next == null || k == 0) return head;
        int len = 1;
        ListNode tail = head;
        while (tail.next != null) { tail = tail.next; len++; }
        tail.next = head; // make circular
        k = k % len;
        int stepsToNewTail = len - k;
        ListNode newTail = head;
        for (int i = 1; i < stepsToNewTail; i++) newTail = newTail.next;
        ListNode newHead = newTail.next;
        newTail.next = null;
        return newHead;
    }

    // Helper: build list from array
    static ListNode of(int... vals) {
        ListNode dummy = new ListNode(0), curr = dummy;
        for (int v : vals) { curr.next = new ListNode(v); curr = curr.next; }
        return dummy.next;
    }
    // Helper: list to string
    static String print(ListNode head) {
        StringBuilder sb = new StringBuilder();
        while (head != null) { sb.append(head.val); if (head.next != null) sb.append(" -> "); head = head.next; }
        return sb.toString();
    }

    public static void main(String[] args) {
        ListNode list = of(1, 2, 3, 4, 5);
        System.out.println("Original: " + print(list));               // 1 -> 2 -> 3 -> 4 -> 5
        System.out.println("Middle:   " + findMiddle(list).val);      // 3
        System.out.println("Reversed: " + print(reverseList(list)));  // 5 -> 4 -> 3 -> 2 -> 1

        ListNode l1 = of(1, 3, 5), l2 = of(2, 4, 6);
        System.out.println("Merged:   " + print(mergeTwoSorted(l1, l2))); // 1 -> 2 -> 3 -> 4 -> 5 -> 6

        System.out.println("hasCycle: " + hasCycle(of(1, 2, 3)));  // false
    }
}
