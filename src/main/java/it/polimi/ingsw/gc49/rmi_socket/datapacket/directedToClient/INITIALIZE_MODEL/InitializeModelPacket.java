package it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.INITIALIZE_MODEL;

import it.polimi.ingsw.gc49.client.view.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.Datapacket;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;

public class InitializeModelPacket extends Datapacket {
    public final MockupGame mockupModel;

    public InitializeModelPacket ( MockupGame mockupModel ) {
        super(DatapacketType.INITIALIZE_MODEL, ApplicationPhase.GAME);
        this.mockupModel = mockupModel;
    }
}
