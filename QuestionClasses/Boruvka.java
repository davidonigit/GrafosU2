import java.util.ArrayList;
import java.util.List;

public class Boruvka {

    private static class Edge {
        int src;
        int dest;
        int weight;

        Edge(int src, int dest, int weight) {
            this.src = src;
            this.dest = dest;
            this.weight = weight;
        }
    }

    private static class Graph {
        int vertices;
        List<Edge> edges;

        Graph(int vertices) {
            this.vertices = vertices;
            this.edges = new ArrayList<>();
        }

        void addUndirectedEdge(int sourceVertex, int targetVertex, int weight) {
            edges.add(new Edge(sourceVertex, targetVertex, weight));
        }
    }

    private static int find(int[] parent, int v) {
        while (parent[v] != v) {
            v = parent[v];
        }
        return v;
    }

    private static void union(int[] parent, int a, int b) {
        int rootA = find(parent, a);
        int rootB = find(parent, b);
        if (rootA != rootB) {
            parent[rootB] = rootA;
        }
    }

    public static List<Edge> boruvkaMST(Graph graph) {
        int n = graph.vertices;
        int[] parent = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }
        List<Edge> mst = new ArrayList<>();

        int components = n;
        Edge[] cheapest = new Edge[n];

        while (components > 1) {
            for (int i = 0; i < n; i++) {
                cheapest[i] = null;
            }
            for (Edge edge : graph.edges) {
                int setU = find(parent, edge.src);
                int setV = find(parent, edge.dest);
                if (setU == setV) {
                    continue;
                }
                if (cheapest[setU] == null || edge.weight < cheapest[setU].weight) {
                    cheapest[setU] = edge;
                }
                if (cheapest[setV] == null || edge.weight < cheapest[setV].weight) {
                    cheapest[setV] = edge;
                }
            }

            boolean anyEdgeAdded = false;

            for (int i = 0; i < n; i++) {
                Edge edge = cheapest[i];
                if (edge == null) {
                    continue;
                }
                int setU = find(parent, edge.src);
                int setV = find(parent, edge.dest);
                if (setU == setV) {
                    continue;
                }
                union(parent, setU, setV);
                mst.add(edge);
                components--;
                anyEdgeAdded = true;
            }

            if (!anyEdgeAdded) {
                break;
            }
        }

        return mst;
    }

    private static Graph buildSampleGraph() {
        Graph g = new Graph(20);

        g.addUndirectedEdge(0, 1, 2);
        g.addUndirectedEdge(0, 5, 3);
        g.addUndirectedEdge(0, 10, 1);

        g.addUndirectedEdge(1, 2, 1);
        g.addUndirectedEdge(1, 5, 7);

        g.addUndirectedEdge(2, 3, 2);
        g.addUndirectedEdge(2, 0, 8);
        g.addUndirectedEdge(2, 7, 9);
        g.addUndirectedEdge(2, 8, 10);

        g.addUndirectedEdge(3, 4, 4);
        g.addUndirectedEdge(3, 8, 2);

        g.addUndirectedEdge(4, 9, 7);

        g.addUndirectedEdge(5, 6, 0);
        g.addUndirectedEdge(5, 11, 2);

        g.addUndirectedEdge(6, 7, 2);

        g.addUndirectedEdge(7, 8, 8);
        g.addUndirectedEdge(7, 12, 4);

        g.addUndirectedEdge(8, 12, 1);
        g.addUndirectedEdge(8, 13, 5);

        g.addUndirectedEdge(9, 3, 9);
        g.addUndirectedEdge(9, 10, 6);
        g.addUndirectedEdge(9, 13, 2);
        g.addUndirectedEdge(9, 14, 1);

        g.addUndirectedEdge(10, 4, 5);
        g.addUndirectedEdge(10, 14, 1);

        g.addUndirectedEdge(11, 15, 3);

        g.addUndirectedEdge(12, 11, 3);
        g.addUndirectedEdge(12, 16, 1);

        g.addUndirectedEdge(13, 17, 4);

        g.addUndirectedEdge(14, 18, 2);
        g.addUndirectedEdge(14, 19, 18);

        g.addUndirectedEdge(17, 18, 20);

        g.addUndirectedEdge(18, 0, 5);

        return g;
    }

    public static void main(String[] args) {
        Graph graph = buildSampleGraph();
        List<Edge> mst = boruvkaMST(graph);

        int totalWeight = 0;
        for (Edge e : mst) {
            int sourceLabel = e.src + 1;
            int targetLabel = e.dest + 1;
            System.out.println(sourceLabel + " - " + targetLabel + " : " + e.weight);
            totalWeight += e.weight;
        }
        System.out.println("Peso total = " + totalWeight);
    }
}


