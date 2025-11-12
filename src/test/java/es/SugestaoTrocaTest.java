package es;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class SugestaoTrocaTest {

    private Propriedade criarPropriedade(int objectId, double area, double perimetro, String owner) {
        return new Propriedade(
                objectId,           // objectId
                0,                  // parId
                String.valueOf(objectId), // parNum
                perimetro,          // shapeLength
                area,               // shapeArea
                "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", // geometry WKT válido
                owner,              // owner
                "Freguesia",        // freguesia
                "Municipio",        // municipio
                "Ilha"              // ilha
        );
    }

    @Test
    public void testToString() {
        Propriedade p1 = criarPropriedade(1, 100.0, 30.0, "Alice");
        Propriedade p2 = criarPropriedade(2, 105.0, 29.0, "Bob");

        SugestaoTroca s = new SugestaoTroca(p1, p2, 2.5, 0.9);
        s.setDetalhes(0.9, 0.0, 0.0);

        String result = s.toString();
        assertTrue(result.contains("Trocar [Alice - 100.00m2] com [Bob - 105.00m2]"));
        assertTrue(result.contains("Ganho: 2.50"));
        assertTrue(result.contains("Score: 0.90"));
        assertTrue(result.contains("Area: 0.90"));
    }

    @Test
    public void testGerarSugestoesSimples() {
        Propriedade p1 = criarPropriedade(1, 100.0, 30.0, "Alice");
        Propriedade p2 = criarPropriedade(2, 105.0, 29.0, "Bob");
        Propriedade p3 = criarPropriedade(3, 150.0, 32.0, "Alice"); // mesmo dono que p1

        List<Propriedade> lista = List.of(p1, p2, p3);
        List<SugestaoTroca> sugestoes = SugestaoTroca.gerar(lista, "Alice");

        assertEquals(1, sugestoes.size());

        SugestaoTroca s = sugestoes.get(0);
        assertEquals(p1, s.getP1());
        assertEquals(p2, s.getP2());
        assertTrue(s.getScoreArea() > 0.0);
    }

    @Test
    public void testNenhumaSugestaoParaAreasMuitoDiferentes() {
        Propriedade p1 = criarPropriedade(1, 100.0, 30.0, "Alice");
        Propriedade p2 = criarPropriedade(2, 200.0, 50.0, "Bob"); // > 30% diferença

        List<Propriedade> lista = List.of(p1, p2);
        List<SugestaoTroca> sugestoes = SugestaoTroca.gerar(lista, "Alice");

        assertTrue(sugestoes.isEmpty());
    }

    @Test
    public void testSugestaoParaAreasSemelhantes() {
        Propriedade p1 = criarPropriedade(1, 100.0, 30.0, "Alice");
        Propriedade p2 = criarPropriedade(2, 110.0, 32.0, "Bob"); // 10% diferença

        List<Propriedade> lista = List.of(p1, p2);
        List<SugestaoTroca> sugestoes = SugestaoTroca.gerar(lista, "Alice");

        assertEquals(1, sugestoes.size());
        SugestaoTroca s = sugestoes.get(0);
        assertTrue(s.getScoreArea() > 0.9); // Alta similaridade
    }

    @Test
    public void testOrdenacaoPorScore() {
        Propriedade p1 = criarPropriedade(1, 100.0, 30.0, "Alice");
        Propriedade p2 = criarPropriedade(2, 105.0, 29.0, "Bob");   // Score alto
        Propriedade p3 = criarPropriedade(3, 120.0, 35.0, "Carlos"); // Score médio
        Propriedade p4 = criarPropriedade(4, 100.0, 30.0, "Alice");

        List<Propriedade> lista = List.of(p1, p2, p3, p4);
        List<SugestaoTroca> sugestoes = SugestaoTroca.gerar(lista, "Alice");

        assertFalse(sugestoes.isEmpty());
        // Verificar que está ordenado por score decrescente
        for (int i = 0; i < sugestoes.size() - 1; i++) {
            assertTrue(sugestoes.get(i).getScoreFinal() >= sugestoes.get(i + 1).getScoreFinal());
        }
    }

    @Test
    public void testGetters() {
        Propriedade p1 = criarPropriedade(1, 100.0, 30.0, "Alice");
        Propriedade p2 = criarPropriedade(2, 105.0, 29.0, "Bob");

        SugestaoTroca s = new SugestaoTroca(p1, p2, 2.5, 0.9);
        s.setDetalhes(0.85, 0.90, 0.75);

        assertEquals(p1, s.getP1());
        assertEquals(p2, s.getP2());
        assertEquals(2.5, s.getGanhoAreaMedia(), 0.01);
        assertEquals(0.9, s.getScoreFinal(), 0.01);
        assertEquals(0.85, s.getScoreArea(), 0.01);
        assertEquals(0.90, s.getScorePerimetro(), 0.01);
        assertEquals(0.75, s.getScoreCoerencia(), 0.01);
    }
}