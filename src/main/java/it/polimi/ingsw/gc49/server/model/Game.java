package it.polimi.ingsw.gc49.server.model;

import it.polimi.ingsw.gc49.client.view.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.client.view.mockupModel.MockupOffer;
import it.polimi.ingsw.gc49.client.view.mockupModel.MockupOrder;
import it.polimi.ingsw.gc49.client.view.mockupModel.MockupPlayer;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.INITIALIZE_MODEL.InitializeModelPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.Cardboard.LowerDrawModelElement;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.Cardboard.UpperDrawModelElement;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.OfferOrderboard.OfferOrderboardModelElement;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.Players.ConnectionModelElement;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.Players.CurrentPlayerModelElement;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.Players.TotemModelElement;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.UpdateModelElement;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.UpdateModelPacket;
import it.polimi.ingsw.gc49.server.model.Card.BuildingCard.BuildingCard;
import it.polimi.ingsw.gc49.server.model.Card.Card;
import it.polimi.ingsw.gc49.server.model.CardBoard.CardBoard;
import it.polimi.ingsw.gc49.server.model.States.InitialSetup;
import it.polimi.ingsw.gc49.server.model.States.State;
import it.polimi.ingsw.gc49.server.model.Track.Track;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualClients.VirtualGameClient;
import it.polimi.ingsw.gc49.server.model.playerExceptions.InvalidTotem;
import it.polimi.ingsw.gc49.server.model.playerExceptions.NotYourTurnException;
import it.polimi.ingsw.gc49.server.model.playerExceptions.PlayerException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import static it.polimi.ingsw.gc49.server.model.Locks.broadcastLock;

public class Game implements Serializable, QueueUpdatable {
    private final String roomName;
    private final List<VirtualGameClient> controllersListeners = new ArrayList<>();
    private List<Player> players;
    private EventManager eventManager;
    private Track track;
    private CardBoard cardBoard;
    private State currentState;
    private Player currentPlayer;
    private int currentPlayerIndex;
    private final EnumSet<Totem> usedTotems = EnumSet.noneOf(Totem.class);
    private boolean lastRound;
    private UpdateModelPacket updatesQueue = new UpdateModelPacket();

    //### Constructors, from 2 to 5 players, handled by the initial stata via the numOfPlayers and playersNicknames
    public Game ( int numOfPlayers, List<String> playersNicknames, String roomName ) {
        this.roomName = roomName;
        currentState = new InitialSetup(this, numOfPlayers, playersNicknames);
        executeCurrentState();
    }

    public void gameLoop () {
        broadcastMockupGame();

        while(currentState != null) { //GAME'S LOOP, UNTIL THE NEXT STATE IS NULL
            synchronized (Locks.playerInput) {
                System.out.println("\"" + roomName + "\": " + "Entrando in un stato [" + currentState.toString() + "]...");
                executeCurrentState();
                System.out.println("\"" + roomName + "\": " + "Finito lo stato di gioco precedente.");
            }
        }

        //send standings (last updates)
        broadcastGameUpdate();
    }

    //### getters
    public int getNumOfPlayers () { return players.size(); }
    public int getNumOfConnectedPlayers () { return players.stream().filter(Player::isConnected).toList().size(); }
    public List<Player> getPlayers () { return players; }
    public List<String> getPlayersNicknames () { return players.stream().map(Player::getNickname).toList(); }
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
    public void setCurrentPlayer ( Player currentPlayer ) { this.currentPlayer = currentPlayer; }
    public void setCurrentPlayerIndex ( int currentPlayerIndex ) { this.currentPlayerIndex = currentPlayerIndex; }
    public void setLastRound ( boolean lastRound ) { this.lastRound = lastRound; }

