package it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement;

import it.polimi.ingsw.gc49.client.view.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.client.view.mockupModel.MockupPlayer;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.UpdateModelElement;

import java.util.List;

public class OrderboardModelElement extends UpdateModelElement {
    private final List<MockupPlayer> orderBoard;

    public OrderboardModelElement ( String actionInfo, List<MockupPlayer> orderBoard) {
        super(actionInfo);
        this.orderBoard = orderBoard;
    }

    @Override
    public void updateMockupModel ( MockupGame game ) {
        game.setOrderBoard(orderBoard);
    }
}
