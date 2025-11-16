package es;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de integração para TestarExportadores.
 * Verifica se os ficheiros HTML e JS são criados corretamente.
 */
public class TestarExportadoresTest {

    private static final String INDEX_FILE = "index.html";

    @AfterEach
    public void cleanup() {
        // Limpar ficheiro de teste após cada teste
        File indexFile = new File(INDEX_FILE);
        if (indexFile.exists()) {
            indexFile.delete();
        }
    }

    @Test
    public void testCriarIndexHTML() throws Exception {
        // Criar index.html
        TestarExportadores.criarIndexHTML();

        // Verificar que o ficheiro foi criado
        File indexFile = new File(INDEX_FILE);
        assertTrue(indexFile.exists(), "index.html deveria existir");
        assertTrue(indexFile.length() > 0, "index.html não deveria estar vazio");

        // Verificar conteúdo básico
        String conteudo = Files.readString(indexFile.toPath());
        assertTrue(conteudo.contains("Sistema de Gestão Territorial"), 
                   "Deveria conter título");
        assertTrue(conteudo.contains("Adjacências") || conteudo.contains("Adjacencias"), 
                   "Deveria conter referência a adjacências");
        assertTrue(conteudo.contains("Proprietários") || conteudo.contains("Proprietarios"), 
                   "Deveria conter referência a proprietários");
    }

    @Test
    public void testCriarIndexHTMLComHTMLValido() throws Exception {
        TestarExportadores.criarIndexHTML();
        
        File indexFile = new File(INDEX_FILE);
        String conteudo = Files.readString(indexFile.toPath());
        
        // Verificar estrutura HTML básica
        assertTrue(conteudo.contains("<!DOCTYPE html>"), "Deveria ter DOCTYPE");
        assertTrue(conteudo.contains("<html"), "Deveria ter tag html");
        assertTrue(conteudo.contains("</html>"), "Deveria fechar tag html");
        assertTrue(conteudo.contains("<head>"), "Deveria ter head");
        assertTrue(conteudo.contains("</head>"), "Deveria fechar head");
        assertTrue(conteudo.contains("<body>"), "Deveria ter body");
        assertTrue(conteudo.contains("</body>"), "Deveria fechar body");
    }

    @Test
    public void testCriarIndexHTMLComCSS() throws Exception {
        TestarExportadores.criarIndexHTML();
        
        File indexFile = new File(INDEX_FILE);
        String conteudo = Files.readString(indexFile.toPath());
        
        // Verificar que tem estilos CSS
        assertTrue(conteudo.contains("<style>") || conteudo.contains("style"), 
                   "Deveria ter estilos CSS");
    }

    @Test
    public void testCriarIndexHTMLComJavaScript() throws Exception {
        TestarExportadores.criarIndexHTML();
        
        File indexFile = new File(INDEX_FILE);
        String conteudo = Files.readString(indexFile.toPath());
        
        // Verificar que tem JavaScript
        assertTrue(conteudo.contains("<script>") || conteudo.contains("function"), 
                   "Deveria ter JavaScript");
    }

    @Test
    public void testCriarIndexHTMLComIframes() throws Exception {
        TestarExportadores.criarIndexHTML();
        
        File indexFile = new File(INDEX_FILE);
        String conteudo = Files.readString(indexFile.toPath());
        
        // Verificar que referencia os grafos
        assertTrue(conteudo.contains("grafo_adjacencias.html") || 
                   conteudo.contains("adjacencias"), 
                   "Deveria referenciar adjacências");
        assertTrue(conteudo.contains("grafo_proprietarios.html") || 
                   conteudo.contains("proprietarios"), 
                   "Deveria referenciar proprietários");
    }

