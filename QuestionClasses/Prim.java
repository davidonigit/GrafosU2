import java.util.Arrays;

public class Prim {

    // Representa infinito
    private static final int INF = Integer.MAX_VALUE;

    /**
     * Algoritmo de Prim para AGM.
     * @param graphMatrix A matriz de adjacência do grafo.
     * Usa INF (Integer.MAX_VALUE) para representar a ausência de aresta.
     */
    public static void primMST(int[][] graphMatrix) {
        int numVertices = graphMatrix.length;

        // T: "parent[i]" armazena o vértice 'j' que conecta 'i' à árvore (aresta (j,i))
        int[] parent = new int[numVertices];

        // "minWeight[i]" armazena o peso mínimo da aresta que conecta o vértice 'i' a AGM
        int[] minWeight = new int[numVertices];

        // Z: (true) Vértices já incluídos na AGM
        // N: (false) Vértices fora da AGM
        boolean[] vertices = new boolean[numVertices];

        // Inicializa todos os pesos como Infinito e nenhum vértice na AGM
        Arrays.fill(minWeight, INF);
        Arrays.fill(vertices, false);

        // s = 1 (indice 0)
        minWeight[0] = 0;
        parent[0] = -1;
        vertices[0] = true;

        boolean finished = false; // Todos vertices adicionados na AGM
        while (!finished) {
            int minVal = INF;
            int selectedJ = -1;
            int selectedK = -1;
            // Encontrar a aresta (j,k) tal que j∈Z, k∈N e djk é mínimo ---
            for (int j = 0; j < numVertices; j++) {
                if (vertices[j]) { // j ∈ Z

                    for (int k = 0; k < numVertices; k++){
                        if (!vertices[k] && graphMatrix[j][k] != 0) { // k ∈ N
                            if (graphMatrix[j][k] < minVal) {
                                // Seleciona os vértices j e k da aresta de menor peso até o momento
                                selectedJ = j;
                                selectedK = k;
                                minVal = graphMatrix[j][k];
                            }
                        }
                    }
                    
                    
                }
            }

            if (selectedK != -1 && selectedJ != -1) {
                // Adiciona a aresta (j, k) na AGM
                minWeight[selectedK] = graphMatrix[selectedJ][selectedK];
                parent[selectedK] = selectedJ;
                vertices[selectedK] = true;
            }
            
            // Verifica se algum vértice ainda está fora da AGM
            for (boolean v : vertices) {
                if (!v) {
                    finished = false;
                    break;
                }
                finished = true;
            }
            
        }

        printMST(parent, graphMatrix, numVertices);
    }

    /**
     * Função para imprimir a AGM
     */
    private static void printMST(int[] parent, int[][] graph, int numVertices) {
        System.out.println("Arestas da Árvore Geradora Mínima (T):");
        System.out.println("Aresta \tPeso");
        int mstCost = 0;
        for (int i = 1; i < numVertices; i++) {
            int peso = graph[parent[i]][i];
            // +1 para transformar índice no número do vértice
            System.out.println((parent[i]+1) + " - " + (i+1) + "\t" + peso);
            mstCost += peso;
        }
        System.out.println("Custo Total da AGM: " + mstCost);
    }


