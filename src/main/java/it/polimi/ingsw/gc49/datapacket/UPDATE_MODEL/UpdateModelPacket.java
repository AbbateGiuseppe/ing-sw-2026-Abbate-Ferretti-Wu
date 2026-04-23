package it.polimi.ingsw.gc49.datapacket.UPDATE_MODEL;

import it.polimi.ingsw.gc49.datapacket.Datapacket;

import java.util.ArrayList;
import java.util.List;

public class UpdateModelPacket extends Datapacket {
    private final List<UpdateModelElement> updatesList = new ArrayList<>();

    public UpdateModelPacket () {
        super(DatapacketType.UPDATE_MODEL);
    }

    public void addUpdateElement ( UpdateModelElement element ) {
        updatesList.add(element);
    }

    public List<UpdateModelElement> getUpdatesList() {
        return updatesList;
    }
}
