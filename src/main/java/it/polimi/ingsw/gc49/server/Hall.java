package it.polimi.ingsw.gc49.server;

import it.polimi.ingsw.gc49.client.view.mockupHall.MockupHall;
import it.polimi.ingsw.gc49.client.view.mockupHall.MockupRoom;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.CHANGE_PHASE.ChangePhasePacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.INITIALIZE_HALL.InitializeHallPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_HALL.UpdateHallPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.ANY_phase.DISCONNECT.DisconnectPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.HALL_phase.HALL_COMMAND.CREATE.HallCreatePacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.HALL_phase.HALL_COMMAND.JOIN.HallJoinPacket;
import it.polimi.ingsw.gc49.server.proxies.PhasedProxyPlayer;
import it.polimi.ingsw.gc49.server.rooms.WaitingRoom;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualHallServer;
import it.polimi.ingsw.gc49.server.rooms.PlayingRoom;
import it.polimi.ingsw.gc49.server.rooms.Room;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.adapters.VirtualHallServerAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The {@code Hall} class acts as the main lobby of the game application.
 * It manages players who are currently not in any game, handles the creation
 * and joining of game rooms, and broadcasts lobby updates to all connected users.
 * It implements {@link VirtualHallServer} to expose hall commands and is
 * {@link Serializable} to support server state persistence.
 */
public class Hall implements VirtualHallServer, Serializable {
    /** Reference to the main server multiplexer. Marked as transient so it is not serialized. */
    private transient ServerMultiplexer server;

    /** Map of all active rooms (both waiting and playing), indexed by their room name. */
    private final Map<String, Room> rooms = new HashMap<>();

    /** Map of all players currently idle in the hall, indexed by their nickname. */
    private final Map<String, PhasedProxyPlayer> PlayersInHall = new HashMap<>();

    /**
     * Sets the server reference for this hall.
     * Useful for initialization and recovering state after a server crash.
     *
     * @param server the {@link ServerMultiplexer} instance handling the main connections.
     */
    public void setServer ( ServerMultiplexer server ){
        this.server = server;
    }


    /**
     * Registers a newly connected or returning player into the hall.
     * It binds the player to the hall environment, updates their client phase,
     * sends them the initial lobby state, and broadcasts the updated hall to everyone else.
     *
     * @param newPlayer the proxy representing the client joining the hall.
     * @throws Exception if an error occurs during the connection or packet transmission.
     */
    public void enterPlayer ( PhasedProxyPlayer newPlayer ) throws Exception {
        synchronized (PlayersInHall) {
            System.out.println("Il giocatore " + newPlayer.nickname + " è entrato nell'atrio.");
            PlayersInHall.put(newPlayer.nickname, newPlayer);

            try {
                //connect the proxy to the hall
                newPlayer.setServerSideObject(new VirtualHallServerAdapter(this));
                //tells the player he entered the hall
                newPlayer.changePhaseClient(new ChangePhasePacket(ApplicationPhase.HALL));
                //sends the new player the hall he is in
                newPlayer.initializeClientHall(new InitializeHallPacket(giveMockupHall()));
            } catch (Exception e) {
                newPlayer.forceDisconnect();
            } finally {
                //broadcasts the new hall
                broadcastMockupHall();
            }
        }
    }


    // ============================================================
    //DISCONNECTIONS
    // ============================================================



    /**
     * Handles the explicit disconnection of a player who is currently in the hall.
     * Removes the player from the active hall list, updates the remaining clients,
     * and notifies the main server.
     *
     * @param disconnectPacket the packet containing details about the disconnecting player.
     * @throws Exception if an error occurs during the disconnection broadcast.
     */
    @Override
    public void disconnect ( DisconnectPacket disconnectPacket ) throws Exception {
        synchronized (PlayersInHall) {
            String disconnectedNickname = disconnectPacket.getSenderNickname();
            PlayersInHall.remove(disconnectedNickname); //disconnects the player from the hall
            broadcastMockupHall(); // updates the hall view
            server.disconnect(disconnectPacket); //removes the player reference from the server
        }
    }



    // ============================================================
    //HALL COMMANDS
    // ============================================================


