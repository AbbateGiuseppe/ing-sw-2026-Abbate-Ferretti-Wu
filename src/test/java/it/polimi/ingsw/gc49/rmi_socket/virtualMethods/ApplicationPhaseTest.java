package it.polimi.ingsw.gc49.rmi_socket.virtualMethods;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationPhaseTest {

    @Test
    @DisplayName("ANY, HALL, ROOM, GAME exist as enum constants")
    void requiredConstantsExist() {
        assertNotNull(ApplicationPhase.valueOf("ANY"));
        assertNotNull(ApplicationPhase.valueOf("HALL"));
        assertNotNull(ApplicationPhase.valueOf("ROOM"));
        assertNotNull(ApplicationPhase.valueOf("GAME"));
    }

    @Test
    @DisplayName("valueOf returns the matching constant for every value")
    void valueOfRoundTrip() {
        for (ApplicationPhase phase : ApplicationPhase.values()) {
            assertEquals(phase, ApplicationPhase.valueOf(phase.name()));
        }
    }

    @Test
    @DisplayName("valueOf throws on an unknown name")
    void valueOfUnknownThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> ApplicationPhase.valueOf("LOBBY"));
    }

    @Test
    @DisplayName("values() returns at least the four known phases")
    void hasAtLeastFourPhases() {
        assertTrue(ApplicationPhase.values().length >= 4);
    }
}
