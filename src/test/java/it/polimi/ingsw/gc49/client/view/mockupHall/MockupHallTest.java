package it.polimi.ingsw.gc49.client.view.mockupHall;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MockupHallTest {

    @Test
    @DisplayName("constructor stores connectedPlayers and rooms")
    void constructorStoresFields() {
        MockupHall hall = new MockupHall(List.of("Peppe", "Wu"), List.of());
        assertEquals(List.of("Peppe", "Wu"), hall.connectedPlayers);
        assertTrue(hall.rooms.isEmpty());
    }

    @Test
    @DisplayName("toAttributedString lists every connected player")
    void toAttributedStringMentionsPlayers() {
        MockupHall hall = new MockupHall(List.of("Peppe", "Wu", "Massi"), List.of());
        String s = hall.toAttributedString().toString();
        assertTrue(s.contains("Peppe"));
        assertTrue(s.contains("Wu"));
        assertTrue(s.contains("Massi"));
    }

    @Test
    @DisplayName("toAttributedString includes every room's representation")
    void toAttributedStringIncludesRooms() {
        MockupRoom room1 = new MockupRoom(MockupRoom.RoomType.WAITING, "Alpha", 4, List.of("Peppe"));
        MockupRoom room2 = new MockupRoom(MockupRoom.RoomType.PLAYING, "Bravo", 3, List.of("Wu"));
        MockupHall hall = new MockupHall(List.of("Peppe", "Wu"), List.of(room1, room2));

        String s = hall.toAttributedString().toString();
        assertTrue(s.contains("Alpha"));
        assertTrue(s.contains("Bravo"));
    }

    @Test
    @DisplayName("toAttributedString works with empty player and room lists")
    void toAttributedStringEmpty() {
        MockupHall hall = new MockupHall(List.of(), List.of());
        assertNotNull(hall.toAttributedString());
    }
}
