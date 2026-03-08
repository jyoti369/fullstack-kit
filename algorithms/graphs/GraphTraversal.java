package algorithms.graphs;

import java.util.*;

/**
 * Graph Traversal — BFS and DFS
 * BFS: shortest path in unweighted graph, level-order exploration
 * DFS: cycle detection, topological sort, connected components
 */
public class GraphTraversal {

    private final int V;
    private final List<List<Integer>> adj;

    public GraphTraversal(int v) {
        this.V = v;
        adj = new ArrayList<>();
        for (int i = 0; i < v; i++) adj.add(new ArrayList<>());
    }

    public void addEdge(int u, int v) { adj.get(u).add(v); }

    // ---- BFS ----
    public List<Integer> bfs(int source) {
        List<Integer> order = new ArrayList<>();
        boolean[] visited = new boolean[V];
        Queue<Integer> queue = new LinkedList<>();
        visited[source] = true;
        queue.offer(source);
        while (!queue.isEmpty()) {
            int node = queue.poll();
            order.add(node);
            for (int neighbor : adj.get(node)) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    queue.offer(neighbor);
                }
            }
        }
        return order;
    }

    // ---- DFS (iterative) ----
    public List<Integer> dfsIterative(int source) {
        List<Integer> order = new ArrayList<>();
        boolean[] visited = new boolean[V];
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(source);
        while (!stack.isEmpty()) {
            int node = stack.pop();
            if (visited[node]) continue;
            visited[node] = true;
            order.add(node);
            for (int neighbor : adj.get(node))
                if (!visited[neighbor]) stack.push(neighbor);
        }
        return order;
    }

    // ---- DFS (recursive) ----
    public void dfsRecursive(int node, boolean[] visited, List<Integer> order) {
        visited[node] = true;
        order.add(node);
        for (int neighbor : adj.get(node))
            if (!visited[neighbor]) dfsRecursive(neighbor, visited, order);
    }

    // ---- Detect Cycle (undirected) ----
    public boolean hasCycleUndirected() {
        boolean[] visited = new boolean[V];
        for (int i = 0; i < V; i++)
            if (!visited[i] && dfsCycle(i, -1, visited)) return true;
        return false;
    }

    private boolean dfsCycle(int node, int parent, boolean[] visited) {
        visited[node] = true;
        for (int neighbor : adj.get(node)) {
            if (!visited[neighbor]) {
                if (dfsCycle(neighbor, node, visited)) return true;
            } else if (neighbor != parent) return true;
        }
        return false;
    }

    // ---- Shortest Path (BFS, unweighted) ----
    public int[] shortestPath(int source) {
        int[] dist = new int[V];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[source] = 0;
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(source);
        while (!queue.isEmpty()) {
            int node = queue.poll();
            for (int neighbor : adj.get(node)) {
                if (dist[neighbor] == Integer.MAX_VALUE) {
                    dist[neighbor] = dist[node] + 1;
                    queue.offer(neighbor);
                }
            }
        }
        return dist;
    }

    // ---- Connected Components ----
    public int countComponents() {
        boolean[] visited = new boolean[V];
        int count = 0;
        for (int i = 0; i < V; i++) {
            if (!visited[i]) {
                dfsRecursive(i, visited, new ArrayList<>());
                count++;
            }
        }
        return count;
    }

    public static void main(String[] args) {
        GraphTraversal g = new GraphTraversal(6);
        g.addEdge(0, 1); g.addEdge(0, 2);
        g.addEdge(1, 3); g.addEdge(2, 4);
        g.addEdge(3, 5);
        System.out.println("BFS: " + g.bfs(0));           // [0,1,2,3,4,5]
        System.out.println("DFS: " + g.dfsIterative(0));  // [0,2,4,1,3,5]
        System.out.println("Dist: " + Arrays.toString(g.shortestPath(0)));
    }
}
