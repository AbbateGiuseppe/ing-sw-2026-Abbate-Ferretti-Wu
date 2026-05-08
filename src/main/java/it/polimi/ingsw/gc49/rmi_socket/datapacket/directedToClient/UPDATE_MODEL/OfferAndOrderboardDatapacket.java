package it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL;

import it.polimi.ingsw.gc49.client.view.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.client.view.mockupModel.MockupPlayer;

import java.util.List;

public class OfferAndOrderboardDatapacket extends UpdateModelElement {
    private final List<MockupPlayer> offerBoard;
    private final List<MockupPlayer> orderBoard;

    public OfferAndOrderboardDatapacket ( List<MockupPlayer> offerBoard, List<MockupPlayer> orderBoard) {
        super();
        this.offerBoard = offerBoard;
        this.orderBoard = orderBoard;
    }

    @Override
    public void updateMockupModel ( MockupGame game ) {
        game.setOfferBoard(offerBoard);
        game.setOrderBoard(orderBoard);
    }
}
