/**
 * Trie (Prefix Tree)
 * insert: O(m) | search: O(m) | startsWith: O(m) where m = word length
 *
 * Efficient for autocomplete, spell checking, and prefix-based search.
 */

class TrieNode {
  constructor() {
    this.children = {};
    this.isEndOfWord = false;
    this.count = 0; // Number of words with this prefix
  }
}

class Trie {
  constructor() {
    this.root = new TrieNode();
  }

  insert(word) {
    let node = this.root;
    for (const char of word) {
      if (!node.children[char]) {
        node.children[char] = new TrieNode();
      }
      node = node.children[char];
      node.count++;
    }
    node.isEndOfWord = true;
  }

  search(word) {
    const node = this._traverse(word);
    return node !== null && node.isEndOfWord;
  }

  startsWith(prefix) {
    return this._traverse(prefix) !== null;
  }

  // Get all words with a given prefix (autocomplete)
  autocomplete(prefix, maxResults = 10) {
    const node = this._traverse(prefix);
    if (!node) return [];
    const results = [];
    this._dfs(node, prefix, results, maxResults);
    return results;
  }

  _traverse(word) {
    let node = this.root;
    for (const char of word) {
      if (!node.children[char]) return null;
      node = node.children[char];
    }
    return node;
  }

  _dfs(node, prefix, results, maxResults) {
    if (results.length >= maxResults) return;
    if (node.isEndOfWord) results.push(prefix);
    for (const [char, child] of Object.entries(node.children)) {
      this._dfs(child, prefix + char, results, maxResults);
    }
  }
}

// --- Example ---
const trie = new Trie();
['apple', 'app', 'application', 'apt', 'bat', 'ball'].forEach(w => trie.insert(w));

console.log(trie.search('app'));       // true
console.log(trie.search('ap'));        // false
console.log(trie.startsWith('ap'));    // true
console.log(trie.autocomplete('app')); // ['app', 'apple', 'application']

module.exports = { Trie };
