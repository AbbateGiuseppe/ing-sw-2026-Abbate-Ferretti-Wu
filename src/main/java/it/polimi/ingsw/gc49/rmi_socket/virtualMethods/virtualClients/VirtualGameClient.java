package it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualClients;

import it.polimi.ingsw.gc49.datapacket.directedToClient.ANY_phase.ERROR.ErrorPacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.GAME_phase.INITIALIZE_MODEL.InitializeModelPacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.GAME_phase.UPDATE_MODEL.UpdateModelPacket;

public interface VirtualGameClient {
    void initializeClientModel ( InitializeModelPacket initializeModelPacket ) throws Exception;

    void updateClientModel ( UpdateModelPacket updateModelPacket ) throws Exception;

    void reportError ( ErrorPacket errorPacket ) throws Exception;
}
