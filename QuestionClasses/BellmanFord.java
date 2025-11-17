import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BellmanFord {

    /**
     * Executa o algoritmo de Bellman-Ford.
     *
     * @param arestas     Lista de arestas. Cada aresta é um int[3] {origem, destino, peso}
     * @param numVertices O número total de vértices (espera-se 1-based)
     * @param s           O vértice de origem (1-based)
     * @param distancia   Array para armazenar as distâncias
     * @param predecessor Array para armazenar os predecessores
     * @return true se não houver ciclo negativo, false caso contrário
     */
    public static boolean bellmanFord(List<int[]> arestas, int numVertices, int s, double[] distancia, int[] predecessor) {
        // Inicializa distâncias
        for (int i = 1; i <= numVertices; i++) {
            distancia[i] = Double.POSITIVE_INFINITY;
            predecessor[i] = -1; // -1 representa null
        }
        distancia[s] = 0;

        // Relaxa arestas repetidamente
        for (int i = 1; i <= numVertices - 1; i++) {
            for (int[] aresta : arestas) {
                int u = aresta[0];
                int v = aresta[1];
                int w = aresta[2];

                if (distancia[u] == Double.POSITIVE_INFINITY) {
                    continue;
                }

                if (distancia[v] > distancia[u] + w) {
                    distancia[v] = distancia[u] + w;
                    predecessor[v] = u;
                }
            }
        }

        // Detecta ciclos negativos
        for (int[] aresta : arestas) {
            int u = aresta[0];
            int v = aresta[1];
            int w = aresta[2];

            if (distancia[u] == Double.POSITIVE_INFINITY) {
                continue;
            }

            if (distancia[v] > distancia[u] + w) {
                return false; // Ciclo de peso negativo detectado
            }
        }

        return true; // Nenhum ciclo de peso negativo
    }

    /**
     * Reconstrói o caminho mais curto até um vértice de destino.
     */
    public static List<Integer> getCaminho(int destino, double[] distancia, int[] predecessor) {
        LinkedList<Integer> caminho = new LinkedList<>();

        if (distancia[destino] == Double.POSITIVE_INFINITY) {
            return caminho; // Caminho não encontrado
        }

        int atual = destino;
        while (atual != -1) {
            caminho.addFirst(atual);
            if (predecessor[atual] != -1) {
                atual = predecessor[atual];
            } else {
                // Chegou na origem (ou o nó não tem predecessor)
                break;
            }
        }

        // Se o caminho não começar na origem (ex: destino == origem)
        if (caminho.isEmpty() || caminho.getFirst() != (distancia[destino] == 0 ? destino : -1)) {
            // Assegura que o primeiro nó é o início do caminho encontrado
        }

        return caminho;
    }

    /**
     * Imprime o caminho formatado.
     */
    private static void imprimirCaminho(List<Integer> caminho) {
        for (int i = 0; i < caminho.size(); i++) {
            System.out.print(caminho.get(i));
            if (i < caminho.size() - 1) {
                System.out.print(" -> ");
            }
        }
        System.out.println();
    }

    /**
     * Adiciona uma aresta à lista.
     */
    private static void adicionarAresta(List<int[]> arestas, int u, int v, int peso) {
        arestas.add(new int[]{u, v, peso});
    }

    /**
     * Cria o grafo de exemplo do Bellman-Ford.
     */
    private static List<int[]> criarGrafoBellmanFord() {
        List<int[]> arestas = new ArrayList<>();

        // Vértices são de 1 a 19
        adicionarAresta(arestas, 1, 6, 3);
        adicionarAresta(arestas, 1, 11, 1);
        adicionarAresta(arestas, 2, 1, 2);
        adicionarAresta(arestas, 2, 3, 8);
        adicionarAresta(arestas, 2, 6, 7);
        adicionarAresta(arestas, 3, 2, 1);
        adicionarAresta(arestas, 3, 4, 2);
        adicionarAresta(arestas, 4, 9, 9);
        adicionarAresta(arestas, 4, 10, 5);
        adicionarAresta(arestas, 4, 13, 15);
        adicionarAresta(arestas, 5, 4, 4);
        adicionarAresta(arestas, 6, 3, 9);
        adicionarAresta(arestas, 6, 7, 2);
        adicionarAresta(arestas, 7, 8, 8);
        adicionarAresta(arestas, 7, 12, 1);
        adicionarAresta(arestas, 8, 9, 7);
        adicionarAresta(arestas, 9, 3, 2);
        adicionarAresta(arestas, 9, 14, 1);
        adicionarAresta(arestas, 10, 5, 5);
        adicionarAresta(arestas, 10, 14, 6);
        adicionarAresta(arestas, 10, 15, 9);
        adicionarAresta(arestas, 11, 6, 0);
        adicionarAresta(arestas, 11, 16, 2);
        adicionarAresta(arestas, 12, 11, 4);
        adicionarAresta(arestas, 12, 17, 1);
        adicionarAresta(arestas, 13, 7, 5);
        adicionarAresta(arestas, 13, 18, 4);
        adicionarAresta(arestas, 14, 15, 1);
        adicionarAresta(arestas, 14, 19, 18);
        adicionarAresta(arestas, 16, 12, 3);
        adicionarAresta(arestas, 17, 12, 1);
        adicionarAresta(arestas, 17, 19, 5);
        adicionarAresta(arestas, 18, 9, 2);
        adicionarAresta(arestas, 18, 17, 20);

        return arestas;
    }

    public static void main(String[] args) {
        System.out.println("Executando Bellman-Ford...");

        int numVertices = 19;
        List<int[]> arestas = criarGrafoBellmanFord();
        double[] distancia = new double[numVertices + 1];
        int[] predecessor = new int[numVertices + 1];

        int origemBF = 1;
        int destinoBF = 15;

        boolean semCicloNegativo = bellmanFord(arestas, numVertices, origemBF, distancia, predecessor);

        if (semCicloNegativo) {
            System.out.println("Algoritmo finalizado. Nenhum ciclo negativo detectado.");
            List<Integer> caminho = getCaminho(destinoBF, distancia, predecessor);

            if (caminho.isEmpty() || distancia[destinoBF] == Double.POSITIVE_INFINITY) {
                System.out.println("Não existe caminho entre " + origemBF + " e " + destinoBF + " .");
            } else {
                System.out.println("Caminho mais curto de " + origemBF + " a " + destinoBF + " (Custo: " + distancia[destinoBF] + "):");
                imprimirCaminho(caminho);
            }
        } else {
            System.out.println("ERRO: Ciclo de peso negativo detectado.");
        }
    }
}
