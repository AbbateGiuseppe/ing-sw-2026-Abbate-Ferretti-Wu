package it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL;

import it.polimi.ingsw.gc49.client.view.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.Datapacket;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;

import java.util.ArrayList;
import java.util.List;

public class UpdateModelPacket extends Datapacket {
    private final List<UpdateModelElement> updatesList = new ArrayList<>();

    public UpdateModelPacket () {
        super(DatapacketType.UPDATE_MODEL, ApplicationPhase.GAME);
    }

    public void addUpdateElement ( UpdateModelElement element ) {
        updatesList.add(element);
    }

    public void updateTheMockupModel( MockupGame mockupGame ) {
        for (UpdateModelElement element : updatesList) {
            element.updateMockupModel(mockupGame);
        }
    }
}
