package es;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;  // ← ADICIONAR ESTE IMPORT

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes melhorados para TestarExportadores com foco em aumentar coverage.
 */
public class TestarExportadoresTest {

    private static final String INDEX_FILE = "index.html";
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @AfterEach
    public void cleanup() {
        // Limpar ficheiros de teste
        File indexFile = new File(INDEX_FILE);
        if (indexFile.exists()) {
            indexFile.delete();
        }
    }

    // ==================== TESTES DO MÉTODO criarIndexHTML() ====================

    @Test
    public void testCriarIndexHTMLCriaFicheiro() {
        TestarExportadores.criarIndexHTML();
        
        File indexFile = new File(INDEX_FILE);
        assertTrue(indexFile.exists(), "index.html deveria existir");
        assertTrue(indexFile.length() > 0, "index.html não deveria estar vazio");
    }

    @Test
    public void testCriarIndexHTMLConteudoBasico() throws Exception {
        TestarExportadores.criarIndexHTML();
        
        File indexFile = new File(INDEX_FILE);
        String conteudo = Files.readString(indexFile.toPath());
        
        // Verificar elementos essenciais
        assertTrue(conteudo.contains("Sistema de Gestão Territorial") || 
                   conteudo.contains("Sistema de Gestao Territorial"),
                   "Deveria conter título do sistema");
        assertTrue(conteudo.contains("Adjacências") || conteudo.contains("Adjacencias"),
                   "Deveria conter referência a adjacências");
        assertTrue(conteudo.contains("Proprietários") || conteudo.contains("Proprietarios"),
                   "Deveria conter referência a proprietários");
    }

    @Test
    public void testCriarIndexHTMLEstruturalHTML() throws Exception {
        TestarExportadores.criarIndexHTML();
        
        File indexFile = new File(INDEX_FILE);
        String conteudo = Files.readString(indexFile.toPath());
        
        // Verificar estrutura HTML válida
        assertTrue(conteudo.contains("<!DOCTYPE html>"), "Deveria ter DOCTYPE");
        assertTrue(conteudo.contains("<html"), "Deveria ter tag html");
        assertTrue(conteudo.contains("</html>"), "Deveria fechar tag html");
        assertTrue(conteudo.contains("<head>"), "Deveria ter head");
        assertTrue(conteudo.contains("</head>"), "Deveria fechar head");
        assertTrue(conteudo.contains("<body>"), "Deveria ter body");
        assertTrue(conteudo.contains("</body>"), "Deveria fechar body");
        assertTrue(conteudo.contains("<title>"), "Deveria ter título");
    }

    @Test
    public void testCriarIndexHTMLTemCSS() throws Exception {
        TestarExportadores.criarIndexHTML();
        
        File indexFile = new File(INDEX_FILE);
        String conteudo = Files.readString(indexFile.toPath());
        
        // Verificar que tem estilos CSS
        assertTrue(conteudo.contains("<style>") || conteudo.contains("style"),
                   "Deveria ter estilos CSS");
        assertTrue(conteudo.contains("background") || conteudo.contains("color"),
                   "Deveria ter propriedades CSS");
    }

    @Test
    public void testCriarIndexHTMLTemJavaScript() throws Exception {
        TestarExportadores.criarIndexHTML();
        
        File indexFile = new File(INDEX_FILE);
        String conteudo = Files.readString(indexFile.toPath());
        
        // Verificar que tem JavaScript
        assertTrue(conteudo.contains("<script>") || conteudo.contains("function"),
                   "Deveria ter JavaScript");
    }

    @Test
    public void testCriarIndexHTMLTemIframes() throws Exception {
        TestarExportadores.criarIndexHTML();
        
        File indexFile = new File(INDEX_FILE);
        String conteudo = Files.readString(indexFile.toPath());
        
        // Verificar que referencia os grafos HTML
        assertTrue(conteudo.contains("grafo_adjacencias.html") || 
                   conteudo.contains("adjacencias"),
                   "Deveria referenciar grafo de adjacências");
        assertTrue(conteudo.contains("grafo_proprietarios.html") || 
                   conteudo.contains("proprietarios"),
                   "Deveria referenciar grafo de proprietários");
    }

