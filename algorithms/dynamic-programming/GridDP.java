package algorithms.dynamic_programming;

import java.util.*;

/**
 * Grid DP Problems in Java
 */
public class GridDP {

    // 1. Minimum Path Sum
    public static int minPathSum(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        int[] dp = new int[n];
        dp[0] = grid[0][0];
        for (int j = 1; j < n; j++) dp[j] = dp[j-1] + grid[0][j];
        for (int i = 1; i < m; i++) {
            dp[0] += grid[i][0];
            for (int j = 1; j < n; j++)
                dp[j] = Math.min(dp[j], dp[j-1]) + grid[i][j];
        }
        return dp[n-1];
    }

    // 2. Number of Islands (DFS on grid)
    public static int numIslands(char[][] grid) {
        int count = 0;
        for (int r = 0; r < grid.length; r++)
            for (int c = 0; c < grid[0].length; c++)
                if (grid[r][c] == '1') { dfs(grid, r, c); count++; }
        return count;
    }
    private static void dfs(char[][] grid, int r, int c) {
        if (r<0||r>=grid.length||c<0||c>=grid[0].length||grid[r][c]!='1') return;
        grid[r][c] = '0';
        dfs(grid,r+1,c); dfs(grid,r-1,c); dfs(grid,r,c+1); dfs(grid,r,c-1);
    }

    // 3. Surrounded Regions
    public static void solve(char[][] board) {
        int m = board.length, n = board[0].length;
        for (int i = 0; i < m; i++) { dfsFlood(board,i,0); dfsFlood(board,i,n-1); }
        for (int j = 0; j < n; j++) { dfsFlood(board,0,j); dfsFlood(board,m-1,j); }
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                board[i][j] = board[i][j]=='S' ? 'O' : (board[i][j]=='O' ? 'X' : board[i][j]);
    }
    private static void dfsFlood(char[][] b, int r, int c) {
        if (r<0||r>=b.length||c<0||c>=b[0].length||b[r][c]!='O') return;
        b[r][c]='S'; dfsFlood(b,r+1,c); dfsFlood(b,r-1,c); dfsFlood(b,r,c+1); dfsFlood(b,r,c-1);
    }

    // 4. Pacific Atlantic Water Flow
    public static List<List<Integer>> pacificAtlantic(int[][] heights) {
        int m = heights.length, n = heights[0].length;
        boolean[][] pacific = new boolean[m][n], atlantic = new boolean[m][n];
        Queue<int[]> pQueue = new LinkedList<>(), aQueue = new LinkedList<>();
        for (int i = 0; i < m; i++) {
            pQueue.offer(new int[]{i,0}); pacific[i][0]=true;
            aQueue.offer(new int[]{i,n-1}); atlantic[i][n-1]=true;
        }
        for (int j = 0; j < n; j++) {
            pQueue.offer(new int[]{0,j}); pacific[0][j]=true;
            aQueue.offer(new int[]{m-1,j}); atlantic[m-1][j]=true;
        }
        bfsFill(heights, pQueue, pacific);
        bfsFill(heights, aQueue, atlantic);
        List<List<Integer>> result = new ArrayList<>();
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                if (pacific[i][j] && atlantic[i][j]) result.add(List.of(i, j));
        return result;
    }
    private static void bfsFill(int[][] h, Queue<int[]> q, boolean[][] visited) {
        int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0}};
        while (!q.isEmpty()) {
            int[] curr = q.poll();
            for (int[] d : dirs) {
                int nr = curr[0]+d[0], nc = curr[1]+d[1];
                if (nr>=0&&nr<h.length&&nc>=0&&nc<h[0].length&&!visited[nr][nc]&&h[nr][nc]>=h[curr[0]][curr[1]]) {
                    visited[nr][nc]=true; q.offer(new int[]{nr,nc});
                }
            }
        }
    }

    // 5. Unique Paths (blocked cells)
    public static int uniquePathsWithObstacles(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        int[] dp = new int[n];
        dp[0] = grid[0][0] == 1 ? 0 : 1;
        for (int j = 1; j < n; j++) dp[j] = grid[0][j] == 1 ? 0 : dp[j-1];
        for (int i = 1; i < m; i++) {
            dp[0] = grid[i][0] == 1 ? 0 : dp[0];
            for (int j = 1; j < n; j++) dp[j] = grid[i][j] == 1 ? 0 : dp[j] + dp[j-1];
        }
        return dp[n-1];
    }

    public static void main(String[] args) {
        System.out.println(minPathSum(new int[][]{{1,3,1},{1,5,1},{4,2,1}})); // 7
        System.out.println(numIslands(new char[][]{
            {'1','1','0','0'},{'1','1','0','0'},{'0','0','1','0'},{'0','0','0','1'}
        })); // 3
        System.out.println(uniquePathsWithObstacles(new int[][]{{0,0,0},{0,1,0},{0,0,0}})); // 2
    }
}


