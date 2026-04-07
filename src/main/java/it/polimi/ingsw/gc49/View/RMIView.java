package it.polimi.ingsw.gc49.View;

import it.polimi.ingsw.gc49.ActionData;
import it.polimi.ingsw.gc49.model.ModelData;

import java.rmi.Remote;

public class RMIView extends AbsView implements Remote {
    @Override
    public void update(ModelData data) {

    }


    @Override
    protected ActionData generateAction() {
        return null;
    }
}
