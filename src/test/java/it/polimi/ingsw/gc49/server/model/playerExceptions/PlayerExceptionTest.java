package it.polimi.ingsw.gc49.server.model.playerExceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerExceptionTest {

    @Test
    @DisplayName("constructor stores both message and title")
    void constructorStoresFields() {
        PlayerException e = new PlayerException("something went wrong", "Errore");
        assertEquals("something went wrong", e.getMessage());
        assertEquals("Errore", e.getTitle());
    }

    @Test
    @DisplayName("PlayerException is an unchecked RuntimeException")
    void isRuntimeException() {
        assertInstanceOf(RuntimeException.class, new PlayerException("m", "t"));
    }

    @Test
    @DisplayName("null message and title are accepted")
    void nullsAccepted() {
        PlayerException e = new PlayerException(null, null);
        assertNull(e.getMessage());
        assertNull(e.getTitle());
    }
}
