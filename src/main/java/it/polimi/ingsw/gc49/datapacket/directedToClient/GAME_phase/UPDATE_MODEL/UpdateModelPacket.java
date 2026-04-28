package it.polimi.ingsw.gc49.datapacket.directedToClient.GAME_phase.UPDATE_MODEL;

import it.polimi.ingsw.gc49.datapacket.Datapacket;
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

    public List<UpdateModelElement> getUpdatesList() {
        return updatesList;
    }
}
