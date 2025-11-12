package es;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class SugestaoTrocaAvancadaTest {

    private Propriedade criarPropriedade(int objectId, double area, double perimetro, String owner, String municipio) {
        return new Propriedade(
                objectId,             // objectId
                0,                    // parId
                String.valueOf(objectId), // parNum
                perimetro,            // shapeLength
                area,                 // shapeArea
                "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", // geometry WKT válido
                owner,                // owner
                "Freguesia",          // freguesia
                municipio,            // municipio
                "Ilha"                // ilha
        );
    }

    @Test
    public void testGerarSugestoesAvancadas() {
        // Propriedades do dono alvo "Alice"
        Propriedade p1 = criarPropriedade(1, 100.0, 30.0, "Alice", "Lisboa");
        Propriedade p2 = criarPropriedade(2, 120.0, 33.0, "Alice", "Porto");

        // Propriedades do outro dono "Bob"
        Propriedade p3 = criarPropriedade(3, 102.0, 29.0, "Bob", "Lisboa"); // mesma zona que p1
        Propriedade p4 = criarPropriedade(4, 98.0, 31.0, "Bob", "Porto");   // mesma zona que p2

        List<Propriedade> lista = List.of(p1, p2, p3, p4);
        List<SugestaoTroca> sugestoes = SugestaoTrocaAvancada.gerar(lista, "Alice");

        assertFalse(sugestoes.isEmpty());
        SugestaoTroca s = sugestoes.get(0);

        assertNotNull(s.getP1());
        assertNotNull(s.getP2());
        assertTrue(s.getScoreFinal() >= 0.7);
        assertTrue(s.getScoreArea() > 0);
        assertTrue(s.getScorePerimetro() > 0);
        assertTrue(s.getScoreCoerencia() > 0);
    }

    @Test
    public void testScoreBaixoNaoGeraSugestao() {
        // Áreas muito diferentes (> 40%)
        Propriedade p1 = criarPropriedade(1, 100.0, 30.0, "Alice", "Lisboa");
        Propriedade p2 = criarPropriedade(2, 200.0, 60.0, "Bob", "Porto");

        List<Propriedade> lista = List.of(p1, p2);
        List<SugestaoTroca> sugestoes = SugestaoTrocaAvancada.gerar(lista, "Alice");

        assertTrue(sugestoes.isEmpty()); // Diferença > 40% = filtrado
    }

    @Test
    public void testCoerenciaTerritorial() {
        // Alice tem propriedades em Lisboa
        Propriedade p1 = criarPropriedade(1, 100.0, 30.0, "Alice", "Lisboa");
        Propriedade p2 = criarPropriedade(2, 100.0, 30.0, "Alice", "Lisboa");

        // Bob tem propriedades em Lisboa (mesma zona que Alice)
        Propriedade p3 = criarPropriedade(3, 102.0, 29.0, "Bob", "Lisboa");

        List<Propriedade> lista = List.of(p1, p2, p3);
        List<SugestaoTroca> sugestoes = SugestaoTrocaAvancada.gerar(lista, "Alice");

        if (!sugestoes.isEmpty()) {
            SugestaoTroca s = sugestoes.get(0);
            // Coerência alta porque ambos já têm propriedades em Lisboa
            assertTrue(s.getScoreCoerencia() > 0.5);
        }
    }

    @Test
    public void testOrdenacaoPorScore() {
        Propriedade p1 = criarPropriedade(1, 100.0, 30.0, "Alice", "Lisboa");
        Propriedade p2 = criarPropriedade(2, 100.0, 30.0, "Alice", "Porto");
        
        Propriedade p3 = criarPropriedade(3, 101.0, 30.0, "Bob", "Lisboa");  // Alta similaridade
        Propriedade p4 = criarPropriedade(4, 110.0, 33.0, "Bob", "Porto");   // Média similaridade

        List<Propriedade> lista = List.of(p1, p2, p3, p4);
        List<SugestaoTroca> sugestoes = SugestaoTrocaAvancada.gerar(lista, "Alice");

        // Verificar ordenação decrescente
        for (int i = 0; i < sugestoes.size() - 1; i++) {
            assertTrue(sugestoes.get(i).getScoreFinal() >= sugestoes.get(i + 1).getScoreFinal());
        }
    }

    @Test
    public void testScoreMinimoDeSetentaPorCento() {
        // Criar cenário onde score ficaria entre 0.6 e 0.7
        Propriedade p1 = criarPropriedade(1, 100.0, 30.0, "Alice", "Lisboa");
        Propriedade p2 = criarPropriedade(2, 130.0, 40.0, "Bob", "Porto"); // Diferenças médias

        List<Propriedade> lista = List.of(p1, p2);
        List<SugestaoTroca> sugestoes = SugestaoTrocaAvancada.gerar(lista, "Alice");

        // Todas as sugestões devem ter score >= 0.7
        for (SugestaoTroca s : sugestoes) {
            assertTrue(s.getScoreFinal() >= 0.7);
        }
    }

    @Test
    public void testTresComponentesDoScore() {
        Propriedade p1 = criarPropriedade(1, 100.0, 30.0, "Alice", "Lisboa");
        Propriedade p2 = criarPropriedade(2, 105.0, 31.0, "Bob", "Lisboa");

        List<Propriedade> lista = List.of(p1, p2);
        List<SugestaoTroca> sugestoes = SugestaoTrocaAvancada.gerar(lista, "Alice");

        if (!sugestoes.isEmpty()) {
            SugestaoTroca s = sugestoes.get(0);
            
            // Verificar que os 3 scores foram calculados
            assertTrue(s.getScoreArea() > 0);
            assertTrue(s.getScorePerimetro() > 0);
            assertTrue(s.getScoreCoerencia() >= 0);
            
            // Verificar que score final é aproximadamente a média ponderada
            double expectedScore = 0.34 * s.getScoreArea() + 
                                  0.33 * s.getScorePerimetro() + 
                                  0.33 * s.getScoreCoerencia();
            assertEquals(expectedScore, s.getScoreFinal(), 0.01);
        }
    }
}