package it.polimi.ingsw.gc49.client.view;

import it.polimi.ingsw.gc49.client.view.mockupHall.MockupHall;
import it.polimi.ingsw.gc49.client.view.mockupHall.MockupRoom;
import it.polimi.ingsw.gc49.client.view.mockupModel.MockupGame;

public class Mockup {
    private MockupHall hall;
    private MockupRoom room;
    private MockupGame game;

    public MockupHall getHall () {
        return hall;
    }
    public MockupRoom getRoom () {
        return room;
    }
    public MockupGame getGame () {
        return game;
    }

    public void setHall ( MockupHall hall ) {
        this.hall = hall;
    }
    public void setRoom ( MockupRoom room ) {
        this.room = room;
    }
    public void setGame ( MockupGame game ) {
        this.game = game;
    }
}
