package it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers;

import it.polimi.ingsw.gc49.datapacket.directedToServer.ROOM_phase.ROOM_COMMAND.LEAVE.RoomLeavePacket;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.Disconnectable;

import java.rmi.Remote;

public interface VirtualRoomServer extends Remote, Disconnectable {
    void leaveRoom ( RoomLeavePacket roomLeavePacket ) throws Exception;

}
