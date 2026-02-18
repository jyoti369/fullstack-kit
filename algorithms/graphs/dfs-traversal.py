"""
Depth-First Search (DFS) — Graph Traversal
Time: O(V + E) | Space: O(V)

Explores as far as possible along each branch before backtracking.
Implemented both recursively and iteratively.
"""

def dfs_recursive(graph, node, visited=None):
    if visited is None:
        visited = set()
    visited.add(node)
    result = [node]
    for neighbor in graph.get(node, []):
        if neighbor not in visited:
            result.extend(dfs_recursive(graph, neighbor, visited))
    return result

def dfs_iterative(graph, start):
    visited = set()
    stack = [start]
    result = []
    while stack:
        node = stack.pop()
        if node not in visited:
            visited.add(node)
            result.append(node)
            # Add neighbors in reverse for consistent ordering
            for neighbor in reversed(graph.get(node, [])):
                if neighbor not in visited:
                    stack.append(neighbor)
    return result

# --- Example ---
if __name__ == '__main__':
    graph = {
        'A': ['B', 'C'],
        'B': ['A', 'D', 'E'],
        'C': ['A', 'F'],
        'D': ['B'],
        'E': ['B', 'F'],
        'F': ['C', 'E'],
    }
    print(f'DFS Recursive: {dfs_recursive(graph, "A")}')
    print(f'DFS Iterative: {dfs_iterative(graph, "A")}')
