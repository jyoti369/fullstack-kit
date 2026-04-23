package algorithms.searching;

/**
 * Floyd's Cycle Detection — Fast/Slow Pointers
 */
public class FastSlowPointers {

    static class ListNode { int val; ListNode next; ListNode(int v){ val=v; } }

    // 1. Detect cycle in linked list
    public static boolean hasCycle(ListNode head) {
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) return true;
        }
        return false;
    }

    // 2. Find start of cycle
    public static ListNode detectCycle(ListNode head) {
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next; fast = fast.next.next;
            if (slow == fast) {
                fast = head;
                while (slow != fast) { slow = slow.next; fast = fast.next; }
                return slow;
            }
        }
        return null;
    }

    // 3. Middle of linked list
    public static ListNode middleNode(ListNode head) {
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next; fast = fast.next.next;
        }
        return slow;
    }

    // 4. Happy Number (cycle detection on integers)
    public static boolean isHappy(int n) {
        int slow = n, fast = digitSquareSum(n);
        while (fast != 1 && slow != fast) {
            slow = digitSquareSum(slow);
            fast = digitSquareSum(digitSquareSum(fast));
        }
        return fast == 1;
    }
    private static int digitSquareSum(int n) {
        int sum = 0;
        while (n > 0) { int d = n % 10; sum += d*d; n /= 10; }
        return sum;
    }

    // 5. Find the Duplicate Number (Floyd's in array)
    public static int findDuplicate(int[] nums) {
        int slow = nums[0], fast = nums[0];
        do { slow = nums[slow]; fast = nums[nums[fast]]; } while (slow != fast);
        fast = nums[0];
        while (slow != fast) { slow = nums[slow]; fast = nums[fast]; }
        return slow;
    }

    // 6. Palindrome Linked List (slow/fast + reverse)
    public static boolean isPalindrome(ListNode head) {
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next; fast = fast.next.next;
        }
        // Reverse second half
        ListNode prev = null, curr = slow;
        while (curr != null) { ListNode next = curr.next; curr.next = prev; prev = curr; curr = next; }
        // Compare
        ListNode left = head, right = prev;
        while (right != null) {
            if (left.val != right.val) return false;
            left = left.next; right = right.next;
        }
        return true;
    }

    public static void main(String[] args) {
        // Build: 3 -> 2 -> 0 -> 4 -> (back to node 2)
        ListNode head = new ListNode(3);
        head.next = new ListNode(2);
        head.next.next = new ListNode(0);
        head.next.next.next = new ListNode(4);
        head.next.next.next.next = head.next; // cycle
        System.out.println(hasCycle(head));  // true
        System.out.println(isHappy(19));     // true
        System.out.println(findDuplicate(new int[]{1,3,4,2,2})); // 2
    }
}


package algorithms.searching;

/**
 * Floyd's Cycle Detection — Fast/Slow Pointers
 */
public class FastSlowPointers {

    static class ListNode { int val; ListNode next; ListNode(int v){ val=v; } }

    // 1. Detect cycle in linked list
    public static boolean hasCycle(ListNode head) {
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) return true;
        }
        return false;
    }

    // 2. Find start of cycle
    public static ListNode detectCycle(ListNode head) {
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next; fast = fast.next.next;
            if (slow == fast) {
                fast = head;
                while (slow != fast) { slow = slow.next; fast = fast.next; }
                return slow;
            }
        }
        return null;
    }

    // 3. Middle of linked list
    public static ListNode middleNode(ListNode head) {
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next; fast = fast.next.next;
        }
        return slow;
    }

    // 4. Happy Number (cycle detection on integers)
    public static boolean isHappy(int n) {
        int slow = n, fast = digitSquareSum(n);
        while (fast != 1 && slow != fast) {
            slow = digitSquareSum(slow);
            fast = digitSquareSum(digitSquareSum(fast));
        }
        return fast == 1;
    }
    private static int digitSquareSum(int n) {
        int sum = 0;
        while (n > 0) { int d = n % 10; sum += d*d; n /= 10; }
        return sum;
    }

    // 5. Find the Duplicate Number (Floyd's in array)
    public static int findDuplicate(int[] nums) {
        int slow = nums[0], fast = nums[0];
        do { slow = nums[slow]; fast = nums[nums[fast]]; } while (slow != fast);
        fast = nums[0];
        while (slow != fast) { slow = nums[slow]; fast = nums[fast]; }
        return slow;
    }

    // 6. Palindrome Linked List (slow/fast + reverse)
    public static boolean isPalindrome(ListNode head) {
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next; fast = fast.next.next;
        }
        // Reverse second half
        ListNode prev = null, curr = slow;
        while (curr != null) { ListNode next = curr.next; curr.next = prev; prev = curr; curr = next; }
        // Compare
        ListNode left = head, right = prev;
        while (right != null) {
            if (left.val != right.val) return false;
            left = left.next; right = right.next;
        }
        return true;
    }

    public static void main(String[] args) {
        // Build: 3 -> 2 -> 0 -> 4 -> (back to node 2)
        ListNode head = new ListNode(3);
        head.next = new ListNode(2);
        head.next.next = new ListNode(0);
        head.next.next.next = new ListNode(4);
        head.next.next.next.next = head.next; // cycle
        System.out.println(hasCycle(head));  // true
        System.out.println(isHappy(19));     // true
        System.out.println(findDuplicate(new int[]{1,3,4,2,2})); // 2
    }
}
