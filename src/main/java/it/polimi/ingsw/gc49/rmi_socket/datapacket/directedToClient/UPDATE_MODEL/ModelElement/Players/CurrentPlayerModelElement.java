package it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.Players;

import it.polimi.ingsw.gc49.client.view.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.UpdateModelElement;

public class CurrentPlayerModelElement extends UpdateModelElement {
    private final int currentPlayerIndex;

    public CurrentPlayerModelElement ( String actionInfo, int currentPlayerIndex ) {
        super(actionInfo);
        this.currentPlayerIndex = currentPlayerIndex;
    }

    @Override
    public void updateMockupModel ( MockupGame game ) {
        game.setCurrentPlayerIndex(currentPlayerIndex);
    }
}
