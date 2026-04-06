package it.polimi.ingsw.gc49.View;

import it.polimi.ingsw.gc49.model.ModelData;

import java.util.EventListener;

public interface ModelListener extends EventListener {
    public void update(ModelData data);
}
