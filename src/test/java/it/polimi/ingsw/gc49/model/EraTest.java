package it.polimi.ingsw.gc49.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EraTest {

    @Test
    void testFirst_returnsFirstEra() {
        Era era = Era.FIRST;
        assertEquals(Era.FIRST, era.first());
    }

    @Test
    void testLast_returnsLastEra() {
        Era era = Era.FIRST;
        assertEquals(Era.THIRD_FINAL, era.last());
    }

    @Test
    void testNext_fromFirst() {
        Era era = Era.FIRST;
        assertEquals(Era.SECOND, era.next());
    }

    @Test
    void testNext_fromSecond() {
        Era era = Era.SECOND;
        assertEquals(Era.THIRD, era.next());
    }

    @Test
    void testNext_fromThird() {
        Era era = Era.THIRD;
        assertEquals(Era.THIRD_FINAL, era.next());
    }

    @Test
    void testNext_fromThirdFinal_returnsNull() {
        Era era = Era.THIRD_FINAL;
        assertNull(era.next(), "Next era after THIRD_FINAL should be null");
    }

    @Test
    void testIsFinal_firstEra() {
        Era era = Era.FIRST;
        assertFalse(era.isFinal(), "FIRST era should not be final");
    }

    @Test
    void testIsFinal_secondEra() {
        Era era = Era.SECOND;
        assertFalse(era.isFinal(), "SECOND era should not be final");
    }

    @Test
    void testIsFinal_thirdEra() {
        Era era = Era.THIRD;
        assertFalse(era.isFinal(), "THIRD era should not be final");
    }

    @Test
    void testIsFinal_thirdFinalEra() {
        Era era = Era.THIRD_FINAL;
        assertTrue(era.isFinal(), "THIRD_FINAL era should be final");
    }

    @Test
    void testEraSequence() {
        // Verifica che la sequenza delle ere sia corretta
        Era era = Era.FIRST;

        assertNotNull(era);
        assertEquals(Era.FIRST, era);

        era = era.next();
        assertEquals(Era.SECOND, era);

        era = era.next();
        assertEquals(Era.THIRD, era);

        era = era.next();
        assertEquals(Era.THIRD_FINAL, era);

        era = era.next();
        assertNull(era, "After THIRD_FINAL, next should be null");
    }

    @Test
    void testEraOrdinal() {
        // Verifica l'ordine delle ere
        assertTrue(Era.FIRST.ordinal() < Era.SECOND.ordinal());
        assertTrue(Era.SECOND.ordinal() < Era.THIRD.ordinal());
        assertTrue(Era.THIRD.ordinal() < Era.THIRD_FINAL.ordinal());
    }

    @Test
    void testEraValues() {
        // Verifica che ci siano esattamente 4 ere
        Era[] eras = Era.values();
        assertEquals(4, eras.length, "Should have exactly 4 eras");
        assertEquals(Era.FIRST, eras[0]);
        assertEquals(Era.SECOND, eras[1]);
        assertEquals(Era.THIRD, eras[2]);
        assertEquals(Era.THIRD_FINAL, eras[3]);
    }
}
