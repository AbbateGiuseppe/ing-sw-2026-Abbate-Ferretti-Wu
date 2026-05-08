package it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualClients;

import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.ERROR.ErrorPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.INITIALIZE_MODEL.InitializeModelPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.UpdateModelPacket;

import java.rmi.Remote;

public interface VirtualGameClient extends Remote {
    void initializeClientModel ( InitializeModelPacket initializeModelPacket ) throws Exception;

    void updateClientModel ( UpdateModelPacket updateModelPacket ) throws Exception;

    void reportError ( ErrorPacket errorPacket ) throws Exception;
}
