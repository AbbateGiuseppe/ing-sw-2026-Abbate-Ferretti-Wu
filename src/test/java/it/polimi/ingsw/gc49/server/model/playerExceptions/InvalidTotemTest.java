package it.polimi.ingsw.gc49.server.model.playerExceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvalidTotemTest {

    @Test
    @DisplayName("constructor stores the message and uses the default title 'Totem inaccettabile'")
    void constructorSetsDefaults() {
        InvalidTotem e = new InvalidTotem("totem already used");
        assertEquals("totem already used", e.getMessage());
        assertEquals("Totem inaccettabile", e.getTitle());
    }

    @Test
    @DisplayName("InvalidTotem is a PlayerException")
    void isPlayerException() {
        assertInstanceOf(PlayerException.class, new InvalidTotem("x"));
    }

    @Test
    @DisplayName("can be thrown and caught as PlayerException")
    void throwAndCatchAsPlayerException() {
        PlayerException caught = assertThrows(PlayerException.class, () -> {
            throw new InvalidTotem("bad totem");
        });
        assertEquals("Totem inaccettabile", caught.getTitle());
    }
}
