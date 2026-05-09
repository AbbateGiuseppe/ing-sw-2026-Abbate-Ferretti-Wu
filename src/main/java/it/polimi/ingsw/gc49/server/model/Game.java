package it.polimi.ingsw.gc49.server.model;

import it.polimi.ingsw.gc49.client.view.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.client.view.mockupModel.MockupPlayer;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.INITIALIZE_MODEL.InitializeModelPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.TotemModelElement;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.UpdateModelPacket;
import it.polimi.ingsw.gc49.server.model.Card.BuildingCard.BuildingCard;
import it.polimi.ingsw.gc49.server.model.Card.Card;
import it.polimi.ingsw.gc49.server.model.CardBoard.CardBoard;
import it.polimi.ingsw.gc49.server.model.States.InitialSetup;
import it.polimi.ingsw.gc49.server.model.States.State;
import it.polimi.ingsw.gc49.server.model.Track.Offer;
import it.polimi.ingsw.gc49.server.model.Track.OrderSlot;
import it.polimi.ingsw.gc49.server.model.Track.Track;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualClients.VirtualGameClient;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import static it.polimi.ingsw.gc49.server.model.Locks.broadcastLock;

public class Game {
    private final List<VirtualGameClient> controllersListeners = new ArrayList<>();
    private final int numOfPlayers;
    private final List<String> playersNicknames;
    private List<Player> players;
    private EventManager eventManager;
    private Track track;
    private CardBoard cardBoard;
    private State currentState;
    private int numOfConnectedPlayers;
    private Player currentPlayer;
    private int currentPlayerIndex;
    private final EnumSet<Totem> usedTotems = EnumSet.noneOf(Totem.class);
    private boolean lastRound;

    //### Constructors, from 2 to 5 players, handled by the initial stata via the numOfPlayers and playersNicknames
    public Game ( int numOfPlayers, List<String> playersNicknames ) {
        this.numOfPlayers = numOfPlayers;
        this.playersNicknames = playersNicknames;
        currentState = new InitialSetup(this, numOfPlayers, playersNicknames);
        executeCurrentState();
    }

    public void gameLoop () {
        broadcastMockupGame();

        while(currentState != null) { //GAME'S LOOP, UNTIL THE NEXT STATE IS NULL
            synchronized (Locks.playerInput) {
                System.out.println("Entrando in un nuovo stato di gioco...");
                executeCurrentState();
                System.out.println("Finito lo stato di gioco precedente.");
            }
        }
    }

    //### getters
    public int getNumOfPlayers () { return numOfPlayers; }
    public int getNumOfConnectedPlayers () { return numOfConnectedPlayers; }
    public List<Player> getPlayers () { return players; }
    public List<String> getPlayersNicknames () { return playersNicknames; }
    public EventManager getEventManager () { return eventManager; }
    public Track getTrack () { return track; }
    public CardBoard getCardBoard () { return cardBoard; }
    public EnumSet<Totem> getUsedTotems () { return usedTotems; }

    public boolean isLastRound () { return lastRound; }

    //### setters
    public void addControllerListener ( VirtualGameClient listener ) { this.controllersListeners.add(listener); }
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

    //### controller communication
    private void broadcastMockupGame () {
        synchronized (broadcastLock) {
            try{
                InitializeModelPacket initializeModel = new InitializeModelPacket(giveMockupGame());

                //iterates through all the controllers but only updates the ones connected
                int i = 0;
                while(i < numOfPlayers) {
                    if(players.get(i).isConnected()){
                        controllersListeners.get(i).initializeClientModel(initializeModel);
                    }
                    i++;
                }
            } catch (Exception _) {

            }
        }
    }
    public void broadcastGameUpdate ( UpdateModelPacket updateModelPacket ) {
        synchronized (broadcastLock) {
            try {
                //iterates through all the controllers but only updates the ones connected
                int i = 0;
                while (i < numOfPlayers) {
                    if (players.get(i).isConnected()) {
                        controllersListeners.get(i).updateClientModel(updateModelPacket);
                    }
                    i++;
                }
            } catch (Exception _){

            }
        }
    }
    public MockupGame giveMockupGame() {
        /*int currentPlayerIndex;
        if(currentPlayer != null) {
            currentPlayerIndex = currentPlayer.getPlayerIndex();
        }else{
            currentPlayerIndex = -1;
        }*/
        List<MockupPlayer> players = new ArrayList<>();
        for (Player player : this.players) {
            players.add(player.giveMockupPlayer());
        }

        List<Card> upperLine = getCardBoard().getLine().getUpperLine();
        List<Card> lowerLine = getCardBoard().getLine().getLowerLine();
        List<Card> upperBuilding = getCardBoard().getLine().getUpperBuilding();
        List<Card> lowerBuilding = getCardBoard().getLine().getLowerBuilding();

        List<MockupPlayer> offerBoard = getTrack().giveOfferBoardMockup();
        List<MockupPlayer> orderBoard = getTrack().giveOrderBoardMockup();

        return new MockupGame(players, cardBoard.getLine().getCurrentEra(), upperLine, lowerLine, upperBuilding, lowerBuilding, offerBoard, orderBoard);
    }

    //### Game's execution
    public void executeCurrentState () {
        currentState = currentState.executeState();
    }

    //TODO: implement a check to know if the current player is choosing the admissible actions of this phase.
    //### Players' actions
    public void chooseTotem ( int playerIndex, Totem chosenTotem ) {
        synchronized (Locks.playerInput) {
            if (players.get(playerIndex).getTotem() == null && !usedTotems.contains(chosenTotem)) {
                usedTotems.add(chosenTotem);
                players.get(playerIndex).setTotem(chosenTotem);

                UpdateModelPacket updateModelPacket = new UpdateModelPacket();
                updateModelPacket.addUpdateElement(
                        new TotemModelElement(
                            new String(playersNicknames.get(playerIndex) + " ha scelto il totem " + chosenTotem.toString()),
                            playerIndex,
                            chosenTotem
                        )
                );
                broadcastGameUpdate(updateModelPacket);

                Locks.playerInput.notify();
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
                    Locks.playerInput.notify();
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
                    Locks.playerInput.notify();
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
                    Locks.playerInput.notify();
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
                    Locks.playerInput.notify();
                }
            }
        }
    }

    public void chooseOffer ( int playerIndex, int offerIndex ) {
        synchronized (Locks.playerInput) {
            if(playerIndex == currentPlayerIndex) {
                track.assignOffer(players.get(playerIndex), offerIndex); //TODO: implement not valid offerIndex exception.
                Locks.playerInput.notify();
            }
        }
    }

    public void passYourTurn ( int playerIndex ) {
        synchronized (Locks.playerInput) {
            if(playerIndex == currentPlayerIndex) {
                players.get(playerIndex).cleanRemainingActions();
                Locks.playerInput.notify();
            }
        }
    }

    //### Connection methods
    public void disconnectPlayer( int playerIndex ) {

    }

    public void connectPlayer( int playerIndex ) {

    }
}
