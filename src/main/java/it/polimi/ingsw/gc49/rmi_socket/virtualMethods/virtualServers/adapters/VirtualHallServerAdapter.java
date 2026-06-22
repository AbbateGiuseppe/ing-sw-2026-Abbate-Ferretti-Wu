package it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.adapters;

import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.CHANGE_PHASE.ChangePhasePacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.INITIALIZE_HALL.InitializeHallPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.GAME_phase.COMMAND.CommandPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.ANY_phase.DISCONNECT.DisconnectPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.HALL_phase.HALL_COMMAND.CREATE.HallCreatePacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.HALL_phase.HALL_COMMAND.JOIN.HallJoinPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.ROOM_phase.ROOM_COMMAND.LEAVE.RoomLeavePacket;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;
import it.polimi.ingsw.gc49.server.Hall;
import it.polimi.ingsw.gc49.server.proxies.PhasedProxyPlayer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualHallServer;

import static it.polimi.ingsw.gc49.server.ServerMultiplexer.saveStateAsync;

/**
 * The {@code VirtualHallServerAdapter} class is a specific implementation of the Adapter pattern
 * for players who are currently in the main lobby (Hall).
 * It routes hall-specific commands (such as creating a room, joining a room, or disconnecting)
 * to the underlying {@link VirtualHallServer}. It safely ignores commands that are invalid
 * for this phase (like in-game actions or leaving a room) to prevent out-of-context errors.
 */


public class VirtualHallServerAdapter extends VirtualServerAdapter {
    private final VirtualHallServer adaptee;


    /**
     * Constructs a new {@code VirtualHallServerAdapter}.
     *
     * @param adaptee the specific {@link VirtualHallServer} (usually the main {@code Hall})
     * that will process the valid commands.
     */
    public VirtualHallServerAdapter(VirtualHallServer adaptee) {
        this.adaptee = adaptee;
    }

    // ============================================================
    // DISCONNECTION
    // ============================================================


    /**
     * Forwards a disconnection event to the hall and triggers an asynchronous server state save.
     *
     * @param disconnectPacket the packet containing disconnection details.
     * @throws Exception if an error occurs during disconnection.
     */
    @Override
    public void disconnect ( DisconnectPacket disconnectPacket ) throws Exception {
        adaptee.disconnect(disconnectPacket);
        saveStateAsync();
    }

    // ============================================================
    // VIRTUAL GAME SERVER
    // ============================================================


    /**
     * Ignored in this phase. A player cannot send in-game commands while in the hall.
     * * @param commandPacket the packet containing the game command.
     * @throws Exception if an unexpected error occurs.
     */
    @Override
    public void sendCommand ( CommandPacket commandPacket ) throws Exception {}

    // ============================================================
    // VIRTUAL HALL SERVER
    // ============================================================


    /**
     * Forwards a player's request to join an existing room and triggers an asynchronous server state save.
     *
     * @param hallJoinPacket the packet indicating the target room to join.
     * @throws Exception if an error occurs while joining the room.
     */
    @Override
    public void joinRoom ( HallJoinPacket hallJoinPacket ) throws Exception {
        adaptee.joinRoom(hallJoinPacket);
        saveStateAsync();
    }


    /**
     * Forwards a player's request to create a new room and triggers an asynchronous server state save.
     *
     * @param hallCreatePacket the packet containing the new room's details.
     * @throws Exception if an error occurs while creating the room.
     */
    @Override
    public void createRoom ( HallCreatePacket hallCreatePacket ) throws Exception {
        adaptee.createRoom(hallCreatePacket);
        saveStateAsync();
    }
    // ============================================================
    // VIRTUAL ROOM SERVER
    // ============================================================

    /**
     * Ignored in this phase. A player cannot leave a room if they are already in the hall.
     * * @param roomLeavePacket the packet indicating the leave request.
     * @throws Exception if an unexpected error occurs.
     */

    @Override
    public void leaveRoom ( RoomLeavePacket roomLeavePacket ) throws Exception {}


    // ============================================================
    // RESILIENCE
    // ============================================================

    /**
     * Ignored, sinchronize only when the player was in-game
     *
     * @param p the {@link PhasedProxyPlayer} representing the reconnected client.
     * @throws Exception if a network error occurs during synchronization.
     */
    @Override
    public void syncPlayer(PhasedProxyPlayer p) throws Exception {
        Hall hall = (Hall) adaptee;
        p.changePhaseClient(new ChangePhasePacket(ApplicationPhase.HALL));
        p.initializeClientHall(new InitializeHallPacket(hall.giveMockupHall()));
    }
}
