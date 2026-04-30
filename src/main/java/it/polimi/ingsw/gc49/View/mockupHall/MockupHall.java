package it.polimi.ingsw.gc49.View.mockupHall;

import java.io.Serializable;
import java.util.List;

public class MockupHall implements Serializable {
    public List<String> connectedPlayers;
    public List<MockupRoom> rooms;

    public MockupHall(List<String> connectedPlayers, List<MockupRoom> rooms) {
        this.connectedPlayers = connectedPlayers;
        this.rooms = rooms;
    }
}
