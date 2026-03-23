package it.polimi.ingsw.gc49;

import java.util.List;

public interface Context {
    public void changeState(State state);
    public List<Player> getPlayers();
    public Plancia getPlancia();
}
