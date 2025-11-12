package es;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PropriedadeTest {

    @Test
    public void testCriacaoEGetters() {
        Propriedade p = new Propriedade(
                10, // objectId
                456.78, // parId
                "B789", // parNum
                100.5, // shapeLength
                500.25, // shapeArea
                "GEOMETRY_DATA", // geometry
                "Maria Santos", // owner
                "Sé", // freguesia
                "Funchal", // municipio
                "Madeira" // ilha
        );

        assertEquals(10, p.getObjectId());
        assertEquals(456.78, p.getParId());
        assertEquals("B789", p.getParNum());
        assertEquals(100.5, p.getShapeLength());
        assertEquals(500.25, p.getShapeArea());
        assertEquals("GEOMETRY_DATA", p.getGeometry());
        assertEquals("Maria Santos", p.getOwner());
        assertEquals("Sé", p.getFreguesia());
        assertEquals("Funchal", p.getMunicipio());
        assertEquals("Madeira", p.getIlha());
    }

    @Test
    public void testGettersSetters() {
        Propriedade p = new Propriedade(1, 100, "P1", 50, 250.5,
                "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))",
                "João Silva", "São Pedro", "Funchal", "Madeira");

        assertEquals(1, p.getObjectId());
        assertEquals(100, p.getParId());
        // ... resto dos getters
    }

    @Test
    public void testPropriedadeComValoresNulos() {
        Propriedade p = new Propriedade(1, 0, "", 0, 0, "", "", "", "", "");
        assertEquals(1, p.getObjectId());
    }
}