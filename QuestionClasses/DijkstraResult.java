import Miscs.Grafo;

import java.util.ArrayList;
import java.util.LinkedList;

public class DijkstraResult {
    public int s;
    public int[] vertex;
    public ArrayList<Boolean> visited;
    public Integer[] predecessor;
    public int[] distance;
    public LinkedList<Integer> path;
    public int pathCost;
    public String pathString;

    public DijkstraResult(Grafo g, int s) {
        this.s = s;
        this.vertex = new int[g.getVertices()];
        this.visited = new ArrayList<>();
        for(int i = 0; i < g.getVertices(); i++) {
            this.vertex[i] = i;
            this.visited.add(false);
        }

        this.predecessor = new Integer[g.getVertices()];
        this.distance = new int[g.getVertices()];
        pathCost = -1;
    }

    /**
     * Imprime os resultados obtidos do algoritmo de Dijkstra
     */
    public void PrintResults() {
        System.out.print("Vertex          : ");
        for (int j : vertex) {
            System.out.printf("%4d ", j + 1);
        }

        System.out.print("\nVisited         : ");
        for (Boolean aBoolean : visited) {
            System.out.printf("%b ", aBoolean);
        }

        System.out.print("\nPredecessor     : ");
        for (Integer integer : predecessor) {
            if (integer == null) System.out.print("  -- ");
            else System.out.printf("%4d ", integer + 1);
        }

        System.out.printf("\nDistance from %2d: ", s);
        for (int j : distance) {
            System.out.printf("%4d ", j);
        }

        System.out.println();
        if(path != null){
            pathString = "";
            System.out.printf("\nPath from %d to target %d:      ", path.getFirst()+1, path.getLast()+1);
            for(int i : path){
                if(i == path.getLast()) System.out.printf("%d", i + 1);
                else System.out.printf("%d -> ", i + 1);
            }

            System.out.printf("     |     Total path cost:     %d", pathCost);
        }
    }
}
