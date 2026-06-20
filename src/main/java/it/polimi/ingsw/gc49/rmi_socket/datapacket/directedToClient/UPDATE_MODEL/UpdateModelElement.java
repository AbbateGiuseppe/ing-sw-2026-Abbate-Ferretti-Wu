package it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL;

import it.polimi.ingsw.gc49.client.view.mockupModel.MockupGame;

import java.io.Serializable;

public abstract class UpdateModelElement implements Serializable {
    protected final String actionInfo;

    public UpdateModelElement ( String actionInfo ) {
        this.actionInfo = actionInfo;
    }

    public String getActionInfo () {
        return actionInfo;
    }

    public abstract void updateMockupModel ( MockupGame game );
}
