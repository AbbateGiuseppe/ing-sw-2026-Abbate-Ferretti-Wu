package it.polimi.ingsw.gc49.server.model.playerExceptions;

public class NotYourTurnException extends PlayerException {
    public NotYourTurnException ( String message ) {
        super(message, "Non il tuo turno");
    }
}
