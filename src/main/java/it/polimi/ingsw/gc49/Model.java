package it.polimi.ingsw.gc49;

import it.polimi.ingsw.gc49.CardBoard.CardBoard;
import it.polimi.ingsw.gc49.States.InitialSetup;
import it.polimi.ingsw.gc49.States.State;

import java.util.EnumSet;

public class Model {
    private final int numOfPlayers;
    private int numOfConnectedPlayers;
    private Player currentPlayer;
    private int currentPlayerIndex;
    private Player[] players;
    private String[] playersNicknames;
    private Track track;
    private CardBoard cardBoard;
    private State currentState;
    private EnumSet<Totem> usedTotems = EnumSet.noneOf(Totem.class);


    //### Constructors, from 2 to 5 players, handled by the initial stata via the numOfPlayers and playersNicknames
    public Model ( int numOfPlayers, String[] playersNicknames ) {
        this.numOfPlayers = numOfPlayers;
        this.playersNicknames = playersNicknames;
        currentState = new InitialSetup(this);
        synchronized (Locks.playerInput) {
            executeCurrentState();
        }
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
    public EnumSet<Totem> getUsedTotems () {
        return usedTotems;
    }

    //### setters
    public void setPlayers ( Player[] players ) {
        this.players = players;
    }
    public void setTrack ( Track track ){
        this.track = track;
    }
    public void setCardBoard ( CardBoard cardBoard ) {
        this.cardBoard = cardBoard;
    }

    //### Game's execution
    public void executeCurrentState () {
        currentState = currentState.executeState();
    }

    //### Players' actions
    public void chooseTotem ( int playerIndex, Totem chosenTotem ) {
        synchronized (Locks.playerInput) {
            if (players[playerIndex].getTotem() != null && !usedTotems.contains(chosenTotem)) {
                usedTotems.add(chosenTotem);
                players[playerIndex].setTotem(chosenTotem);
                notify();
            }
        }
    }

    public void drawUpperCharacter ( int playerIndex, int cardIndex ) {
        synchronized (Locks.playerInput) {
            if(playerIndex == currentPlayerIndex) {
                if(players[playerIndex].getDrawableUpper() > 0){
                    players[playerIndex].setDrawableUpper(players[playerIndex].getDrawableUpper() - 1); //decreases by one the player's drawable upper cards.
                    players[playerIndex].addCharacterCard(cardBoard.drawUpperCharacter(cardIndex)); //adds the drawn card to the player.
                    notify();
                }
            }
        }
    }

    public void drawLowerCharacter ( int playerIndex, int cardIndex ) {
        synchronized (Locks.playerInput) {
            if(playerIndex == currentPlayerIndex) {
                if(players[playerIndex].getDrawableLower() > 0){
                    players[playerIndex].setDrawableLower(players[playerIndex].getDrawableLower() - 1); //decreases by one the player's drawable lower cards.
                    players[playerIndex].addCharacterCard(cardBoard.drawLowerCharacter(cardIndex)); //adds the drawn card to the player.
                    notify();
                }
            }
        }
    }

    public void drawUpperBuilding ( int playerIndex, int cardIndex ) {
        synchronized (Locks.playerInput) {
            if(playerIndex == currentPlayerIndex) {
                if(players[playerIndex].getDrawableUpper() > 0){
                    players[playerIndex].setDrawableUpper(players[playerIndex].getDrawableUpper() - 1); //decreases by one the player's drawable upper cards.
                    players[playerIndex].addBuildingCard(cardBoard.drawUpperBuilding(cardIndex, players[playerIndex].getFood())); //adds the drawn card to the player, if he has enough food.
                    notify();
                }
            }
        }
    }

    public void drawLowerBuilding ( int playerIndex, int cardIndex ) {
        synchronized (Locks.playerInput) {
            if(playerIndex == currentPlayerIndex) {
                if(players[playerIndex].getDrawableLower() > 0){
                    players[playerIndex].setDrawableLower(players[playerIndex].getDrawableLower() - 1); //decreases by one the player's drawable lower cards.
                    players[playerIndex].addBuildingCard(cardBoard.drawLowerBuilding(cardIndex, players[playerIndex].getFood())); //adds the drawn card to the player, if he has enough food.
                    notify();
                }
            }
        }
    }

    public void chooseOffer ( int playerIndex, int cardIndex ) {
        synchronized (Locks.playerInput) {
            notify();
        }
    }

    //### Connection methods
    public void disconnectPlayer( int playerIndex ) {

    }

    public void connectPlayer( int playerIndex ) {

    }
}
