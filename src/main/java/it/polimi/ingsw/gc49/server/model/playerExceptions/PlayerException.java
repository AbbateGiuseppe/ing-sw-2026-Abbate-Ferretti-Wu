package it.polimi.ingsw.gc49.server.model.playerExceptions;

public class PlayerException extends RuntimeException {
    private final String title;

    public PlayerException ( String message, String title ) {
        super(message);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
