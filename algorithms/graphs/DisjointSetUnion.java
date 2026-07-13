package algorithms.graphs;

import java.util.*;

/**
 * Disjoint Set Union (Union-Find) with Applications
 * Path compression + union by rank = near O(1) amortized
 */
public class DisjointSetUnion {

    private int[] parent, rank, size;
    private int components;

    public DisjointSetUnion(int n) {
        parent = new int[n]; rank = new int[n]; size = new int[n];
        components = n;
        for (int i = 0; i < n; i++) { parent[i] = i; size[i] = 1; }
    }

    public int find(int x) {
        if (parent[x] != x) parent[x] = find(parent[x]); // path compression
        return parent[x];
    }

    public boolean union(int x, int y) {
        int px = find(x), py = find(y);
        if (px == py) return false; // already same component
        if (rank[px] < rank[py]) { int t=px; px=py; py=t; } // union by rank
        parent[py] = px;
        size[px] += size[py];
        if (rank[px] == rank[py]) rank[px]++;
        components--;
        return true;
    }

    public boolean connected(int x, int y) { return find(x) == find(y); }
    public int getSize(int x) { return size[find(x)]; }
    public int getComponents() { return components; }

    // ---- Number of Provinces ----
    public static int findCircleNum(int[][] isConnected) {
        int n = isConnected.length;
        DisjointSetUnion dsu = new DisjointSetUnion(n);
        for (int i = 0; i < n; i++)
            for (int j = i+1; j < n; j++)
                if (isConnected[i][j] == 1) dsu.union(i, j);
        return dsu.getComponents();
    }

    // ---- Redundant Connection (find cycle) ----
    public static int[] findRedundantConnection(int[][] edges) {
        DisjointSetUnion dsu = new DisjointSetUnion(edges.length + 1);
        for (int[] e : edges)
            if (!dsu.union(e[0], e[1])) return e; // already connected
        return new int[0];
    }

    // ---- Accounts Merge ----
    public static List<List<String>> accountsMerge(List<List<String>> accounts) {
        Map<String, Integer> emailToId = new HashMap<>();
        Map<String, String> emailToName = new HashMap<>();
        int id = 0;
        DisjointSetUnion dsu = new DisjointSetUnion(10001);
        for (List<String> account : accounts) {
            String name = account.get(0);
            for (int i = 1; i < account.size(); i++) {
                emailToName.put(account.get(i), name);
                emailToId.putIfAbsent(account.get(i), id++);
                dsu.union(emailToId.get(account.get(1)), emailToId.get(account.get(i)));
            }
        }
        Map<Integer, List<String>> groups = new HashMap<>();
        for (String email : emailToId.keySet())
            groups.computeIfAbsent(dsu.find(emailToId.get(email)), k -> new ArrayList<>()).add(email);
        List<List<String>> result = new ArrayList<>();
        for (List<String> group : groups.values()) {
            Collections.sort(group);
            group.add(0, emailToName.get(group.get(0)));
            result.add(group);
        }
        return result;
    }

    public static void main(String[] args) {
        DisjointSetUnion dsu = new DisjointSetUnion(5);
        dsu.union(0,1); dsu.union(1,2); dsu.union(3,4);
        System.out.println(dsu.getComponents()); // 2
        System.out.println(dsu.connected(0,2));  // true
        System.out.println(dsu.connected(0,3));  // false
        System.out.println(Arrays.toString(findRedundantConnection(new int[][]{{1,2},{1,3},{2,3}})));
        // [2,3]
    }
}


package algorithms.graphs;

import java.util.*;

/**
 * Disjoint Set Union (Union-Find) with Applications
 * Path compression + union by rank = near O(1) amortized
 */
public class DisjointSetUnion {

    private int[] parent, rank, size;
    private int components;

    public DisjointSetUnion(int n) {
        parent = new int[n]; rank = new int[n]; size = new int[n];
        components = n;
        for (int i = 0; i < n; i++) { parent[i] = i; size[i] = 1; }
    }

    public int find(int x) {
        if (parent[x] != x) parent[x] = find(parent[x]); // path compression
        return parent[x];
    }

    public boolean union(int x, int y) {
        int px = find(x), py = find(y);
        if (px == py) return false; // already same component
        if (rank[px] < rank[py]) { int t=px; px=py; py=t; } // union by rank
        parent[py] = px;
        size[px] += size[py];
        if (rank[px] == rank[py]) rank[px]++;
        components--;
        return true;
    }

    public boolean connected(int x, int y) { return find(x) == find(y); }
    public int getSize(int x) { return size[find(x)]; }
    public int getComponents() { return components; }

    // ---- Number of Provinces ----
    public static int findCircleNum(int[][] isConnected) {
        int n = isConnected.length;
        DisjointSetUnion dsu = new DisjointSetUnion(n);
        for (int i = 0; i < n; i++)
            for (int j = i+1; j < n; j++)
                if (isConnected[i][j] == 1) dsu.union(i, j);
        return dsu.getComponents();
    }

    // ---- Redundant Connection (find cycle) ----
    public static int[] findRedundantConnection(int[][] edges) {
        DisjointSetUnion dsu = new DisjointSetUnion(edges.length + 1);
        for (int[] e : edges)
            if (!dsu.union(e[0], e[1])) return e; // already connected
        return new int[0];
    }

    // ---- Accounts Merge ----
    public static List<List<String>> accountsMerge(List<List<String>> accounts) {
        Map<String, Integer> emailToId = new HashMap<>();
        Map<String, String> emailToName = new HashMap<>();
        int id = 0;
        DisjointSetUnion dsu = new DisjointSetUnion(10001);
        for (List<String> account : accounts) {
            String name = account.get(0);
            for (int i = 1; i < account.size(); i++) {
                emailToName.put(account.get(i), name);
                emailToId.putIfAbsent(account.get(i), id++);
                dsu.union(emailToId.get(account.get(1)), emailToId.get(account.get(i)));
            }
        }
        Map<Integer, List<String>> groups = new HashMap<>();
        for (String email : emailToId.keySet())
            groups.computeIfAbsent(dsu.find(emailToId.get(email)), k -> new ArrayList<>()).add(email);
        List<List<String>> result = new ArrayList<>();
        for (List<String> group : groups.values()) {
            Collections.sort(group);
            group.add(0, emailToName.get(group.get(0)));
            result.add(group);
        }
        return result;
    }

    public static void main(String[] args) {
        DisjointSetUnion dsu = new DisjointSetUnion(5);
        dsu.union(0,1); dsu.union(1,2); dsu.union(3,4);
        System.out.println(dsu.getComponents()); // 2
        System.out.println(dsu.connected(0,2));  // true
        System.out.println(dsu.connected(0,3));  // false
        System.out.println(Arrays.toString(findRedundantConnection(new int[][]{{1,2},{1,3},{2,3}})));
        // [2,3]
    }
}
