import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;

public class HierholzerCaminhos {

    /**
     * Encontra um Caminho Euleriano em um grafo.
     *
     * @param adj Lista de adjacência do grafo
     * @param startVertex O vértice inicial
     * @return Uma LinkedList contendo os vértices do caminho euleriano em ordem.
     */
    public static LinkedList<Integer> findEulerianPath(Map<Integer, LinkedList<Integer>> adj, int startVertex) {

        // P ← ∅ - PILHA
        Stack<Integer> P = new Stack<>();

        // L ← ∅ - LISTA
        LinkedList<Integer> L = new LinkedList<>();

        // u ← v0
        int u = startVertex;

        // Enquanto ((P ≠ ∅) ou (u possui arestas não visitadas)) faça
        while (!P.isEmpty() || !adj.get(u).isEmpty()) {

            // Se (u possui arestas não visitadas) então
            if (!adj.get(u).isEmpty()) {
                // empilhar (P,u)
                P.push(u);

                // escolher uma aresta (u,v) ainda não visitada e marcar como visitada
                int v = adj.get(u).removeFirst(); // Pega v da lista de u (remove v da lista de u)
                
                adj.get(v).remove(Integer.valueOf(u)); // Remove u da lista de v

                // u ← v
                u = v;
            }
            // Senão
            else {
                // adicionar u no início de L
                L.addFirst(u);

                // u ← desempilhar(P)
                u = P.pop();
            }
        }

        // Adiciona o último nó (que é o nó inicial) à lista
        L.addFirst(u);

        // Imprimir L
        return L;
    }

    /**
     * Se houver vértices de grau ímpar, retorna o primeiro que encontrar.
     * Se todos tiverem grau par (Circuito), retorna o primeiro com grau > 0.
     */
    public static int findStartVertex(Map<Integer, LinkedList<Integer>> adj) {
        int startVertex = 1;

        for (Integer i : adj.keySet()) {
            int degree = adj.get(i).size();

            if (degree % 2 != 0) {
                return i; // Retorna o primeiro vértice de grau ímpar
            }
        }
        // Se todos são grau par, retorna o primeiro
        return startVertex;
    }

    /**
     * Adiciona uma aresta não-direcionada a lista de adjacência.
     */
    private static void addEdge(Map<Integer, LinkedList<Integer>> adj, int u, int v) {
        adj.get(u).add(v); // u -> v
        adj.get(v).add(u); // v -> u
    }

    public static void main(String[] args) {
        // Grafo do Projeto
        int numVertices = 7;
        Map<Integer, LinkedList<Integer>> grafo = new HashMap<>();
        for (int i = 1; i <= numVertices; i++) {
            grafo.put(i, new LinkedList<>());
        }
        addEdge(grafo, 1, 2);
        addEdge(grafo, 1, 3);
        addEdge(grafo, 2, 3);
        addEdge(grafo, 2, 4);
        addEdge(grafo, 2, 5);
        addEdge(grafo, 3, 4);
        addEdge(grafo, 3, 6);
        addEdge(grafo, 4, 5);
        addEdge(grafo, 4, 6);
        addEdge(grafo, 5, 6);
        addEdge(grafo, 5, 7);
        addEdge(grafo, 6, 7);

        // Encontrar o vértice inicial
        int v0 = findStartVertex(grafo);

        // Hierholzer
        LinkedList<Integer> eulerianPath = findEulerianPath(grafo, v0);

        // Imprimir a lista com o caminho euleriano
        System.out.println("\nCaminho Euleriano:");
        for (int i = 0; i < eulerianPath.size(); i++) {
            System.out.print(eulerianPath.get(i));
            if (i < eulerianPath.size() - 1) {
                System.out.print(" -> ");
            }
        }

        // Saída para o grafo do projeto
        //Caminho Euleriano:
        // 1 -> 2 -> 3 -> 4 -> 2 -> 5 -> 4 -> 6 -> 5 -> 7 -> 6 -> 3 -> 1
    }
}