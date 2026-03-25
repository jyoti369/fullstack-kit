package algorithms.trees;

import java.util.*;

/**
 * Trie (Prefix Tree) — O(m) insert/search, m = word length
 */
public class Trie {

    private final TrieNode root;

    public Trie() { root = new TrieNode(); }

    public void insert(String word) {
        TrieNode curr = root;
        for (char c : word.toCharArray()) {
            curr.children.putIfAbsent(c, new TrieNode());
            curr = curr.children.get(c);
        }
        curr.isEnd = true;
        curr.word = word;
    }

    public boolean search(String word) {
        TrieNode node = find(word);
        return node != null && node.isEnd;
    }

    public boolean startsWith(String prefix) {
        return find(prefix) != null;
    }

    private TrieNode find(String s) {
        TrieNode curr = root;
        for (char c : s.toCharArray()) {
            if (!curr.children.containsKey(c)) return null;
            curr = curr.children.get(c);
        }
        return curr;
    }

    // Autocomplete: all words with given prefix
    public List<String> autocomplete(String prefix) {
        List<String> result = new ArrayList<>();
        TrieNode node = find(prefix);
        if (node != null) dfs(node, result);
        return result;
    }

    private void dfs(TrieNode node, List<String> result) {
        if (node.isEnd) result.add(node.word);
        for (TrieNode child : node.children.values()) dfs(child, result);
    }

    // Word Search II — find all words from board using Trie
    public List<String> findWords(char[][] board, String[] words) {
        for (String w : words) insert(w);
        Set<String> found = new HashSet<>();
        for (int r = 0; r < board.length; r++)
            for (int c = 0; c < board[0].length; c++)
                searchBoard(board, r, c, root, found);
        return new ArrayList<>(found);
    }

    private void searchBoard(char[][] board, int r, int c, TrieNode node, Set<String> found) {
        if (r < 0 || r >= board.length || c < 0 || c >= board[0].length) return;
        char ch = board[r][c];
        if (ch == '#' || !node.children.containsKey(ch)) return;
        node = node.children.get(ch);
        if (node.isEnd) found.add(node.word);
        board[r][c] = '#'; // visited
        for (int[] d : new int[][]{{0,1},{0,-1},{1,0},{-1,0}})
            searchBoard(board, r+d[0], c+d[1], node, found);
        board[r][c] = ch;
    }

    static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        boolean isEnd = false;
        String word = null;
    }

    public static void main(String[] args) {
        Trie trie = new Trie();
        trie.insert("apple"); trie.insert("app"); trie.insert("application");
        System.out.println(trie.search("app"));        // true
        System.out.println(trie.startsWith("appl"));   // true
        System.out.println(trie.autocomplete("app"));  // [app, apple, application]
    }
}
