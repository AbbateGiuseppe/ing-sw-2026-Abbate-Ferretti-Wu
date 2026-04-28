package it.polimi.ingsw.gc49.rmi_socket.virtualServers;

import it.polimi.ingsw.gc49.datapacket.sentFromClient.ROOM_COMMAND.RoomLeavePacket;

public interface VirtualRoomServer {
    void leaveRoom ( RoomLeavePacket roomLeavePacket ) throws Exception;
}
