package it.polimi.ingsw.gc49.View.mockupHall;

import java.util.List;

public class MockupRoom {
    public enum RoomType { WAITING, PLAYING }
    public RoomType type;
    public int roomId;
    public int maxNumOfPlayers;
    public List<String> connectedPlayers;

    public MockupRoom(RoomType type, int roomId, int maxNumOfPlayers, List<String> connectedPlayers) {
        this.type = type;
        this.roomId = roomId;
        this.maxNumOfPlayers = maxNumOfPlayers;
        this.connectedPlayers = connectedPlayers;
    }
}
