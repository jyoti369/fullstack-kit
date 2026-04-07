package algorithms.graphs;

import java.util.*;

/**
 * Shortest Path Algorithms in Java
 * Dijkstra: O((V+E) log V) — non-negative weights
 * Bellman-Ford: O(VE) — handles negative weights, detects negative cycles
 */
public class ShortestPath {

    // ---- Dijkstra ----
    public static int[] dijkstra(int n, List<int[]>[] adj, int src) {
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[src] = 0;
        // MinHeap: [distance, node]
        PriorityQueue<int[]> heap = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        heap.offer(new int[]{0, src});
        while (!heap.isEmpty()) {
            int[] curr = heap.poll();
            int d = curr[0], u = curr[1];
            if (d > dist[u]) continue;  // stale entry
            for (int[] edge : adj[u]) {
                int v = edge[0], w = edge[1];
                if (dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                    heap.offer(new int[]{dist[v], v});
                }
            }
        }
        return dist;
    }

    // ---- Bellman-Ford ----
    public static int[] bellmanFord(int n, int[][] edges, int src) {
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[src] = 0;
        for (int i = 0; i < n - 1; i++) {
            for (int[] e : edges) {
                int u = e[0], v = e[1], w = e[2];
                if (dist[u] != Integer.MAX_VALUE && dist[u] + w < dist[v])
                    dist[v] = dist[u] + w;
            }
        }
        // Check negative cycle
        for (int[] e : edges)
            if (dist[e[0]] != Integer.MAX_VALUE && dist[e[0]] + e[2] < dist[e[1]])
                return null; // negative cycle exists
        return dist;
    }

    // ---- Floyd-Warshall (all-pairs shortest path) ----
    public static int[][] floydWarshall(int[][] graph) {
        int n = graph.length;
        int[][] dist = new int[n][n];
        for (int[] row : dist) Arrays.fill(row, Integer.MAX_VALUE / 2);
        for (int i = 0; i < n; i++) {
            dist[i][i] = 0;
            for (int j = 0; j < n; j++)
                if (graph[i][j] != 0) dist[i][j] = graph[i][j];
        }
        for (int k = 0; k < n; k++)
            for (int i = 0; i < n; i++)
                for (int j = 0; j < n; j++)
                    dist[i][j] = Math.min(dist[i][j], dist[i][k] + dist[k][j]);
        return dist;
    }

    // ---- Network Delay Time (Dijkstra application) ----
    public static int networkDelayTime(int[][] times, int n, int k) {
        @SuppressWarnings("unchecked")
        List<int[]>[] adj = new List[n + 1];
        for (int i = 0; i <= n; i++) adj[i] = new ArrayList<>();
        for (int[] t : times) adj[t[0]].add(new int[]{t[1], t[2]});
        int[] dist = dijkstra(n + 1, adj, k);
        int max = 0;
        for (int i = 1; i <= n; i++) {
            if (dist[i] == Integer.MAX_VALUE) return -1;
            max = Math.max(max, dist[i]);
        }
        return max;
    }

    public static void main(String[] args) {
        int n = 5;
        @SuppressWarnings("unchecked")
        List<int[]>[] adj = new List[n];
        for (int i = 0; i < n; i++) adj[i] = new ArrayList<>();
        adj[0].add(new int[]{1,10}); adj[0].add(new int[]{2,3});
        adj[1].add(new int[]{3,2}); adj[2].add(new int[]{1,4});
        adj[2].add(new int[]{3,8}); adj[2].add(new int[]{4,2});
        adj[3].add(new int[]{4,5}); adj[4].add(new int[]{3,1});
        System.out.println(Arrays.toString(dijkstra(n, adj, 0))); // [0,7,3,9,5]
    }
}
