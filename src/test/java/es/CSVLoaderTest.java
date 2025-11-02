package es;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para a classe CSVLoader.
 */
public class CSVLoaderTest {

    /**
     * Testa o carregamento de um ficheiro CSV válido do classpath.
     */
    @Test
    public void testCarregarPropriedadesComCSVValido() {
        List<Propriedade> propriedades = CSVLoader.carregarPropriedades("Madeira-Moodle-1.1.csv");
        assertNotNull(propriedades, "A lista não deveria ser nula");
        assertFalse(propriedades.isEmpty(), "A lista não deveria estar vazia");
        assertNotNull(propriedades.get(0).getOwner(), "O proprietário não deveria ser nulo");
    }

    /**
     * Testa se a área da primeira propriedade é válida (maior que zero).
     */
    @Test
    public void testPropriedadeTemAreaValida() {
        List<Propriedade> propriedades = CSVLoader.carregarPropriedades("Madeira-Moodle-1.1.csv");
        assertFalse(propriedades.isEmpty(), "A lista de propriedades não deveria estar vazia");
        assertTrue(propriedades.get(0).getShapeArea() > 0, "A área da propriedade deve ser maior que 0");
    }

    /**
     * Testa a resposta quando o ficheiro não existe no classpath.
     */
    @Test
    public void testFicheiroInexistente() {
        List<Propriedade> propriedades = CSVLoader.carregarPropriedades("ficheiro_nao_existe.csv");
        assertNotNull(propriedades, "A lista não deve ser nula mesmo se o ficheiro não existir");
        assertTrue(propriedades.isEmpty(), "A lista deve estar vazia se o ficheiro não existir");
    }
}
