package it.polimi.ingsw.gc49.datapacket.directedToClient.GAME_phase.INITIALIZE_MODEL;

import it.polimi.ingsw.gc49.View.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.datapacket.Datapacket;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;

public class InitializeModelPacket extends Datapacket {
    public final MockupGame mockupModel;

    public InitializeModelPacket ( MockupGame mockupModel ) {
        super(DatapacketType.INITIALIZE_MODEL, ApplicationPhase.GAME);
        this.mockupModel = mockupModel;
    }
}
