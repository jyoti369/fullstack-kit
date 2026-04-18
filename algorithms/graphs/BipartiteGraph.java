package algorithms.graphs;

import java.util.*;

/**
 * Graph Coloring and Bipartite Check — Java
 */
public class BipartiteGraph {

    // 1. Check if graph is bipartite (BFS)
    public static boolean isBipartite(int[][] graph) {
        int n = graph.length;
        int[] color = new int[n];
        Arrays.fill(color, -1);
        for (int start = 0; start < n; start++) {
            if (color[start] != -1) continue;
            Queue<Integer> queue = new LinkedList<>();
            queue.offer(start); color[start] = 0;
            while (!queue.isEmpty()) {
                int u = queue.poll();
                for (int v : graph[u]) {
                    if (color[v] == -1) { color[v] = 1 - color[u]; queue.offer(v); }
                    else if (color[v] == color[u]) return false;
                }
            }
        }
        return true;
    }

    // 2. Possible Bipartition (divide n people)
    public static boolean possibleBipartition(int n, int[][] dislikes) {
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i <= n; i++) adj.add(new ArrayList<>());
        for (int[] d : dislikes) { adj.get(d[0]).add(d[1]); adj.get(d[1]).add(d[0]); }
        int[] color = new int[n + 1];
        Arrays.fill(color, -1);
        for (int i = 1; i <= n; i++) {
            if (color[i] != -1) continue;
            Queue<Integer> q = new LinkedList<>(); q.offer(i); color[i] = 0;
            while (!q.isEmpty()) {
                int u = q.poll();
                for (int v : adj.get(u)) {
                    if (color[v] == -1) { color[v] = 1 - color[u]; q.offer(v); }
                    else if (color[v] == color[u]) return false;
                }
            }
        }
        return true;
    }

    // 3. M-Coloring (backtracking)
    public static boolean mColoring(int[][] graph, int m) {
        int n = graph.length;
        int[] colors = new int[n];
        return solve(graph, colors, 0, m);
    }
    private static boolean solve(int[][] graph, int[] colors, int node, int m) {
        if (node == graph.length) return true;
        for (int c = 1; c <= m; c++) {
            if (isSafe(graph, colors, node, c)) {
                colors[node] = c;
                if (solve(graph, colors, node+1, m)) return true;
                colors[node] = 0;
            }
        }
        return false;
    }
    private static boolean isSafe(int[][] graph, int[] colors, int node, int c) {
        for (int i = 0; i < graph.length; i++)
            if (graph[node][i] == 1 && colors[i] == c) return false;
        return true;
    }

    // 4. Max Bipartite Matching (Hopcroft-Karp helper)
    static class MaxMatching {
        int[][] adj; int n, m;
        int[] matchL, matchR;
        MaxMatching(int n, int m, int[][] adj) { this.n=n; this.m=m; this.adj=adj; matchL=new int[n]; matchR=new int[m]; Arrays.fill(matchL,-1); Arrays.fill(matchR,-1); }
        boolean dfs(int u, boolean[] visited) {
            for (int v : adj[u]) {
                if (!visited[v]) {
                    visited[v] = true;
                    if (matchR[v]==-1 || dfs(matchR[v], visited)) { matchL[u]=v; matchR[v]=u; return true; }
                }
            }
            return false;
        }
        int maxMatching() {
            int result = 0;
            for (int u = 0; u < n; u++) {
                boolean[] visited = new boolean[m];
                if (dfs(u, visited)) result++;
            }
            return result;
        }
    }

    public static void main(String[] args) {
        System.out.println(isBipartite(new int[][]{{1,3},{0,2},{1,3},{0,2}})); // true
        System.out.println(isBipartite(new int[][]{{1,2,3},{0,2},{0,1,3},{0,2}})); // false
        System.out.println(possibleBipartition(4, new int[][]{{1,2},{1,3},{2,4}})); // true
        System.out.println(mColoring(new int[][]{{0,1,1,1},{1,0,1,0},{1,1,0,1},{1,0,1,0}}, 3)); // true
    }
}


