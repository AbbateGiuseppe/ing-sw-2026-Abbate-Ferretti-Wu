package it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers;

import it.polimi.ingsw.gc49.datapacket.directedToServer.ROOM_phase.ROOM_COMMAND.LEAVE.RoomLeavePacket;

public interface VirtualRoomServer extends VirtualServer {
    void leaveRoom ( RoomLeavePacket roomLeavePacket ) throws Exception;
}
