package it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.adapters;

import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.CHANGE_PHASE.ChangePhasePacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.INITIALIZE_ROOM.InitializeRoomPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.GAME_phase.COMMAND.CommandPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.ANY_phase.DISCONNECT.DisconnectPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.HALL_phase.HALL_COMMAND.CREATE.HallCreatePacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.HALL_phase.HALL_COMMAND.JOIN.HallJoinPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.ROOM_phase.ROOM_COMMAND.LEAVE.RoomLeavePacket;
import it.polimi.ingsw.gc49.server.proxies.PhasedProxyPlayer;
import it.polimi.ingsw.gc49.server.rooms.WaitingRoom;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualRoomServer;

import static it.polimi.ingsw.gc49.server.ServerMultiplexer.saveStateAsync;

/**
 * The {@code VirtualRoomServerAdapter} class is a specific implementation of the Adapter pattern
 * for players who are currently waiting inside a game room.
 * It routes room-specific commands (such as leaving the room or disconnecting) to the underlying
 * {@link VirtualRoomServer}. It safely ignores commands that are invalid for this phase
 * (like in-game actions or hall commands) to prevent out-of-context errors.
 */
public class VirtualRoomServerAdapter extends VirtualServerAdapter {
    private final VirtualRoomServer adaptee;

    /**
     * Constructs a new {@code VirtualRoomServerAdapter}.
     *
     * @param adaptee the specific {@link VirtualRoomServer} (usually a {@code WaitingRoom})
     * that will process the valid commands.
     */
    public VirtualRoomServerAdapter(VirtualRoomServer adaptee) {
        this.adaptee = adaptee;
    }

    // ============================================================
    // DISCONNECTION
    // ============================================================

    /**
     * Forwards a disconnection event to the room and triggers an asynchronous server state save.
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
     * Ignored in this phase. A player cannot send in-game commands while in a waiting room.
     */
    @Override
    public void sendCommand ( CommandPacket commandPacket ) throws Exception {}


    // ============================================================
    // VIRTUAL HALL SERVER
    // ============================================================

    /**
     * Ignored in this phase. A player cannot join a new room if they are already inside one.
     */
    @Override
    public void joinRoom ( HallJoinPacket hallJoinPacket ) throws Exception {}
    @Override
    public void createRoom ( HallCreatePacket hallCreatePacket ) throws Exception {}


    // ============================================================
    // VIRTUAL ROOM SERVER
    // ============================================================


    /**
     * Forwards a player's request to leave the room back to the hall,
     * and triggers an asynchronous server state save.
     *
     * @param roomLeavePacket the packet indicating the leave request.
     * @throws Exception if an error occurs while leaving the room.
     */
    @Override
    public void leaveRoom ( RoomLeavePacket roomLeavePacket ) throws Exception {
        adaptee.leaveRoom(roomLeavePacket);
        saveStateAsync();
    }

    // ============================================================
    // RESILIENCE
    // ============================================================

    /**
     * Synchronizes the client's state if the player reconnects while the room is still in the waiting phase.
     * It forces the client's phase back to {@code ROOM} and sends the current room mockup.
     *
     * @param proxy the {@link PhasedProxyPlayer} representing the reconnected client.
     * @throws Exception if a network error occurs during synchronization.
     */
    @Override
    public void syncPlayer ( PhasedProxyPlayer proxy ) throws Exception {

        WaitingRoom waitingRoom = (WaitingRoom) adaptee;

        proxy.changePhaseClient( new ChangePhasePacket(ApplicationPhase.ROOM) );

        proxy.initializeClientRoom( new InitializeRoomPacket(waitingRoom.giveMockupRoom()) );

        System.out.println("[REJOIN] Sync completed for " + proxy.nickname);
    }

}
