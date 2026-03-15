package algorithms.graphs;

import java.util.*;

/**
 * MST Algorithms in Java
 * Kruskal: sort edges + Union-Find = O(E log E)
 * Prim:    greedy from vertex + MinHeap  = O(E log V)
 */
public class MinimumSpanningTree {

    // ---- Kruskal with Union-Find ----
    static class UnionFind {
        int[] parent, rank;
        UnionFind(int n) { parent = new int[n]; rank = new int[n]; for(int i=0;i<n;i++) parent[i]=i; }
        int find(int x) { if(parent[x]!=x) parent[x]=find(parent[x]); return parent[x]; }
        boolean union(int x, int y) {
            int px = find(x), py = find(y);
            if (px == py) return false;
            if (rank[px] < rank[py]) { int t=px; px=py; py=t; }
            parent[py] = px;
            if (rank[px] == rank[py]) rank[px]++;
            return true;
        }
    }

    public static int kruskal(int n, int[][] edges) {
        // edges[i] = {u, v, weight}
        Arrays.sort(edges, Comparator.comparingInt(e -> e[2]));
        UnionFind uf = new UnionFind(n);
        int totalCost = 0, edgesUsed = 0;
        for (int[] edge : edges) {
            if (uf.union(edge[0], edge[1])) {
                totalCost += edge[2];
                edgesUsed++;
                if (edgesUsed == n - 1) break;
            }
        }
        return edgesUsed == n - 1 ? totalCost : -1; // -1 if disconnected
    }

    // ---- Prim's Algorithm ----
    public static int prim(int n, List<int[]>[] adj) {
        boolean[] inMST = new boolean[n];
        PriorityQueue<int[]> heap = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        heap.offer(new int[]{0, 0}); // {cost, node}
        int totalCost = 0;
        while (!heap.isEmpty()) {
            int[] curr = heap.poll();
            int cost = curr[0], u = curr[1];
            if (inMST[u]) continue;
            inMST[u] = true;
            totalCost += cost;
            for (int[] edge : adj[u]) {
                if (!inMST[edge[0]]) heap.offer(new int[]{edge[1], edge[0]});
            }
        }
        return totalCost;
    }

    // ---- Min Cost to Connect All Points (Prim application) ----
    public static int minCostConnectPoints(int[][] points) {
        int n = points.length;
        boolean[] inMST = new boolean[n];
        int[] minCost = new int[n];
        Arrays.fill(minCost, Integer.MAX_VALUE);
        minCost[0] = 0;
        PriorityQueue<int[]> heap = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        heap.offer(new int[]{0, 0});
        int total = 0;
        while (!heap.isEmpty()) {
            int[] curr = heap.poll();
            int cost = curr[0], u = curr[1];
            if (inMST[u]) continue;
            inMST[u] = true; total += cost;
            for (int v = 0; v < n; v++) {
                if (!inMST[v]) {
                    int d = Math.abs(points[u][0]-points[v][0]) + Math.abs(points[u][1]-points[v][1]);
                    if (d < minCost[v]) { minCost[v] = d; heap.offer(new int[]{d, v}); }
                }
            }
        }
        return total;
    }

    public static void main(String[] args) {
        int[][] edges = {{0,1,10},{0,2,6},{0,3,5},{1,3,15},{2,3,4}};
        System.out.println(kruskal(4, edges)); // 19
        int[][] points = {{0,0},{2,2},{3,10},{5,2},{7,0}};
        System.out.println(minCostConnectPoints(points)); // 20
    }
}
