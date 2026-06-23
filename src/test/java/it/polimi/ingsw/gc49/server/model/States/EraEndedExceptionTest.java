package it.polimi.ingsw.gc49.server.model.States;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EraEndedExceptionTest {

    @Test
    void constructorStoresMessage() {
        EraEndedException e = new EraEndedException("era over");
        assertEquals("era over", e.getMessage());
    }

    @Test
    void isCheckedException() {
        // Corretto: verifichiamo che sia un'Exception
        assertInstanceOf(Exception.class, new EraEndedException("x"));

        // CORREZIONE: Usiamo la reflection per verificare l'ereditarietà a runtime
        assertFalse(RuntimeException.class.isAssignableFrom(EraEndedException.class));
    }
}
