package it.polimi.ingsw.gc49.View;

import it.polimi.ingsw.gc49.ActionData;
import it.polimi.ingsw.gc49.controller.ViewListener;

public abstract class AbsView implements ModelListener{
    private String nickname;
    private ViewListener listener;
    protected abstract ActionData generateAction();

    /**
     * To be called after the View receives what players have done on client side
     */
    public void invokeListener(){
        ActionData currentAction = generateAction();
        listener.playerAction(currentAction);
    }

}
