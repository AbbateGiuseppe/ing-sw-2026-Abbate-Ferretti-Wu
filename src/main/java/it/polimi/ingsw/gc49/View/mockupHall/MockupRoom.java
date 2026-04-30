package it.polimi.ingsw.gc49.View.mockupHall;

import java.util.List;

public class MockupRoom {
    public enum RoomType { WAITING, PLAYING }
    public RoomType type;
    public String roomName;
    public int maxNumOfPlayers;
    public List<String> connectedPlayers;

    public MockupRoom(RoomType type, String roomName, int maxNumOfPlayers, List<String> connectedPlayers) {
        this.type = type;
        this.roomName = roomName;
        this.maxNumOfPlayers = maxNumOfPlayers;
        this.connectedPlayers = connectedPlayers;
    }
}