package algorithms.dynamic_programming;

import java.util.*;

/**
 * Grid DP Problems in Java
 */
public class GridDP {

    // 1. Minimum Path Sum
    public static int minPathSum(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        int[] dp = new int[n];
        dp[0] = grid[0][0];
        for (int j = 1; j < n; j++) dp[j] = dp[j-1] + grid[0][j];
        for (int i = 1; i < m; i++) {
            dp[0] += grid[i][0];
            for (int j = 1; j < n; j++)
                dp[j] = Math.min(dp[j], dp[j-1]) + grid[i][j];
        }
        return dp[n-1];
    }

    // 2. Number of Islands (DFS on grid)
    public static int numIslands(char[][] grid) {
        int count = 0;
        for (int r = 0; r < grid.length; r++)
            for (int c = 0; c < grid[0].length; c++)
                if (grid[r][c] == '1') { dfs(grid, r, c); count++; }
        return count;
    }
    private static void dfs(char[][] grid, int r, int c) {
        if (r<0||r>=grid.length||c<0||c>=grid[0].length||grid[r][c]!='1') return;
        grid[r][c] = '0';
        dfs(grid,r+1,c); dfs(grid,r-1,c); dfs(grid,r,c+1); dfs(grid,r,c-1);
    }

    // 3. Surrounded Regions
    public static void solve(char[][] board) {
        int m = board.length, n = board[0].length;
        for (int i = 0; i < m; i++) { dfsFlood(board,i,0); dfsFlood(board,i,n-1); }
        for (int j = 0; j < n; j++) { dfsFlood(board,0,j); dfsFlood(board,m-1,j); }
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                board[i][j] = board[i][j]=='S' ? 'O' : (board[i][j]=='O' ? 'X' : board[i][j]);
    }
    private static void dfsFlood(char[][] b, int r, int c) {
        if (r<0||r>=b.length||c<0||c>=b[0].length||b[r][c]!='O') return;
        b[r][c]='S'; dfsFlood(b,r+1,c); dfsFlood(b,r-1,c); dfsFlood(b,r,c+1); dfsFlood(b,r,c-1);
    }

    // 4. Pacific Atlantic Water Flow
    public static List<List<Integer>> pacificAtlantic(int[][] heights) {
        int m = heights.length, n = heights[0].length;
        boolean[][] pacific = new boolean[m][n], atlantic = new boolean[m][n];
        Queue<int[]> pQueue = new LinkedList<>(), aQueue = new LinkedList<>();
        for (int i = 0; i < m; i++) {
            pQueue.offer(new int[]{i,0}); pacific[i][0]=true;
            aQueue.offer(new int[]{i,n-1}); atlantic[i][n-1]=true;
        }
        for (int j = 0; j < n; j++) {
            pQueue.offer(new int[]{0,j}); pacific[0][j]=true;
            aQueue.offer(new int[]{m-1,j}); atlantic[m-1][j]=true;
        }
        bfsFill(heights, pQueue, pacific);
        bfsFill(heights, aQueue, atlantic);
        List<List<Integer>> result = new ArrayList<>();
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                if (pacific[i][j] && atlantic[i][j]) result.add(List.of(i, j));
        return result;
    }
    private static void bfsFill(int[][] h, Queue<int[]> q, boolean[][] visited) {
        int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0}};
        while (!q.isEmpty()) {
            int[] curr = q.poll();
            for (int[] d : dirs) {
                int nr = curr[0]+d[0], nc = curr[1]+d[1];
                if (nr>=0&&nr<h.length&&nc>=0&&nc<h[0].length&&!visited[nr][nc]&&h[nr][nc]>=h[curr[0]][curr[1]]) {
                    visited[nr][nc]=true; q.offer(new int[]{nr,nc});
                }
            }
        }
    }

    // 5. Unique Paths (blocked cells)
    public static int uniquePathsWithObstacles(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        int[] dp = new int[n];
        dp[0] = grid[0][0] == 1 ? 0 : 1;
        for (int j = 1; j < n; j++) dp[j] = grid[0][j] == 1 ? 0 : dp[j-1];
        for (int i = 1; i < m; i++) {
            dp[0] = grid[i][0] == 1 ? 0 : dp[0];
            for (int j = 1; j < n; j++) dp[j] = grid[i][j] == 1 ? 0 : dp[j] + dp[j-1];
        }
        return dp[n-1];
    }

    public static void main(String[] args) {
        System.out.println(minPathSum(new int[][]{{1,3,1},{1,5,1},{4,2,1}})); // 7
        System.out.println(numIslands(new char[][]{
            {'1','1','0','0'},{'1','1','0','0'},{'0','0','1','0'},{'0','0','0','1'}
        })); // 3
        System.out.println(uniquePathsWithObstacles(new int[][]{{0,0,0},{0,1,0},{0,0,0}})); // 2
    }
}
