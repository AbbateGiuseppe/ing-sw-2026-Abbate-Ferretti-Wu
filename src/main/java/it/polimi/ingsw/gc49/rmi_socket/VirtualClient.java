package it.polimi.ingsw.gc49.rmi_socket;

import it.polimi.ingsw.gc49.View.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.datapacket.UPDATE_MODEL.UpdateModel;

public interface VirtualClient {
    void initializeClientModel ( MockupGame mockupGame ) throws Exception;

    void updateClientModel ( UpdateModel updateModel ) throws Exception;

    void reportError ( String details ) throws Exception;
}
