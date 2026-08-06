package algorithms.graphs;

import java.util.*;

/**
 * Union-Find (DSU) — Core and Applications
 * find: O(α(n)) amortized | union: O(α(n)) amortized
 */
public class UnionFindApplications {

    static class DSU {
        int[] parent, rank;
        int components;
        DSU(int n) { parent = new int[n]; rank = new int[n]; components = n; for(int i=0;i<n;i++)parent[i]=i; }
        int find(int x) { if(parent[x]!=x)parent[x]=find(parent[x]); return parent[x]; }
        boolean union(int x, int y) {
            int px=find(x),py=find(y); if(px==py)return false;
            if(rank[px]<rank[py]){int t=px;px=py;py=t;}
            parent[py]=px; if(rank[px]==rank[py])rank[px]++; components--; return true;
        }
        boolean connected(int x,int y){return find(x)==find(y);}
    }

    // 1. Number of Connected Components in Undirected Graph
    public static int countComponents(int n, int[][] edges) {
        DSU dsu = new DSU(n);
        for (int[] e : edges) dsu.union(e[0], e[1]);
        return dsu.components;
    }

    // 2. Detect cycle in undirected graph
    public static boolean hasCycle(int n, int[][] edges) {
        DSU dsu = new DSU(n);
        for (int[] e : edges) if (!dsu.union(e[0], e[1])) return true;
        return false;
    }

    // 3. Kruskal's MST
    public static int kruskalMST(int n, int[][] edges) {
        Arrays.sort(edges, Comparator.comparingInt(e -> e[2]));
        DSU dsu = new DSU(n);
        int total = 0, used = 0;
        for (int[] e : edges) {
            if (dsu.union(e[0], e[1])) { total += e[2]; if (++used == n-1) break; }
        }
        return used == n-1 ? total : -1;
    }

    // 4. Smallest String With Swaps (group connected chars)
    public static String smallestStringWithSwaps(String s, List<List<Integer>> pairs) {
        int n = s.length();
        DSU dsu = new DSU(n);
        for (List<Integer> p : pairs) dsu.union(p.get(0), p.get(1));
        Map<Integer, PriorityQueue<Character>> groups = new HashMap<>();
        for (int i = 0; i < n; i++)
            groups.computeIfAbsent(dsu.find(i), k -> new PriorityQueue<>()).offer(s.charAt(i));
        char[] result = new char[n];
        for (int i = 0; i < n; i++) result[i] = groups.get(dsu.find(i)).poll();
        return new String(result);
    }

    // 5. Grid island count using DSU
    public static int numIslandsDSU(char[][] grid) {
        int m = grid.length, n = grid[0].length;
        DSU dsu = new DSU(m * n);
        int waterCount = 0;
        for (int r = 0; r < m; r++)
            for (int c = 0; c < n; c++) {
                if (grid[r][c] == '0') { waterCount++; continue; }
                if (r+1 < m && grid[r+1][c] == '1') dsu.union(r*n+c, (r+1)*n+c);
                if (c+1 < n && grid[r][c+1] == '1') dsu.union(r*n+c, r*n+c+1);
            }
        return dsu.components - waterCount;
    }

    public static void main(String[] args) {
        System.out.println(countComponents(5, new int[][]{{0,1},{1,2},{3,4}})); // 2
        System.out.println(hasCycle(3, new int[][]{{0,1},{1,2},{0,2}})); // true
        System.out.println(kruskalMST(4, new int[][]{{0,1,10},{0,2,6},{0,3,5},{1,3,15},{2,3,4}})); // 19
        System.out.println(smallestStringWithSwaps("dcab", List.of(List.of(0,3),List.of(1,2)))); // bacd
    }
}


package algorithms.graphs;

import java.util.*;

/**
 * Union-Find (DSU) — Core and Applications
 * find: O(α(n)) amortized | union: O(α(n)) amortized
 */
public class UnionFindApplications {

    static class DSU {
        int[] parent, rank;
        int components;
        DSU(int n) { parent = new int[n]; rank = new int[n]; components = n; for(int i=0;i<n;i++)parent[i]=i; }
        int find(int x) { if(parent[x]!=x)parent[x]=find(parent[x]); return parent[x]; }
        boolean union(int x, int y) {
            int px=find(x),py=find(y); if(px==py)return false;
            if(rank[px]<rank[py]){int t=px;px=py;py=t;}
            parent[py]=px; if(rank[px]==rank[py])rank[px]++; components--; return true;
        }
        boolean connected(int x,int y){return find(x)==find(y);}
    }

    // 1. Number of Connected Components in Undirected Graph
    public static int countComponents(int n, int[][] edges) {
        DSU dsu = new DSU(n);
        for (int[] e : edges) dsu.union(e[0], e[1]);
        return dsu.components;
    }

    // 2. Detect cycle in undirected graph
    public static boolean hasCycle(int n, int[][] edges) {
        DSU dsu = new DSU(n);
        for (int[] e : edges) if (!dsu.union(e[0], e[1])) return true;
        return false;
    }

    // 3. Kruskal's MST
    public static int kruskalMST(int n, int[][] edges) {
        Arrays.sort(edges, Comparator.comparingInt(e -> e[2]));
        DSU dsu = new DSU(n);
        int total = 0, used = 0;
        for (int[] e : edges) {
            if (dsu.union(e[0], e[1])) { total += e[2]; if (++used == n-1) break; }
        }
        return used == n-1 ? total : -1;
    }

