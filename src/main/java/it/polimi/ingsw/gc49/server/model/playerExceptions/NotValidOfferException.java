package it.polimi.ingsw.gc49.server.model.playerExceptions;

public class NotValidOfferException extends PlayerException {
    public NotValidOfferException ( String message ) {
        super(message, "Offerta non valida");
    }
}
