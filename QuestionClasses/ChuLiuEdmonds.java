import Miscs.Aresta;
import Miscs.Grafo;
import Miscs.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class ChuLiuEdmonds {

    /**
     * Remove as arestas de entrada do alvo
     * @param g Grafo que será afetado
     * @param target Vértice que terá os vértices de entrada removidos
     */
    static void RemoveIncomingEdges(Grafo g, int target){
        int[][] new_matriz = g.getMatrizIncidencia();
        for (int i = 0; i < g.getVertices(); i++){
            new_matriz[i][target] = -1;
        }
        g.setMatrizIncidencia(new_matriz);
    }

    /**
     * Retorna uma matriz de adjacência invertida onde g[destino][origem] = G[origem][destino]
     * @param g Grafo original
     */
    static int[][] reverse_graph(Grafo g){
        int[][] matriz = g.getMatrizIncidencia();
        int[][] reverse_matrix = new int[g.getVertices()][];
        for (int src = 0; src < g.getVertices(); src++){
            for (int dst = 0; dst < g.getVertices(); dst++){
                if(reverse_matrix[dst] == null){
                    reverse_matrix[dst] = new int[g.getVertices()];
                }
                reverse_matrix[dst][src] = matriz[src][dst];
            }
        }

        return reverse_matrix;
    }

    /**
     * Encontra a aresta de entrada de menor custo para todos os vértices do grafo, com exceção da raiz
     * @param g Grafo em que o algoritmo vai executar
     * @param raiz Raiz do grafo
     * @param rg Matriz reversa de adjacência
     * @return Retorna um HashMap que mapeia um par contendo o vértice de origem e o peso para um vértice de destino
     */
    static HashMap<Integer,ArrayList<Pair<Integer,Integer>>> build_min(Grafo g, int raiz, int[][] rg){
        HashMap<Integer,ArrayList<Pair<Integer,Integer>>> mg = new HashMap<>();

        for(int dst = 0; dst < g.getVertices(); dst++){
            if(dst == raiz) continue;

            int min_ind = Integer.MAX_VALUE;
            int min_value = Integer.MAX_VALUE;
            for(int src = 0; src < g.getVertices(); src++){
                // Verifica qual aresta incidente em dst que possui o menor custo
                if(rg[dst][src] > -1 && rg[dst][src] <= min_value){
                    min_ind = src;
                    min_value = rg[dst][src];
                }
            }
            Pair<Integer, Integer> inner = new Pair<>(min_ind, min_value);


            if(mg.containsKey(dst)){
                mg.get(dst).add(inner);
            }else{
                // Adiciona o vértice no mapeamento
                ArrayList<Pair<Integer, Integer>> pairList = new ArrayList<>();
                pairList.add(inner);
                mg.put(dst, pairList);
            }
        }
        return mg;
    }

    /**
     * Detecta os ciclos do grafo a partir do mapeamento de menores custo de arestas de entrada de um grafo
     * @param mg Mapeamento dos arestas de entrada de menor custo para cada vértice do grafo
     * @return Retorna o primeiro ciclo do grafo encontrado
     */
    static ArrayList<Integer> find_circle(HashMap<Integer,ArrayList<Pair<Integer,Integer>>> mg){
        for(int startKey : mg.keySet()){
            Pair<Integer, Integer> start = mg.get(startKey).getFirst();

            ArrayList<Integer> visited = new ArrayList<>();
            Stack<Integer> stack = new Stack<>();
            stack.push(start.key());

            while (!stack.empty()){
                int n = stack.pop();
                if(visited.contains(n)){
                    ArrayList<Integer> C = new ArrayList<>();
                    while (!C.contains(n)){
                        C.add(n);
                        n = mg.get(n).getFirst().key();
                    }
                    return C;
                }
                visited.add(n);
                if(mg.containsKey(n)){
                    for(Pair<Integer, Integer> pair : mg.get(n)){
                        stack.push(pair.key());
                    }
                }
            }
        }
        return null;
    }

    /**
     * Executa o algoritmo de Chu-Liu/Edmonds
     *
     * @param g O grafo em que o algoritmo será executado
     * @param raiz O nó raiz, por onde o algoritmo começará
     * @return Retorna a árvore geradora mínima de g com a raiz definida.
     */
    public static Grafo execute(Grafo g, int raiz){
        Grafo modified_graph = new Grafo(g);

        // Remove os vértices de entrada da raiz, caso existam
        RemoveIncomingEdges(modified_graph, raiz);

        // Inverte o grafo
        int[][] rg = reverse_graph(g);

        // Mapeia as arestas de entrada de menor custo para cada vértice, exceto a raiz
        HashMap<Integer,ArrayList<Pair<Integer,Integer>>> mg = build_min(g, raiz, rg);

        // Detecta os ciclos do grafo
        ArrayList<Integer> C = find_circle(mg);

        // Se não for encontrado um ciclo, o grafo já é uma árvore geradora mínima
        if(C == null){
            // Retorna o grafo que representa a árvore geradora mínima
            return GenerateTree(mg);
        }

        // Mapeamento das arestas internas do ciclo e das arestas que entram no cíclo
        List<Aresta> internal_circle_edges = new ArrayList<>();
        List<Aresta> external_to_circle_edges = new ArrayList<>();
        HashMap<Integer, Aresta> mapping = new HashMap<>();

        Aresta min_cost_internal_edge = null;
        for(Aresta aresta: g.getListaArestas()){
            if(C.contains(aresta.getDestino())){
                if(C.contains(aresta.getOrigem())){
                    internal_circle_edges.add(aresta);
                    mapping.put(aresta.getDestino(), aresta);

                    if(min_cost_internal_edge == null) min_cost_internal_edge = aresta;
                    else if(min_cost_internal_edge.getPeso() > aresta.getPeso()) min_cost_internal_edge = aresta;
                }else{
                    external_to_circle_edges.add(aresta);
                }
            }
        }

        // Cálculo da aresta de menor valor que entra no cíclo
        Pair<Aresta, Integer> min_new_cost_external_to_circle_edge = null;
        Aresta edge_to_remove = null;
        for(Aresta edge: external_to_circle_edges){
            int new_value = edge.getPeso() - (mapping.get(edge.getDestino()).getPeso() - min_cost_internal_edge.getPeso());
            if(min_new_cost_external_to_circle_edge == null) {
                min_new_cost_external_to_circle_edge = new Pair<>(edge, edge.getPeso());
                edge_to_remove = mapping.get(edge.getDestino());
            }else if(min_new_cost_external_to_circle_edge.value() > new_value){
                min_new_cost_external_to_circle_edge = new Pair<>(edge, edge.getPeso());
                edge_to_remove = mapping.get(edge.getDestino());
            }
        }

        // Remoção da aresta interna que incide no vértice conectado com o lado de fora do ciclo no mapeamento das arestas de menor valor, quebrando o ciclo
        mg.remove(edge_to_remove.getDestino());

        // Adição da aresta que conecta os vértices externos ao mapeamento das arestas de menor
        Pair<Integer, Integer> inner = new Pair<>(min_new_cost_external_to_circle_edge.key().getOrigem(), min_new_cost_external_to_circle_edge.value());
        ArrayList<Pair<Integer, Integer>> pairList = new ArrayList<>();
        pairList.add(inner);
        mg.put(edge_to_remove.getDestino(), pairList);

        // Retorna o grafo que representa a árvore geradora mínima
        return GenerateTree(mg);
    }

    static void imprimirMatriz(int[][] m) {
        System.out.println("Matriz:");
        System.out.print("   ");
        // Cabeçalho com índices das colunas
        for (int i = 0; i < m.length; i++) {
            System.out.printf("%3d", i+1);
        }
        System.out.println();

        // Exibe cada linha da matriz
        for (int i = 0; i < m.length; i++) {
            System.out.printf("%2d:", i+1);
            for (int j = 0; j < m[i].length; j++) {
                System.out.printf("%3d", m[i][j]);
            }
            System.out.println();
        }
    }

    /**
     * Gera uma árvore geradora mínima utilizando o mapeamento das arestas de menor custo
     * @param mg Mapeamento
     * @return Uma árvore geradora mínima gerada pelo algortimo de Chu-Liu/Edmonds
     */
    static Grafo GenerateTree(HashMap<Integer,ArrayList<Pair<Integer,Integer>>> mg){
        Grafo grafo = new Grafo(mg.size()+1);
        for(Integer edge: mg.keySet()){
            int[][] matrix = grafo.getMatrizIncidencia();
            matrix[mg.get(edge).getFirst().key()][edge] = mg.get(edge).getFirst().value();
        }

        return grafo;
    }

    public static void main(String[] args) {
        try{
            System.out.println(" ------- Executing Chu-Liu/Edmonds ------- ");
            Grafo g = Grafo.lerGrafoDeArquivo("grafos/chuliu-edmonds", true);

            Grafo min_tree = ChuLiuEdmonds.execute(g, 0);
            min_tree.imprimirMatrizAdjacencia();
            System.out.println(" ------- Finished execution ------- ");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
