package it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualClients;

import it.polimi.ingsw.gc49.datapacket.directedToClient.INITIALIZE_ROOM.InitializeRoomPacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.UPDATE_ROOM.UpdateRoomPacket;

public interface VirtualRoomClient {
    void initializeClientRoom ( InitializeRoomPacket initializeRoomPacket ) throws Exception;

    void updateClientRoom ( UpdateRoomPacket updateRoomPacket ) throws Exception;
}
