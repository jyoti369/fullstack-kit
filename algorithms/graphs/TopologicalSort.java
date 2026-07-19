package algorithms.graphs;

import java.util.*;

/**
 * Topological Sort — Kahn's BFS and Recursive DFS
 * Only for Directed Acyclic Graphs (DAG)
 */
public class TopologicalSort {

    // ---- Kahn's Algorithm (BFS) ----
    public static int[] topoSortBFS(int n, int[][] edges) {
        List<List<Integer>> adj = new ArrayList<>();
        int[] inDegree = new int[n];
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        for (int[] e : edges) { adj.get(e[0]).add(e[1]); inDegree[e[1]]++; }
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < n; i++) if (inDegree[i] == 0) queue.offer(i);
        int[] order = new int[n]; int idx = 0;
        while (!queue.isEmpty()) {
            int u = queue.poll(); order[idx++] = u;
            for (int v : adj.get(u)) if (--inDegree[v] == 0) queue.offer(v);
        }
        return idx == n ? order : new int[0]; // empty if cycle
    }

    // ---- DFS-based ----
    public static List<Integer> topoSortDFS(int n, List<List<Integer>> adj) {
        boolean[] visited = new boolean[n];
        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = 0; i < n; i++)
            if (!visited[i]) dfs(adj, i, visited, stack);
        List<Integer> result = new ArrayList<>(stack);
        return result;
    }
    private static void dfs(List<List<Integer>> adj, int u, boolean[] visited, Deque<Integer> stack) {
        visited[u] = true;
        for (int v : adj.get(u)) if (!visited[v]) dfs(adj, v, visited, stack);
        stack.push(u);
    }

    // ---- Course Schedule (cycle detection) ----
    public static boolean canFinish(int numCourses, int[][] prerequisites) {
        return topoSortBFS(numCourses, prerequisites).length == numCourses;
    }

    // ---- Course Schedule II (return order) ----
    public static int[] findOrder(int numCourses, int[][] prerequisites) {
        return topoSortBFS(numCourses, prerequisites);
    }

    // ---- Alien Dictionary (construct topo from word order) ----
    public static String alienOrder(String[] words) {
        Map<Character, Set<Character>> adj = new LinkedHashMap<>();
        Map<Character, Integer> inDegree = new LinkedHashMap<>();
        for (String w : words) for (char c : w.toCharArray()) { adj.putIfAbsent(c, new HashSet<>()); inDegree.putIfAbsent(c, 0); }
        for (int i = 0; i < words.length - 1; i++) {
            String w1 = words[i], w2 = words[i+1];
            int minLen = Math.min(w1.length(), w2.length());
            if (w1.length() > w2.length() && w1.startsWith(w2)) return "";
            for (int j = 0; j < minLen; j++) {
                if (w1.charAt(j) != w2.charAt(j)) {
                    if (!adj.get(w1.charAt(j)).contains(w2.charAt(j))) {
                        adj.get(w1.charAt(j)).add(w2.charAt(j));
                        inDegree.merge(w2.charAt(j), 1, Integer::sum);
                    }
                    break;
                }
            }
        }
        Queue<Character> q = new LinkedList<>();
        for (char c : inDegree.keySet()) if (inDegree.get(c) == 0) q.offer(c);
        StringBuilder sb = new StringBuilder();
        while (!q.isEmpty()) {
            char c = q.poll(); sb.append(c);
            for (char next : adj.get(c)) if (inDegree.merge(next, -1, Integer::sum) == 0) q.offer(next);
        }
        return sb.length() == inDegree.size() ? sb.toString() : "";
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(topoSortBFS(6, new int[][]{{5,2},{5,0},{4,0},{4,1},{2,3},{3,1}})));
        System.out.println(canFinish(2, new int[][]{{1,0}}));  // true
        System.out.println(canFinish(2, new int[][]{{1,0},{0,1}}));  // false (cycle)
        System.out.println(alienOrder(new String[]{"wrt","wrf","er","ett","rftt"})); // wertf
    }
}


