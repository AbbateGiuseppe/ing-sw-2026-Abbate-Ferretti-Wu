package it.polimi.ingsw.gc49.server.rooms;

import it.polimi.ingsw.gc49.client.view.mockupHall.MockupRoom;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_ROOM.UpdateRoomPacket;
import it.polimi.ingsw.gc49.server.Hall;
import it.polimi.ingsw.gc49.server.ServerMultiplexer;
import it.polimi.ingsw.gc49.server.proxies.PhasedProxyPlayer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.Disconnectable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * The {@code Room} class represents an abstract base concept for any game room within the application.
 * It manages the fundamental properties shared by all rooms, such as the room's name,
 * its maximum capacity, and the list of currently connected players.
 * Implementing classes (e.g., WaitingRoom, PlayingRoom) must define specific behaviors
 * for entering the room and generating its mockup.
 */
public abstract class Room implements Disconnectable, Serializable {

    /** * Reference to the main server multiplexer.
     * Marked as transient so it is ignored during the serialization of the room state.
     */
    protected transient ServerMultiplexer server;


    /** The main lobby (Hall) that manages this room. */
    protected final Hall hall;

    /** The unique identifier or name of this room. */
    public final String roomName;

    /** The maximum number of players allowed in this room. */
    public final int maxNumOfPlayers;

    /** The list of players currently inside this room. */
    protected final List<PhasedProxyPlayer> players = new ArrayList<>();

    /**
     * Constructs a new, empty {@code Room}.
     *
     * @param server          the main server multiplexer managing the connections.
     * @param hall            the main hall that spawned this room.
     * @param roomName        the unique identifier/name of this room.
     * @param maxNumOfPlayers the maximum capacity of the room.
     */
    public Room ( ServerMultiplexer server, Hall hall, String roomName, int maxNumOfPlayers ) {
        this.server = server;
        this.hall = hall;
        this.roomName = roomName;
        this.maxNumOfPlayers = maxNumOfPlayers;
    }

    /**
     * Constructs a {@code Room} and pre-fills it with an existing list of players.
     * This is particularly useful when converting a WaitingRoom into a PlayingRoom.
     *
     * @param server          the main server multiplexer managing the connections.
     * @param hall            the main hall that spawned this room.
     * @param roomName        the unique identifier/name of this room.
     * @param maxNumOfPlayers the maximum capacity of the room.
     * @param players         the initial list of players to migrate into this room.
     */
    public Room ( ServerMultiplexer server, Hall hall, String roomName, int maxNumOfPlayers, List<PhasedProxyPlayer> players ) {
        this.server = server;
        this.hall = hall;
        this.roomName = roomName;
        this.maxNumOfPlayers = maxNumOfPlayers;
        this.players.addAll(players);
    }

    /**
     * Adds a player to the room's internal list and logs the entry.
     *
     * @param newPlayer the {@link PhasedProxyPlayer} attempting to enter the room.
     * @throws Exception if an error occurs during the entry process.
     */
    public void enterPlayer ( PhasedProxyPlayer newPlayer ) throws Exception {
        System.out.println("Il giocatore " + newPlayer.nickname + " è entrato nella stanza " + roomName + ".");
        players.add(newPlayer);
    }

    /**
     * Determines whether the room can accept new players.
     *
     * @return {@code true} if the room is open and not full, {@code false} otherwise.
     */
    protected abstract boolean canEnter();


    /**
     * Retrieves the current number of players inside the room.
     *
     * @return the size of the internal players list.
     */
    public int getNumConnectedPlayers () {
        return players.size();
    }


    /**
     * Searches for a specific player inside the room by their nickname.
     *
     * @param nickname the exact nickname of the player to search for.
     * @return the {@link PhasedProxyPlayer} matching the nickname, or {@code null} if not found.
     */
    protected PhasedProxyPlayer getPlayerByString ( String nickname ) {
        for( PhasedProxyPlayer player : players ) {
            if(nickname.equals(player.nickname)){
                return player;
            }
        }
        return null;
    }

    /**
     * Extracts a list containing only the nicknames of the currently connected players.
     *
     * @return a {@link List} of {@link String} representing the players' nicknames.
     */
    protected List<String> getListPlayerNicknames () {
        return players.stream()
                .map(player -> player.nickname)
                .collect(Collectors.toList());
    }

    /**
     * Generates a lightweight, serializable representation (Mockup) of this room state.
     *
     * @return a {@link MockupRoom} containing the current snapshot of the room.
     */
    public abstract MockupRoom giveMockupRoom ();


    /**
     * Broadcasts the current room state (as a Mockup) to all players inside.
     * If a transmission to a specific player fails, that player is forcefully disconnected.
     *
     * @throws Exception if a critical network error disrupts the entire broadcasting process.
     */
    protected void broadcastMockupRoom() throws Exception {
        UpdateRoomPacket updatedRoom = new UpdateRoomPacket(giveMockupRoom());

        for (PhasedProxyPlayer player : players) {
            try {
                player.updateClientRoom(updatedRoom);
            } catch (Exception e) {
                player.forceDisconnect();
                return; //already finished the broadcast in the forceDisconnect
            }
        }
    }

    // ============================================================
    //PERSISTENCE
    // ============================================================

    /**
     * Sets or restores the server reference for this room.
     * Essential for restoring the room's capabilities after a server state recovery
     * (since the server reference is transient).
     *
     * @param server the new {@link ServerMultiplexer} to inject.
     */
    public void setServer(ServerMultiplexer server){
        this.server=server;
    }


    /**
     * Verify if room's empty
     * * @return {@code true} if there are no players, {@code false} otherwise.
     */
    public boolean isEmpty() {
        return players.isEmpty();
    }
}
