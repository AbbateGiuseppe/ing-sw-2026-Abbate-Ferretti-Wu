package it.polimi.ingsw.gc49.model.States;

import it.polimi.ingsw.gc49.model.Game;

public abstract class State {
    public final Game game;

    public State ( Game game ) {
        this.game = game;
    }

    public abstract State executeState();
}
