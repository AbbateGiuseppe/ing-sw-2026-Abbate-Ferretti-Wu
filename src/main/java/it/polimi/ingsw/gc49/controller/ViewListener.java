package it.polimi.ingsw.gc49.controller;

import it.polimi.ingsw.gc49.ActionData;

import java.util.EventListener;

public interface ViewListener extends EventListener {
    public void playerAction(ActionData data);
}
