package it.polimi.ingsw.gc49.rmi_socket;

import it.polimi.ingsw.gc49.datapacket.ERROR.ErrorPacket;
import it.polimi.ingsw.gc49.datapacket.INITIALIZE_MODEL.InitializeModelPacket;
import it.polimi.ingsw.gc49.datapacket.UPDATE_MODEL.UpdateModelPacket;

import java.rmi.Remote;

public interface VirtualClient extends Remote {
    void initializeClientModel ( InitializeModelPacket initializeModelPacket ) throws Exception;

    void updateClientModel ( UpdateModelPacket updateModelPacket ) throws Exception;

    void reportError ( ErrorPacket errorPacket ) throws Exception;
}
