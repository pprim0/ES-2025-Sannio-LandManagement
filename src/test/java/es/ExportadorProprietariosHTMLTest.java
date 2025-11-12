package es;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ExportadorProprietariosHTMLTest {

    private static final String pastaSaida = "output_test/";
    private static final String nomeHTML = "grafo_test.html";
    private static final String nomeJS = "grafo_test.js";

    @Test
    public void testExportarCriaFicheirosValidos() throws Exception {
        // Grafo com IDs numéricos (para serem usados como nós)
        Map<String, Set<String>> grafo = new HashMap<>();
        grafo.put("1", Set.of("2"));
        grafo.put("2", Set.of("1"));

        prepararDiretorio();

        ExportadorProprietariosHTML.exportar(grafo, pastaSaida, nomeHTML, nomeJS);

        File html = new File(pastaSaida + nomeHTML);
        File js = new File(pastaSaida + nomeJS);

        assertTrue(html.exists());
        assertTrue(js.exists());

        String jsContent = Files.readString(js.toPath());
        assertTrue(jsContent.contains("\"id\":1"));
        assertTrue(jsContent.contains("\"id\":2"));
        assertTrue(jsContent.contains("\"from\":1"));
        assertTrue(jsContent.contains("\"to\":2"));
    }

    @Test
    public void testExportarIgnoraProprietariosNaoNumericos() throws Exception {
        // Um dos proprietários é não numérico, deve ser ignorado
        Map<String, Set<String>> grafo = new HashMap<>();
        grafo.put("Alice", Set.of("2"));
        grafo.put("2", Set.of("Alice"));

        prepararDiretorio();

        ExportadorProprietariosHTML.exportar(grafo, pastaSaida, nomeHTML, nomeJS);

        String jsContent = Files.readString(new File(pastaSaida + nomeJS).toPath());
        assertFalse(jsContent.contains("Alice"));
        assertTrue(jsContent.contains("\"id\":2"));
    }

    @Test
    public void testExportarNaoCriaArestaRepetida() throws Exception {
        // mesma aresta entre 1 e 2 aparece duas vezes, deve ser apenas uma
        Map<String, Set<String>> grafo = new HashMap<>();
        grafo.put("1", Set.of("2"));
        grafo.put("2", Set.of("1"));

        prepararDiretorio();

        ExportadorProprietariosHTML.exportar(grafo, pastaSaida, nomeHTML, nomeJS);
        String jsContent = Files.readString(new File(pastaSaida + nomeJS).toPath());

        // Confirma que só há uma aresta
        int ocorrencias = jsContent.split("from").length - 1;
        assertEquals(1, ocorrencias, "Deve haver apenas uma aresta entre 1 e 2");
    }

    @Test
    public void testGrafoVazio() throws Exception {
        Map<String, Set<String>> grafo = new HashMap<>();

        prepararDiretorio();

        ExportadorProprietariosHTML.exportar(grafo, pastaSaida, nomeHTML, nomeJS);

        File html = new File(pastaSaida + nomeHTML);
        File js = new File(pastaSaida + nomeJS);

        assertTrue(html.exists());
        assertTrue(js.exists());

        String jsContent = Files.readString(js.toPath());
        assertTrue(jsContent.contains("const nodes = []"));
        assertTrue(jsContent.contains("const allEdges = []"));
    }

    @Test
    public void testGrafoComMultiplosProprietarios() throws Exception {
        Map<String, Set<String>> grafo = new HashMap<>();
        grafo.put("1", Set.of("2", "3"));
        grafo.put("2", Set.of("1", "3"));
        grafo.put("3", Set.of("1", "2"));

        prepararDiretorio();

        ExportadorProprietariosHTML.exportar(grafo, pastaSaida, nomeHTML, nomeJS);

        String jsContent = Files.readString(new File(pastaSaida + nomeJS).toPath());
        
        assertTrue(jsContent.contains("\"id\":1"));
        assertTrue(jsContent.contains("\"id\":2"));
        assertTrue(jsContent.contains("\"id\":3"));
        
        // 3 nós totalmente conectados = 3 arestas
        int arestas = jsContent.split("from").length - 1;
        assertEquals(3, arestas);
    }

    private void prepararDiretorio() throws IOException {
        File dir = new File(pastaSaida);
        if (!dir.exists()) dir.mkdirs();

        Files.deleteIfExists(new File(pastaSaida + nomeHTML).toPath());
        Files.deleteIfExists(new File(pastaSaida + nomeJS).toPath());
    }
}