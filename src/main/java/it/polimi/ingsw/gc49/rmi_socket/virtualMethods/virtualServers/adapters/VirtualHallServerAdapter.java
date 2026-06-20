package it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.adapters;

import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.INITIALIZE_HALL.InitializeHallPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.CHANGE_PHASE.ChangePhasePacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.GAME_phase.COMMAND.CommandPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.ANY_phase.DISCONNECT.DisconnectPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.HALL_phase.HALL_COMMAND.CREATE.HallCreatePacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.HALL_phase.HALL_COMMAND.JOIN.HallJoinPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.ROOM_phase.ROOM_COMMAND.LEAVE.RoomLeavePacket;
import it.polimi.ingsw.gc49.server.Hall;
import it.polimi.ingsw.gc49.server.proxies.PhasedProxyPlayer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualHallServer;

import static it.polimi.ingsw.gc49.server.ServerMultiplexer.saveStateAsync;


public class VirtualHallServerAdapter extends VirtualServerAdapter {
    private final VirtualHallServer adaptee;

    public VirtualHallServerAdapter(VirtualHallServer adaptee) {
        this.adaptee = adaptee;
    }

    ///------------------------
    // disconnection
    @Override
    public void disconnect ( DisconnectPacket disconnectPacket ) throws Exception {
        adaptee.disconnect(disconnectPacket);
        saveStateAsync();
    }

    //### VirtualGameServer
    @Override
    public void sendCommand ( CommandPacket commandPacket ) throws Exception {}

    //### VirtualHallServer
    @Override
    public void joinRoom ( HallJoinPacket hallJoinPacket ) throws Exception {
        adaptee.joinRoom(hallJoinPacket);
        saveStateAsync();
    }

    @Override
    public void createRoom ( HallCreatePacket hallCreatePacket ) throws Exception {
        adaptee.createRoom(hallCreatePacket);
        saveStateAsync();
    }
    //### VirtualRoomServer
    @Override
    public void leaveRoom ( RoomLeavePacket roomLeavePacket ) throws Exception {}

    @Override
    public void syncPlayer(PhasedProxyPlayer p) throws Exception {
        Hall hall = (Hall) adaptee;
        p.changePhaseClient(new ChangePhasePacket(ApplicationPhase.HALL));
        p.initializeClientHall(new InitializeHallPacket(hall.giveMockupHall()));
    }
}
