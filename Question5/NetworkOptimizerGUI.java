package Question5;

import javax.swing.*;
import java.awt.*;
import java.util.*;

// Edge class to store network connections
class Edge {
    int src, dest, cost, bandwidth;
    
    public Edge(int src, int dest, int cost, int bandwidth) {
        this.src = src;
        this.dest = dest;
        this.cost = cost;
        this.bandwidth = bandwidth;
    }
}

// Graph class with adjacency list representation
class Graph {
    int V;
    LinkedList<Edge>[] adjList;

    public Graph(int V) {
        this.V = V;
        adjList = new LinkedList[V];
        for (int i = 0; i < V; i++) {
            adjList[i] = new LinkedList<>();
        }
    }

    public void addEdge(int src, int dest, int cost, int bandwidth) {
        Edge edge = new Edge(src, dest, cost, bandwidth);
        adjList[src].add(edge);
        adjList[dest].add(edge); // Undirected graph
    }
}

// Kruskal's Algorithm for Minimum Spanning Tree
class KruskalMST {
    class Subset {
        int parent, rank;
    }

    int find(Subset[] subsets, int i) {
        if (subsets[i].parent != i)
            subsets[i].parent = find(subsets, subsets[i].parent);
        return subsets[i].parent;
    }

    void union(Subset[] subsets, int x, int y) {
        int rootX = find(subsets, x);
        int rootY = find(subsets, y);
        if (subsets[rootX].rank < subsets[rootY].rank) {
            subsets[rootX].parent = rootY;
        } else if (subsets[rootX].rank > subsets[rootY].rank) {
            subsets[rootY].parent = rootX;
        } else {
            subsets[rootY].parent = rootX;
            subsets[rootX].rank++;
        }
    }

    void kruskalMST(Graph graph, JTextArea outputArea) {
        java.util.List<Edge> result = new ArrayList<>();
        java.util.List<Edge> edges = new ArrayList<>();
        for (LinkedList<Edge> list : graph.adjList) {
            edges.addAll(list);
        }
        edges.sort(Comparator.comparingInt(edge -> edge.cost));
        Subset[] subsets = new Subset[graph.V];
        for (int i = 0; i < graph.V; i++) {
            subsets[i] = new Subset();
            subsets[i].parent = i;
            subsets[i].rank = 0;
        }

        for (Edge edge : edges) {
            int root1 = find(subsets, edge.src);
            int root2 = find(subsets, edge.dest);
            if (root1 != root2) {
                result.add(edge);
                union(subsets, root1, root2);
            }
        }
        outputArea.setText("Optimized Network Topology:\n");
        for (Edge edge : result) {
            outputArea.append("Edge: " + edge.src + " - " + edge.dest + " | Cost: " + edge.cost + "\n");
        }
    }
}

// Dijkstra's Algorithm for Shortest Path
class Dijkstra {
    void shortestPath(Graph graph, int src, JTextArea outputArea) {
        int[] dist = new int[graph.V];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[src] = 0;
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        pq.offer(new int[]{src, 0});

        while (!pq.isEmpty()) {
            int[] curr = pq.poll();
            int u = curr[0];

            for (Edge edge : graph.adjList[u]) {
                int v = (edge.src == u) ? edge.dest : edge.src;
                int weight = edge.bandwidth;
                if (dist[u] + weight < dist[v]) {
                    dist[v] = dist[u] + weight;
                    pq.offer(new int[]{v, dist[v]});
                }
            }
        }
        outputArea.setText("Shortest paths from node " + src + ":\n");
        for (int i = 0; i < graph.V; i++) {
            outputArea.append("To node " + i + " -> Distance: " + dist[i] + "\n");
        }
    }
}

// GUI Application
public class NetworkOptimizerGUI extends JFrame {
    private Graph graph;
    private JTextArea outputArea;

    public NetworkOptimizerGUI() {
        setTitle("Network Optimizer");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        JPanel panel = new JPanel();
        JButton btnOptimize = new JButton("Optimize Network");
        JButton btnShortestPath = new JButton("Find Shortest Path");
        outputArea = new JTextArea(10, 50);
        panel.add(btnOptimize);
        panel.add(btnShortestPath);
        add(panel, BorderLayout.NORTH);
        add(new JScrollPane(outputArea), BorderLayout.SOUTH);
        
        graph = new Graph(5); // Example with 5 nodes
        graph.addEdge(0, 1, 4, 2);
        graph.addEdge(0, 2, 1, 1);
        graph.addEdge(1, 2, 2, 3);
        graph.addEdge(1, 3, 5, 2);
        graph.addEdge(2, 3, 8, 6);
        graph.addEdge(3, 4, 3, 1);
        
        btnOptimize.addActionListener(e -> {
            KruskalMST kruskal = new KruskalMST();
            kruskal.kruskalMST(graph, outputArea);
        });

        btnShortestPath.addActionListener(e -> {
            Dijkstra dijkstra = new Dijkstra();
            dijkstra.shortestPath(graph, 0, outputArea);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new NetworkOptimizerGUI().setVisible(true);
        });
    }
}

