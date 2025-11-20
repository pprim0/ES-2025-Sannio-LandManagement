package es;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GrafoProprietarios {

    private final Map<String, Set<String>> adjacencias;
    private final Map<String, Integer> numPropriedades;

    public GrafoProprietarios(GrafoAdjacencias grafoAdjacencias) {
        this.adjacencias = new HashMap<>();
        this.numPropriedades = new HashMap<>();
        construir(grafoAdjacencias);
    }

    private void construir(GrafoAdjacencias grafoAdjacencias) {
        Set<Propriedade> vertices = grafoAdjacencias.getVertices();
        
        for (Propriedade p : vertices) {
            String owner = p.getOwner();
            numPropriedades.put(owner, numPropriedades.getOrDefault(owner, 0) + 1);
        }
        
        for (Propriedade p1 : vertices) {
            String dono1 = p1.getOwner();
            Set<Propriedade> vizinhas = grafoAdjacencias.getVizinhos(p1);
            
            for (Propriedade p2 : vizinhas) {
                String dono2 = p2.getOwner();
                if (!dono1.equals(dono2)) {
                    adjacencias.computeIfAbsent(dono1, k -> new HashSet<>()).add(dono2);
                }
            }
        }
    }

    public Set<String> getVizinhos(String owner) {
        return adjacencias.getOrDefault(owner, Collections.emptySet());
    }

    public boolean saoVizinhos(String owner1, String owner2) {
        return adjacencias.containsKey(owner1) && adjacencias.get(owner1).contains(owner2);
    }

    public int getNumVizinhos(String owner) {
        return getVizinhos(owner).size();
    }

    public Set<String> getProprietarios() {
        return new HashSet<>(adjacencias.keySet());
    }

    public int getNumProprietarios() {
        return adjacencias.size();
    }

    public int getNumPropriedades(String owner) {
        return numPropriedades.getOrDefault(owner, 0);
    }

    public Map<String, Set<String>> getGrafoCompleto() {
        return new HashMap<>(adjacencias);
    }

    public List<String> getProprietariosComMaisDeNVizinhos(int n) {
        List<String> resultado = new ArrayList<>();
        for (Map.Entry<String, Set<String>> entry : adjacencias.entrySet()) {
            if (entry.getValue().size() > n) {
                resultado.add(entry.getKey());
            }
        }
        return resultado;
    }
}