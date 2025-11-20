package es;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

public class GrafoAdjacenciasTest {

    @Test
    public void testGrafoComDuasPropriedadesAdjacentes() {
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
    }

    @Test
    public void testGrafoComPropriedadesNaoAdjacentes() {
        Propriedade p1 = new Propriedade(1, 123, "A", 0, 0,
                "POLYGON((0 0, 0 1, 1 1, 1 0, 0 0))", 
                "João", "Freg1", "Mun1", "Ilha1");

        Propriedade p2 = new Propriedade(2, 124, "B", 0, 0,
                "POLYGON((5 0, 5 1, 6 1, 6 0, 5 0))", 
                "Maria", "Freg2", "Mun2", "Ilha2");

        List<Propriedade> propriedades = Arrays.asList(p1, p2);
        GrafoAdjacencias grafo = new GrafoAdjacencias(propriedades);

        assertEquals(2, grafo.getNumVertices());
        assertEquals(0, grafo.getNumArestas());
        assertFalse(grafo.saoAdjacentes(p1, p2));
    }

    @Test
    public void testGrafoVazio() {
        List<Propriedade> propriedades = new ArrayList<>();
        GrafoAdjacencias grafo = new GrafoAdjacencias(propriedades);

        assertEquals(0, grafo.getNumVertices());
        assertEquals(0, grafo.getNumArestas());
    }

    @Test
    public void testGetVizinhos() {
        Propriedade p1 = new Propriedade(1, 123, "A", 0, 0,
                "POLYGON((0 0, 0 1, 1 1, 1 0, 0 0))", 
                "João", "Freg1", "Mun1", "Ilha1");

        Propriedade p2 = new Propriedade(2, 124, "B", 0, 0,
                "POLYGON((1 0, 1 1, 2 1, 2 0, 1 0))", 
                "Maria", "Freg2", "Mun2", "Ilha2");

        List<Propriedade> propriedades = Arrays.asList(p1, p2);
        GrafoAdjacencias grafo = new GrafoAdjacencias(propriedades);

        Set<Propriedade> vizinhosP1 = grafo.getVizinhos(p1);
        Set<Propriedade> vizinhosP2 = grafo.getVizinhos(p2);

        assertEquals(1, vizinhosP1.size());
        assertTrue(vizinhosP1.contains(p2));
        assertEquals(1, vizinhosP2.size());
        assertTrue(vizinhosP2.contains(p1));
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
                "POLYGON((0 1, 0 2, 1 2, 1 1, 0 1))", 
                "Carlos", "Freg3", "Mun3", "Ilha3");

        List<Propriedade> propriedades = Arrays.asList(p1, p2, p3);
        GrafoAdjacencias grafo = new GrafoAdjacencias(propriedades);

        assertEquals(2, grafo.getGrau(p1));
        assertEquals(2, grafo.getGrau(p2));
        assertEquals(2, grafo.getGrau(p3));
    }

    @Test
    public void testGetVertices() {
        Propriedade p1 = new Propriedade(1, 123, "A", 0, 0,
                "POLYGON((0 0, 0 1, 1 1, 1 0, 0 0))", 
                "João", "Freg1", "Mun1", "Ilha1");

        Propriedade p2 = new Propriedade(2, 124, "B", 0, 0,
                "POLYGON((1 0, 1 1, 2 1, 2 0, 1 0))", 
                "Maria", "Freg2", "Mun2", "Ilha2");

        List<Propriedade> propriedades = Arrays.asList(p1, p2);
        GrafoAdjacencias grafo = new GrafoAdjacencias(propriedades);

        Set<Propriedade> vertices = grafo.getVertices();
        assertEquals(2, vertices.size());
        assertTrue(vertices.contains(p1));
        assertTrue(vertices.contains(p2));
    }

    @Test
    public void testPropriedadeIsolada() {
        Propriedade p1 = new Propriedade(1, 123, "A", 0, 0,
                "POLYGON((0 0, 0 1, 1 1, 1 0, 0 0))", 
                "João", "Freg1", "Mun1", "Ilha1");

        List<Propriedade> propriedades = List.of(p1);
        GrafoAdjacencias grafo = new GrafoAdjacencias(propriedades);

        assertEquals(1, grafo.getNumVertices());
        assertEquals(0, grafo.getNumArestas());
        assertEquals(0, grafo.getGrau(p1));
        assertTrue(grafo.getVizinhos(p1).isEmpty());
    }

    @Test
    public void testTresPropriedadesEmLinha() {
        Propriedade p1 = new Propriedade(1, 123, "A", 0, 0,
                "POLYGON((0 0, 0 1, 1 1, 1 0, 0 0))", 
                "João", "Freg1", "Mun1", "Ilha1");

        Propriedade p2 = new Propriedade(2, 124, "B", 0, 0,
                "POLYGON((1 0, 1 1, 2 1, 2 0, 1 0))", 
                "Maria", "Freg2", "Mun2", "Ilha2");

        Propriedade p3 = new Propriedade(3, 125, "C", 0, 0,
                "POLYGON((2 0, 2 1, 3 1, 3 0, 2 0))", 
                "Carlos", "Freg3", "Mun3", "Ilha3");

        List<Propriedade> propriedades = Arrays.asList(p1, p2, p3);
        GrafoAdjacencias grafo = new GrafoAdjacencias(propriedades);

        assertEquals(3, grafo.getNumVertices());
        assertEquals(2, grafo.getNumArestas());
        assertEquals(1, grafo.getGrau(p1));
        assertEquals(2, grafo.getGrau(p2));
        assertEquals(1, grafo.getGrau(p3));
        assertFalse(grafo.saoAdjacentes(p1, p3));
    }
}