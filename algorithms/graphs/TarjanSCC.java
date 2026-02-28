package algorithms.graphs;

import java.util.*;

/**
 * Tarjan's Algorithm — Strongly Connected Components + Bridges
 * O(V + E)
 */
public class TarjanSCC {

    private int[] disc, low, comp;
    private boolean[] onStack;
    private Deque<Integer> stack;
    private List<List<Integer>> adj;
    private int timer = 0, sccCount = 0;

    public TarjanSCC(int n, List<List<Integer>> adj) {
        this.adj = adj;
        disc = new int[n]; low = new int[n];
        comp = new int[n];
        onStack = new boolean[n];
        stack = new ArrayDeque<>();
        Arrays.fill(disc, -1);
        for (int i = 0; i < n; i++) if (disc[i] == -1) dfs(i);
    }

    private void dfs(int u) {
        disc[u] = low[u] = timer++;
        stack.push(u); onStack[u] = true;
        for (int v : adj.get(u)) {
            if (disc[v] == -1) { dfs(v); low[u] = Math.min(low[u], low[v]); }
            else if (onStack[v]) low[u] = Math.min(low[u], disc[v]);
        }
        if (low[u] == disc[u]) {
            while (true) {
                int v = stack.pop(); onStack[v] = false; comp[v] = sccCount;
                if (v == u) break;
            }
            sccCount++;
        }
    }

    public int getSCCCount() { return sccCount; }
    public int getComponent(int v) { return comp[v]; }

    // ---- Find Bridges (undirected graph) ----
    static class BridgeFinder {
        private int[] disc, low;
        private boolean[] visited;
        private int timer = 0;
        private List<int[]> bridges;
        private List<List<Integer>> adj;

        public List<int[]> findBridges(int n, List<List<Integer>> adj) {
            this.adj = adj;
            disc = new int[n]; low = new int[n]; visited = new boolean[n];
            bridges = new ArrayList<>();
            for (int i = 0; i < n; i++) if (!visited[i]) dfs(i, -1);
            return bridges;
        }

        private void dfs(int u, int parent) {
            visited[u] = true;
            disc[u] = low[u] = timer++;
            for (int v : adj.get(u)) {
                if (!visited[v]) {
                    dfs(v, u); low[u] = Math.min(low[u], low[v]);
                    if (low[v] > disc[u]) bridges.add(new int[]{u, v}); // bridge!
                } else if (v != parent) low[u] = Math.min(low[u], disc[v]);
            }
        }
    }

    public static void main(String[] args) {
        int n = 5;
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        adj.get(1).add(0); adj.get(0).add(2); adj.get(2).add(1);
        adj.get(0).add(3); adj.get(3).add(4);
        TarjanSCC scc = new TarjanSCC(n, adj);
        System.out.println("SCC count: " + scc.getSCCCount()); // 3
        for (int i = 0; i < n; i++) System.out.println("Node " + i + " → SCC " + scc.getComponent(i));
    }
}
