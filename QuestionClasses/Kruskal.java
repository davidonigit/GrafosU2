import java.io.*;
import java.util.*;

/**
 * Classe que representa uma aresta do grafo.
 * Implementa Comparable para permitir ordenação por peso.
 */
class Edge implements Comparable<Edge> {
    int u, v, w; // u = origem, v = destino, w = peso da aresta

    public Edge(int u, int v, int w) {
        this.u = u;
        this.v = v;
        this.w = w;
    }

    // Método usado para ordenar arestas pelo peso (crescente)
    @Override
    public int compareTo(Edge other) {
        return Integer.compare(this.w, other.w);
    }
}

/**
 * Implementação da estrutura Union-Find (Disjoint Set Union)
 * usada no algoritmo de Kruskal para evitar ciclos.
 */
class UnionFind {
    int[] parent; // guarda o pai de cada componente
    int[] rank;   // otimiza a união (union by rank)

    public UnionFind(int n) {
        parent = new int[n + 1];  // vértices começam em 1
        rank = new int[n + 1];

        // Inicialmente, cada vértice é seu próprio pai
        for (int i = 1; i <= n; i++) {
            parent[i] = i;
            rank[i] = 0;
        }
    }

    /**
     * Método find com path compression.
     * Encontra o representante (raiz) do conjunto.
     */
    public int find(int x) {
        if (parent[x] != x)
            parent[x] = find(parent[x]); // compressão de caminho
        return parent[x];
    }

    /**
     * Une dois conjuntos usando union by rank.
     */
    public void union(int x, int y) {
        int rx = find(x);
        int ry = find(y);

        // Se já estão no mesmo conjunto, não faz nada
        if (rx == ry) return;

        // União por rank: o conjunto "menor" aponta para o "maior"
        if (rank[rx] < rank[ry]) {
            parent[rx] = ry;
        } else if (rank[rx] > rank[ry]) {
            parent[ry] = rx;
        } else {
            parent[ry] = rx;
            rank[rx]++; // aumenta o rank da nova raiz
        }
    }
}

public class Kruskal {

    public static void main(String[] args) {

        String file = "../grafos/grafo-kruskal.txt"; // arquivo de entrada
        List<Edge> edges = new ArrayList<>(); // lista de arestas lidas
        int maxVertex = 0; // maior índice de vértice encontrado
        int numArestas = 0;

        // lê  arquivo
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            // Primeira linha contém o número de arestas
            numArestas = Integer.parseInt(br.readLine().trim());

            String line;
            // Cada linha seguinte tem: u,v,w
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                int u = Integer.parseInt(parts[0]);
                int v = Integer.parseInt(parts[1]);
                int w = Integer.parseInt(parts[2]);

                // Adiciona a aresta na lista
                edges.add(new Edge(u, v, w));

                // Descobre o maior vértice
                maxVertex = Math.max(maxVertex, Math.max(u, v));
            }

        } catch (Exception e) {
            System.out.println("Erro ao ler arquivo: " + e.getMessage());
            return;
        }

        // Kruskal

        // Ordena as arestas pelo peso
        Collections.sort(edges);

        // Estrutura Union-Find com base no número de vértices encontrados
        UnionFind uf = new UnionFind(maxVertex);

        List<Edge> mst = new ArrayList<>(); // arestas da árvore geradora mínima
        int totalCost = 0;

        // Percorre as arestas em ordem crescente
        for (Edge e : edges) {
            // Se u e v pertencem a componentes diferentes
            if (uf.find(e.u) != uf.find(e.v)) {
                // ... então podemos unir (não forma ciclo)
                uf.union(e.u, e.v);
                mst.add(e);      // adiciona na solução
                totalCost += e.w; // acumula custo
            }
        }

        System.out.println("Árvore Geradora Mínima (Kruskal):");
        for (Edge e : mst) {
            System.out.println(e.u + " -- " + e.v + "  (peso " + e.w + ")");
        }

        System.out.println("Custo total: " + totalCost);
    }
}
