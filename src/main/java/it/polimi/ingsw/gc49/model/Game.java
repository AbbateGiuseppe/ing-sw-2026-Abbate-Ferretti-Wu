package it.polimi.ingsw.gc49.model;

import it.polimi.ingsw.gc49.model.Card.BuildingCard.BuildingCard;
import it.polimi.ingsw.gc49.model.Card.Card;
import it.polimi.ingsw.gc49.model.CardBoard.CardBoard;
import it.polimi.ingsw.gc49.model.States.InitialSetup;
import it.polimi.ingsw.gc49.model.States.State;
import it.polimi.ingsw.gc49.model.Track.Track;

import java.util.EnumSet;
import java.util.List;

public class Game {
    private final int numOfPlayers;
    private int numOfConnectedPlayers;
    private Player currentPlayer;
    private int currentPlayerIndex;
    private List<Player> players;
    private final String[] playersNicknames;
    private EventManager eventManager;
    private Track track;
    private CardBoard cardBoard;
    private State currentState;
    private final EnumSet<Totem> usedTotems = EnumSet.noneOf(Totem.class);
    private boolean lastRound;


    //### Constructors, from 2 to 5 players, handled by the initial stata via the numOfPlayers and playersNicknames
    public Game ( int numOfPlayers, String[] playersNicknames ) {
        this.numOfPlayers = numOfPlayers;
        this.playersNicknames = playersNicknames;
        currentState = new InitialSetup(this);
    }

    public void gameLoop () {
        while(currentState != null) { //GAME'S LOOP, UNTIL THE NEXT STATE IS NULL
            synchronized (Locks.playerInput) {
                executeCurrentState();
            }
        }
    }

    //### getters
    public int getNumOfPlayers () { return numOfPlayers; }
    public int getNumOfConnectedPlayers () { return numOfConnectedPlayers; }
    public List<Player> getPlayers () { return players; }
    public String[] getPlayersNicknames () { return playersNicknames; }
    public EventManager getEventManager () { return eventManager; }
    public Track getTrack () { return track; }
    public CardBoard getCardBoard () { return cardBoard; }
    public EnumSet<Totem> getUsedTotems () { return usedTotems; }

    public boolean isLastRound () { return lastRound; }

    //### setters
    public void setPlayers ( List<Player> players ) { this.players = players; }
    public void setEventManager ( EventManager eventManager ) { this.eventManager = eventManager; }
    public void setTrack ( Track track ){
        this.track = track;
    }
    public void setCardBoard ( CardBoard cardBoard ) { this.cardBoard = cardBoard; }
    public void setNumOfConnectedPlayers ( int numOfConnectedPlayers ) { this.numOfConnectedPlayers = numOfConnectedPlayers; }
    public void setCurrentPlayer ( Player currentPlayer ) { this.currentPlayer = currentPlayer; }
    public void setCurrentPlayerIndex ( int currentPlayerIndex ) { this.currentPlayerIndex = currentPlayerIndex; }
    public void setLastRound ( boolean lastRound ) { this.lastRound = lastRound; }

    //### Game's execution
    public void executeCurrentState () {
        currentState = currentState.executeState();
    }

    //TODO: implement a check to know if the current player is choosing the admissible actions of this phase.
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

    //### Event calls
    public void callDrawEvent() {
        eventManager.invokeEventByPlayer(currentPlayer, BuildingEvent.DRAW_EVENT);
    }
    public void callTurnEndEvent() {
        eventManager.invokeEventByPlayer(currentPlayer, BuildingEvent.TURN_END);
    }
    public void callRoundEndEvent() {
        eventManager.invokeEvent(BuildingEvent.ROUND_END);
    }
    public void callGameEndEvent() {
        eventManager.invokeEvent(BuildingEvent.GAME_END);
    }

    public void drawUpperCharacter ( int playerIndex, int cardIndex ) {
        synchronized (Locks.playerInput) {
            if(playerIndex == currentPlayerIndex) {
                Player drawingPlayer = players.get(playerIndex);
                if(drawingPlayer.getDrawableUpper() > 0){
                    Card drawnCard = cardBoard.drawUpperCharacter(cardIndex, drawingPlayer);
                    if (drawnCard != null) {
                        drawingPlayer.setDrawableUpper(drawingPlayer.getDrawableUpper() - 1); //decreases by one the player's drawable upper cards.
                        drawingPlayer.addCharacterCard(drawnCard); //adds the drawn card to the player, if it's drawable by him.
                        callDrawEvent();
                    }
                    notify();
                }
            }
        }
    }

    public void drawLowerCharacter ( int playerIndex, int cardIndex ) {
        synchronized (Locks.playerInput) {
            if(playerIndex == currentPlayerIndex) {
                Player drawingPlayer = players.get(playerIndex);
                if(drawingPlayer.getDrawableLower() > 0) {
                    Card drawnCard = cardBoard.drawLowerCharacter(cardIndex, drawingPlayer);
                    if (drawnCard != null) {
                        drawingPlayer.setDrawableLower(drawingPlayer.getDrawableLower() - 1); //decreases by one the player's drawable lower cards.
                        drawingPlayer.addCharacterCard(drawnCard); //adds the drawn card to the player, if it's drawable by him.
                        callDrawEvent();
                    }
                    notify();
                }
            }
        }
    }

    public void drawUpperBuilding ( int playerIndex, int cardIndex ) {
        synchronized (Locks.playerInput) {
            if(playerIndex == currentPlayerIndex) {
                Player drawingPlayer = players.get(playerIndex);
                if(drawingPlayer.getDrawableUpper() > 0) {
                    BuildingCard drawnBuildingCard = (BuildingCard) cardBoard.drawUpperBuilding(cardIndex, drawingPlayer);
                    if (drawnBuildingCard != null) {
                        drawingPlayer.setDrawableUpper(drawingPlayer.getDrawableUpper() - 1); //decreases by one the player's drawable upper cards.
                        drawnBuildingCard.addBuildingToManager(currentPlayer, eventManager); //adds the building as a listener.
                        drawingPlayer.addBuildingCard(drawnBuildingCard); //adds the drawn card to the player, if it's drawable by him.
                        callDrawEvent();
                    }
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
                    BuildingCard drawnBuildingCard = (BuildingCard) cardBoard.drawLowerBuilding(cardIndex, drawingPlayer);
                    if (drawnBuildingCard != null) {
                        drawingPlayer.setDrawableLower(drawingPlayer.getDrawableLower() - 1); //decreases by one the player's drawable lower cards.
                        drawnBuildingCard.addBuildingToManager(currentPlayer, eventManager);
                        drawingPlayer.addBuildingCard(drawnBuildingCard); //adds the drawn card to the player, if it's drawable by him.
                        callDrawEvent();
                    }
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
