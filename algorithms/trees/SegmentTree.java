package algorithms.trees;

/**
 * Segment Tree — Range Query + Point Update in O(log n)
 * Used for: range sum, range min/max, range GCD queries.
 */
public class SegmentTree {

    private final int[] tree;
    private final int n;

    public SegmentTree(int[] nums) {
        n = nums.length;
        tree = new int[4 * n];
        build(nums, 0, 0, n - 1);
    }

    private void build(int[] nums, int node, int start, int end) {
        if (start == end) { tree[node] = nums[start]; return; }
        int mid = (start + end) / 2;
        build(nums, 2*node+1, start, mid);
        build(nums, 2*node+2, mid+1, end);
        tree[node] = tree[2*node+1] + tree[2*node+2];  // sum
    }

    // Point update
    public void update(int idx, int val) { update(0, 0, n-1, idx, val); }
    private void update(int node, int start, int end, int idx, int val) {
        if (start == end) { tree[node] = val; return; }
        int mid = (start + end) / 2;
        if (idx <= mid) update(2*node+1, start, mid, idx, val);
        else            update(2*node+2, mid+1, end, idx, val);
        tree[node] = tree[2*node+1] + tree[2*node+2];
    }

    // Range query [l, r]
    public int query(int l, int r) { return query(0, 0, n-1, l, r); }
    private int query(int node, int start, int end, int l, int r) {
        if (r < start || end < l) return 0;                     // out of range
        if (l <= start && end <= r) return tree[node];          // fully in range
        int mid = (start + end) / 2;
        return query(2*node+1, start, mid, l, r) + query(2*node+2, mid+1, end, l, r);
    }

    // ---- Segment Tree — Range Min Variant ----
    static class RangeMin {
        private final int[] tree;
        private final int n;
        RangeMin(int[] nums) {
            n = nums.length; tree = new int[4*n];
            build(nums, 0, 0, n-1);
        }
        private void build(int[] nums, int node, int s, int e) {
            if (s == e) { tree[node] = nums[s]; return; }
            int mid = (s+e)/2;
            build(nums, 2*node+1, s, mid); build(nums, 2*node+2, mid+1, e);
            tree[node] = Math.min(tree[2*node+1], tree[2*node+2]);
        }
        public int queryMin(int l, int r) { return query(0, 0, n-1, l, r); }
        private int query(int node, int s, int e, int l, int r) {
            if (r < s || e < l) return Integer.MAX_VALUE;
            if (l <= s && e <= r) return tree[node];
            int mid = (s+e)/2;
            return Math.min(query(2*node+1, s, mid, l, r), query(2*node+2, mid+1, e, l, r));
        }
    }

    public static void main(String[] args) {
        int[] nums = {1, 3, 5, 7, 9, 11};
        SegmentTree st = new SegmentTree(nums);
        System.out.println(st.query(1, 3));  // 3+5+7 = 15
        st.update(1, 10);                    // nums[1] = 10
        System.out.println(st.query(1, 3));  // 10+5+7 = 22

        RangeMin rm = new RangeMin(nums);
        System.out.println(rm.queryMin(0, 4));  // 1
    }
}


package algorithms.trees;

/**
 * Segment Tree — Range Query + Point Update in O(log n)
 * Used for: range sum, range min/max, range GCD queries.
 */
public class SegmentTree {

    private final int[] tree;
    private final int n;

    public SegmentTree(int[] nums) {
        n = nums.length;
        tree = new int[4 * n];
        build(nums, 0, 0, n - 1);
    }

    private void build(int[] nums, int node, int start, int end) {
        if (start == end) { tree[node] = nums[start]; return; }
        int mid = (start + end) / 2;
        build(nums, 2*node+1, start, mid);
        build(nums, 2*node+2, mid+1, end);
        tree[node] = tree[2*node+1] + tree[2*node+2];  // sum
    }

    // Point update
    public void update(int idx, int val) { update(0, 0, n-1, idx, val); }
    private void update(int node, int start, int end, int idx, int val) {
        if (start == end) { tree[node] = val; return; }
        int mid = (start + end) / 2;
        if (idx <= mid) update(2*node+1, start, mid, idx, val);
        else            update(2*node+2, mid+1, end, idx, val);
        tree[node] = tree[2*node+1] + tree[2*node+2];
    }

    // Range query [l, r]
    public int query(int l, int r) { return query(0, 0, n-1, l, r); }
    private int query(int node, int start, int end, int l, int r) {
        if (r < start || end < l) return 0;                     // out of range
        if (l <= start && end <= r) return tree[node];          // fully in range
        int mid = (start + end) / 2;
        return query(2*node+1, start, mid, l, r) + query(2*node+2, mid+1, end, l, r);
    }

