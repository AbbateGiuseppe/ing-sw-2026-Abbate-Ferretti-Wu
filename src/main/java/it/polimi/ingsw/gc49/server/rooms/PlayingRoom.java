package it.polimi.ingsw.gc49.server.rooms;

import it.polimi.ingsw.gc49.client.view.mockupHall.MockupRoom;
import it.polimi.ingsw.gc49.server.controller.MassiWuPeppeController;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.CHANGE_PHASE.ChangePhasePacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.GAME_phase.COMMAND.CommandPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.ANY_phase.DISCONNECT.DisconnectPacket;
import it.polimi.ingsw.gc49.server.model.Game;
import it.polimi.ingsw.gc49.server.Hall;
import it.polimi.ingsw.gc49.server.ServerMultiplexer;
import it.polimi.ingsw.gc49.server.proxies.PhasedProxyPlayer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualGameServer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.adapters.VirtualGameServerAdapter;

import java.util.List;

/**
 * The {@code PlayingRoom} class represents a game room where an active match is currently taking place.
 * It encapsulates the main {@link Game} instance and handles the initialization of the MVC architecture
 * (connecting the model, views, and controllers for each player).
 * Unlike a waiting room, a playing room restricts new players from entering and retains player
 * references upon disconnection to allow for seamless reconnections.
 */
public class PlayingRoom extends Room implements VirtualGameServer {

    /** The actual game instance running in this room. */
    private Game game;

    /**
     * Constructs a new, empty {@code PlayingRoom}.
     * Note: This constructor is generally not used since a playing room is typically
     * born from a populated waiting room.
     *
     * @param server          the main server multiplexer managing the connections.
     * @param hall            the main hall that spawned this room.
     * @param roomName        the unique identifier/name of this room.
     * @param maxNumOfPlayers the maximum capacity of the room.
     */
    @SuppressWarnings("unused")
    public PlayingRoom ( ServerMultiplexer server, Hall hall, String roomName, int maxNumOfPlayers ) {
        super(server, hall, roomName, maxNumOfPlayers);
    }

    /**
     * Constructs a {@code PlayingRoom} pre-filled with an existing list of players.
     * This is the standard constructor used when converting a WaitingRoom into a PlayingRoom.
     *
     * @param server          the main server multiplexer managing the connections.
     * @param hall            the main hall that spawned this room.
     * @param roomName        the unique identifier/name of this room.
     * @param maxNumOfPlayers the maximum capacity of the room.
     * @param players         the list of players who are part of this game.
     */
    public PlayingRoom ( ServerMultiplexer server, Hall hall, String roomName, int maxNumOfPlayers, List<PhasedProxyPlayer> players ) {
        super(server, hall, roomName, maxNumOfPlayers, players);
    }

    /**
     * Initializes the game logic, instantiates the controllers, and wires the MVC architecture.
     * For each connected player, it creates a {@link MassiWuPeppeController}, binds it to the game model,
     * updates the player's application phase to {@code GAME}, and assigns the necessary server adapters.
     * If a player disconnects during this delicate phase, they are forcefully disconnected.
     *
     * @throws Exception if an error occurs during the creation of the game or the network transmission.
     */
    public void createGame() throws Exception {
        //creates the game
        game = new Game(maxNumOfPlayers, getListPlayerNicknames(), roomName);

        //creates the controllers and connects them
        int playerIndex = 0;
        for(PhasedProxyPlayer player : players){
            MassiWuPeppeController controller = new MassiWuPeppeController(playerIndex, player); //creates a controller with the current index
            controller.connectModel(game); //connects controller to the game
            player.setController(controller); //connects the proxy to the controller
            try {
                player.changePhaseClient(new ChangePhasePacket(ApplicationPhase.GAME));//tells them they've entered a game
                player.setServerSideObject(new VirtualGameServerAdapter(this));
            } catch (Exception e) { //some bastard disconnected
                player.forceDisconnect();
            }
            playerIndex++;
        }
    }
    /**
     * Starts the main loop of the game.
     * This method blocks until the game finishes, so it should normally be executed
     * in its own dedicated thread to prevent blocking the main server threads.
     */
    public void runGame() {
        game.gameLoop();
        System.out.println("La partita nella stanza " + roomName + " è conclusa.");
    }

    /**
     * Retrieves the instance of the game running in this room.
     *
     * @return the current {@link Game} object.
     */
    public Game getGame() {
        return game;
    }

    // ============================================================
    //ROOM'S METHODS
    // ============================================================

    /**
     * Prevents new players from entering a game that has already started.
     * Since {@link #canEnter()} always returns {@code false} for a {@code PlayingRoom},
     * this method will invariably throw a {@code RuntimeException} unless specifically bypassed.
     *
     * @param newPlayer the {@link PhasedProxyPlayer} attempting to enter.
     * @throws RuntimeException indicating that the room is closed for new entries.
     * @throws Exception if a network error occurs (though highly unlikely due to the block).
     */
    @Override
    public void enterPlayer ( PhasedProxyPlayer newPlayer ) throws Exception {
        //Shouldn't be possible to enter a game that already started anyway.
        if(canEnter()) {
            try {
                newPlayer.changePhaseClient(new ChangePhasePacket(ApplicationPhase.GAME));
                super.enterPlayer(newPlayer);
                newPlayer.setServerSideObject(new VirtualGameServerAdapter(this));
            } catch (Exception e) {
                newPlayer.forceDisconnect();
            }
        }else{
            throw new RuntimeException("La partita è già iniziata, non puoi entrare nella stanza " + roomName + "." );
        }
    }

    /**
     * Determines whether the room can accept new players.
     * Active playing rooms are strictly locked to external entries.
     *
     * @return always {@code false}.
     */
    @Override
    protected boolean canEnter() {
        return false;
    }

    /**
     * Generates a lightweight, serializable representation (Mockup) of this playing room.
     *
     * @return a {@link MockupRoom} configured as a PLAYING room, containing the current players.
     */
    @Override
    public MockupRoom giveMockupRoom () {
        return new MockupRoom(MockupRoom.RoomType.PLAYING, roomName, maxNumOfPlayers, getListPlayerNicknames());
    }

    // ============================================================
    //CLIENT'S COMMAND
    // ============================================================

    /**
     * Intercepts a generic command packet sent by a client.
     * In the context of a {@code PlayingRoom}, in-game commands are typically routed
     * directly to the player's specific controller rather than being handled by the room itself.
     *
     * @param commandPacket the packet containing the command payload.
     * @throws Exception if an error occurs while processing the command.
     */
    @Override
    public void sendCommand ( CommandPacket commandPacket ) throws Exception {
        //already directed by controller.
    }

    // ============================================================
    //DISCONNECTION
    // ============================================================


    /**
     * Handles the network disconnection of a player during an active game.
     * Unlike the waiting room, a playing room deliberately does NOT remove the player's reference
     * from the server or the room. This allows the disconnected player to rejoin and seamlessly
     * resume their game session later.
     *
     * @param disconnectPacket the packet containing the disconnection details.
     * @throws Exception if an unexpected error occurs.
     */
    @Override
    public void disconnect ( DisconnectPacket disconnectPacket ) throws Exception {
        //player's reference remains on the server.
    }
}
