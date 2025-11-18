package es;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

/**
 * Testes para a classe AreaAvancada.
 * Verifica o cálculo de área média corrigida considerando componentes conexas.
 */
public class AreaAvancadaTest {

    /**
     * Testa cálculo com propriedades adjacentes do mesmo dono.
     * 
     * Cenário:
     * - Ana tem 3 propriedades em São Pedro
     * - Propriedades 1 e 2 são adjacentes (formam 1 grupo de 300m²)
     * - Propriedade 3 está isolada (forma 1 grupo de 300m²)
     * - Média = (300 + 300) / 2 = 300.0
     */
    @Test
    public void testAreaMediaCorrigidaComAdjacencia() {
        List<Propriedade> propriedades = List.of(
                // Ordem correta: objectId, parId, parNum, shapeLength, shapeArea, 
                //                geometry, owner, freguesia, municipio, ilha
                new Propriedade(1, 1.0, "P1", 100.0, 100.0, "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", 
                               "Ana", "São Pedro", "Funchal", "Madeira"),
                new Propriedade(2, 2.0, "P2", 100.0, 200.0, "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", 
                               "Ana", "São Pedro", "Funchal", "Madeira"),
                new Propriedade(3, 3.0, "P3", 100.0, 300.0, "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", 
                               "Ana", "São Pedro", "Funchal", "Madeira")
        );

        // Grafo: 1-2 são adjacentes, 3 está isolado
        Map<Integer, Set<Integer>> grafo = new HashMap<>();
        grafo.put(1, Set.of(2));
        grafo.put(2, Set.of(1));
        grafo.put(3, Set.of());

        double media = AreaAvancada.calcularAreaMediaCorrigida(propriedades, "freguesia", "São Pedro", grafo);
        
        // Grupo [1,2] = 100+200 = 300m²
        // Grupo [3] = 300m²
        // Média = (300 + 300) / 2 = 300.0
        assertEquals(300.0, media, 0.001);
    }

    /**
     * Testa com lista vazia - deve retornar 0.
     */
    @Test
    public void testAreaMediaCorrigidaSemDados() {
        List<Propriedade> propriedades = List.of();
        Map<Integer, Set<Integer>> grafo = new HashMap<>();
        
        double media = AreaAvancada.calcularAreaMediaCorrigida(propriedades, "freguesia", "Inexistente", grafo);
        
        assertEquals(0.0, media);
    }

    /**
     * Testa com região que não existe nas propriedades - deve retornar 0.
     */
    @Test
    public void testAreaMediaCorrigidaRegiaoInexistente() {
        List<Propriedade> propriedades = List.of(
                new Propriedade(1, 1.0, "P1", 100.0, 100.0, "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", 
                               "Ana", "São Pedro", "Funchal", "Madeira")
        );
        Map<Integer, Set<Integer>> grafo = new HashMap<>();
        grafo.put(1, Set.of());

        double media = AreaAvancada.calcularAreaMediaCorrigida(propriedades, "freguesia", "Lisboa", grafo);
        
        assertEquals(0.0, media);
    }

    /**
     * Testa com todas as propriedades de um dono formando uma componente conexa.
     */
    @Test
    public void testAreaMediaCorrigidaTodasConectadas() {
        List<Propriedade> propriedades = List.of(
                new Propriedade(1, 1.0, "P1", 100.0, 100.0, "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", 
                               "Ana", "São Pedro", "Funchal", "Madeira"),
                new Propriedade(2, 2.0, "P2", 100.0, 200.0, "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", 
                               "Ana", "São Pedro", "Funchal", "Madeira"),
                new Propriedade(3, 3.0, "P3", 100.0, 300.0, "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", 
                               "Ana", "São Pedro", "Funchal", "Madeira")
        );

        // Todas adjacentes: 1-2-3 formam uma cadeia
        Map<Integer, Set<Integer>> grafo = new HashMap<>();
        grafo.put(1, Set.of(2));
        grafo.put(2, Set.of(1, 3));
        grafo.put(3, Set.of(2));

        double media = AreaAvancada.calcularAreaMediaCorrigida(propriedades, "freguesia", "São Pedro", grafo);
        
        // Apenas 1 grupo = 100+200+300 = 600m²
        // Média = 600 / 1 = 600.0
        assertEquals(600.0, media, 0.001);
    }

