package it.polimi.ingsw.gc49.server.rooms;

import it.polimi.ingsw.gc49.client.view.mockupHall.MockupRoom;
import it.polimi.ingsw.gc49.server.Hall;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link PlayingRoom}, limited to the surface that does not require a real
 * {@code ServerMultiplexer} nor any connected {@code PhasedProxyPlayer}.
 */
class PlayingRoomTest {

    @Test
    @DisplayName("zero-arg constructor stores roomName and maxNumOfPlayers; room starts empty")
    void constructorEmpty() {
        PlayingRoom room = new PlayingRoom(null, new Hall(), "RoomA", 3);
        assertEquals("RoomA", room.roomName);
        assertEquals(3, room.maxNumOfPlayers);
        assertTrue(room.isEmpty());
        assertEquals(0, room.getNumConnectedPlayers());
    }

    @Test
    @DisplayName("players-arg constructor accepts an empty player list")
    void constructorWithEmptyPlayers() {
        PlayingRoom room = new PlayingRoom(null, new Hall(), "RoomA", 3, List.of());
        assertTrue(room.isEmpty());
        assertEquals(0, room.getNumConnectedPlayers());
    }

    @Test
    @DisplayName("setServer round trip (null accepted)")
    void setServerNull() {
        PlayingRoom room = new PlayingRoom(null, new Hall(), "RoomA", 3);
        assertDoesNotThrow(() -> room.setServer(null));
    }

    @Test
    @DisplayName("giveMockupRoom on an empty playing room returns a PLAYING type MockupRoom")
    void giveMockupRoomEmpty() {
        PlayingRoom room = new PlayingRoom(null, new Hall(), "RoomA", 4);
        MockupRoom mockup = room.giveMockupRoom();
        assertNotNull(mockup);
        assertEquals(MockupRoom.RoomType.PLAYING, mockup.type);
        assertEquals("RoomA", mockup.roomName);
        assertEquals(4, mockup.maxNumOfPlayers);
        assertTrue(mockup.connectedPlayers.isEmpty());
    }
}
