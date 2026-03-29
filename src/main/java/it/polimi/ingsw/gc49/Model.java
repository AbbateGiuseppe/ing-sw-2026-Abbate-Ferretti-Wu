package it.polimi.ingsw.gc49;

import it.polimi.ingsw.gc49.CardBoard.CardBoard;
import it.polimi.ingsw.gc49.States.InitialSetup;
import it.polimi.ingsw.gc49.States.State;
import it.polimi.ingsw.gc49.Track.Track;

import java.util.EnumSet;
import java.util.List;

public class Model {
    private final int numOfPlayers;
    private int numOfConnectedPlayers;
    private Player currentPlayer;
    private int currentPlayerIndex;
    private List<Player> players;
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
    public List<Player> getPlayers () {
        return players;
    }
    public String[] getPlayersNicknames () {
        return playersNicknames;
    }
    public Track getTrack () {
        return track;
    }
    public CardBoard getCardBoard () { return cardBoard; }
    public EnumSet<Totem> getUsedTotems () {
        return usedTotems;
    }

    //### setters
    public void setPlayers ( List<Player> players ) {
        this.players = players;
    }
    public void setTrack ( Track track ){
        this.track = track;
    }
    public void setCardBoard ( CardBoard cardBoard ) {
        this.cardBoard = cardBoard;
    }
    public void setCurrentPlayer ( Player currentPlayer ) { this.currentPlayer = currentPlayer; }
    public void setCurrentPlayerIndex ( int currentPlayerIndex ) { this.currentPlayerIndex = currentPlayerIndex; }

    //### Game's execution
    public void executeCurrentState () {
        currentState = currentState.executeState();
    }

    //TODO: implement a check to know if the current player is choosing the admissable actions of this phase.
    //### Players' actions
    public void chooseTotem ( int playerIndex, Totem chosenTotem ) {
        synchronized (Locks.playerInput) {
            if (players.get(playerIndex).getTotem() != null && !usedTotems.contains(chosenTotem)) {
                usedTotems.add(chosenTotem);
                players.get(playerIndex).setTotem(chosenTotem);
                notify();
            }
        }
    }

    public void drawUpperCharacter ( int playerIndex, int cardIndex ) {
        synchronized (Locks.playerInput) {
            if(playerIndex == currentPlayerIndex) {
                Player drawingPlayer = players.get(playerIndex);
                if(drawingPlayer.getDrawableUpper() > 0){
                    drawingPlayer.setDrawableUpper(drawingPlayer.getDrawableUpper() - 1); //decreases by one the player's drawable upper cards.
                    drawingPlayer.addCharacterCard(cardBoard.drawUpperCharacter(cardIndex, drawingPlayer)); //adds the drawn card to the player, if it's drawable by him.
                    notify();
                }
            }
        }
    }

    public void drawLowerCharacter ( int playerIndex, int cardIndex ) {
        synchronized (Locks.playerInput) {
            if(playerIndex == currentPlayerIndex) {
                Player drawingPlayer = players.get(playerIndex);
                if(drawingPlayer.getDrawableLower() > 0){
                    drawingPlayer.setDrawableLower(drawingPlayer.getDrawableLower() - 1); //decreases by one the player's drawable lower cards.
                    drawingPlayer.addCharacterCard(cardBoard.drawLowerCharacter(cardIndex, drawingPlayer)); //adds the drawn card to the player, if it's drawable by him.
                    notify();
                }
            }
        }
    }

    public void drawUpperBuilding ( int playerIndex, int cardIndex ) {
        synchronized (Locks.playerInput) {
            if(playerIndex == currentPlayerIndex) {
                Player drawingPlayer = players.get(playerIndex);
                if(drawingPlayer.getDrawableUpper() > 0){
                    drawingPlayer.setDrawableUpper(drawingPlayer.getDrawableUpper() - 1); //decreases by one the player's drawable upper cards.
                    drawingPlayer.addBuildingCard(cardBoard.drawUpperBuilding(cardIndex, drawingPlayer)); //adds the drawn card to the player, if it's drawable by him.
                    notify();
                }
            }
        }
    }

    public void drawLowerBuilding ( int playerIndex, int cardIndex ) {
        synchronized (Locks.playerInput) {
            if(playerIndex == currentPlayerIndex) {
                Player drawingPlayer = players.get(playerIndex);
                if(drawingPlayer.getDrawableLower() > 0){
                    drawingPlayer.setDrawableLower(drawingPlayer.getDrawableLower() - 1); //decreases by one the player's drawable lower cards.
                    drawingPlayer.addBuildingCard(cardBoard.drawLowerBuilding(cardIndex, drawingPlayer)); //adds the drawn card to the player, if it's drawable by him.
                    notify();
                }
            }
        }
    }

    public void chooseOffer ( int playerIndex, int offerIndex ) {
        synchronized (Locks.playerInput) {
            if(playerIndex == currentPlayerIndex) {
                track.assignOffer(players.get(playerIndex), offerIndex); //TODO: implement not valid offerIndex exception.
                notify();
            }
        }
    }

    public void passYourTurn ( int playerIndex ) {
        synchronized (Locks.playerInput) {
            if(playerIndex == currentPlayerIndex) {
                players.get(playerIndex).cleanRemainingActions();
                notify();
            }
        }
    }

    //### Connection methods
    public void disconnectPlayer( int playerIndex ) {

    }

    public void connectPlayer( int playerIndex ) {

    }
}
