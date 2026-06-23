package it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.adapters;

import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.CHANGE_PHASE.ChangePhasePacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.INITIALIZE_MODEL.InitializeModelPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.GAME_phase.COMMAND.CommandPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.ANY_phase.DISCONNECT.DisconnectPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.HALL_phase.HALL_COMMAND.CREATE.HallCreatePacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.HALL_phase.HALL_COMMAND.JOIN.HallJoinPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.ROOM_phase.ROOM_COMMAND.LEAVE.RoomLeavePacket;
import it.polimi.ingsw.gc49.server.proxies.PhasedProxyPlayer;
import it.polimi.ingsw.gc49.server.rooms.PlayingRoom;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualGameServer;

import static it.polimi.ingsw.gc49.server.ServerMultiplexer.saveStateAsync;


/**
 * Adapter used when the player is in the GAME phase. Forwards in-game commands and
 * disconnects to the underlying {@link VirtualGameServer} (usually a {@link PlayingRoom});
 * hall and room commands are ignored because they don't apply in this phase.
 */
public class VirtualGameServerAdapter extends VirtualServerAdapter {
    private final VirtualGameServer adaptee;

    public VirtualGameServerAdapter(VirtualGameServer adaptee) {
        this.adaptee = adaptee;
    }

    // ============================================================
    // DISCONNECTION
    // ============================================================

    /** Forwards the disconnection and persists the server state. */
    @Override
    public void disconnect ( DisconnectPacket disconnectPacket ) throws Exception {
        adaptee.disconnect(disconnectPacket);
        saveStateAsync();
    }

    // ============================================================
    // VIRTUAL GAME SERVER
    // ============================================================


    /** Forwards an in-game command and persists the server state. */
    @Override
    public void sendCommand ( CommandPacket commandPacket ) throws Exception {
        adaptee.sendCommand(commandPacket);
        saveStateAsync();
    }

    // ============================================================
    // VIRTUAL HALL SERVER
    // ============================================================

    @Override
    public void joinRoom ( HallJoinPacket hallJoinPacket ) throws Exception {}
    @Override
    public void createRoom ( HallCreatePacket hallCreatePacket ) throws Exception {}


    // ============================================================
    // VIRTUAL ROOM SERVER
    // ============================================================

    @Override
    public void leaveRoom ( RoomLeavePacket roomLeavePacket ) throws Exception {}


    // ============================================================
    // RESILIENCE
    // ============================================================

    @Override
    public void syncPlayer(PhasedProxyPlayer proxy) throws Exception {

        PlayingRoom playingRoom = (PlayingRoom) adaptee;
        proxy.changePhaseClient(new ChangePhasePacket(ApplicationPhase.GAME));
        proxy.initializeClientModel( new InitializeModelPacket(playingRoom.getGame().giveMockupGame()) );

        System.out.println("[GAME-SYNC] Synchronizing Completed " + proxy.nickname);
    }
}
