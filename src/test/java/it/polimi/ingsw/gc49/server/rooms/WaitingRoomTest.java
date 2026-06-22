package it.polimi.ingsw.gc49.server.rooms;

import it.polimi.ingsw.gc49.client.view.mockupHall.MockupRoom;
import it.polimi.ingsw.gc49.server.Hall;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link WaitingRoom}, limited to the surface that does not require a real
 * {@code ServerMultiplexer} nor any connected {@code PhasedProxyPlayer}.
 */
class WaitingRoomTest {

    @Test
    @DisplayName("constructor stores roomName and maxNumOfPlayers; room starts empty")
    void constructorStoresFields() {
        WaitingRoom room = new WaitingRoom(null, new Hall(), "RoomA", 3);
        assertEquals("RoomA", room.roomName);
        assertEquals(3, room.maxNumOfPlayers);
        assertTrue(room.isEmpty());
        assertEquals(0, room.getNumConnectedPlayers());
    }

    @Test
    @DisplayName("setServer round trip (null accepted)")
    void setServerNull() {
        WaitingRoom room = new WaitingRoom(null, new Hall(), "RoomA", 3);
        assertDoesNotThrow(() -> room.setServer(null));
    }

    @Test
    @DisplayName("giveMockupRoom on an empty waiting room returns a WAITING type MockupRoom")
    void giveMockupRoomEmpty() {
        WaitingRoom room = new WaitingRoom(null, new Hall(), "RoomA", 4);
        MockupRoom mockup = room.giveMockupRoom();
        assertNotNull(mockup);
        assertEquals(MockupRoom.RoomType.WAITING, mockup.type);
        assertEquals("RoomA", mockup.roomName);
        assertEquals(4, mockup.maxNumOfPlayers);
        assertTrue(mockup.connectedPlayers.isEmpty());
    }
}
