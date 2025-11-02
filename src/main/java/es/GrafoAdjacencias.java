package es;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKTReader;
import java.util.*;

/**
 * Classe responsável por construir um grafo de adjacências entre propriedades
 * usando JGraphT e JTS para análise geométrica.
 */
public class GrafoAdjacencias {
    
    private final Graph<Propriedade, DefaultEdge> grafo;
    private final Map<Integer, Propriedade> idToPropriedade;
    
    /**
     * Constrói o grafo de adjacências a partir de uma lista de propriedades.
     * 
     * @param propriedades Lista de propriedades com geometrias WKT
     */
    public GrafoAdjacencias(List<Propriedade> propriedades) {
        this.grafo = new SimpleGraph<>(DefaultEdge.class);
        this.idToPropriedade = new HashMap<>();
        construirGrafo(propriedades);
    }
    
    private void construirGrafo(List<Propriedade> propriedades) {
        // 1. Adicionar vértices
        for (Propriedade p : propriedades) {
            grafo.addVertex(p);
            idToPropriedade.put(p.getObjectId(), p);
        }
        
        // 2. Parse geometrias com JTS
        WKTReader reader = new WKTReader();
        Map<Propriedade, Geometry> geometrias = new HashMap<>();
        
        for (Propriedade p : propriedades) {
            try {
                Geometry geom = reader.read(p.getGeometry());
                if (geom.isValid()) {
                    geometrias.put(p, geom);
                }
            } catch (Exception e) {
                System.err.println("[ERRO] Geometria inválida ID: " + p.getObjectId());
            }
        }
        
        // 3. Detectar adjacências e criar arestas
        List<Propriedade> propList = new ArrayList<>(propriedades);
        for (int i = 0; i < propList.size(); i++) {
            Propriedade p1 = propList.get(i);
            Geometry geom1 = geometrias.get(p1);
            if (geom1 == null) continue;
            
            for (int j = i + 1; j < propList.size(); j++) {
                Propriedade p2 = propList.get(j);
                Geometry geom2 = geometrias.get(p2);
                if (geom2 == null) continue;
                
                // Usar JTS para detectar adjacência
                if (geom1.intersects(geom2)) {
                    grafo.addEdge(p1, p2);
                }
            }
        }
    }
    
    /**
     * Retorna o grafo JGraphT.
     */
    public Graph<Propriedade, DefaultEdge> getGrafo() {
        return grafo;
    }
    
    /**
     * Retorna propriedades adjacentes a uma dada propriedade.
     */
    public Set<Propriedade> getVizinhos(Propriedade p) {
        Set<DefaultEdge> edges = grafo.edgesOf(p);
        Set<Propriedade> vizinhos = new HashSet<>();
        
        for (DefaultEdge edge : edges) {
            Propriedade source = grafo.getEdgeSource(edge);
            Propriedade target = grafo.getEdgeTarget(edge);
            vizinhos.add(source.equals(p) ? target : source);
        }
        
        return vizinhos;
    }
    
    /**
     * Número de propriedades no grafo.
     */
    public int getNumVertices() {
        return grafo.vertexSet().size();
    }
    
    /**
     * Número de adjacências no grafo.
     */
    public int getNumArestas() {
        return grafo.edgeSet().size();
    }
    
    /**
     * Verifica se duas propriedades são adjacentes.
     */
    public boolean saoAdjacentes(Propriedade p1, Propriedade p2) {
        return grafo.containsEdge(p1, p2);
    }
    
    /**
     * Grau de uma propriedade (número de vizinhos).
     */
    public int getGrau(Propriedade p) {
        return grafo.degreeOf(p);
    }
}