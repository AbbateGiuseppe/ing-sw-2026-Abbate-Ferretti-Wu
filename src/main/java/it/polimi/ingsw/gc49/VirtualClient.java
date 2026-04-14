package it.polimi.ingsw.gc49;

import it.polimi.ingsw.gc49.View.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.datapacket.UPDATE_MODEL.MockupModelDatapacketable;

import java.util.List;

public interface VirtualClient {
    void initializeClientModel ( MockupGame mockupGame ) throws Exception;

    void updateClientModel ( List<MockupModelDatapacketable> updatesList ) throws Exception;

    void reportError ( String details ) throws Exception;
}
