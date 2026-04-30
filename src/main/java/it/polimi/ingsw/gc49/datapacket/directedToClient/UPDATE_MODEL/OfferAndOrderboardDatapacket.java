package it.polimi.ingsw.gc49.datapacket.directedToClient.UPDATE_MODEL;

import it.polimi.ingsw.gc49.View.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.View.mockupModel.MockupPlayer;

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