    @Test
    public void testCriarIndexHTMLTemEstatisticas() throws Exception {
        TestarExportadores.criarIndexHTML();
        
        File indexFile = new File(INDEX_FILE);
        String conteudo = Files.readString(indexFile.toPath());
        
        // Verificar que mostra estatísticas
        assertTrue(conteudo.contains("Propriedades") || conteudo.contains("propriedades"),
                   "Deveria mencionar propriedades");
        
        // CORREÇÃO: Usar Pattern.find() em vez de matches()
        // Razão: matches() requer match da string INTEIRA, e . não corresponde a \n
        // find() procura o padrão em qualquer parte da string (idioma correto)
        assertTrue(Pattern.compile("\\d+").matcher(conteudo).find(),
                   "Deveria conter números (estatísticas)");
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
    public void testCriarIndexHTMLTamanhoMinimo() throws Exception {
        TestarExportadores.criarIndexHTML();
        
        File indexFile = new File(INDEX_FILE);
        assertTrue(indexFile.exists(), "Ficheiro deveria existir");
        assertTrue(indexFile.length() > 1000,
                   "Ficheiro deveria ter conteúdo substancial (>1KB)");
    }

    @Test
    public void testCriarIndexHTMLMetadata() throws Exception {
        TestarExportadores.criarIndexHTML();
        
        File indexFile = new File(INDEX_FILE);
        String conteudo = Files.readString(indexFile.toPath());
        
        // Verificar metadata
        assertTrue(conteudo.contains("UTF-8") || conteudo.contains("charset"),
                   "Deveria ter charset");
        assertTrue(conteudo.contains("viewport") || conteudo.contains("width=device-width"),
                   "Deveria ter viewport para responsive design");
    }

    @Test
    public void testCriarIndexHTMLTemTabs() throws Exception {
        TestarExportadores.criarIndexHTML();
        
        File indexFile = new File(INDEX_FILE);
        String conteudo = Files.readString(indexFile.toPath());
        
        // Verificar que tem sistema de tabs para alternar grafos
        assertTrue(conteudo.toLowerCase().contains("tab") || 
                   conteudo.contains("button") ||
                   conteudo.contains("showGraph"),
                   "Deveria ter sistema de navegação entre grafos");
    }

    @Test
    public void testCriarIndexHTMLSemErros() {
        // Verificar que não lança exceções
        assertDoesNotThrow(() -> TestarExportadores.criarIndexHTML(),
                          "Não deveria lançar exceções");
        
        // Verificar que imprime mensagem de sucesso
        String output = outContent.toString();
        assertTrue(output.contains("index.html criado") || 
                   output.contains("sucesso") ||
                   output.isEmpty(), // Pode não imprimir nada
                   "Deveria indicar sucesso ou não imprimir erro");
    }

    @Test
    public void testCriarIndexHTMLConteudoLegivel() throws Exception {
        TestarExportadores.criarIndexHTML();
        
        File indexFile = new File(INDEX_FILE);
        String conteudo = Files.readString(indexFile.toPath());
        
        // Verificar que não está vazio
        assertFalse(conteudo.isEmpty(), "Conteúdo não deveria estar vazio");
        
        // Verificar que tem conteúdo HTML substancial
        assertTrue(conteudo.length() > 500, "Deveria ter conteúdo substancial");
        
        // Verificar que não tem erros óbvios
        assertFalse(conteudo.contains("null"), "Não deveria conter 'null'");
        assertFalse(conteudo.contains("undefined"), "Não deveria conter 'undefined'");
    }

    @Test
    public void testCriarIndexHTMLUtf8Encoding() throws Exception {
        TestarExportadores.criarIndexHTML();
        
        File indexFile = new File(INDEX_FILE);
        String conteudo = Files.readString(indexFile.toPath());
        
        // Verificar que suporta caracteres portugueses
        assertTrue(conteudo.contains("UTF-8") || conteudo.contains("utf-8"),
                   "Deveria especificar encoding UTF-8");
        
        // O conteúdo já tem caracteres PT (ã, ç, etc.)
        assertTrue(conteudo.contains("Gestão") || conteudo.contains("Gestao"),
                   "Deveria ter texto em português");
    }

    @Test
    public void testCriarIndexHTMLResponsivo() throws Exception {
        TestarExportadores.criarIndexHTML();
        
        File indexFile = new File(INDEX_FILE);
        String conteudo = Files.readString(indexFile.toPath());
        
        // Verificar elementos de design responsivo
        assertTrue(conteudo.contains("viewport") || 
                   conteudo.contains("max-width") ||
                   conteudo.contains("width: 100%"),
                   "Deveria ter elementos de design responsivo");
    }

    // ==================== TESTE DE INTEGRAÇÃO SIMPLES ====================

    @Test
    public void testMetodosPublicosExistem() {
        // Verificar que os métodos públicos existem e são acessíveis
        assertDoesNotThrow(() -> {
            java.lang.reflect.Method criarIndex = TestarExportadores.class
                .getMethod("criarIndexHTML");
            assertNotNull(criarIndex, "Método criarIndexHTML deveria existir");
            
            java.lang.reflect.Method main = TestarExportadores.class
                .getMethod("main", String[].class);
            assertNotNull(main, "Método main deveria existir");
            assertTrue(java.lang.reflect.Modifier.isStatic(main.getModifiers()),
                      "Método main deveria ser estático");
            assertTrue(java.lang.reflect.Modifier.isPublic(main.getModifiers()),
                      "Método main deveria ser público");
        });
    }
}