package algorithms.graphs;

import java.util.*;

/**
 * Topological Sort — Kahn's BFS and Recursive DFS
 * Only for Directed Acyclic Graphs (DAG)
 */
public class TopologicalSort {

    // ---- Kahn's Algorithm (BFS) ----
    public static int[] topoSortBFS(int n, int[][] edges) {
        List<List<Integer>> adj = new ArrayList<>();
        int[] inDegree = new int[n];
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        for (int[] e : edges) { adj.get(e[0]).add(e[1]); inDegree[e[1]]++; }
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < n; i++) if (inDegree[i] == 0) queue.offer(i);
        int[] order = new int[n]; int idx = 0;
        while (!queue.isEmpty()) {
            int u = queue.poll(); order[idx++] = u;
            for (int v : adj.get(u)) if (--inDegree[v] == 0) queue.offer(v);
        }
        return idx == n ? order : new int[0]; // empty if cycle
    }

    // ---- DFS-based ----
    public static List<Integer> topoSortDFS(int n, List<List<Integer>> adj) {
        boolean[] visited = new boolean[n];
        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = 0; i < n; i++)
            if (!visited[i]) dfs(adj, i, visited, stack);
        List<Integer> result = new ArrayList<>(stack);
        return result;
    }
    private static void dfs(List<List<Integer>> adj, int u, boolean[] visited, Deque<Integer> stack) {
        visited[u] = true;
        for (int v : adj.get(u)) if (!visited[v]) dfs(adj, v, visited, stack);
        stack.push(u);
    }

    // ---- Course Schedule (cycle detection) ----
    public static boolean canFinish(int numCourses, int[][] prerequisites) {
        return topoSortBFS(numCourses, prerequisites).length == numCourses;
    }

    // ---- Course Schedule II (return order) ----
    public static int[] findOrder(int numCourses, int[][] prerequisites) {
        return topoSortBFS(numCourses, prerequisites);
    }

    // ---- Alien Dictionary (construct topo from word order) ----
    public static String alienOrder(String[] words) {
        Map<Character, Set<Character>> adj = new LinkedHashMap<>();
        Map<Character, Integer> inDegree = new LinkedHashMap<>();
        for (String w : words) for (char c : w.toCharArray()) { adj.putIfAbsent(c, new HashSet<>()); inDegree.putIfAbsent(c, 0); }
        for (int i = 0; i < words.length - 1; i++) {
            String w1 = words[i], w2 = words[i+1];
            int minLen = Math.min(w1.length(), w2.length());
            if (w1.length() > w2.length() && w1.startsWith(w2)) return "";
            for (int j = 0; j < minLen; j++) {
                if (w1.charAt(j) != w2.charAt(j)) {
                    if (!adj.get(w1.charAt(j)).contains(w2.charAt(j))) {
                        adj.get(w1.charAt(j)).add(w2.charAt(j));
                        inDegree.merge(w2.charAt(j), 1, Integer::sum);
                    }
                    break;
                }
            }
        }
        Queue<Character> q = new LinkedList<>();
        for (char c : inDegree.keySet()) if (inDegree.get(c) == 0) q.offer(c);
        StringBuilder sb = new StringBuilder();
        while (!q.isEmpty()) {
            char c = q.poll(); sb.append(c);
            for (char next : adj.get(c)) if (inDegree.merge(next, -1, Integer::sum) == 0) q.offer(next);
        }
        return sb.length() == inDegree.size() ? sb.toString() : "";
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(topoSortBFS(6, new int[][]{{5,2},{5,0},{4,0},{4,1},{2,3},{3,1}})));
        System.out.println(canFinish(2, new int[][]{{1,0}}));  // true
        System.out.println(canFinish(2, new int[][]{{1,0},{0,1}}));  // false (cycle)
        System.out.println(alienOrder(new String[]{"wrt","wrf","er","ett","rftt"})); // wertf
    }
}
