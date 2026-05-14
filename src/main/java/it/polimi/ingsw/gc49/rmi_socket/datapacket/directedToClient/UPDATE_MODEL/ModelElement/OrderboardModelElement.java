package it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement;

import it.polimi.ingsw.gc49.client.view.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.client.view.mockupModel.MockupOrder;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.UpdateModelElement;

import java.util.List;

public class OrderboardModelElement extends UpdateModelElement {
    private final List<MockupOrder> orderBoard;

    public OrderboardModelElement ( String actionInfo, List<MockupOrder> orderBoard) {
        super(actionInfo);
        this.orderBoard = orderBoard;
    }

    @Override
    public void updateMockupModel ( MockupGame game ) {
        game.getOrderBoard().clear();
        game.getOrderBoard().addAll( orderBoard );
    }
}