    // 4. Smallest String With Swaps (group connected chars)
    public static String smallestStringWithSwaps(String s, List<List<Integer>> pairs) {
        int n = s.length();
        DSU dsu = new DSU(n);
        for (List<Integer> p : pairs) dsu.union(p.get(0), p.get(1));
        Map<Integer, PriorityQueue<Character>> groups = new HashMap<>();
        for (int i = 0; i < n; i++)
            groups.computeIfAbsent(dsu.find(i), k -> new PriorityQueue<>()).offer(s.charAt(i));
        char[] result = new char[n];
        for (int i = 0; i < n; i++) result[i] = groups.get(dsu.find(i)).poll();
        return new String(result);
    }

    // 5. Grid island count using DSU
    public static int numIslandsDSU(char[][] grid) {
        int m = grid.length, n = grid[0].length;
        DSU dsu = new DSU(m * n);
        int waterCount = 0;
        for (int r = 0; r < m; r++)
            for (int c = 0; c < n; c++) {
                if (grid[r][c] == '0') { waterCount++; continue; }
                if (r+1 < m && grid[r+1][c] == '1') dsu.union(r*n+c, (r+1)*n+c);
                if (c+1 < n && grid[r][c+1] == '1') dsu.union(r*n+c, r*n+c+1);
            }
        return dsu.components - waterCount;
    }

    public static void main(String[] args) {
        System.out.println(countComponents(5, new int[][]{{0,1},{1,2},{3,4}})); // 2
        System.out.println(hasCycle(3, new int[][]{{0,1},{1,2},{0,2}})); // true
        System.out.println(kruskalMST(4, new int[][]{{0,1,10},{0,2,6},{0,3,5},{1,3,15},{2,3,4}})); // 19
        System.out.println(smallestStringWithSwaps("dcab", List.of(List.of(0,3),List.of(1,2)))); // bacd
    }
}


package algorithms.graphs;

import java.util.*;

/**
 * Union-Find (DSU) — Core and Applications
 * find: O(α(n)) amortized | union: O(α(n)) amortized
 */
public class UnionFindApplications {

    static class DSU {
        int[] parent, rank;
        int components;
        DSU(int n) { parent = new int[n]; rank = new int[n]; components = n; for(int i=0;i<n;i++)parent[i]=i; }
        int find(int x) { if(parent[x]!=x)parent[x]=find(parent[x]); return parent[x]; }
        boolean union(int x, int y) {
            int px=find(x),py=find(y); if(px==py)return false;
            if(rank[px]<rank[py]){int t=px;px=py;py=t;}
            parent[py]=px; if(rank[px]==rank[py])rank[px]++; components--; return true;
        }
        boolean connected(int x,int y){return find(x)==find(y);}
    }

    // 1. Number of Connected Components in Undirected Graph
    public static int countComponents(int n, int[][] edges) {
        DSU dsu = new DSU(n);
        for (int[] e : edges) dsu.union(e[0], e[1]);
        return dsu.components;
    }

    // 2. Detect cycle in undirected graph
    public static boolean hasCycle(int n, int[][] edges) {
        DSU dsu = new DSU(n);
        for (int[] e : edges) if (!dsu.union(e[0], e[1])) return true;
        return false;
    }

    // 3. Kruskal's MST
    public static int kruskalMST(int n, int[][] edges) {
        Arrays.sort(edges, Comparator.comparingInt(e -> e[2]));
        DSU dsu = new DSU(n);
        int total = 0, used = 0;
        for (int[] e : edges) {
            if (dsu.union(e[0], e[1])) { total += e[2]; if (++used == n-1) break; }
        }
        return used == n-1 ? total : -1;
    }

    // 4. Smallest String With Swaps (group connected chars)
    public static String smallestStringWithSwaps(String s, List<List<Integer>> pairs) {
        int n = s.length();
        DSU dsu = new DSU(n);
        for (List<Integer> p : pairs) dsu.union(p.get(0), p.get(1));
        Map<Integer, PriorityQueue<Character>> groups = new HashMap<>();
        for (int i = 0; i < n; i++)
            groups.computeIfAbsent(dsu.find(i), k -> new PriorityQueue<>()).offer(s.charAt(i));
        char[] result = new char[n];
        for (int i = 0; i < n; i++) result[i] = groups.get(dsu.find(i)).poll();
        return new String(result);
    }

    // 5. Grid island count using DSU
    public static int numIslandsDSU(char[][] grid) {
        int m = grid.length, n = grid[0].length;
        DSU dsu = new DSU(m * n);
        int waterCount = 0;
        for (int r = 0; r < m; r++)
            for (int c = 0; c < n; c++) {
                if (grid[r][c] == '0') { waterCount++; continue; }
                if (r+1 < m && grid[r+1][c] == '1') dsu.union(r*n+c, (r+1)*n+c);
                if (c+1 < n && grid[r][c+1] == '1') dsu.union(r*n+c, r*n+c+1);
            }
        return dsu.components - waterCount;
    }

    public static void main(String[] args) {
        System.out.println(countComponents(5, new int[][]{{0,1},{1,2},{3,4}})); // 2
        System.out.println(hasCycle(3, new int[][]{{0,1},{1,2},{0,2}})); // true
        System.out.println(kruskalMST(4, new int[][]{{0,1,10},{0,2,6},{0,3,5},{1,3,15},{2,3,4}})); // 19
        System.out.println(smallestStringWithSwaps("dcab", List.of(List.of(0,3),List.of(1,2)))); // bacd
    }
}
