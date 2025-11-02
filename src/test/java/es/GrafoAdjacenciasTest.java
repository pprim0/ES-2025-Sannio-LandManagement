package es;

import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class GrafoAdjacenciasTest {

    @Test
    public void testConstruirGrafoComGeometriasAdjacentes() {
        Propriedade p1 = new Propriedade(1, 123, "A", 0, 0,
                "POLYGON((0 0, 0 1, 1 1, 1 0, 0 0))", 
                "João", "Freg1", "Mun1", "Ilha1");

        Propriedade p2 = new Propriedade(2, 124, "B", 0, 0,
                "POLYGON((1 0, 1 1, 2 1, 2 0, 1 0))", 
                "Maria", "Freg2", "Mun2", "Ilha2");

        List<Propriedade> propriedades = Arrays.asList(p1, p2);
        GrafoAdjacencias grafo = new GrafoAdjacencias(propriedades);

        assertEquals(2, grafo.getNumVertices());
        assertEquals(1, grafo.getNumArestas());
        assertTrue(grafo.saoAdjacentes(p1, p2));
        assertTrue(grafo.getVizinhos(p1).contains(p2));
    }

    @Test
    public void testGrafoComGeometriaInvalida() {
        Propriedade p1 = new Propriedade(1, 123, "A", 0, 0,
                "INVALID_GEOM", "João", "Freg1", "Mun1", "Ilha1");

        Propriedade p2 = new Propriedade(2, 124, "B", 0, 0,
                "POLYGON((1 0, 1 1, 2 1, 2 0, 1 0))", 
                "Maria", "Freg2", "Mun2", "Ilha2");

        List<Propriedade> propriedades = Arrays.asList(p1, p2);
        GrafoAdjacencias grafo = new GrafoAdjacencias(propriedades);

        assertEquals(2, grafo.getNumVertices());
        assertEquals(0, grafo.getNumArestas());
    }

    @Test
    public void testGrafoSemAdjacencias() {
        Propriedade p1 = new Propriedade(1, 123, "A", 0, 0,
                "POLYGON((0 0, 0 1, 1 1, 1 0, 0 0))", 
                "João", "Freg1", "Mun1", "Ilha1");

        Propriedade p2 = new Propriedade(2, 124, "B", 0, 0,
                "POLYGON((2 0, 2 1, 3 1, 3 0, 2 0))", 
                "Maria", "Freg2", "Mun2", "Ilha2");

        List<Propriedade> propriedades = Arrays.asList(p1, p2);
        GrafoAdjacencias grafo = new GrafoAdjacencias(propriedades);

        assertEquals(2, grafo.getNumVertices());
        assertEquals(0, grafo.getNumArestas());
        assertFalse(grafo.saoAdjacentes(p1, p2));
    }

    @Test
    public void testGetGrau() {
        Propriedade p1 = new Propriedade(1, 123, "A", 0, 0,
                "POLYGON((0 0, 0 1, 1 1, 1 0, 0 0))", 
                "João", "Freg1", "Mun1", "Ilha1");

        Propriedade p2 = new Propriedade(2, 124, "B", 0, 0,
                "POLYGON((1 0, 1 1, 2 1, 2 0, 1 0))", 
                "Maria", "Freg2", "Mun2", "Ilha2");

        Propriedade p3 = new Propriedade(3, 125, "C", 0, 0,
                "POLYGON((1 1, 1 2, 2 2, 2 1, 1 1))", 
                "Carlos", "Freg3", "Mun3", "Ilha3");

        List<Propriedade> propriedades = Arrays.asList(p1, p2, p3);
        GrafoAdjacencias grafo = new GrafoAdjacencias(propriedades);

        assertEquals(2, grafo.getGrau(p1));  // ← p1 toca p2 E p3
        assertEquals(2, grafo.getGrau(p2));  // ← p2 toca p1 E p3
        assertEquals(2, grafo.getGrau(p3));  // ← p3 toca p1 E p2
    }
}