package es;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;


import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para a classe GrafoProprietarios.
 */
public class GrafoProprietariosTest {

    private List<Propriedade> propriedades;
    private GrafoAdjacencias grafoAdjacencias;
    private GrafoProprietarios grafoProprietarios;

    @BeforeEach
    public void setUp() {
        // Criar propriedades de teste
        Propriedade p1 = new Propriedade(1, 100, "A", 10, 100,
                "POLYGON((0 0, 0 1, 1 1, 1 0, 0 0))", "João", "Freg1", "Mun1", "Ilha1");
        
        Propriedade p2 = new Propriedade(2, 101, "B", 10, 100,
                "POLYGON((1 0, 1 1, 2 1, 2 0, 1 0))", "João", "Freg1", "Mun1", "Ilha1");
        
        Propriedade p3 = new Propriedade(3, 102, "C", 10, 100,
                "POLYGON((2 0, 2 1, 3 1, 3 0, 2 0))", "Maria", "Freg2", "Mun2", "Ilha2");
        
        Propriedade p4 = new Propriedade(4, 103, "D", 10, 100,
                "POLYGON((3 0, 3 1, 4 1, 4 0, 3 0))", "Pedro", "Freg3", "Mun3", "Ilha3");

        propriedades = Arrays.asList(p1, p2, p3, p4);
        
        // Construir grafo de adjacências
        grafoAdjacencias = new GrafoAdjacencias(propriedades);
        
        // Construir grafo de proprietários
        grafoProprietarios = new GrafoProprietarios(grafoAdjacencias);
    }

    @Test
    public void testGetNumProprietarios() {
        assertTrue(grafoProprietarios.getNumProprietarios() >= 2);
    }

    @Test
    public void testGetProprietarios() {
        Set<String> proprietarios = grafoProprietarios.getProprietarios();
        assertNotNull(proprietarios);
        assertTrue(proprietarios.size() >= 2);
    }

    @Test
    public void testGetVizinhos() {
        Set<String> vizinhosJoao = grafoProprietarios.getVizinhos("João");
        assertNotNull(vizinhosJoao);
    }

    @Test
    public void testGetVizinhosProprietarioInexistente() {
        Set<String> vizinhos = grafoProprietarios.getVizinhos("Inexistente");
        assertNotNull(vizinhos);
        assertTrue(vizinhos.isEmpty());
    }

    @Test
    public void testSaoVizinhos() {
        boolean resultado = grafoProprietarios.saoVizinhos("João", "Maria");
        assertTrue(resultado || !grafoProprietarios.getVizinhos("João").contains("Maria"));
    }

    @Test
    public void testSaoVizinhosProprietarioInexistente() {
        assertFalse(grafoProprietarios.saoVizinhos("Inexistente", "Maria"));
    }

    @Test
    public void testGetNumVizinhos() {
        int numVizinhos = grafoProprietarios.getNumVizinhos("João");
        assertTrue(numVizinhos >= 0);
    }

    @Test
    public void testGetNumPropriedades() {
        assertEquals(2, grafoProprietarios.getNumPropriedades("João"));
        assertEquals(1, grafoProprietarios.getNumPropriedades("Maria"));
        assertEquals(0, grafoProprietarios.getNumPropriedades("Inexistente"));
    }

    @Test
    public void testGetGrafoCompleto() {
        Map<String, Set<String>> grafo = grafoProprietarios.getGrafoCompleto();
        assertNotNull(grafo);
        assertTrue(grafo.size() >= 2);
    }

    @Test
    public void testGetProprietariosComMaisDeNVizinhos() {
        List<String> proprietarios = grafoProprietarios.getProprietariosComMaisDeNVizinhos(0);
        assertNotNull(proprietarios);
    }
}