    //### controller communication
    @Override
    public void queueUpdateModelElement ( UpdateModelElement updateModelElement ) {
        updatesQueue.addUpdateElement(updateModelElement);
    }
    private void broadcastMockupGame () {
        synchronized (broadcastLock) {
            try{
                InitializeModelPacket initializeModel = new InitializeModelPacket(giveMockupGame());

                //iterates through all the controllers but only updates the ones connected
                int i = 0;
                while(i < getNumOfPlayers()) {
                    if(players.get(i).isConnected()){
                        controllersListeners.get(i).initializeClientModel(initializeModel);
                    }
                    i++;
                }
            } catch (Exception _) {

            }
        }
    }
    public void broadcastGameUpdate () {
        synchronized (broadcastLock) {
            try {
                //iterates through all the controllers but only updates the ones connected
                int i = 0;
                while (i < getNumOfPlayers()) {
                    if (players.get(i).isConnected()) {
                        controllersListeners.get(i).updateClientModel(updatesQueue);
                    }
                    i++;
                }
            } catch (Exception _){

            } finally {
                updatesQueue = new UpdateModelPacket();
            }
        }
    }
    public void broadcastCurrentPlayerTurn() {
        synchronized (broadcastLock) {
            queueUpdateModelElement(new CurrentPlayerModelElement(
                    "...tocca a " + currentPlayer.getNickname() + "...",
                    currentPlayerIndex
                    )
            );
            broadcastGameUpdate();
        }
    }

    /**
     * Creates the mockup for the entire game, to send to the clients, usually for initialization.
     * @return MockupGame
     */
    public MockupGame giveMockupGame() {
        List<MockupPlayer> players = new ArrayList<>();
        for (Player player : this.players) {
            players.add(player.giveMockupPlayer());
        }

        List<Card> upperLine = getCardBoard().getLine().getUpperLine();
        List<Card> lowerLine = getCardBoard().getLine().getLowerLine();
        List<Card> upperBuilding = getCardBoard().getLine().getUpperBuilding();
        List<Card> lowerBuilding = getCardBoard().getLine().getLowerBuilding();

        List<MockupOffer> offerBoard = getTrack().giveOfferBoardMockup();
        List<MockupOrder> orderBoard = getTrack().giveOrderBoardMockup();

        return new MockupGame(players, cardBoard.getLine().getCurrentEra(), upperLine, lowerLine, upperBuilding, lowerBuilding, offerBoard, orderBoard);
    }

