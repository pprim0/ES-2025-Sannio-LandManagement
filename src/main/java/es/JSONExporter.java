package es;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Classe responsável por exportar dados do sistema para formato JSON.
 * Utiliza a biblioteca GSON para serialização.
 */
public class JSONExporter {

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    /**
     * Exporta uma lista de propriedades para um ficheiro JSON.
     *
     * @param propriedades Lista de propriedades a exportar
     * @param filename     Nome do ficheiro de destino
     * @throws IOException Se ocorrer erro na escrita do ficheiro
     */
    public static void exportarPropriedades(List<Propriedade> propriedades, String filename) 
            throws IOException {
        try (FileWriter writer = new FileWriter(filename)) {
            gson.toJson(propriedades, writer);
        }
    }

    /**
     * Converte uma lista de propriedades para string JSON.
     *
     * @param propriedades Lista de propriedades
     * @return String JSON representando as propriedades
     */
    public static String propriedadesToJson(List<Propriedade> propriedades) {
        return gson.toJson(propriedades);
    }

    /**
     * Exporta análise de áreas por região para JSON.
     *
     * @param areasPorRegiao Mapa com regiões e áreas
     * @param filename       Nome do ficheiro de destino
     * @param tipoRegiao     Tipo de região (freguesia, município, ilha)
     * @throws IOException Se ocorrer erro na escrita do ficheiro
     */
    public static void exportarAnaliseAreas(Map<String, Double> areasPorRegiao, 
                                           String filename, 
                                           String tipoRegiao) throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("tipo", tipoRegiao);
        jsonObject.addProperty("total_regioes", areasPorRegiao.size());
        
        // Calcular área total
        double areaTotal = areasPorRegiao.values().stream()
                .mapToDouble(Double::doubleValue)
                .sum();
        jsonObject.addProperty("area_total", areaTotal);
        
        // Adicionar dados por região
        jsonObject.add("areas_por_regiao", gson.toJsonTree(areasPorRegiao));
        
        try (FileWriter writer = new FileWriter(filename)) {
            gson.toJson(jsonObject, writer);
        }
    }

    /**
     * Converte análise de áreas para string JSON.
     *
     * @param areasPorRegiao Mapa com regiões e áreas
     * @param tipoRegiao     Tipo de região
     * @return String JSON
     */
    public static String analiseAreasToJson(Map<String, Double> areasPorRegiao, String tipoRegiao) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("tipo", tipoRegiao);
        jsonObject.addProperty("total_regioes", areasPorRegiao.size());
        
        double areaTotal = areasPorRegiao.values().stream()
                .mapToDouble(Double::doubleValue)
                .sum();
        jsonObject.addProperty("area_total", areaTotal);
        jsonObject.add("areas_por_regiao", gson.toJsonTree(areasPorRegiao));
        
        return gson.toJson(jsonObject);
    }

    /**
     * Exporta estatísticas do grafo de adjacências para JSON.
     *
     * @param grafo    Grafo de adjacências
     * @param filename Nome do ficheiro de destino
     * @throws IOException Se ocorrer erro na escrita do ficheiro
     */
    public static void exportarEstatisticasGrafo(GrafoAdjacencias grafo, String filename) 
            throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("num_vertices", grafo.getNumVertices());
        jsonObject.addProperty("num_arestas", grafo.getNumArestas());
        
        // Calcular grau médio
        if (grafo.getNumVertices() > 0) {
            double grauMedio = (2.0 * grafo.getNumArestas()) / grafo.getNumVertices();
            jsonObject.addProperty("grau_medio", grauMedio);
        } else {
            jsonObject.addProperty("grau_medio", 0.0);
        }
        
        try (FileWriter writer = new FileWriter(filename)) {
            gson.toJson(jsonObject, writer);
        }
    }

    /**
     * Converte estatísticas do grafo para string JSON.
     *
     * @param grafo Grafo de adjacências
     * @return String JSON
     */
    public static String estatisticasGrafoToJson(GrafoAdjacencias grafo) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("num_vertices", grafo.getNumVertices());
        jsonObject.addProperty("num_arestas", grafo.getNumArestas());
        
        if (grafo.getNumVertices() > 0) {
            double grauMedio = (2.0 * grafo.getNumArestas()) / grafo.getNumVertices();
            jsonObject.addProperty("grau_medio", grauMedio);
        } else {
            jsonObject.addProperty("grau_medio", 0.0);
        }
        
        return gson.toJson(jsonObject);
    }

    /**
     * Exporta relatório completo para JSON.
     *
     * @param propriedades   Lista de propriedades
     * @param areasPorIlha   Áreas por ilha
     * @param grafo          Grafo de adjacências
     * @param filename       Nome do ficheiro de destino
     * @throws IOException Se ocorrer erro na escrita do ficheiro
     */
    public static void exportarRelatorioCompleto(List<Propriedade> propriedades,
                                                 Map<String, Double> areasPorIlha,
                                                 GrafoAdjacencias grafo,
                                                 String filename) throws IOException {
        JsonObject relatorio = new JsonObject();
        
        // Informações gerais
        relatorio.addProperty("total_propriedades", propriedades.size());
        relatorio.addProperty("data_exportacao", java.time.LocalDateTime.now().toString());
        
        // Análise de áreas
        JsonObject analiseAreas = new JsonObject();
        analiseAreas.addProperty("total_ilhas", areasPorIlha.size());
        double areaTotal = areasPorIlha.values().stream().mapToDouble(Double::doubleValue).sum();
        analiseAreas.addProperty("area_total", areaTotal);
        analiseAreas.add("areas_por_ilha", gson.toJsonTree(areasPorIlha));
        relatorio.add("analise_areas", analiseAreas);
        
        // Estatísticas do grafo
        JsonObject estatisticasGrafo = new JsonObject();
        estatisticasGrafo.addProperty("num_vertices", grafo.getNumVertices());
        estatisticasGrafo.addProperty("num_arestas", grafo.getNumArestas());
        if (grafo.getNumVertices() > 0) {
            double grauMedio = (2.0 * grafo.getNumArestas()) / grafo.getNumVertices();
            estatisticasGrafo.addProperty("grau_medio", grauMedio);
        }
        relatorio.add("estatisticas_grafo", estatisticasGrafo);
        
        try (FileWriter writer = new FileWriter(filename)) {
            gson.toJson(relatorio, writer);
        }
    }
}