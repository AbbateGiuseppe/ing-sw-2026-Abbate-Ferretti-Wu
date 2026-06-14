package it.polimi.ingsw.gc49.server.model.playerExceptions;

public class InvalidTotem extends PlayerException {
    public InvalidTotem ( String message ) {
        super(message, "Totem inaccettabile");
    }
}
