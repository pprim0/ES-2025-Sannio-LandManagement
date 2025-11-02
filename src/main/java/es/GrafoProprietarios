package es;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import java.util.*;

/**
 * Classe responsável por construir um grafo de vizinhança entre proprietários.
 * Dois proprietários são vizinhos se possuem propriedades adjacentes.
 */
public class GrafoProprietarios {

    private final Graph<String, DefaultEdge> grafo;
    
    /**
     * Constrói o grafo de proprietários a partir do grafo de adjacências de propriedades.
     * 
     * @param grafoAdjacencias Grafo de adjacências entre propriedades
     */
    public GrafoProprietarios(GrafoAdjacencias grafoAdjacencias) {
        this.grafo = new SimpleGraph<>(DefaultEdge.class);
        construirGrafo(grafoAdjacencias);
    }
    
    private void construirGrafo(GrafoAdjacencias grafoAdjacencias) {
        Graph<Propriedade, DefaultEdge> grafoProp = grafoAdjacencias.getGrafo();
        
        // Adicionar todos os proprietários como vértices
        Set<String> proprietarios = new HashSet<>();
        for (Propriedade p : grafoProp.vertexSet()) {
            String owner = p.getOwner();
            if (owner != null && !owner.isEmpty()) {
                proprietarios.add(owner);
            }
        }
        
        for (String owner : proprietarios) {
            grafo.addVertex(owner);
        }
        
        // Para cada aresta no grafo de propriedades
        for (DefaultEdge edge : grafoProp.edgeSet()) {
            Propriedade p1 = grafoProp.getEdgeSource(edge);
            Propriedade p2 = grafoProp.getEdgeTarget(edge);
            
            String owner1 = p1.getOwner();
            String owner2 = p2.getOwner();
            
            // Criar aresta entre proprietários diferentes
            if (owner1 != null && owner2 != null && 
                !owner1.equals(owner2) && 
                !grafo.containsEdge(owner1, owner2)) {
                grafo.addEdge(owner1, owner2);
            }
        }
    }
    
    /**
     * Retorna o grafo JGraphT de proprietários.
     */
    public Graph<String, DefaultEdge> getGrafo() {
        return grafo;
    }
    
    /**
     * Retorna os proprietários vizinhos de um dado proprietário.
     */
    public Set<String> getVizinhos(String proprietario) {
        Set<DefaultEdge> edges = grafo.edgesOf(proprietario);
        Set<String> vizinhos = new HashSet<>();
        
        for (DefaultEdge edge : edges) {
            String source = grafo.getEdgeSource(edge);
            String target = grafo.getEdgeTarget(edge);
            vizinhos.add(source.equals(proprietario) ? target : source);
        }
        
        return vizinhos;
    }
    
    /**
     * Número de proprietários no grafo.
     */
    public int getNumProprietarios() {
        return grafo.vertexSet().size();
    }
    
    /**
     * Número de relações de vizinhança.
     */
    public int getNumRelacoes() {
        return grafo.edgeSet().size();
    }
    
    /**
     * Verifica se dois proprietários são vizinhos.
     */
    public boolean saoVizinhos(String prop1, String prop2) {
        return grafo.containsEdge(prop1, prop2);
    }
}