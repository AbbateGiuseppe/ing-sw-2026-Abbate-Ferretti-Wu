package it.polimi.ingsw.gc49.model.States;

import it.polimi.ingsw.gc49.server.model.Game;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class InitialSetupTest {

    @Test
    void testInitialSetupState() {
        List<String> nicknames = new ArrayList<>();
        nicknames.add("pinco");
        nicknames.add("palo");
        new Game(2, nicknames, "testGame");
    }
}