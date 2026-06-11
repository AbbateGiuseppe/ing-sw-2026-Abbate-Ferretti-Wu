package it.polimi.ingsw.gc49.server.model;

import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.UpdateModelElement;

public interface QueueUpdatable {
    void queueUpdateModelElement ( UpdateModelElement updateModelElement );
}
