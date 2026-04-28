package it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualClients;

import it.polimi.ingsw.gc49.datapacket.directedToClient.HALL_phase.HALL_CLIENT.INITIALIZE.HallClientInitializePacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.HALL_phase.HALL_CLIENT.UPDATE.HallClientUpdatePacket;

public interface VirtualHallClient {
    void initializeClientHall ( HallClientInitializePacket hallClientInitializePacket ) throws Exception;

    void updateClientHall ( HallClientUpdatePacket hallClientUpdatePacket ) throws Exception;
}