    // ---- Segment Tree — Range Min Variant ----
    static class RangeMin {
        private final int[] tree;
        private final int n;
        RangeMin(int[] nums) {
            n = nums.length; tree = new int[4*n];
            build(nums, 0, 0, n-1);
        }
        private void build(int[] nums, int node, int s, int e) {
            if (s == e) { tree[node] = nums[s]; return; }
            int mid = (s+e)/2;
            build(nums, 2*node+1, s, mid); build(nums, 2*node+2, mid+1, e);
            tree[node] = Math.min(tree[2*node+1], tree[2*node+2]);
        }
        public int queryMin(int l, int r) { return query(0, 0, n-1, l, r); }
        private int query(int node, int s, int e, int l, int r) {
            if (r < s || e < l) return Integer.MAX_VALUE;
            if (l <= s && e <= r) return tree[node];
            int mid = (s+e)/2;
            return Math.min(query(2*node+1, s, mid, l, r), query(2*node+2, mid+1, e, l, r));
        }
    }

    public static void main(String[] args) {
        int[] nums = {1, 3, 5, 7, 9, 11};
        SegmentTree st = new SegmentTree(nums);
        System.out.println(st.query(1, 3));  // 3+5+7 = 15
        st.update(1, 10);                    // nums[1] = 10
        System.out.println(st.query(1, 3));  // 10+5+7 = 22

        RangeMin rm = new RangeMin(nums);
        System.out.println(rm.queryMin(0, 4));  // 1
    }
}


package algorithms.trees;

/**
 * Segment Tree — Range Query + Point Update in O(log n)
 * Used for: range sum, range min/max, range GCD queries.
 */
public class SegmentTree {

    private final int[] tree;
    private final int n;

    public SegmentTree(int[] nums) {
        n = nums.length;
        tree = new int[4 * n];
        build(nums, 0, 0, n - 1);
    }

    private void build(int[] nums, int node, int start, int end) {
        if (start == end) { tree[node] = nums[start]; return; }
        int mid = (start + end) / 2;
        build(nums, 2*node+1, start, mid);
        build(nums, 2*node+2, mid+1, end);
        tree[node] = tree[2*node+1] + tree[2*node+2];  // sum
    }

    // Point update
    public void update(int idx, int val) { update(0, 0, n-1, idx, val); }
    private void update(int node, int start, int end, int idx, int val) {
        if (start == end) { tree[node] = val; return; }
        int mid = (start + end) / 2;
        if (idx <= mid) update(2*node+1, start, mid, idx, val);
        else            update(2*node+2, mid+1, end, idx, val);
        tree[node] = tree[2*node+1] + tree[2*node+2];
    }

    // Range query [l, r]
    public int query(int l, int r) { return query(0, 0, n-1, l, r); }
    private int query(int node, int start, int end, int l, int r) {
        if (r < start || end < l) return 0;                     // out of range
        if (l <= start && end <= r) return tree[node];          // fully in range
        int mid = (start + end) / 2;
        return query(2*node+1, start, mid, l, r) + query(2*node+2, mid+1, end, l, r);
    }

    // ---- Segment Tree — Range Min Variant ----
    static class RangeMin {
        private final int[] tree;
        private final int n;
        RangeMin(int[] nums) {
            n = nums.length; tree = new int[4*n];
            build(nums, 0, 0, n-1);
        }
        private void build(int[] nums, int node, int s, int e) {
            if (s == e) { tree[node] = nums[s]; return; }
            int mid = (s+e)/2;
            build(nums, 2*node+1, s, mid); build(nums, 2*node+2, mid+1, e);
            tree[node] = Math.min(tree[2*node+1], tree[2*node+2]);
        }
        public int queryMin(int l, int r) { return query(0, 0, n-1, l, r); }
        private int query(int node, int s, int e, int l, int r) {
            if (r < s || e < l) return Integer.MAX_VALUE;
            if (l <= s && e <= r) return tree[node];
            int mid = (s+e)/2;
            return Math.min(query(2*node+1, s, mid, l, r), query(2*node+2, mid+1, e, l, r));
        }
    }

    public static void main(String[] args) {
        int[] nums = {1, 3, 5, 7, 9, 11};
        SegmentTree st = new SegmentTree(nums);
        System.out.println(st.query(1, 3));  // 3+5+7 = 15
        st.update(1, 10);                    // nums[1] = 10
        System.out.println(st.query(1, 3));  // 10+5+7 = 22

        RangeMin rm = new RangeMin(nums);
        System.out.println(rm.queryMin(0, 4));  // 1
    }
}
