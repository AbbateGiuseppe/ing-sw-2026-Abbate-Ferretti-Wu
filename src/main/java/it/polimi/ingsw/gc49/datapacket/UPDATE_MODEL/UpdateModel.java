package it.polimi.ingsw.gc49.datapacket.UPDATE_MODEL;

import it.polimi.ingsw.gc49.datapacket.Datapacket;

import java.util.ArrayList;
import java.util.List;

public class UpdateModel extends Datapacket {
    private List<UpdateModelElement> updatesList = new ArrayList<>();

    public UpdateModel () {
        super(DatapacketType.UPDATE_MODEL);
    }

    public void addUpdateElement ( UpdateModelElement element ) {
        updatesList.add(element);
    }

    public List<UpdateModelElement> getUpdatesList() {
        return updatesList;
    }
}
