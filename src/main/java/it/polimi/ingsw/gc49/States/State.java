package it.polimi.ingsw.gc49.States;

import it.polimi.ingsw.gc49.Game;

public abstract class State {
    public final Game game;

    public State ( Game game ) {
        this.game = game;
    }

    public abstract State executeState();
}
