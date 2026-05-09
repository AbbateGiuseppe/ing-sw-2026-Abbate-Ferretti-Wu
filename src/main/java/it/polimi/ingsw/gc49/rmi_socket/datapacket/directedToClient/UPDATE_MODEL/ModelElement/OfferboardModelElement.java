package it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement;

import it.polimi.ingsw.gc49.client.view.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.client.view.mockupModel.MockupPlayer;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.UpdateModelElement;

import java.util.List;

public class OfferboardModelElement extends UpdateModelElement {
    private final List<MockupPlayer> offerBoard;

    public OfferboardModelElement ( String actionInfo, List<MockupPlayer> offerBoard) {
        super(actionInfo);
        this.offerBoard = offerBoard;
    }

    @Override
    public void updateMockupModel ( MockupGame game ) {
        game.setOfferBoard(offerBoard);
    }
}
