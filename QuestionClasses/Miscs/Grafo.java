package Miscs;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Grafo {
    private int vertices; // Número de vértices do grafo
    private List<List<Integer>> listaAdjacencia; // Lista de adjacência para representar conexões
    private List<Aresta> listaArestas; // Lista de arestas do grafo
    private int[][] matrizIncidencia; // Matriz de incidência (vértices x arestas)

    // Construtor: inicializa grafo com número de vértices especificado
    public Grafo(int vertices) {
        this.vertices = vertices;
        this.listaAdjacencia = new ArrayList<>();
        this.listaArestas = new ArrayList<>();
        this.matrizIncidencia = new int[vertices][vertices];

        // Cria lista vazia para cada vértice
        for (int i = 0; i < vertices; i++) {
            listaAdjacencia.add(new ArrayList<>());
            for(int j = 0; j < vertices; j++){
                matrizIncidencia[i][j] = -1;
            }
        }
    }

    public Grafo(Grafo g) {
        this.vertices = g.vertices;
        this.listaAdjacencia = g.listaAdjacencia;
        this.listaArestas = g.listaArestas;
        this.matrizIncidencia = g.matrizIncidencia;
    }

    public int getVertices() {
        return vertices;
    }

    public void setVertices(int vertices) {
        this.vertices = vertices;
    }

    public List<List<Integer>> getListaAdjacencia() {
        return listaAdjacencia;
    }

    public void setListaAdjacencia(List<List<Integer>> listaAdjacencia) {
        this.listaAdjacencia = listaAdjacencia;
    }

    public void setListaArestas(List<Aresta> listaArestas) {
        this.listaArestas = listaArestas;
    }

    public List<Aresta> getListaArestas() {
        return listaArestas;
    }

    public int[][] getMatrizIncidencia() {
        return matrizIncidencia;
    }

    public void setMatrizIncidencia(int[][] matrizIncidencia) {
        this.matrizIncidencia = matrizIncidencia;
    }

    // Adiciona aresta direcionada (origem → destino)
    public void adicionarAresta(int origem, int destino, int peso) {
        listaArestas.add(new Aresta(origem, destino, peso));
        listaAdjacencia.get(origem).add(destino);
    }

    // Adiciona aresta não direcionada (origem ↔ destino)
    public void adicionarArestaNaoDirecionada(int origem, int destino) {
        listaAdjacencia.get(origem).add(destino);
        listaAdjacencia.get(destino).add(origem);
    }

    // Exibe o grafo em formato de lista de adjacência
    public void imprimirGrafo() {
        System.out.println("Grafo:");
        for (int i = 0; i < vertices; i++) {
            System.out.print("Vértice " + (i+1) + ": ");
            for (Integer vizinho : listaAdjacencia.get(i)) {
                System.out.print(vizinho+1 + " ");
            }
            System.out.println();
        }
    }

    // Exibe matriz de adjacência formatada
    public void imprimirMatrizAdjacencia() {
        System.out.println("Matriz de Adjacência:");
        System.out.print("   ");
        // Cabeçalho com índices das colunas
        for (int i = 0; i < matrizIncidencia.length; i++) {
            System.out.printf("%3d", i+1);
        }
        System.out.println();

        // Exibe cada linha da matriz
        for (int i = 0; i < matrizIncidencia.length; i++) {
            System.out.printf("%2d:", i+1);
            for (int j = 0; j < matrizIncidencia[i].length; j++) {
                System.out.printf("%3d", matrizIncidencia[i][j]);
            }
            System.out.println();
        }
    }

    // Carrega grafo a partir de arquivo de texto
    // Formato: primeira linha = número de vértices, demais linhas = arestas
    // (origem,destino,peso)
    public static Grafo lerGrafoDeArquivo(String caminhoArquivo, boolean direcionado) throws FileNotFoundException {
        Scanner arquivo = new Scanner(new File(caminhoArquivo));

        // Lê número de vértices da primeira linha
        int vertices = Integer.parseInt(arquivo.nextLine().trim());
        Grafo grafo = new Grafo(vertices);

        // Lê cada linha de aresta
        while (arquivo.hasNextLine()) {
            String linha = arquivo.nextLine().trim();
            if (linha.isEmpty())
                continue;

            // Divide linha por vírgula (formato: origem,destino,peso)
            String[] partes = linha.split(",+");
            if (partes.length == 3) {
                int origem = Integer.parseInt(partes[0])-1;
                int destino = Integer.parseInt(partes[1])-1;
                int peso = Integer.parseInt(partes[2]);

                // Adiciona aresta baseado no tipo de grafo
                if (direcionado) {
                    grafo.adicionarAresta(origem, destino, peso);
                    grafo.matrizIncidencia[origem][destino] = peso;
                } else {
                    grafo.matrizIncidencia[origem][destino] = peso;
                    grafo.adicionarArestaNaoDirecionada(origem, destino);
                }
            }
        }

        arquivo.close();
        return grafo;
    }
}
