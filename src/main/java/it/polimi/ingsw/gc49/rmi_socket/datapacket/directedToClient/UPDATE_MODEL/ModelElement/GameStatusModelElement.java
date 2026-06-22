package it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement;

import it.polimi.ingsw.gc49.client.view.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.UpdateModelElement;
import it.polimi.ingsw.gc49.server.model.States.State;

public class GameStatusModelElement extends UpdateModelElement {
    private final State.States gameState;

    public GameStatusModelElement(String actionInfo, State.States gameState) {
        super(actionInfo);
        this.gameState = gameState;
    }

    @Override
    public void updateMockupModel(MockupGame game) {
        game.setGameState(gameState);
    }
}
