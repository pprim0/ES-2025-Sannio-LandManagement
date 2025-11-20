package es;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GrafoAdjacencias {
    
    private Graph<Propriedade, DefaultEdge> grafo;
    private Map<Propriedade, Geometry> geometriaCache;

    public GrafoAdjacencias(List<Propriedade> propriedades) {
        this.grafo = new SimpleGraph<>(DefaultEdge.class);
        this.geometriaCache = new HashMap<>();
        construirGrafo(propriedades);
    }

    private void construirGrafo(List<Propriedade> propriedades) {
        WKTReader wktReader = new WKTReader();
        
        System.out.println("[INFO] Parseando geometrias...");
        long inicio = System.currentTimeMillis();
        
        // 1. PARSEAR TODAS AS GEOMETRIAS UMA ÚNICA VEZ (cache)
        for (Propriedade p : propriedades) {
            grafo.addVertex(p);
            
            try {
                Geometry geom = wktReader.read(p.getGeometry());
                if (geom != null && geom.isValid()) {
                    geometriaCache.put(p, geom);
                }
            } catch (ParseException e) {
                // Geometria inválida, não adiciona ao cache
            }
        }
        
        long tempoParse = System.currentTimeMillis() - inicio;
        System.out.println("[INFO] Geometrias parseadas em " + tempoParse + "ms");
        System.out.println("[INFO] Construindo adjacencias...");
        
        inicio = System.currentTimeMillis();
        
        // 2. CONSTRUIR ADJACÊNCIAS USANDO GEOMETRIAS JÁ PARSEADAS
        List<Propriedade> propriedadesValidas = new ArrayList<>(geometriaCache.keySet());
        
        for (int i = 0; i < propriedadesValidas.size(); i++) {
            Propriedade p1 = propriedadesValidas.get(i);
            Geometry geom1 = geometriaCache.get(p1);
            
            for (int j = i + 1; j < propriedadesValidas.size(); j++) {
                Propriedade p2 = propriedadesValidas.get(j);
                Geometry geom2 = geometriaCache.get(p2);
                
                if (geom1.intersects(geom2)) {
                    grafo.addEdge(p1, p2);
                }
            }
            
            // Progress indicator a cada 100 propriedades
            if ((i + 1) % 100 == 0) {
                System.out.println("[INFO] Processadas " + (i + 1) + "/" + propriedadesValidas.size() + " propriedades");
            }
        }
        
        long tempoAdjacencias = System.currentTimeMillis() - inicio;
        System.out.println("[INFO] Adjacencias construidas em " + (tempoAdjacencias / 1000) + "s");
    }

    public int getNumVertices() {
        return grafo.vertexSet().size();
    }

    public int getNumArestas() {
        return grafo.edgeSet().size();
    }

    public int getGrau(Propriedade propriedade) {
        return grafo.degreeOf(propriedade);
    }

    public Set<Propriedade> getVizinhos(Propriedade propriedade) {
        Set<Propriedade> vizinhos = new HashSet<>();
        Set<DefaultEdge> arestas = grafo.edgesOf(propriedade);
        
        for (DefaultEdge aresta : arestas) {
            Propriedade source = grafo.getEdgeSource(aresta);
            Propriedade target = grafo.getEdgeTarget(aresta);
            
            if (source.equals(propriedade)) {
                vizinhos.add(target);
            } else {
                vizinhos.add(source);
            }
        }
        
        return vizinhos;
    }

    public boolean saoAdjacentes(Propriedade p1, Propriedade p2) {
        return grafo.containsEdge(p1, p2);
    }

    public Set<Propriedade> getVertices() {
        return grafo.vertexSet();
    }

    public Graph<Propriedade, DefaultEdge> getGrafo() {
        return grafo;
    }
}