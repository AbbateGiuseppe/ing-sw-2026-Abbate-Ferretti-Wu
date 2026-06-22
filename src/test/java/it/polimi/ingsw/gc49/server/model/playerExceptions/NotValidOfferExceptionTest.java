package it.polimi.ingsw.gc49.server.model.playerExceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotValidOfferExceptionTest {

    @Test
    @DisplayName("constructor stores the message and uses the default title 'Offerta non valida'")
    void constructorSetsDefaults() {
        NotValidOfferException e = new NotValidOfferException("offer is occupied");
        assertEquals("offer is occupied", e.getMessage());
        assertEquals("Offerta non valida", e.getTitle());
    }

    @Test
    @DisplayName("NotValidOfferException is a PlayerException")
    void isPlayerException() {
        assertInstanceOf(PlayerException.class, new NotValidOfferException("x"));
    }
}