    //### Game's execution
    public void executeCurrentState () {
        currentState = currentState.executeState();
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

    //### Players' actions
    public void chooseTotem ( int playerIndex, Totem chosenTotem ) throws PlayerException {
        synchronized (Locks.playerInput) {
            if (players.get(playerIndex).getTotem() == null) {
                if (!usedTotems.contains(chosenTotem)) {
                    if (currentState.getCurrentStateType().equals(State.States.TOTEM_CHOOSING)) {
                        usedTotems.add(chosenTotem);
                        players.get(playerIndex).setTotem(chosenTotem);

                        queueUpdateModelElement(
                                new TotemModelElement(
                                        players.get(playerIndex).getNickname() + " ha scelto il totem " + chosenTotem.toString(),
                                        playerIndex,
                                        chosenTotem
                                )
                        );
                        broadcastGameUpdate();

                        Locks.playerInput.notify();
                    }
                } else {
                    throw new InvalidTotem("Questo totem è già stato preso da un altro giocatore.");
                }
            } else {
                throw new InvalidTotem("Hai già scelto un totem.");
            }
        }
    }

    public void drawUpperCharacter ( int playerIndex, int cardIndex ) throws PlayerException {
        synchronized (Locks.playerInput) {
            if( playerIndex == currentPlayerIndex ) {
                if (currentState.getCurrentStateType().equals(State.States.OFFER_EXECUTION) ) {
                    Player drawingPlayer = players.get(playerIndex);
                    if (drawingPlayer.getDrawableUpper() > 0) {
                        Card drawnCard = cardBoard.drawUpperCharacter(cardIndex, drawingPlayer);
                        if (drawnCard != null) {
                            drawingPlayer.setDrawableUpper(drawingPlayer.getDrawableUpper() - 1); //decreases by one the player's drawable upper cards.
                            drawingPlayer.addCharacterCard(drawnCard); //adds the drawn card to the player, if it's drawable by him.
                            callDrawEvent();

                            queueUpdateModelElement(
                                    new UpperDrawModelElement(
                                            players.get(playerIndex).getNickname() + " ha pescato " + drawnCard.simpleToString() + " dalla fila superiore"
                                                    + " (azioni rimanenti: " + players.get(playerIndex).getDrawableUpper() + " sup, " + players.get(playerIndex).getDrawableLower() + " inf)",
                                            cardBoard.getLine().getUpperLine(),
                                            cardBoard.getLine().getUpperBuilding(),
                                            playerIndex,
                                            drawnCard,
                                            true
                                    )
                            );
                            broadcastGameUpdate();

                            Locks.playerInput.notify();
                        }
                    }
                }
            } else {
                throw new NotYourTurnException("Non è il tuo turno.");
            }
        }
    }

    public void drawLowerCharacter ( int playerIndex, int cardIndex ) throws PlayerException {
        synchronized (Locks.playerInput) {
            if( playerIndex == currentPlayerIndex ) {
                if (currentState.getCurrentStateType().equals(State.States.OFFER_EXECUTION) ) {
                    Player drawingPlayer = players.get(playerIndex);
                    if(drawingPlayer.getDrawableLower() > 0) {
                        Card drawnCard = cardBoard.drawLowerCharacter(cardIndex, drawingPlayer);
                        if (drawnCard != null) {
                            drawingPlayer.setDrawableLower(drawingPlayer.getDrawableLower() - 1); //decreases by one the player's drawable lower cards.
                            drawingPlayer.addCharacterCard(drawnCard); //adds the drawn card to the player, if it's drawable by him.
                            callDrawEvent();

                            queueUpdateModelElement(
                                    new LowerDrawModelElement(
                                            players.get(playerIndex).getNickname() + " ha pescato " + drawnCard.simpleToString() + " dalla fila inferiore"
                                                    + " (azioni rimanenti: " + players.get(playerIndex).getDrawableUpper() +  " sup, " + players.get(playerIndex).getDrawableLower() + " inf)",
                                            cardBoard.getLine().getLowerLine(),
                                            cardBoard.getLine().getLowerBuilding(),
                                            playerIndex,
                                            drawnCard,
                                            true
                                    )
                            );
                            broadcastGameUpdate();

                            Locks.playerInput.notify();
                        }
                    }
                }
            } else {
                throw new NotYourTurnException("Non è il tuo turno.");
            }
        }
    }

    public void drawUpperBuilding ( int playerIndex, int cardIndex ) throws PlayerException {
        synchronized (Locks.playerInput) {
            if( playerIndex == currentPlayerIndex ) {
                if( currentState.getCurrentStateType().equals(State.States.OFFER_EXECUTION) ) {
                    Player drawingPlayer = players.get(playerIndex);
                    if(drawingPlayer.getDrawableUpper() > 0) {
                        BuildingCard drawnBuildingCard = (BuildingCard) cardBoard.drawUpperBuilding(cardIndex, drawingPlayer);
                        if (drawnBuildingCard != null) {
                            drawingPlayer.setDrawableUpper(drawingPlayer.getDrawableUpper() - 1); //decreases by one the player's drawable upper cards.
                            drawnBuildingCard.addBuildingToManager(currentPlayer, eventManager); //adds the building as a listener.
                            drawingPlayer.addBuildingCard(drawnBuildingCard); //adds the drawn card to the player, if it's drawable by him.
                            callDrawEvent();

                            queueUpdateModelElement(
                                    new UpperDrawModelElement(
                                            players.get(playerIndex).getNickname() + " ha pescato " + drawnBuildingCard.simpleToString() + " dalla fila superiore"
                                                    + " (azioni rimanenti: " + players.get(playerIndex).getDrawableUpper() +  " sup, " + players.get(playerIndex).getDrawableLower() + " inf)",
                                            cardBoard.getLine().getUpperLine(),
                                            cardBoard.getLine().getUpperBuilding(),
                                            playerIndex,
                                            drawnBuildingCard,
                                            false
                                    )
                            );
                            broadcastGameUpdate();

                            Locks.playerInput.notify();
                        }
                    }
                }
            } else {
                throw new NotYourTurnException("Non è il tuo turno.");
            }
        }
    }

    public void drawLowerBuilding ( int playerIndex, int cardIndex ) throws PlayerException {
        synchronized (Locks.playerInput) {
            if( playerIndex == currentPlayerIndex ) {
                if(currentState.getCurrentStateType().equals(State.States.OFFER_EXECUTION) ) {
                    Player drawingPlayer = players.get(playerIndex);
                    if(drawingPlayer.getDrawableLower() > 0){
                        BuildingCard drawnBuildingCard = (BuildingCard) cardBoard.drawLowerBuilding(cardIndex, drawingPlayer);
                        if (drawnBuildingCard != null) {
                            drawingPlayer.setDrawableLower(drawingPlayer.getDrawableLower() - 1); //decreases by one the player's drawable lower cards.
                            drawnBuildingCard.addBuildingToManager(currentPlayer, eventManager);
                            drawingPlayer.addBuildingCard(drawnBuildingCard); //adds the drawn card to the player, if it's drawable by him.
                            callDrawEvent();

                            queueUpdateModelElement(
                                    new LowerDrawModelElement(
                                            players.get(playerIndex).getNickname() + " ha pescato " + drawnBuildingCard.simpleToString() + " dalla fila inferiore"
                                            + " (azioni rimanenti: " + players.get(playerIndex).getDrawableUpper() +  " sup, " + players.get(playerIndex).getDrawableLower() + " inf)",
                                            cardBoard.getLine().getLowerLine(),
                                            cardBoard.getLine().getLowerBuilding(),
                                            playerIndex,
                                            drawnBuildingCard,
                                            false
                                    )
                            );
                            broadcastGameUpdate();

                            Locks.playerInput.notify();
                        }
                    }
                }
            } else {
                throw new NotYourTurnException("Non è il tuo turno.");
            }
        }
    }

    public void chooseOffer ( int playerIndex, int offerIndex ) throws PlayerException {
        synchronized (Locks.playerInput) {
            if( playerIndex == currentPlayerIndex ) {
                if (currentState.getCurrentStateType().equals(State.States.OFFER_CHOOSING)) {
                    Player callingPlayer = players.get(playerIndex);

                    track.assignOffer(callingPlayer, offerIndex); //throws NotValidOfferException
                    queueUpdateModelElement(new OfferOrderboardModelElement(
                            callingPlayer.getNickname() + " ha scelto l'offerta[" + (offerIndex) + "].",
                            track.giveOfferBoardMockup(),
                            track.giveOrderBoardMockup()
                    ));
                    broadcastGameUpdate();
                    callingPlayer.setChoseAnOffer(true);
                    Locks.playerInput.notify();
                }
            } else {
                throw new NotYourTurnException("Non è il tuo turno.");
            }
        }
    }

    public void passYourTurn ( int playerIndex ) {
        synchronized (Locks.playerInput) {
            if( playerIndex == currentPlayerIndex && !currentState.getCurrentStateType().equals(State.States.OTHER) ) {
                players.get(playerIndex).cleanRemainingActions();
                Locks.playerInput.notify();
            }
        }
    }

    //### Connection methods
    public void disconnectPlayer( int playerIndex ) {
        queueUpdateModelElement(new ConnectionModelElement(
                players.get(playerIndex).getNickname() + " si è disconnesso",
                playerIndex,
                false
        ));
        players.get(playerIndex).setConnected(false);
        //TODO: finish these two disconnection methods and implement the symbiosis with the state-machine's states
        synchronized (Locks.playerInput) {
            Locks.playerInput.notify();
        }

    }

    public void connectPlayer( int playerIndex ) {
        queueUpdateModelElement(new ConnectionModelElement(
                players.get(playerIndex).getNickname() + " si è riconnesso",
                playerIndex,
                true
        ));
        players.get(playerIndex).setConnected(true);

        synchronized (Locks.playerInput) {
            Locks.playerInput.notify();
        }
    }
}
