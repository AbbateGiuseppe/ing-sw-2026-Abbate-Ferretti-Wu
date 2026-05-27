package it.polimi.ingsw.gc49.model.Track;

import it.polimi.ingsw.gc49.server.model.Player;
import it.polimi.ingsw.gc49.server.model.Track.Track;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TrackTest {

    @Test
    void testGetNextPlayerOrderSlot () {
        Player testPlayerOne = new Player("tizio", 0);
        Player testPlayerTwo = new Player("caio", 1);
        ArrayList<Player> players = new ArrayList<>();
        players.add(testPlayerOne);
        players.add(testPlayerTwo);
        Track track = new Track(2);

        track.randomizeStartingOrder(players);
        //should finish the order slot after two calls.
        assertNotNull(track.getNextPlayerOrderSlot());
        assertNotNull(track.getNextPlayerOrderSlot());
        assertNull(track.getNextPlayerOrderSlot());
    }

    @Test
    void testOffers () {
        Player testPlayerOne = new Player("tizio", 0);
        Player testPlayerTwo = new Player("caio", 1);
        Track track = new Track(2);
        track.assignOffer(testPlayerOne, 0);
        track.assignOffer(testPlayerTwo, 1);
        track.assignOffer(null, 2);
        //does the assigned offer activate?
        track.getNextPlayerOfferAndActivate();
        assertEquals(testPlayerOne.getDrawableLower(), 1);
        track.deassignCurrentOffer();
        track.getNextPlayerOfferAndActivate();
        assertEquals(testPlayerTwo.getDrawableUpper(), 1);
        track.deassignCurrentOffer();
        track.deassignCurrentOffer(); //null, does it exclude empty offers?
        //did the order board fill up?
        assertNotNull(track.getNextPlayerOrderSlot());
        assertNotNull(track.getNextPlayerOrderSlot());
        assertNull(track.getNextPlayerOrderSlot());
    }
}