package it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement;

import it.polimi.ingsw.gc49.client.view.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.client.view.mockupModel.MockupOffer;
import it.polimi.ingsw.gc49.client.view.mockupModel.MockupPlayer;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.UpdateModelElement;

import java.util.List;

public class OfferboardModelElement extends UpdateModelElement {
    private final List<MockupOffer> offerBoard;

    public OfferboardModelElement ( String actionInfo, List<MockupOffer> offerBoard) {
        super(actionInfo);
        this.offerBoard = offerBoard;
    }

    @Override
    public void updateMockupModel ( MockupGame game ) {
        game.getOfferBoard().clear();
        game.getOfferBoard().addAll( offerBoard );
    }
}
