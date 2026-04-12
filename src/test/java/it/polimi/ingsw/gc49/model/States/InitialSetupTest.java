package it.polimi.ingsw.gc49.model.States;

import it.polimi.ingsw.gc49.model.Game;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InitialSetupTest {

    @Test
    void testInitialSetupState() {
        new Game(2, new String[] {new String ("pinco"), new String("pallo")});
    }
}