package algorithms.graphs;

import java.util.*;

/**
 * Graph Coloring and Bipartite Check — Java
 */
public class BipartiteGraph {

    // 1. Check if graph is bipartite (BFS)
    public static boolean isBipartite(int[][] graph) {
        int n = graph.length;
        int[] color = new int[n];
        Arrays.fill(color, -1);
        for (int start = 0; start < n; start++) {
            if (color[start] != -1) continue;
            Queue<Integer> queue = new LinkedList<>();
            queue.offer(start); color[start] = 0;
            while (!queue.isEmpty()) {
                int u = queue.poll();
                for (int v : graph[u]) {
                    if (color[v] == -1) { color[v] = 1 - color[u]; queue.offer(v); }
                    else if (color[v] == color[u]) return false;
                }
            }
        }
        return true;
    }

    // 2. Possible Bipartition (divide n people)
    public static boolean possibleBipartition(int n, int[][] dislikes) {
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i <= n; i++) adj.add(new ArrayList<>());
        for (int[] d : dislikes) { adj.get(d[0]).add(d[1]); adj.get(d[1]).add(d[0]); }
        int[] color = new int[n + 1];
        Arrays.fill(color, -1);
        for (int i = 1; i <= n; i++) {
            if (color[i] != -1) continue;
            Queue<Integer> q = new LinkedList<>(); q.offer(i); color[i] = 0;
            while (!q.isEmpty()) {
                int u = q.poll();
                for (int v : adj.get(u)) {
                    if (color[v] == -1) { color[v] = 1 - color[u]; q.offer(v); }
                    else if (color[v] == color[u]) return false;
                }
            }
        }
        return true;
    }

    // 3. M-Coloring (backtracking)
    public static boolean mColoring(int[][] graph, int m) {
        int n = graph.length;
        int[] colors = new int[n];
        return solve(graph, colors, 0, m);
    }
    private static boolean solve(int[][] graph, int[] colors, int node, int m) {
        if (node == graph.length) return true;
        for (int c = 1; c <= m; c++) {
            if (isSafe(graph, colors, node, c)) {
                colors[node] = c;
                if (solve(graph, colors, node+1, m)) return true;
                colors[node] = 0;
            }
        }
        return false;
    }
    private static boolean isSafe(int[][] graph, int[] colors, int node, int c) {
        for (int i = 0; i < graph.length; i++)
            if (graph[node][i] == 1 && colors[i] == c) return false;
        return true;
    }

    // 4. Max Bipartite Matching (Hopcroft-Karp helper)
    static class MaxMatching {
        int[][] adj; int n, m;
        int[] matchL, matchR;
        MaxMatching(int n, int m, int[][] adj) { this.n=n; this.m=m; this.adj=adj; matchL=new int[n]; matchR=new int[m]; Arrays.fill(matchL,-1); Arrays.fill(matchR,-1); }
        boolean dfs(int u, boolean[] visited) {
            for (int v : adj[u]) {
                if (!visited[v]) {
                    visited[v] = true;
                    if (matchR[v]==-1 || dfs(matchR[v], visited)) { matchL[u]=v; matchR[v]=u; return true; }
                }
            }
            return false;
        }
        int maxMatching() {
            int result = 0;
            for (int u = 0; u < n; u++) {
                boolean[] visited = new boolean[m];
                if (dfs(u, visited)) result++;
            }
            return result;
        }
    }

    public static void main(String[] args) {
        System.out.println(isBipartite(new int[][]{{1,3},{0,2},{1,3},{0,2}})); // true
        System.out.println(isBipartite(new int[][]{{1,2,3},{0,2},{0,1,3},{0,2}})); // false
        System.out.println(possibleBipartition(4, new int[][]{{1,2},{1,3},{2,4}})); // true
        System.out.println(mColoring(new int[][]{{0,1,1,1},{1,0,1,0},{1,1,0,1},{1,0,1,0}}, 3)); // true
    }
}
