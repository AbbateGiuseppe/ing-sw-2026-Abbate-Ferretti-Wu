package it.polimi.ingsw.gc49;

import it.polimi.ingsw.gc49.States.InitialSetup;
import it.polimi.ingsw.gc49.States.State;

public class Model {
    private final int numOfPlayers;
    private int numOfConnectedPlayers;
    private Player[] players;
    private String[] playersNicknames;
    private Track track;
    private State currentState = new InitialSetup(this);


    //### Constructors, from 2 to 5 players, handled by the initial stata via the numOfPlayers and playersNicknames
    public Model ( int numOfPlayers, String[] playersNicknames ) {
        this.numOfPlayers = numOfPlayers;
        this.playersNicknames = playersNicknames;
        executeCurrentState();
    }

    //### getters
    public int getNumOfPlayers () {
        return numOfPlayers;
    }
    public int getNumOfConnectedPlayers () {
        return numOfConnectedPlayers;
    }
    public Player[] getPlayers () {
        return players;
    }
    public String[] getPlayersNicknames () {
        return playersNicknames;
    }
    public Track getTrack () {
        return track;
    }

    //### setters
    public void setPlayers ( Player[] players ) {
        this.players = players;
    }
    public void setTrack ( Track track ){
        this.track = track;
    }

    //### Game's execution
    public void executeCurrentState () {
        currentState = currentState.executeState();
    }

    //### Players' actions
    public void chooseTotem() {

    }

    public void drawUpperCharacter ( int playerIndex, int cardIndex ) {
    }

    public void drawLowerCharacter ( int playerIndex, int cardIndex ) {

    }

    public void drawUpperBuilding ( int playerIndex, int cardIndex ) {

    }

    public void drawLowerBuilding ( int playerIndex, int cardIndex ) {

    }

    public void chooseOffer ( int playerIndex, int cardIndex ) {

    }

    //### Connection methods
    public void disconnectPlayer( int playerIndex ) {

    }

    public void connectPlayer( int playerIndex ) {

    }
}
