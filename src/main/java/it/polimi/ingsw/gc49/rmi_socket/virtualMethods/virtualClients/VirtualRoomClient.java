package it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualClients;

import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.INITIALIZE_ROOM.InitializeRoomPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_ROOM.UpdateRoomPacket;

import java.rmi.Remote;

public interface VirtualRoomClient extends Remote {
    void initializeClientRoom ( InitializeRoomPacket initializeRoomPacket ) throws Exception;

    void updateClientRoom ( UpdateRoomPacket updateRoomPacket ) throws Exception;
}
