package it.polimi.ingsw.gc49.server.rooms;

import it.polimi.ingsw.gc49.client.view.mockupHall.MockupRoom;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.CHANGE_PHASE.ChangePhasePacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.ANY_phase.DISCONNECT.DisconnectPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.ROOM_phase.ROOM_COMMAND.LEAVE.RoomLeavePacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.INITIALIZE_ROOM.InitializeRoomPacket;
import it.polimi.ingsw.gc49.server.Hall;
import it.polimi.ingsw.gc49.server.ServerMultiplexer;
import it.polimi.ingsw.gc49.server.proxies.PhasedProxyPlayer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualRoomServer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.adapters.VirtualRoomServerAdapter;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static it.polimi.ingsw.gc49.server.ServerMultiplexer.saveState;

/**
 * The {@code WaitingRoom} class represents a game room that is currently in the pre-game phase.
 * It gathers players until the maximum capacity is reached. Once full, it initiates a
 * countdown before automatically converting into a playing room and starting the match.
 */
public class WaitingRoom extends Room implements VirtualRoomServer {
    /** The countdown time (in seconds) to wait before the game starts once the room is full. */
    private static final int TIME_BEFORE_GAME_START = 10;

    /** * Scheduler used to handle the countdown timer for the game start.
     * Marked as transient so it is ignored during server state serialization.
     * Note: Because it is final, it cannot be easily re-instantiated upon deserialization.
     */
    private transient final ScheduledExecutorService startingGameScheduler = Executors.newSingleThreadScheduledExecutor();


    /**
     * Constructs a new {@code WaitingRoom}.
     *
     * @param server          the main server multiplexer managing the connections.
     * @param hall            the main hall that spawned this room.
     * @param roomName        the unique identifier/name of this room.
     * @param maxNumOfPlayers the maximum number of players required to start the game.
     */
    public WaitingRoom ( ServerMultiplexer server, Hall hall, String roomName, int maxNumOfPlayers ) {
        super(server, hall, roomName, maxNumOfPlayers);
    }

    /**
     * Schedules the start of the game after a predefined delay ({@value #TIME_BEFORE_GAME_START} seconds).
     * This method is triggered as soon as the room reaches its maximum player capacity.
     * * @throws RuntimeException if the scheduling fails or is interrupted.
     */
    private void scheduleGameStart() {
        try {
            startingGameScheduler.schedule(() -> {
                try {
                    startGame();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }, TIME_BEFORE_GAME_START, TimeUnit.SECONDS);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Attempts to start the game. It verifies that the room is still full,
     * and if so, asks the hall to convert this waiting room into an active playing room.
     *
     * @throws Exception if an error occurs during the room conversion or game initialization.
     */
    private void startGame() throws Exception {
        if(maxNumOfPlayers == getNumConnectedPlayers()){
            System.out.println("La partita nella stanza " + roomName + " sta iniziando...");
            //creates the game
            hall.changeRoomIntoPlaying(roomName);
        }
    }

    // ============================================================
    //DISCONNECTION
    // ============================================================

    /**
     * Handles the explicit network disconnection of a player currently in the waiting room.
     * Removes the player from the room's list, updates the remaining clients,
     * and forwards the disconnection event to the main server.
     *
     * @param disconnectPacket the packet containing the nickname of the disconnected player.
     * @throws Exception if an error occurs during the broadcasting or server disconnection.
     */
    @Override
    public void disconnect ( DisconnectPacket disconnectPacket ) throws Exception {
        String disconnectedNickname = disconnectPacket.getSenderNickname();
        //disconnects the player from the room
        players.removeIf(player -> player.nickname.equals(disconnectedNickname));
        broadcastMockupRoom(); // updates the rooms view
        server.disconnect(disconnectPacket); //removes the player reference from the server
    }

    // ============================================================
    //ROOM'S METHODS
    // ============================================================

    /**
     * Processes a new player entering the waiting room.
     * If there is space, it changes the player's phase to 'ROOM', binds them to the room,
     * updates their client, and checks if the room has reached full capacity to start the countdown.
     * If the room is full, an exception is thrown.
     *
     * @param newPlayer the {@link PhasedProxyPlayer} attempting to enter.
     * @throws RuntimeException if the room is already full.
     * @throws Exception if a network or synchronization error occurs.
     */
    @Override
    public void enterPlayer ( PhasedProxyPlayer newPlayer ) throws Exception {
        if(canEnter()) {
            try {
                newPlayer.changePhaseClient(new ChangePhasePacket(ApplicationPhase.ROOM));

                super.enterPlayer(newPlayer);
                newPlayer.setServerSideObject(new VirtualRoomServerAdapter(this));

                //sends the new player the room he is in
                newPlayer.initializeClientRoom(new InitializeRoomPacket(giveMockupRoom()));

                //broadcasts the new room
                broadcastMockupRoom();

                //starts the countdown to game start if the number of necessary players was reached
                if (maxNumOfPlayers == getNumConnectedPlayers()) {
                    scheduleGameStart();
                }
            } catch (Exception e) {
                newPlayer.forceDisconnect();
            }
        }else{
            throw new RuntimeException("La stanza è piena, non puoi entrare nella stanza " + roomName + ".");
        }
    }

    /**
     * Checks if the room can accept more players.
     *
     * @return {@code true} if the current number of players is strictly less than the maximum capacity.
     */
    @Override
    protected boolean canEnter() {
        return maxNumOfPlayers > getNumConnectedPlayers();
    }


    /**
     * Generates a lightweight, serializable representation (Mockup) of this waiting room.
     *
     * @return a {@link MockupRoom} configured as a WAITING room, containing the current players.
     */
    @Override
    public MockupRoom giveMockupRoom () {
        return new MockupRoom(MockupRoom.RoomType.WAITING, roomName, maxNumOfPlayers, getListPlayerNicknames());
    }

    /**
     * Upgrades this waiting room into an active playing room.
     * This is typically called right before the game begins.
     *
     * @return a new {@link PlayingRoom} instance retaining the same name, capacity, and connected players.
     */
    public PlayingRoom convertIntoPlaying () {
        return new PlayingRoom(server, hall, roomName, maxNumOfPlayers, players);
    }


    // ============================================================
    //CLIENTS COMMANDS
    // ============================================================

    /**
     * Handles a player's deliberate choice to leave the waiting room and return to the hall.
     *
     * @param roomLeavePacket the packet containing the nickname of the leaving player.
     * @throws Exception if an error occurs while moving the player back to the hall or broadcasting.
     */
    @Override
    public void leaveRoom ( RoomLeavePacket roomLeavePacket ) throws Exception {
        PhasedProxyPlayer senderPlayer = getPlayerByString(roomLeavePacket.getSenderNickname());
        if( senderPlayer != null ){
            //enter the hall
            players.remove(senderPlayer);
            hall.enterPlayer(senderPlayer);

            //broadcasts the new room
            broadcastMockupRoom();
        }
    }
}