    @Test
    public void testCriarIndexHTMLMetadata() throws Exception {
        TestarExportadores.criarIndexHTML();
        
        File indexFile = new File(INDEX_FILE);
        String conteudo = Files.readString(indexFile.toPath());
        
        // Verificar metadata
        assertTrue(conteudo.contains("UTF-8") || conteudo.contains("charset"), 
                   "Deveria ter charset");
        assertTrue(conteudo.contains("<title>"), "Deveria ter título");
    }

    @Test
    public void testCriarIndexHTMLMultiplasVezes() throws Exception {
        // Criar primeira vez
        TestarExportadores.criarIndexHTML();
        File indexFile = new File(INDEX_FILE);
        assertTrue(indexFile.exists(), "Ficheiro deveria existir após primeira criação");
        long tamanho1 = indexFile.length();
        
        // Aguardar um pouco
        Thread.sleep(50);
        
        // Criar segunda vez (deveria sobrescrever)
        TestarExportadores.criarIndexHTML();
        assertTrue(indexFile.exists(), "Ficheiro deveria continuar a existir");
        long tamanho2 = indexFile.length();
        
        // Verificar que foi sobrescrito (tamanho deve ser similar)
        assertTrue(Math.abs(tamanho1 - tamanho2) < 100, 
                   "Tamanhos devem ser similares após sobrescrever");
    }

    @Test
    public void testCriarIndexHTMLNaoVazio() throws Exception {
        TestarExportadores.criarIndexHTML();
        
        File indexFile = new File(INDEX_FILE);
        assertTrue(indexFile.exists(), "Ficheiro deveria existir");
        assertTrue(indexFile.length() > 1000, 
                   "Ficheiro deveria ter conteúdo substancial (>1KB)");
    }

    @Test
    public void testCriarIndexHTMLComConteudoLegivel() throws Exception {
        TestarExportadores.criarIndexHTML();
        
        File indexFile = new File(INDEX_FILE);
        String conteudo = Files.readString(indexFile.toPath());
        
        // Verificar que não está vazio
        assertFalse(conteudo.isEmpty(), "Conteúdo não deveria estar vazio");
        
        // Verificar que tem algum conteúdo HTML
        assertTrue(conteudo.length() > 500, "Deveria ter conteúdo substancial");
    }

    @Test
    public void testCriarIndexHTMLSemErros() {
        // Verificar que não lança exceções
        assertDoesNotThrow(() -> TestarExportadores.criarIndexHTML(),
                          "Não deveria lançar exceções");
    }

    @Test
    public void testMainMethodExiste() {
        // Verificar que o método main existe e é acessível
        assertDoesNotThrow(() -> {
            Class<?> clazz = TestarExportadores.class;
            java.lang.reflect.Method mainMethod = clazz.getMethod("main", String[].class);
            assertNotNull(mainMethod, "Método main deveria existir");
            assertTrue(java.lang.reflect.Modifier.isStatic(mainMethod.getModifiers()), 
                      "Método main deveria ser estático");
            assertTrue(java.lang.reflect.Modifier.isPublic(mainMethod.getModifiers()), 
                      "Método main deveria ser público");
        }, "Verificação do método main não deveria lançar exceção");
    }

    @Test
    public void testCriarIndexHTMLComHeader() throws Exception {
        TestarExportadores.criarIndexHTML();
        
        File indexFile = new File(INDEX_FILE);
        String conteudo = Files.readString(indexFile.toPath());
        
        // Verificar elementos do header
        assertTrue(conteudo.toLowerCase().contains("header") || 
                   conteudo.contains("Sistema") ||
                   conteudo.contains("Gestão"), 
                   "Deveria ter elementos de cabeçalho");
    }

    @Test
    public void testCriarIndexHTMLResponsivo() throws Exception {
        TestarExportadores.criarIndexHTML();
        
        File indexFile = new File(INDEX_FILE);
        String conteudo = Files.readString(indexFile.toPath());
        
        // Verificar elementos responsivos
        assertTrue(conteudo.contains("width") || conteudo.contains("responsive"), 
                   "Deveria ter elementos de design responsivo");
    }
}