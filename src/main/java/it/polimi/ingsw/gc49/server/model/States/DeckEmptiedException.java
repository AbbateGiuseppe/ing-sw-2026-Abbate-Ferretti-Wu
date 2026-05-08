package it.polimi.ingsw.gc49.server.model.States;

public class DeckEmptiedException extends RuntimeException {
    public DeckEmptiedException ( String message ) {
        super(message);
    }
}
