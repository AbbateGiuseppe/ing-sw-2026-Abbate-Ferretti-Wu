package it.polimi.ingsw.gc49.States;

import it.polimi.ingsw.gc49.Model;

public abstract class State {
    public final Model model;

    public State ( Model model ) {
        this.model = model;
    }

    public abstract State executeState();

    public Model getModel() {
        return model;
    }
}
