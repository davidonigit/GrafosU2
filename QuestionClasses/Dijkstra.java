import Miscs.Grafo;
import Miscs.Pair;

import java.util.LinkedList;
import java.util.Stack;

public class Dijkstra {
    /**
     * Executa o algoritmo de menor caminho de dijkstra sobre um grafo g
     * @param g Grafo em que o algoritmo será executado
     * @param w Matriz de adjacência com os pesos
     * @param s Vértice raiz onde o algoritmo começará a ser executado
     * @param target Vértice alvo em caso de se desejar obter diretamente o menor caminho partindo do vértice s para ele. Para não devolver uma busca, digitar: 0
     * @return Uma classe com os dados obtidos do algoritmo armazenados nele.
     */
    static DijkstraResult execute(Grafo g, int[][] w, int s, int target){
        // Instancia uma classe para armazenar os resultados
        DijkstraResult dr = new DijkstraResult(g,s);

        Pair<Integer,Integer> min_distance_vertex = new Pair<>(-1, Integer.MAX_VALUE);
        // Execução inicial do algoritmo, setando os valores padrões para cada um dos vértices do grafo
        for(int i = 0; i < g.getVertices(); i++){
            if(i == s-1){
                // Marca a raiz como visitada e seta a distância para 0
                dr.visited.set(i, true);
                dr.distance[i] = 0;
                continue;
            }

            dr.visited.set(i, false);

            if(g.getListaAdjacencia().get(s-1).contains(i)){
                // Caso o vértice possua uma aresta incidente com origem na raiz, os seus valores iniciais serão setados com base nela
                dr.predecessor[i] = s-1;
                dr.distance[i] = w[s-1][i];
            }else{
                dr.predecessor[i] = null;
                dr.distance[i] = Integer.MAX_VALUE;
            }

            if(min_distance_vertex.value() >= dr.distance[i]) {
                // Armazena o vértice com menor custo de distância, ignorando a raiz
                min_distance_vertex = new Pair<>(i, dr.distance[i]);
            }
        }

        // Cálculo do menor caminho do vértice raiz para todos os outros vértices do grafo
        // Enquanto houver um vértice não visitado, o algoritmo continuará sendo executado
        while(dr.visited.contains(false)){

            for(int i = 0; i < g.getVertices(); i++){
                if(!dr.visited.get(i) && min_distance_vertex.value() > dr.distance[i]){
                    // Armazena o vértice não visitado com menor custo de distância
                    min_distance_vertex = new Pair<>(i,dr.distance[i]);
                }
            }
            // Marca o vértice de menor custo de distância como visitado
            int x = min_distance_vertex.key();
            dr.visited.set(x, true);

            min_distance_vertex = new Pair<>(-1, Integer.MAX_VALUE);
            for(int y : g.getListaAdjacencia().get(x)){
                if(!dr.visited.get(y)){
                    if(dr.distance[y] > dr.distance[x]+w[x][y]){
                        // Atualiza o menor custo de distância de cada um dos vértices não visitados que incidem do último vértice visitado
                        dr.distance[y] = dr.distance[x]+w[x][y];
                        dr.predecessor[y] = x;
                    }
                }
            }
        }

        if(target-1 != -1){
            if (target-1 >= g.getVertices() || (target-1 != s-1 && dr.predecessor[target-1] == null)) {
                   dr.pathString = "Path to value not found";
            }else{
                // Caso exista um vértice alvo, realiza um backtracking dele para o vértice raiz
                LinkedList<Integer> path = new LinkedList<>();
                Stack<Integer> reverse_path = new Stack<>();

                int current_vertex = target-1;
                reverse_path.push(current_vertex);
                while(current_vertex != s-1){
                    // Adiciona os predecessores numa pilha
                    current_vertex = dr.predecessor[current_vertex];
                    reverse_path.push(current_vertex);
                }

                while(!reverse_path.isEmpty()){
                    // Adiciona os predecessores em uma lista, formando assim o menor caminho entre a raiz e o alvo
                    path.add(reverse_path.pop());
                }

                dr.path = path;
                dr.pathCost = dr.distance[target-1];
            }
        }

        // Retorna os resultados obtidos do algoritmo de Dijkstra
        return dr;
    }

    public static void main(String[] args) {
        try{
            System.out.println(" ------- Executing Dijkstra ------- ");
            Grafo g = Grafo.lerGrafoDeArquivo("grafos/dijkstra2_direcionado", true);

            DijkstraResult shortest_path = Dijkstra.execute(g, g.getMatrizIncidencia(), 1, 15);
            shortest_path.PrintResults();
            System.out.println("\n ------- Finished execution ------- ");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
