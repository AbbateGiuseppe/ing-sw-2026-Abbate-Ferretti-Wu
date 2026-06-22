package it.polimi.ingsw.gc49.server.model.States;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeckEmptiedExceptionTest {

    @Test
    @DisplayName("constructor stores the message")
    void constructorStoresMessage() {
        DeckEmptiedException e = new DeckEmptiedException("deck empty");
        assertEquals("deck empty", e.getMessage());
    }

    @Test
    @DisplayName("DeckEmptiedException is a RuntimeException")
    void isRuntimeException() {
        // Questo è il modo più pulito per testare l'ereditarietà della classe
        assertTrue(RuntimeException.class.isAssignableFrom(DeckEmptiedException.class));
    }
}