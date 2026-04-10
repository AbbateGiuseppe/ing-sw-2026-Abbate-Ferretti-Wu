package it.polimi.ingsw.gc49.View.mockupModel;

import java.util.List;

public class MockupGame {
    private MockupPlayer currentPlayer;
    private int currentPlayerIndex;
    private final List<MockupPlayer> players;
    private final MockupCardBoard cardBoard;
    private final MockupTrack track;

    //[constructor] Should be initialized AFTER the real game has been initialized on the server!
    public MockupGame (List<MockupPlayer> players, MockupCardBoard cardBoard, MockupTrack track) {
        this.players = players;
        this.cardBoard = cardBoard;
        this.track = track;
    }

    //### setters
    public void setCurrentPlayer (MockupPlayer currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
    public void setCurrentPlayerIndex (int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }

    //### getters
    public MockupPlayer getCurrentPlayer () {
        return currentPlayer;
    }
    public int getCurrentPlayerIndex () {
        return currentPlayerIndex;
    }
    public List<MockupPlayer> getPlayers () {
        return players;
    }
    public MockupCardBoard getCardBoard () {
        return cardBoard;
    }
    public MockupTrack getTrack () {
        return track;
    }
}
