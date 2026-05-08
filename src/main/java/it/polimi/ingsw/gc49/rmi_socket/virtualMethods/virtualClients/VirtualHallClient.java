package it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualClients;

import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.INITIALIZE_HALL.InitializeHallPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_HALL.UpdateHallPacket;

import java.rmi.Remote;

public interface VirtualHallClient extends Remote {
    void initializeClientHall ( InitializeHallPacket initializeHallPacket ) throws Exception;

    void updateClientHall ( UpdateHallPacket updateHallPacket ) throws Exception;
}
