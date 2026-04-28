package it.polimi.ingsw.gc49.rmi_socket.virtualClients;

import it.polimi.ingsw.gc49.datapacket.sentFromServer.HALL_RETURN.ClientHallInitializePacket;
import it.polimi.ingsw.gc49.datapacket.sentFromServer.HALL_RETURN.ClientHallUpdatePacket;

public interface VirtualHallClient {
    void initializeClientHall ( ClientHallInitializePacket clientHallInitializePacket ) throws Exception;

    void updateClientHall ( ClientHallUpdatePacket clientHallUpdatePacket ) throws Exception;
}
