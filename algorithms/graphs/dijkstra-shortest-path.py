"""
Dijkstra's Shortest Path Algorithm
Time: O((V + E) log V) with min-heap | Space: O(V)

Finds shortest paths from a source vertex to all other vertices
in a weighted graph with non-negative edges.
"""

import heapq
from collections import defaultdict

def dijkstra(graph, start):
    distances = {node: float('inf') for node in graph}
    distances[start] = 0
    previous = {node: None for node in graph}
    pq = [(0, start)]  # (distance, node)
    visited = set()

    while pq:
        current_dist, current = heapq.heappop(pq)
        if current in visited:
            continue
        visited.add(current)

        for neighbor, weight in graph[current]:
            if neighbor in visited:
                continue
            new_dist = current_dist + weight
            if new_dist < distances[neighbor]:
                distances[neighbor] = new_dist
                previous[neighbor] = current
                heapq.heappush(pq, (new_dist, neighbor))

    return distances, previous

def reconstruct_path(previous, start, end):
    path = []
    current = end
    while current is not None:
        path.append(current)
        current = previous[current]
    path.reverse()
    return path if path[0] == start else []

# --- Example ---
if __name__ == '__main__':
    graph = {
        'A': [('B', 4), ('C', 1)],
        'B': [('A', 4), ('D', 1)],
        'C': [('A', 1), ('B', 2), ('D', 5)],
        'D': [('B', 1), ('C', 5)],
    }
    distances, previous = dijkstra(graph, 'A')
    print(f'Distances from A: {distances}')  # A:0, B:3, C:1, D:4
    print(f'Path A->D: {reconstruct_path(previous, "A", "D")}')  # A->C->B->D
