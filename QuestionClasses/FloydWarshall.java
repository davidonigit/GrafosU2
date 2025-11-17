import java.io.*;
import java.util.*;

public class FloydWarshall {

    // Valor muito grande para representar "infinito" (ausência de caminho)
    static final int INF = 1000000000;

    public static void main(String[] args) throws Exception {

        // Abre o arquivo de texto contendo o grafo
        BufferedReader br = new BufferedReader(new FileReader("../grafos/grafo-floydwarshall.txt"));

        // Lê a primeira linha com o vértice inicial e final
        String[] startEnd = br.readLine().trim().split(" ");
        int start = Integer.parseInt(startEnd[0]);
        int end = Integer.parseInt(startEnd[1]);

        // Lista temporária para armazenar as arestas lidas do arquivo
        ArrayList<int[]> edges = new ArrayList<>();

        String line;
        int maxVertex = 0;

        // Lê as demais linhas (arestas)
        while ((line = br.readLine()) != null) {

            // Ignora linhas vazias
            if (line.trim().isEmpty()) continue;

            // Divide a linha em u v w
            String[] parts = line.trim().split(" ");
            int u = Integer.parseInt(parts[0]);
            int v = Integer.parseInt(parts[1]);
            int w = Integer.parseInt(parts[2]);

            // Adiciona a aresta na lista
            edges.add(new int[]{u, v, w});

            // Atualiza o maior número de vértice encontrado
            maxVertex = Math.max(maxVertex, Math.max(u, v));
        }
        br.close();

        // Quantidade total de vértices (1 até maxVertex)
        int n = maxVertex;

        // Matrizes de distâncias e de caminho (next)
        int[][] dist = new int[n + 1][n + 1];
        int[][] next = new int[n + 1][n + 1];

        // Inicializa a matriz de distâncias
        for (int i = 1; i <= n; i++) {
            Arrays.fill(dist[i], INF); // Inicialmente tudo é infinito
            dist[i][i] = 0;           // Distância de um vértice para ele mesmo é zero
        }

        // Insere cada aresta na matriz dist
        for (int[] e : edges) {
            int u = e[0], v = e[1], w = e[2];
            dist[u][v] = w;  // custo da aresta u -> v
            next[u][v] = v;  // para ir de u até v, o próximo nó é v
        }

        // Algoritmo de Floyd–Warshall
        // Tenta relaxar caminhos passando por cada k
        for (int k = 1; k <= n; k++) {
            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= n; j++) {

                    // Se for melhor ir de i até j passando por k
                    if (dist[i][k] + dist[k][j] < dist[i][j]) {

                        // Atualiza a menor distância
                        dist[i][j] = dist[i][k] + dist[k][j];

                        // Atualiza o primeiro passo do caminho i → j
                        next[i][j] = next[i][k];
                    }
                }
            }
        }

        // Recupera o caminho mínimo entre 'start' e 'end'
        List<Integer> path = reconstructPath(start, end, next);

        // Imprime a distância
        System.out.println("Distância mínima de " + start + " até " + end + ": " + dist[start][end]);

        // Imprime o caminho completo
        if (path == null) {
            System.out.println("Não existe caminho.");
        } else {
            System.out.println("Caminho: " + path);
        }
    }

    // Função responsável por reconstruir o caminho mínimo usando a matriz "next"
    public static List<Integer> reconstructPath(int u, int v, int[][] next) {

        // Se next[u][v] = 0 significa "não existe caminho"
        if (next[u][v] == 0) return null;

        List<Integer> path = new ArrayList<>();
        path.add(u); // começa pelo vértice inicial

        // Caminha sempre para o próximo vértice indicado por next
        while (u != v) {
            u = next[u][v];
            path.add(u);
        }

        return path;
    }
}
