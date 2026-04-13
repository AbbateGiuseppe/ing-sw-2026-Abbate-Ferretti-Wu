package it.polimi.ingsw.gc49.datapacket.UPDATE_MODEL;

import it.polimi.ingsw.gc49.View.mockupModel.MockupGame;

import java.io.Serializable;

public interface MockupModelDatapacketable extends Serializable {
    void updateMockupModel ( MockupGame game );
}
