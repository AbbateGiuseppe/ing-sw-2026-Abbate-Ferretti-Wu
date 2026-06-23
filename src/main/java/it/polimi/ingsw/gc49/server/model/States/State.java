package it.polimi.ingsw.gc49.server.model.States;

import it.polimi.ingsw.gc49.server.model.Game;
import it.polimi.ingsw.gc49.server.model.Locks;

import java.io.Serializable;

public abstract class State implements Serializable {
    public enum States { OTHER, TOTEM_CHOOSING, OFFER_CHOOSING, OFFER_EXECUTION, GAME_END }
    protected final Game game;
    protected final States currentStateType ;
    protected final Locks locks ;

    public State ( Game game, States currentStateType, Locks locks ) {
        this.game = game;
        this.currentStateType = currentStateType;
        this.locks = locks;
    }

    public abstract State executeState();

    public States getCurrentStateType () {
        return currentStateType ;
    }
}
