package it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualClients;

import it.polimi.ingsw.gc49.datapacket.directedToClient.INITIALIZE_HALL.InitializeHallPacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.UPDATE_HALL.UpdateHallPacket;

public interface VirtualHallClient {
    void initializeClientHall ( InitializeHallPacket initializeHallPacket ) throws Exception;

    void updateClientHall ( UpdateHallPacket updateHallPacket ) throws Exception;
}
