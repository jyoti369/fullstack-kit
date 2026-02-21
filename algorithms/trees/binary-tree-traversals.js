/**
 * Binary Tree Traversals — Inorder, Preorder, Postorder, Level-Order
 * Time: O(n) for all | Space: O(h) for recursive, O(n) for level-order
 */

class TreeNode {
  constructor(val, left = null, right = null) {
    this.val = val;
    this.left = left;
    this.right = right;
  }
}

// Inorder: Left → Root → Right (gives sorted order for BST)
function inorder(root) {
  const result = [];
  function traverse(node) {
    if (!node) return;
    traverse(node.left);
    result.push(node.val);
    traverse(node.right);
  }
  traverse(root);
  return result;
}

// Preorder: Root → Left → Right
function preorder(root) {
  const result = [];
  function traverse(node) {
    if (!node) return;
    result.push(node.val);
    traverse(node.left);
    traverse(node.right);
  }
  traverse(root);
  return result;
}

// Postorder: Left → Right → Root
function postorder(root) {
  const result = [];
  function traverse(node) {
    if (!node) return;
    traverse(node.left);
    traverse(node.right);
    result.push(node.val);
  }
  traverse(root);
  return result;
}

// Level-Order (BFS)
function levelOrder(root) {
  if (!root) return [];
  const result = [];
  const queue = [root];
  while (queue.length > 0) {
    const level = [];
    const size = queue.length;
    for (let i = 0; i < size; i++) {
      const node = queue.shift();
      level.push(node.val);
      if (node.left) queue.push(node.left);
      if (node.right) queue.push(node.right);
    }
    result.push(level);
  }
  return result;
}

// --- Example ---
const tree = new TreeNode(1,
  new TreeNode(2, new TreeNode(4), new TreeNode(5)),
  new TreeNode(3, null, new TreeNode(6))
);

console.log('Inorder:', inorder(tree));       // [4, 2, 5, 1, 3, 6]
console.log('Preorder:', preorder(tree));     // [1, 2, 4, 5, 3, 6]
console.log('Postorder:', postorder(tree));   // [4, 5, 2, 6, 3, 1]
console.log('Level-Order:', levelOrder(tree)); // [[1], [2,3], [4,5,6]]

module.exports = { TreeNode, inorder, preorder, postorder, levelOrder };
