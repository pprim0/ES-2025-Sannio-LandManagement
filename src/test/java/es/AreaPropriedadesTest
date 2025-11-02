package es;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AreaPropriedadesTest {

    @Test
    public void testAreaMediaPorFreguesia() {
        List<Propriedade> propriedades = List.of(
                new Propriedade(1, 1, "P1", 10, 100.0, "geom", "Ana", "São Pedro", "Funchal", "Madeira"),
                new Propriedade(2, 2, "P2", 10, 300.0, "geom", "Bruno", "São Pedro", "Funchal", "Madeira")
        );

        double media = AreaPropriedades.calcularAreaMediaPorNivel(propriedades, "freguesia", "São Pedro");
        assertEquals(200.0, media, 0.001);
    }

    @Test
    public void testAreaMediaZonaVazia() {
        List<Propriedade> propriedades = List.of();
        double media = AreaPropriedades.calcularAreaMediaPorNivel(propriedades, "freguesia", "Inexistente");
        assertEquals(0.0, media);
    }

    @Test
    public void testTipoNivelInvalido() {
        List<Propriedade> propriedades = List.of(
                new Propriedade(1, 1, "P1", 10, 100, "geom", "João", "X", "Y", "Z")
        );
        double media = AreaPropriedades.calcularAreaMediaPorNivel(propriedades, "invalido", "X");
        assertEquals(0.0, media);
    }

    @Test
    public void testSemCorrespondencia() {
        List<Propriedade> propriedades = List.of(
                new Propriedade(1, 1, "P1", 10, 100, "geom", "João", "São Roque", "Funchal", "Madeira")
        );
        double media = AreaPropriedades.calcularAreaMediaPorNivel(propriedades, "freguesia", "Outra");
        assertEquals(0.0, media);
    }

    @Test
    public void testCalcularAreaTotalPorFreguesia() {
        List<Propriedade> propriedades = Arrays.asList(
            new Propriedade(1, 1, "P1", 10, 100.0, "geom", "Ana", "São Pedro", "Funchal", "Madeira"),
            new Propriedade(2, 2, "P2", 10, 200.0, "geom", "Bruno", "São Pedro", "Funchal", "Madeira"),
            new Propriedade(3, 3, "P3", 10, 300.0, "geom", "Carlos", "Sé", "Funchal", "Madeira")
        );

        Map<String, Double> areas = AreaPropriedades.calcularAreaTotalPorFreguesia(propriedades);

        assertEquals(2, areas.size());
        assertEquals(300.0, areas.get("São Pedro"), 0.001);
        assertEquals(300.0, areas.get("Sé"), 0.001);
    }

    @Test
    public void testCalcularAreaTotalPorMunicipio() {
        List<Propriedade> propriedades = Arrays.asList(
            new Propriedade(1, 1, "P1", 10, 100.0, "geom", "Ana", "X", "Funchal", "Madeira"),
            new Propriedade(2, 2, "P2", 10, 200.0, "geom", "Bruno", "Y", "Funchal", "Madeira"),
            new Propriedade(3, 3, "P3", 10, 300.0, "geom", "Carlos", "Z", "Câmara de Lobos", "Madeira")
        );

        Map<String, Double> areas = AreaPropriedades.calcularAreaTotalPorMunicipio(propriedades);

        assertEquals(2, areas.size());
        assertEquals(300.0, areas.get("Funchal"), 0.001);
        assertEquals(300.0, areas.get("Câmara de Lobos"), 0.001);
    }

    @Test
    public void testCalcularAreaTotalPorIlha() {
        List<Propriedade> propriedades = Arrays.asList(
            new Propriedade(1, 1, "P1", 10, 100.0, "geom", "Ana", "X", "Y", "Madeira"),
            new Propriedade(2, 2, "P2", 10, 200.0, "geom", "Bruno", "X", "Y", "Madeira"),
            new Propriedade(3, 3, "P3", 10, 300.0, "geom", "Carlos", "X", "Y", "Porto Santo")
        );

        Map<String, Double> areas = AreaPropriedades.calcularAreaTotalPorIlha(propriedades);

        assertEquals(2, areas.size());
        assertEquals(300.0, areas.get("Madeira"), 0.001);
        assertEquals(300.0, areas.get("Porto Santo"), 0.001);
    }

    @Test
    public void testCalcularAreaMediaPorTipo() {
        List<Propriedade> propriedades = Arrays.asList(
            new Propriedade(1, 1, "P1", 10, 100.0, "geom", "Ana", "São Pedro", "Funchal", "Madeira"),
            new Propriedade(2, 2, "P2", 10, 200.0, "geom", "Bruno", "São Pedro", "Funchal", "Madeira"),
            new Propriedade(3, 3, "P3", 10, 300.0, "geom", "Carlos", "Sé", "Funchal", "Madeira")
        );

        Map<String, Double> medias = AreaPropriedades.calcularAreaMediaPorTipo(propriedades, "freguesia");

        assertEquals(2, medias.size());
        assertEquals(150.0, medias.get("São Pedro"), 0.001);
        assertEquals(300.0, medias.get("Sé"), 0.001);
    }

    @Test
    public void testGetTopN() {
        Map<String, Double> areas = Map.of(
            "A", 100.0,
            "B", 300.0,
            "C", 200.0,
            "D", 400.0
        );

        List<Map.Entry<String, Double>> top2 = AreaPropriedades.getTopN(areas, 2);

        assertEquals(2, top2.size());
        assertEquals("D", top2.get(0).getKey());
        assertEquals(400.0, top2.get(0).getValue());
        assertEquals("B", top2.get(1).getKey());
        assertEquals(300.0, top2.get(1).getValue());
    }

    @Test
    public void testListaVazia() {
        List<Propriedade> propriedades = List.of();
        
        Map<String, Double> areasPorFreguesia = AreaPropriedades.calcularAreaTotalPorFreguesia(propriedades);
        Map<String, Double> areasPorMunicipio = AreaPropriedades.calcularAreaTotalPorMunicipio(propriedades);
        Map<String, Double> areasPorIlha = AreaPropriedades.calcularAreaTotalPorIlha(propriedades);
        
        assertTrue(areasPorFreguesia.isEmpty());
        assertTrue(areasPorMunicipio.isEmpty());
        assertTrue(areasPorIlha.isEmpty());
    }

    @Test
    public void testPropriedadesComNomesVazios() {
        List<Propriedade> propriedades = Arrays.asList(
            new Propriedade(1, 1, "P1", 10, 100.0, "geom", "Ana", "", "", ""),
            new Propriedade(2, 2, "P2", 10, 200.0, "geom", "Bruno", "São Pedro", "Funchal", "Madeira")
        );

        Map<String, Double> areas = AreaPropriedades.calcularAreaTotalPorFreguesia(propriedades);

        assertEquals(1, areas.size());
        assertEquals(200.0, areas.get("São Pedro"), 0.001);
    }

    @Test
    public void testGetTopNComMenosElementos() {
        Map<String, Double> areas = Map.of(
            "A", 100.0,
            "B", 200.0
        );

        List<Map.Entry<String, Double>> top5 = AreaPropriedades.getTopN(areas, 5);

        assertEquals(2, top5.size());
    }
}