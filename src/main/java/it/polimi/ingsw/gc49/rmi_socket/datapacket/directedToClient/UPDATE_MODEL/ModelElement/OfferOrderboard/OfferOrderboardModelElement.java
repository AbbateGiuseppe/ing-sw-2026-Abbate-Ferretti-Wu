package it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.OfferOrderboard;

import it.polimi.ingsw.gc49.client.view.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.client.view.mockupModel.MockupOffer;
import it.polimi.ingsw.gc49.client.view.mockupModel.MockupOrder;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.UpdateModelElement;

import java.util.List;

public class OfferOrderboardModelElement extends UpdateModelElement {
    private final List<MockupOffer> offerBoard;
    private final List<MockupOrder> orderBoard;

    public OfferOrderboardModelElement ( String actionInfo, List<MockupOffer> offerBoard, List<MockupOrder> orderBoard) {
        super(actionInfo);
        this.offerBoard = offerBoard;
        this.orderBoard = orderBoard;
    }

    @Override
    public void updateMockupModel ( MockupGame game ) {
        for(int i = 0; i < offerBoard.size(); i++) {
            game.setOfferPlayerIndex(i, offerBoard.get(i).getAssignedPlayerIndex());
        }
        for(int i = 0; i < orderBoard.size(); i++) {
            game.setOrderPlayerIndex(i, orderBoard.get(i).getAssignedPlayerIndex());
        }
    }
}
