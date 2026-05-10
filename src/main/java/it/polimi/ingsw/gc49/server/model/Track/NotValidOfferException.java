package it.polimi.ingsw.gc49.server.model.Track;

public class NotValidOfferException extends RuntimeException {
    public NotValidOfferException ( String message ) {
        super(message);
    }
}
