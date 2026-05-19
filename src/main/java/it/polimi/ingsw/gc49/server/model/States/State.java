package it.polimi.ingsw.gc49.server.model.States;

import it.polimi.ingsw.gc49.server.model.Game;

import java.io.Serializable;

public abstract class State implements Serializable {
    public enum States { OTHER, TOTEM_CHOOSING, OFFER_CHOOSING, OFFER_EXECUTION }
    protected final Game game;
    protected final States currentStateType ;

    public State ( Game game, States currentStateType ) {
        this.game = game;
        this.currentStateType = currentStateType;
    }

    public abstract State executeState();

    public States getCurrentStateType () {
        return currentStateType ;
    }
}
