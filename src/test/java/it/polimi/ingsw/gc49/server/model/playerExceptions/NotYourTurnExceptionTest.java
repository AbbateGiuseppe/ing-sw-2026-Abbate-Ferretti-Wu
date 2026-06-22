package it.polimi.ingsw.gc49.server.model.playerExceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotYourTurnExceptionTest {

    @Test
    @DisplayName("constructor stores the message and uses the default title 'Non il tuo turno'")
    void constructorSetsDefaults() {
        NotYourTurnException e = new NotYourTurnException("it is not your turn");
        assertEquals("it is not your turn", e.getMessage());
        assertEquals("Non il tuo turno", e.getTitle());
    }

    @Test
    @DisplayName("NotYourTurnException is a PlayerException")
    void isPlayerException() {
        assertInstanceOf(PlayerException.class, new NotYourTurnException("x"));
    }
}