    /**
     * Processes a player's request to join an existing game room.
     * Moves the player from the hall into the requested room and broadcasts the lobby update.
     *
     * @param hallJoinPacket the packet containing the sender's nickname and the target room name.
     * @throws Exception if the target room does not exist, or the player is not found in the hall.
     */
    @Override
    public void joinRoom ( HallJoinPacket hallJoinPacket ) throws Exception {
        synchronized (PlayersInHall) {
            Room joiningRoom = rooms.get(hallJoinPacket.roomName);
            if (joiningRoom != null) {
                //gets the sending player
                String senderNickname = hallJoinPacket.getSenderNickname();
                PhasedProxyPlayer senderPlayer = PlayersInHall.get(senderNickname);

                if (senderPlayer != null) {
                    joiningRoom.enterPlayer(senderPlayer); //enters the player into the room.
                    PlayersInHall.remove(senderNickname); //removes the player from the hall.

                    //broadcasts the new hall
                    broadcastMockupHall();
                } else {
                    throw new RuntimeException("Giocatore non presente nell'atrio");
                }
            } else {
                throw new RuntimeException("Stanza non trovata");
            }
        }
    }

    /**
     * Processes a player's request to create a new game room.
     * Initializes a {@link WaitingRoom}, moves the creator inside it, and broadcasts the hall update.
     *
     * @param hallCreatePacket the packet containing the sender's nickname, the room name, and its max capacity.
     * @throws Exception if the player is not found in the hall.
     */
    @Override
    public void createRoom ( HallCreatePacket hallCreatePacket ) throws Exception {
        synchronized (PlayersInHall) {
            String senderNickname = hallCreatePacket.getSenderNickname();
            PhasedProxyPlayer senderPlayer = PlayersInHall.get(senderNickname);

            if (senderPlayer != null) {
                Room newRoom = new WaitingRoom(server, this, hallCreatePacket.roomName, hallCreatePacket.maxNumOfPlayers); //Creates a new room with Id of the last room + 1.

                newRoom.enterPlayer(senderPlayer); //adds the player to the room.
                PlayersInHall.remove(senderNickname); //removes the player from the hall.
                rooms.put(newRoom.roomName, newRoom); //adds the room to the list of rooms.

                //broadcasts the new hall
                broadcastMockupHall();
            } else {
                throw new RuntimeException("Giocatore non presente nell'atrio");
            }
        }
    }

    public void closeRoom ( String roomName ) {
        rooms.remove(roomName);
        try {
            broadcastMockupHall();
        } catch (Exception _) {
        }
    }

    /**
     * Generates a lightweight, serializable representation (Mockup) of the current hall state.
     * This object is suitable for being transmitted over the network to the clients.
     *
     * @return a {@link MockupHall} containing the list of player nicknames and the status of active rooms.
     */

    public MockupHall giveMockupHall() {
        synchronized (PlayersInHall) {
            synchronized (rooms) {
                List<MockupRoom> mockupRooms = new ArrayList<>();
                for (Room room : rooms.values()) {
                    mockupRooms.add(room.giveMockupRoom());
                }

                return new MockupHall(
                        new ArrayList<>(PlayersInHall.keySet()),
                        mockupRooms
                );
            }
        }
    }

    /**
     * Broadcasts the current hall state (as a Mockup) to all players currently waiting in the hall.
     * If a player is unreachable during the broadcast, they are forcibly disconnected.
     *
     * @throws Exception if a critical network error occurs.
     */
    public void broadcastMockupHall() throws Exception {
        synchronized (PlayersInHall) {
            UpdateHallPacket updatedHall = new UpdateHallPacket(giveMockupHall());

            for (PhasedProxyPlayer player : PlayersInHall.values()) {
                try {
                    player.updateClientHall(updatedHall);
                } catch (Exception e) {
                    player.forceDisconnect();
                    return; //already finished the broadcast in the forceDisconnect
                }
            }
        }
    }

    /**
     * Converts a {@link WaitingRoom} into a {@link PlayingRoom} once it reaches the required conditions.
     * Initializes the game controllers, replaces the room in the active rooms map,
     * and starts the game loop in a new background thread.
     *
     * @param WaitingRoomName the name of the waiting room that is being changed into a playing room.
     * @throws Exception if an error occurs during room conversion or game initialization.
     */
    public void changeRoomIntoPlaying (String WaitingRoomName) throws Exception {
        synchronized (rooms) {
            WaitingRoom changingRoom = (WaitingRoom) rooms.get(WaitingRoomName); //gets the waitingRoom to change into a playing room
            PlayingRoom playingRoom = changingRoom.convertIntoPlaying(); //gets its converted version.
            rooms.remove(WaitingRoomName);
            rooms.put(playingRoom.roomName, playingRoom);

            //creates,initializes the new game, even connects the controllers.
            playingRoom.createGame();

            //starts the new game with its own thread.
            new Thread(playingRoom::runGame).start();
        }
    }


    /**
     * Retrieves the map of all currently active rooms.
     *
     * @return a map containing all the {@link Room} instances, indexed by their room names.
     */
    public Map<String, Room> getRooms(){return rooms;}


}
