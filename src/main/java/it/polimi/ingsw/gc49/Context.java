package it.polimi.ingsw.gc49;

import it.polimi.ingsw.gc49.States.State;

import java.util.List;

public interface Context {
    public void changeState( State state);
    public List<Player> getPlayers();
    public Plancia getPlancia();
}
