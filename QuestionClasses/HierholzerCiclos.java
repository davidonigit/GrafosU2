import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class HierholzerCiclos {

    /**
     * Algoritmo de Hierholzer para grafos dirigidos.
     * Encontra um ciclo/caminho Euleriano.
     *
     * @param adj Lista de adjacência (Map<Vértice, ListaDeDestinos>)
     * @param v0 Vértice inicial
     * @return Lista de vértices no caminho/ciclo
     */
    public static List<String> hierholzer(Map<String, LinkedList<String>> adj, String v0) {
        // Pilha
        Stack<String> P = new Stack<>();
        // Lista
        LinkedList<String> L = new LinkedList<>();
        // v0
        String u = v0;

        // Enquanto a pilha ou o vértice atual tem aresta
        while (!P.isEmpty() || (adj.get(u) != null && !adj.get(u).isEmpty())) {

            // Vértice atual tem aresta não visitada
            if (adj.get(u) != null && !adj.get(u).isEmpty()) {
                P.push(u);

                // Pega o próximo destino e "visita" a aresta (removendo-a)
                String v = adj.get(u).removeFirst();
                u = v;
            }
            // Vértice atual não tem mais arestas
            else {
                // Adiciona u na lista final
                L.addFirst(u);
                // Volta para o vértice anterior para tentar outro caminho
                u = P.pop();
            }
        }

        // Adiciona o último vértice (que é o inicial)
        L.addFirst(u); // Adiciona o v0 final

        return L;
    }

    /**
     * Imprime o caminho formatado.
     */
    private static void imprimirCaminho(List<String> caminho) {
        for (int i = 0; i < caminho.size(); i++) {
            System.out.print(caminho.get(i));
            if (i < caminho.size() - 1) {
                System.out.print(" -> ");
            }
        }
        System.out.println();
    }

    /**
     * Cria o grafo de exemplo do Hierholzer (Ciclos).
     */
    private static Map<String, LinkedList<String>> criarGrafoHierholzer() {
        Map<String, LinkedList<String>> g = new HashMap<>();
        String[] rotulos = {"A", "B", "C", "D", "E", "F"};
        for (String r : rotulos) {
            g.put(r, new LinkedList<>());
        }

        g.get("A").add("B");
        g.get("A").add("F");
        g.get("B").add("A");
        g.get("B").add("D");
        g.get("C").add("B");
        g.get("D").add("B");
        g.get("D").add("C");
        g.get("E").add("A");
        g.get("E").add("D");
        g.get("F").add("E");

        return g;
    }

    public static void main(String[] args) {
        System.out.println("Executando Hierholzer (Ciclos)...");
        Map<String, LinkedList<String>> grafoH = criarGrafoHierholzer();

        String origemH = "E";

        List<String> cicloEuleriano = hierholzer(grafoH, origemH);

        System.out.println("Ciclo Euleriano encontrado:");
        imprimirCaminho(cicloEuleriano);
    }
}
