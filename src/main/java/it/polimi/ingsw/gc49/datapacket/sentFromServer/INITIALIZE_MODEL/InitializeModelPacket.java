package it.polimi.ingsw.gc49.datapacket.sentFromServer.INITIALIZE_MODEL;

import it.polimi.ingsw.gc49.View.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.datapacket.Datapacket;

public class InitializeModelPacket extends Datapacket {
    public final MockupGame mockupModel;

    public InitializeModelPacket ( MockupGame mockupModel ) {
        super(DatapacketType.INITIALIZE_MODEL);
        this.mockupModel = mockupModel;
    }
}