    public static void main(String[] args) {

        int[][] matrizProjeto = {
        //Vértices: 1   2    3    4    5    6    7    8    9    10   11   12   13   14   15   16   17   18   19
        /* ( 1)*/{INF,   2, INF, INF, INF,   3, INF, INF, INF, INF,   1, INF, INF, INF, INF, INF, INF, INF, INF },
        /* ( 2)*/{  2, INF,   1, INF, INF, INF, INF, INF, INF, INF, INF, INF, INF, INF, INF, INF, INF, INF, INF },
        /* ( 3)*/{INF,   1, INF,   2, INF, INF, INF,  10,   2, INF, INF, INF, INF, INF, INF, INF, INF, INF, INF },
        /* ( 4)*/{INF, INF,   2, INF,   4, INF, INF, INF, INF, INF, INF, INF, INF, INF, INF, INF, INF, INF, INF },
        /* ( 5)*/{INF, INF, INF,   4, INF, INF, INF, INF, INF,   5, INF, INF, INF, INF, INF, INF, INF, INF, INF },
        /* ( 6)*/{  3, INF, INF, INF, INF, INF,   2, INF, INF, INF, INF, INF, INF, INF, INF, INF, INF, INF, INF },
        /* ( 7)*/{INF, INF, INF, INF, INF,   2, INF,   8, INF, INF, INF,   1,   5, INF, INF, INF, INF, INF, INF },
        /* ( 8)*/{INF, INF,  10, INF, INF, INF,   8, INF, INF, INF, INF, INF, INF, INF, INF, INF, INF, INF, INF },
        /* ( 9)*/{INF, INF,   2, INF, INF, INF, INF, INF, INF,   6, INF, INF,  15,   1, INF, INF, INF, INF, INF },
        /* (10)*/{INF, INF, INF, INF,   5, INF, INF, INF,   6, INF, INF, INF, INF, INF,   9, INF, INF, INF, INF },
        /* (11)*/{  0, INF, INF, INF, INF, INF, INF, INF, INF, INF, INF,   4, INF, INF, INF,   2, INF, INF, INF },
        /* (12)*/{INF, INF, INF, INF, INF, INF,   1, INF, INF, INF,   4, INF, INF, INF, INF,   3,   1, INF, INF },
        /* (13)*/{INF, INF, INF, INF, INF, INF,   5, INF,  15, INF, INF, INF, INF, INF, INF, INF, INF,   4, INF },
        /* (14)*/{INF, INF, INF, INF, INF, INF, INF, INF,   1, INF, INF, INF, INF, INF,   1, INF, INF,  18, INF },
        /* (15)*/{INF, INF, INF, INF, INF, INF, INF, INF, INF,   9, INF, INF, INF,   1, INF, INF, INF, INF,   5 },
        /* (16)*/{INF, INF, INF, INF, INF, INF, INF, INF, INF, INF,   2,   3, INF, INF, INF, INF,   1, INF, INF },
        /* (17)*/{INF, INF, INF, INF, INF, INF, INF, INF, INF, INF, INF,   1, INF, INF, INF,   1, INF,  20, INF },
        /* (18)*/{INF, INF, INF, INF, INF, INF, INF, INF, INF, INF, INF, INF,   4,  18, INF, INF,  20, INF,   5 },
        /* (19)*/{INF, INF, INF, INF, INF, INF, INF, INF, INF, INF, INF, INF, INF, INF,   5, INF, INF,   5, INF }
        };

        int[][] matrizSlide = {
        { INF, INF,   7, INF, INF, INF, INF, INF, INF, INF }, // a (0)
        { INF, INF,   2, INF,   8,   7, INF, INF, INF, INF }, // b (1)
        {   7,   2, INF,   6, INF,   1, INF, INF, INF, INF }, // c (2)
        { INF, INF,   6, INF, INF, INF,   6, INF, INF, INF }, // d (3)
        { INF,   8, INF, INF, INF,   2, INF,   1, INF, INF }, // e (4)
        { INF,   7,   1, INF,   2, INF,   5,   4,   1, INF }, // f (5)
        { INF, INF, INF,   6, INF,   5, INF, INF, INF,   2 }, // g (6)
        { INF, INF, INF, INF,   1,   4, INF, INF,   6, INF }, // h (7)
        { INF, INF, INF, INF, INF,   1, INF,   6, INF,   5 }, // i (8)
        { INF, INF, INF, INF, INF, INF,   2, INF,   5, INF }  // j (9)
    };

        primMST(matrizSlide);

        // Saída para o grafo do slide:
        // Arestas da Árvore Geradora Mínima (T):
        // Aresta  Peso
        // 3 - 2   2
        // 1 - 3   7
        // 3 - 4   6
        // 6 - 5   2
        // 3 - 6   1
        // 6 - 7   5
        // 5 - 8   1
        // 6 - 9   1
        // 7 - 10  2
        // Custo Total da AGM: 27
    }
}