package es;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class JSONExporterTest {

    @Test
    public void testPropriedadesToJson() {
        List<Propriedade> propriedades = Arrays.asList(
            new Propriedade(1, 1, "P1", 10, 100.0, "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", 
                          "João", "São Pedro", "Funchal", "Madeira"),
            new Propriedade(2, 2, "P2", 20, 200.0, "POLYGON((1 0, 2 0, 2 1, 1 1, 1 0))", 
                          "Maria", "Sé", "Funchal", "Madeira")
        );

        String json = JSONExporter.propriedadesToJson(propriedades);

        assertNotNull(json);
        assertTrue(json.contains("\"objectId\":1"));
        assertTrue(json.contains("\"objectId\":2"));
        assertTrue(json.contains("João"));
        assertTrue(json.contains("Maria"));
    }

    @Test
    public void testExportarPropriedades(@TempDir Path tempDir) throws IOException {
        List<Propriedade> propriedades = Arrays.asList(
            new Propriedade(1, 1, "P1", 10, 100.0, "geom", "Ana", "X", "Y", "Z")
        );

        File outputFile = tempDir.resolve("propriedades.json").toFile();
        JSONExporter.exportarPropriedades(propriedades, outputFile.getAbsolutePath());

        assertTrue(outputFile.exists());
        assertTrue(outputFile.length() > 0);
    }

    @Test
    public void testAnaliseAreasToJson() {
        Map<String, Double> areas = new LinkedHashMap<>();
        areas.put("Funchal", 1000.0);
        areas.put("Câmara de Lobos", 500.0);

        String json = JSONExporter.analiseAreasToJson(areas, "município");

        assertNotNull(json);
        assertTrue(json.contains("\"tipo\":\"município\""));
        assertTrue(json.contains("\"total_regioes\":2"));
        assertTrue(json.contains("\"area_total\":1500"));
        assertTrue(json.contains("Funchal"));
        assertTrue(json.contains("Câmara de Lobos"));
    }

    @Test
    public void testExportarAnaliseAreas(@TempDir Path tempDir) throws IOException {
        Map<String, Double> areas = new HashMap<>();
        areas.put("Madeira", 2000.0);
        areas.put("Porto Santo", 500.0);

        File outputFile = tempDir.resolve("areas.json").toFile();
        JSONExporter.exportarAnaliseAreas(areas, outputFile.getAbsolutePath(), "ilha");

        assertTrue(outputFile.exists());

        // Ler e validar conteúdo
        JsonObject jsonObject = JsonParser.parseReader(new FileReader(outputFile)).getAsJsonObject();
        assertEquals("ilha", jsonObject.get("tipo").getAsString());
        assertEquals(2, jsonObject.get("total_regioes").getAsInt());
        assertEquals(2500.0, jsonObject.get("area_total").getAsDouble(), 0.001);
    }

    @Test
    public void testEstatisticasGrafoToJson() {
        List<Propriedade> propriedades = Arrays.asList(
            new Propriedade(1, 1, "P1", 10, 100.0, "POLYGON((0 0, 0 1, 1 1, 1 0, 0 0))", 
                          "Ana", "X", "Y", "Z"),
            new Propriedade(2, 2, "P2", 10, 200.0, "POLYGON((1 0, 1 1, 2 1, 2 0, 1 0))", 
                          "Bruno", "X", "Y", "Z")
        );

        GrafoAdjacencias grafo = new GrafoAdjacencias(propriedades);
        String json = JSONExporter.estatisticasGrafoToJson(grafo);

        assertNotNull(json);
        assertTrue(json.contains("\"num_vertices\":2"));
        assertTrue(json.contains("\"num_arestas\":1"));
        assertTrue(json.contains("\"grau_medio\":1"));
    }

    @Test
    public void testExportarEstatisticasGrafo(@TempDir Path tempDir) throws IOException {
        List<Propriedade> propriedades = Arrays.asList(
            new Propriedade(1, 1, "P1", 10, 100.0, "POLYGON((0 0, 0 1, 1 1, 1 0, 0 0))", 
                          "Ana", "X", "Y", "Z"),
            new Propriedade(2, 2, "P2", 10, 200.0, "POLYGON((1 0, 1 1, 2 1, 2 0, 1 0))", 
                          "Bruno", "X", "Y", "Z")
        );

        GrafoAdjacencias grafo = new GrafoAdjacencias(propriedades);
        File outputFile = tempDir.resolve("grafo_stats.json").toFile();
        
        JSONExporter.exportarEstatisticasGrafo(grafo, outputFile.getAbsolutePath());

        assertTrue(outputFile.exists());

        JsonObject jsonObject = JsonParser.parseReader(new FileReader(outputFile)).getAsJsonObject();
        assertEquals(2, jsonObject.get("num_vertices").getAsInt());
        assertEquals(1, jsonObject.get("num_arestas").getAsInt());
    }

    @Test
    public void testExportarRelatorioCompleto(@TempDir Path tempDir) throws IOException {
        List<Propriedade> propriedades = Arrays.asList(
            new Propriedade(1, 1, "P1", 10, 100.0, "POLYGON((0 0, 0 1, 1 1, 1 0, 0 0))", 
                          "Ana", "X", "Y", "Madeira"),
            new Propriedade(2, 2, "P2", 10, 200.0, "POLYGON((1 0, 1 1, 2 1, 2 0, 1 0))", 
                          "Bruno", "X", "Y", "Madeira")
        );

        Map<String, Double> areasPorIlha = AreaPropriedades.calcularAreaTotalPorIlha(propriedades);
        GrafoAdjacencias grafo = new GrafoAdjacencias(propriedades);

        File outputFile = tempDir.resolve("relatorio.json").toFile();
        JSONExporter.exportarRelatorioCompleto(propriedades, areasPorIlha, grafo, 
                                              outputFile.getAbsolutePath());

        assertTrue(outputFile.exists());

        JsonObject relatorio = JsonParser.parseReader(new FileReader(outputFile)).getAsJsonObject();
        assertEquals(2, relatorio.get("total_propriedades").getAsInt());
        assertTrue(relatorio.has("data_exportacao"));
        assertTrue(relatorio.has("analise_areas"));
        assertTrue(relatorio.has("estatisticas_grafo"));
    }

    @Test
    public void testPropriedadesVazias() {
        List<Propriedade> propriedades = List.of();
        String json = JSONExporter.propriedadesToJson(propriedades);

        assertNotNull(json);
        assertEquals("[]", json);
    }

    @Test
    public void testGrafoVazio() {
        List<Propriedade> propriedades = Arrays.asList(
            new Propriedade(1, 1, "P1", 10, 100.0, "POLYGON((0 0, 0 1, 1 1, 1 0, 0 0))", 
                          "Ana", "X", "Y", "Z"),
            new Propriedade(2, 2, "P2", 10, 200.0, "POLYGON((3 0, 3 1, 4 1, 4 0, 3 0))", 
                          "Bruno", "X", "Y", "Z")
        );

        GrafoAdjacencias grafo = new GrafoAdjacencias(propriedades);
        String json = JSONExporter.estatisticasGrafoToJson(grafo);

        assertNotNull(json);
        assertTrue(json.contains("\"num_vertices\":2"));
        assertTrue(json.contains("\"num_arestas\":0"));
        assertTrue(json.contains("\"grau_medio\":0"));
    }

    @Test
    public void testAreasVazias() {
        Map<String, Double> areas = new HashMap<>();
        String json = JSONExporter.analiseAreasToJson(areas, "freguesia");

        assertNotNull(json);
        assertTrue(json.contains("\"tipo\":\"freguesia\""));
        assertTrue(json.contains("\"total_regioes\":0"));
        assertTrue(json.contains("\"area_total\":0"));
    }
}