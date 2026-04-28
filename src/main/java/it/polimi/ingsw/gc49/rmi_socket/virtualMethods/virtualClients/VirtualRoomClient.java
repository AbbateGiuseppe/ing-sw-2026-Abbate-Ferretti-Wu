package it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualClients;

import it.polimi.ingsw.gc49.datapacket.directedToClient.ROOM_phase.ROOM_CLIENT.INITIALIZE.RoomClientInitializePacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.ROOM_phase.ROOM_CLIENT.UPDATE.RoomClientUpdatePacket;

public interface VirtualRoomClient {
    void initializeClientRoom ( RoomClientInitializePacket roomClientInitializePacket ) throws Exception;

    void updateClientRoom ( RoomClientUpdatePacket roomClientUpdatePacket ) throws Exception;
}
