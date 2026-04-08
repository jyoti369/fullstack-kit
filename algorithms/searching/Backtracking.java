package algorithms.searching;

import java.util.*;

/**
 * Backtracking Patterns — Java
 */
public class Backtracking {

    // 1. Subsets
    public static List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        backtrack(result, new ArrayList<>(), nums, 0);
        return result;
    }
    private static void backtrack(List<List<Integer>> res, List<Integer> curr, int[] nums, int start) {
        res.add(new ArrayList<>(curr));
        for (int i = start; i < nums.length; i++) {
            curr.add(nums[i]); backtrack(res, curr, nums, i+1); curr.remove(curr.size()-1);
        }
    }

    // 2. Permutations
    public static List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        boolean[] used = new boolean[nums.length];
        permuteHelper(result, new ArrayList<>(), nums, used);
        return result;
    }
    private static void permuteHelper(List<List<Integer>> res, List<Integer> curr, int[] nums, boolean[] used) {
        if (curr.size() == nums.length) { res.add(new ArrayList<>(curr)); return; }
        for (int i = 0; i < nums.length; i++) {
            if (used[i]) continue;
            used[i] = true; curr.add(nums[i]);
            permuteHelper(res, curr, nums, used);
            used[i] = false; curr.remove(curr.size()-1);
        }
    }

    // 3. Combination Sum
    public static List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(candidates);
        combineHelper(result, new ArrayList<>(), candidates, target, 0);
        return result;
    }
    private static void combineHelper(List<List<Integer>> res, List<Integer> curr, int[] cands, int remain, int start) {
        if (remain == 0) { res.add(new ArrayList<>(curr)); return; }
        for (int i = start; i < cands.length; i++) {
            if (cands[i] > remain) break;
            curr.add(cands[i]); combineHelper(res, curr, cands, remain - cands[i], i); curr.remove(curr.size()-1);
        }
    }

    // 4. N-Queens
    public static List<List<String>> solveNQueens(int n) {
        List<List<String>> result = new ArrayList<>();
        int[] queens = new int[n];
        Arrays.fill(queens, -1);
        Set<Integer> cols = new HashSet<>(), diag1 = new HashSet<>(), diag2 = new HashSet<>();
        queensHelper(result, queens, n, 0, cols, diag1, diag2);
        return result;
    }
    private static void queensHelper(List<List<String>> res, int[] queens, int n, int row,
            Set<Integer> cols, Set<Integer> diag1, Set<Integer> diag2) {
        if (row == n) {
            List<String> board = new ArrayList<>();
            for (int q : queens) {
                char[] line = new char[n]; Arrays.fill(line, '.'); line[q] = 'Q';
                board.add(new String(line));
            }
            res.add(board); return;
        }
        for (int col = 0; col < n; col++) {
            if (cols.contains(col) || diag1.contains(row-col) || diag2.contains(row+col)) continue;
            queens[row] = col; cols.add(col); diag1.add(row-col); diag2.add(row+col);
            queensHelper(res, queens, n, row+1, cols, diag1, diag2);
            cols.remove(col); diag1.remove(row-col); diag2.remove(row+col);
        }
    }

    public static void main(String[] args) {
        System.out.println(subsets(new int[]{1,2,3}));
        System.out.println(permute(new int[]{1,2,3}));
        System.out.println(combinationSum(new int[]{2,3,6,7}, 7)); // [[2,2,3],[7]]
        System.out.println(solveNQueens(4).size()); // 2
    }
}
