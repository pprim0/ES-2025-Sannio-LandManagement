package es;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

import java.util.*;

public class GrafoAdjacencias {
    
    private Graph<Propriedade, DefaultEdge> grafo;
    private WKTReader wktReader;

    public GrafoAdjacencias(List<Propriedade> propriedades) {
        this.grafo = new SimpleGraph<>(DefaultEdge.class);
        this.wktReader = new WKTReader();
        construirGrafo(propriedades);
    }

    private void construirGrafo(List<Propriedade> propriedades) {
        for (Propriedade p : propriedades) {
            grafo.addVertex(p);
        }

        for (int i = 0; i < propriedades.size(); i++) {
            Propriedade p1 = propriedades.get(i);
            Geometry geom1 = parseGeometry(p1.getGeometry());
            
            if (geom1 == null) continue;

            for (int j = i + 1; j < propriedades.size(); j++) {
                Propriedade p2 = propriedades.get(j);
                Geometry geom2 = parseGeometry(p2.getGeometry());
                
                if (geom2 == null) continue;

                if (geom1.intersects(geom2)) {
                    grafo.addEdge(p1, p2);
                }
            }
        }
    }

    private Geometry parseGeometry(String wkt) {
        try {
            return wktReader.read(wkt);
        } catch (ParseException e) {
            return null;
        }
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