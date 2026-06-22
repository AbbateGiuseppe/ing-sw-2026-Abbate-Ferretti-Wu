package it.polimi.ingsw.gc49.client.view.mockupHall;

import org.jline.utils.AttributedString;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MockupRoomTest {

    @Test
    @DisplayName("constructor stores every field as public state")
    void constructorStoresFields() {
        MockupRoom room = new MockupRoom(MockupRoom.RoomType.WAITING, "Room42", 4, List.of("Peppe", "Wu"));
        assertEquals(MockupRoom.RoomType.WAITING, room.type);
        assertEquals("Room42", room.roomName);
        assertEquals(4, room.maxNumOfPlayers);
        assertEquals(List.of("Peppe", "Wu"), room.connectedPlayers);
    }

    @Test
    @DisplayName("toAttributedString for a WAITING room mentions the room name, the wait state and the player count")
    void toAttributedStringWaiting() {
        MockupRoom room = new MockupRoom(MockupRoom.RoomType.WAITING, "Room42", 4, List.of("Peppe", "Wu"));
        String s = room.toAttributedString().toString();
        assertTrue(s.contains("Room42"));
        assertTrue(s.contains("waiting"));
        assertTrue(s.contains("2/4"));
    }

    @Test
    @DisplayName("toAttributedString for a PLAYING room mentions the in-game state")
    void toAttributedStringPlaying() {
        MockupRoom room = new MockupRoom(MockupRoom.RoomType.PLAYING, "Room42", 4, List.of("Peppe"));
        String s = room.toAttributedString().toString();
        assertTrue(s.contains("Room42"));
        assertTrue(s.contains("in game"));
        assertTrue(s.contains("1/4"));
    }

    @Test
    @DisplayName("toAttributedStringPlayers lists every connected player")
    void toAttributedStringPlayers() {
        MockupRoom room = new MockupRoom(MockupRoom.RoomType.WAITING, "Room42", 4, List.of("Peppe", "Wu", "Massi"));
        String s = room.toAttributedStringPlayers().toString();
        assertTrue(s.contains("Peppe"));
        assertTrue(s.contains("Wu"));
        assertTrue(s.contains("Massi"));
    }

    @Test
    @DisplayName("RoomType enum has WAITING and PLAYING")
    void roomTypeValues() {
        assertEquals(2, MockupRoom.RoomType.values().length);
        assertNotNull(MockupRoom.RoomType.valueOf("WAITING"));
        assertNotNull(MockupRoom.RoomType.valueOf("PLAYING"));
    }
}
