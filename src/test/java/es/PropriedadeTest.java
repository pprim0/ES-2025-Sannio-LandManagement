package es;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PropriedadeTest {

    @Test
    void testConstrutorEGetters() {
        // Criar uma propriedade com dados de teste
        Propriedade p = new Propriedade(
                1,              // objectId
                10.5,           // parId
                "123",          // parNum
                100.0,          // shapeLength
                200.0,          // shapeArea
                "POINT(0 0)",   // geometry
                "João",         // owner
                "Sé",           // freguesia
                "Funchal",      // municipio
                "Madeira"       // ilha
        );

        // Testar se cada campo foi guardado corretamente (cobertura total dos getters)
        assertEquals(1, p.getObjectId());
        assertEquals(10.5, p.getParId());
        assertEquals("123", p.getParNum());
        assertEquals(100.0, p.getShapeLength());
        assertEquals(200.0, p.getShapeArea());
        assertEquals("POINT(0 0)", p.getGeometry());
        assertEquals("João", p.getOwner());
        assertEquals("Sé", p.getFreguesia());
        assertEquals("Funchal", p.getMunicipio());
        assertEquals("Madeira", p.getIlha());
    }

    @Test
    void testValoresNulos() {
        // Testar robustez com nulos (garante que o construtor não crasha)
        Propriedade p = new Propriedade(2, 0, null, 0, 0, null, null, null, null, null);
        
        assertEquals(2, p.getObjectId());
        assertNull(p.getOwner());
        assertNull(p.getMunicipio());
    }
}