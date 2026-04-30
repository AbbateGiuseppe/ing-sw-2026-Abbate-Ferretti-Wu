package it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualClients;

import it.polimi.ingsw.gc49.datapacket.directedToClient.ERROR.ErrorPacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.INITIALIZE_MODEL.InitializeModelPacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.UPDATE_MODEL.UpdateModelPacket;

public interface VirtualGameClient {
    void initializeClientModel ( InitializeModelPacket initializeModelPacket ) throws Exception;

    void updateClientModel ( UpdateModelPacket updateModelPacket ) throws Exception;

    void reportError ( ErrorPacket errorPacket ) throws Exception;
}