    /**
     * Testa com múltiplos proprietários.
     */
    @Test
    public void testAreaMediaCorrigidaMultiplosProprietarios() {
        List<Propriedade> propriedades = List.of(
                // Ana: 2 propriedades adjacentes
                new Propriedade(1, 1.0, "P1", 100.0, 100.0, "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", 
                               "Ana", "São Pedro", "Funchal", "Madeira"),
                new Propriedade(2, 2.0, "P2", 100.0, 200.0, "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", 
                               "Ana", "São Pedro", "Funchal", "Madeira"),
                // João: 1 propriedade isolada
                new Propriedade(3, 3.0, "P3", 100.0, 400.0, "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", 
                               "João", "São Pedro", "Funchal", "Madeira")
        );

        Map<Integer, Set<Integer>> grafo = new HashMap<>();
        grafo.put(1, Set.of(2));
        grafo.put(2, Set.of(1));
        grafo.put(3, Set.of());

        double media = AreaAvancada.calcularAreaMediaCorrigida(propriedades, "freguesia", "São Pedro", grafo);
        
        // Ana: 1 grupo de 300m² (1+2)
        // João: 1 grupo de 400m²
        // Média = (300 + 400) / 2 = 350.0
        assertEquals(350.0, media, 0.001);
    }

    /**
     * Testa com propriedades isoladas (sem adjacências).
     */
    @Test
    public void testAreaMediaCorrigidaTodasIsoladas() {
        List<Propriedade> propriedades = List.of(
                new Propriedade(1, 1.0, "P1", 100.0, 100.0, "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", 
                               "Ana", "São Pedro", "Funchal", "Madeira"),
                new Propriedade(2, 2.0, "P2", 100.0, 200.0, "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", 
                               "Ana", "São Pedro", "Funchal", "Madeira"),
                new Propriedade(3, 3.0, "P3", 100.0, 300.0, "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", 
                               "Ana", "São Pedro", "Funchal", "Madeira")
        );

        // Nenhuma adjacência
        Map<Integer, Set<Integer>> grafo = new HashMap<>();
        grafo.put(1, Set.of());
        grafo.put(2, Set.of());
        grafo.put(3, Set.of());

        double media = AreaAvancada.calcularAreaMediaCorrigida(propriedades, "freguesia", "São Pedro", grafo);
        
        // 3 grupos isolados: 100, 200, 300
        // Média = (100 + 200 + 300) / 3 = 200.0
        assertEquals(200.0, media, 0.001);
    }

    /**
     * Testa filtro por município.
     */
    @Test
    public void testAreaMediaCorrigidaPorMunicipio() {
        List<Propriedade> propriedades = List.of(
                new Propriedade(1, 1.0, "P1", 100.0, 100.0, "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", 
                               "Ana", "São Pedro", "Funchal", "Madeira"),
                new Propriedade(2, 2.0, "P2", 100.0, 200.0, "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", 
                               "Ana", "Santa Luzia", "Funchal", "Madeira"),
                new Propriedade(3, 3.0, "P3", 100.0, 300.0, "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", 
                               "Ana", "Centro", "Câmara de Lobos", "Madeira")
        );

        Map<Integer, Set<Integer>> grafo = new HashMap<>();
        grafo.put(1, Set.of(2));
        grafo.put(2, Set.of(1));
        grafo.put(3, Set.of());

        // Filtrar por Funchal (apenas props 1 e 2)
        double media = AreaAvancada.calcularAreaMediaCorrigida(propriedades, "municipio", "Funchal", grafo);
        
        // 1 grupo de 300m² (1+2)
        // Média = 300 / 1 = 300.0
        assertEquals(300.0, media, 0.001);
    }

    /**
     * Testa filtro por ilha.
     */
    @Test
    public void testAreaMediaCorrigidaPorIlha() {
        List<Propriedade> propriedades = List.of(
                new Propriedade(1, 1.0, "P1", 100.0, 100.0, "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", 
                               "Ana", "São Pedro", "Funchal", "Madeira"),
                new Propriedade(2, 2.0, "P2", 100.0, 200.0, "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", 
                               "Ana", "Vila Baleira", "Porto Santo", "Porto Santo")
        );

        Map<Integer, Set<Integer>> grafo = new HashMap<>();
        grafo.put(1, Set.of());
        grafo.put(2, Set.of());

        // Filtrar por Madeira (apenas prop 1)
        double media = AreaAvancada.calcularAreaMediaCorrigida(propriedades, "ilha", "Madeira", grafo);
        
        // 1 grupo de 100m²
        assertEquals(100.0, media, 0.001);
    }
}