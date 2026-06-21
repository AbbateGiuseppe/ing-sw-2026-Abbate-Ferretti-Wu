package it.polimi.ingsw.gc49.client.view;

import it.polimi.ingsw.gc49.client.view.mockupHall.MockupHall;
import it.polimi.ingsw.gc49.client.view.mockupHall.MockupRoom;
import it.polimi.ingsw.gc49.client.view.mockupModel.MockupGame;

/**
 * The {@code Mockup} class serves as a centralized local repository for the client application.
 * It holds the most recently received lightweight representations (mockups) of the server's state,
 * including the main lobby (Hall), the waiting area (Room), and the active match (Game).
 * This allows the user interface to easily access and render the current state of the application.
 */
public class Mockup {

    /** The most recent snapshot of the main lobby (Hall). */
    private MockupHall hall;

    /** The most recent snapshot of the waiting or playing room the player is currently in. */
    private MockupRoom room;

    /** The most recent snapshot of the active game model. */
    private MockupGame game;

    /**
     * Retrieves the currently stored hall mockup.
     *
     * @return the {@link MockupHall} instance, or {@code null} if not yet initialized.
     */
    public MockupHall getHall () {
        return hall;
    }

    /**
     * Retrieves the currently stored room mockup.
     *
     * @return the {@link MockupRoom} instance, or {@code null} if not yet initialized.
     */
    public MockupRoom getRoom () {
        return room;
    }

    /**
     * Retrieves the currently stored game mockup.
     *
     * @return the {@link MockupGame} instance, or {@code null} if not yet initialized.
     */
    public MockupGame getGame () {
        return game;
    }


    /**
     * Updates the local hall state with a new snapshot from the server.
     *
     * @param hall the new {@link MockupHall} to store.
     */
    public void setHall ( MockupHall hall ) {
        this.hall = hall;
    }

    /**
     * Updates the local room state with a new snapshot from the server.
     *
     * @param room the new {@link MockupRoom} to store.
     */
    public void setRoom ( MockupRoom room ) {
        this.room = room;
    }

    /**
     * Updates the local game state with a new snapshot from the server.
     *
     * @param game the new {@link MockupGame} to store.
     */
    public void setGame ( MockupGame game ) {
        this.game = game;
    }
}
