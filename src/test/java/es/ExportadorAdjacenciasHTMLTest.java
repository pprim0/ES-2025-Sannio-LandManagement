package es;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ExportadorAdjacenciasHTMLTest {

    private File tempHTML, tempJS;

    private void prepararFicheiros() throws Exception {
        tempHTML = File.createTempFile("grafo_test", ".html");
        tempJS = File.createTempFile("grafo_test", ".js");
        tempHTML.deleteOnExit();
        tempJS.deleteOnExit();
    }

    @Test
    public void testExportarCriaFicheirosValidos() throws Exception {
        prepararFicheiros();

        Propriedade p1 = new Propriedade(1, 1, "1", 40.0, 500.0,
                "POLYGON((0 0, 0 1, 1 1, 1 0, 0 0))", "Owner1", "Freguesia1", "Municipio1", "Ilha1");
        Propriedade p2 = new Propriedade(2, 2, "2", 30.0, 400.0,
                "POLYGON((1 0, 1 1, 2 1, 2 0, 1 0))", "Owner2", "Freguesia2", "Municipio2", "Ilha2");

        List<Propriedade> props = List.of(p1, p2);
        
        // Construir grafo de adjacências usando GrafoAdjacencias
        GrafoAdjacencias grafoAdj = new GrafoAdjacencias(props);
        Map<Integer, Set<Integer>> grafo = new HashMap<>();
        
        for (Propriedade p : props) {
            Set<Propriedade> vizinhos = grafoAdj.getVizinhos(p);
            Set<Integer> vizinhosIds = new HashSet<>();
            for (Propriedade v : vizinhos) {
                vizinhosIds.add(v.getObjectId());
            }
            grafo.put(p.getObjectId(), vizinhosIds);
        }

        ExportadorAdjacenciasHTML.exportar(props, grafo,
                tempHTML.getAbsolutePath(), tempJS.getAbsolutePath(), 2);

        assertTrue(tempHTML.exists() && tempHTML.length() > 0, "HTML nao criado");
        assertTrue(tempJS.exists() && tempJS.length() > 0, "JS nao criado");

        String jsContent = Files.readString(tempJS.toPath());
        assertTrue(jsContent.contains("\"id\":1"));
        assertTrue(jsContent.contains("\"id\":2"));
    }

    @Test
    public void testIgnoraGeometriaInvalida() throws Exception {
        prepararFicheiros();

        Propriedade invalida = new Propriedade(3, 3, "3", 25.0, 300.0,
                "INVALIDWKT", "Owner3", "Freguesia3", "Municipio3", "Ilha3");
        List<Propriedade> props = List.of(invalida);
        Map<Integer, Set<Integer>> grafo = new HashMap<>();

        ExportadorAdjacenciasHTML.exportar(props, grafo,
                tempHTML.getAbsolutePath(), tempJS.getAbsolutePath(), 1);

        String jsContent = Files.readString(tempJS.toPath());
        assertFalse(jsContent.contains("\"id\":3"), "Nao devia exportar geometria invalida");
    }

    @Test
    public void testLimiteDeNos() throws Exception {
        prepararFicheiros();

        List<Propriedade> props = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            props.add(new Propriedade(i, i, "" + i, 30.0, 500.0,
                    "POLYGON((0 0, 0 1, 1 1, 1 0, 0 0))", "Owner" + i, "Freg" + i, "Mun" + i, "Ilha" + i));
        }
        
        GrafoAdjacencias grafoAdj = new GrafoAdjacencias(props);
        Map<Integer, Set<Integer>> grafo = new HashMap<>();
        for (Propriedade p : props) {
            Set<Propriedade> vizinhos = grafoAdj.getVizinhos(p);
            Set<Integer> vizinhosIds = new HashSet<>();
            for (Propriedade v : vizinhos) {
                vizinhosIds.add(v.getObjectId());
            }
            grafo.put(p.getObjectId(), vizinhosIds);
        }

        ExportadorAdjacenciasHTML.exportar(props, grafo,
                tempHTML.getAbsolutePath(), tempJS.getAbsolutePath(), 3);

        String jsContent = Files.readString(tempJS.toPath());
        // Só deve haver 3 nós exportados
        int count = jsContent.split("\"id\":").length - 1;
        assertEquals(3, count, "Deveria exportar so 3 nos com limite=3");
    }

    @Test
    public void testHTMLContemScriptVisJS() throws Exception {
        prepararFicheiros();

        Propriedade p1 = new Propriedade(1, 1, "1", 40.0, 500.0,
                "POLYGON((0 0, 0 1, 1 1, 1 0, 0 0))", "Owner1", "F1", "M1", "I1");

        List<Propriedade> props = List.of(p1);
        Map<Integer, Set<Integer>> grafo = new HashMap<>();
        grafo.put(1, new HashSet<>());

        ExportadorAdjacenciasHTML.exportar(props, grafo,
                tempHTML.getAbsolutePath(), tempJS.getAbsolutePath(), 1);

        String htmlContent = Files.readString(tempHTML.toPath());
        assertTrue(htmlContent.contains("vis-network"));
        assertTrue(htmlContent.contains("new vis.Network"));
    }

    @Test
    public void testExportaComCoordenadaYInvertida() throws Exception {
        prepararFicheiros();

        // Propriedade em (0.5, 0.5) -> centróide deve ter y invertido
        Propriedade p1 = new Propriedade(1, 1, "1", 40.0, 500.0,
                "POLYGON((0 0, 0 1, 1 1, 1 0, 0 0))", "Owner1", "F1", "M1", "I1");

        List<Propriedade> props = List.of(p1);
        Map<Integer, Set<Integer>> grafo = new HashMap<>();
        grafo.put(1, new HashSet<>());

        ExportadorAdjacenciasHTML.exportar(props, grafo,
                tempHTML.getAbsolutePath(), tempJS.getAbsolutePath(), 1);

        String jsContent = Files.readString(tempJS.toPath());
        // Y deve ser negativo (invertido)
        assertTrue(jsContent.contains("\"y\":-"));
    }
}