package it.polimi.ingsw.gc49.model.States;

import it.polimi.ingsw.gc49.model.Game;

public abstract class State {
    protected final Game game;
    public static final Object playerInputLock = new Object();

    public State ( Game game ) {
        this.game = game;
    }

    public abstract State executeState();
}
