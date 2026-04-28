package it.polimi.ingsw.gc49.rmi_socket.virtualClients;

import it.polimi.ingsw.gc49.datapacket.sentFromServer.ROOM_RETURN.ClientRoomInitializePacket;
import it.polimi.ingsw.gc49.datapacket.sentFromServer.ROOM_RETURN.ClientRoomUpdatePacket;

public interface VirtualRoomClient {
    void initializeClientRoom ( ClientRoomInitializePacket clientRoomInitializePacket ) throws Exception;

    void updateClientRoom ( ClientRoomUpdatePacket clientRoomUpdatePacket ) throws Exception;
}
