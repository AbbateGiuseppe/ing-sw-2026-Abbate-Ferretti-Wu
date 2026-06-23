package it.polimi.ingsw.gc49.server.model.States;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeckEmptiedExceptionTest {

    @Test
    void constructorStoresMessage() {
        DeckEmptiedException e = new DeckEmptiedException("deck empty");
        assertEquals("deck empty", e.getMessage());
    }

    @Test
    void isRuntimeException() {
        // Questo è il modo più pulito per testare l'ereditarietà della classe
        assertTrue(RuntimeException.class.isAssignableFrom(DeckEmptiedException.class));
    }
}