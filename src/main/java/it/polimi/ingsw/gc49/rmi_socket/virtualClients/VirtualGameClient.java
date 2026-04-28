package it.polimi.ingsw.gc49.rmi_socket.virtualClients;

import it.polimi.ingsw.gc49.datapacket.sentFromServer.ERROR.ErrorPacket;
import it.polimi.ingsw.gc49.datapacket.sentFromServer.INITIALIZE_MODEL.InitializeModelPacket;
import it.polimi.ingsw.gc49.datapacket.sentFromServer.UPDATE_MODEL.UpdateModelPacket;

public interface VirtualGameClient {
    void initializeClientModel ( InitializeModelPacket initializeModelPacket ) throws Exception;

    void updateClientModel ( UpdateModelPacket updateModelPacket ) throws Exception;

    void reportError ( ErrorPacket errorPacket ) throws Exception;
}
