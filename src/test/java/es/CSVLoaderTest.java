package es;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CSVLoaderTest {

    @Test
    void testCarregarFicheiroValido() {
        // Testa o caminho feliz
        List<Propriedade> props = CSVLoader.carregarPropriedades("data/Madeira-Moodle-1.1.csv");
        assertNotNull(props);
        assertFalse(props.isEmpty());
    }

    @Test
    void testCarregarFicheiroInexistente() {
        // Testa o caminho de erro (try-catch)
        List<Propriedade> props = CSVLoader.carregarPropriedades("nao_existe.csv");
        assertNotNull(props);
        assertTrue(props.isEmpty());
    }
}