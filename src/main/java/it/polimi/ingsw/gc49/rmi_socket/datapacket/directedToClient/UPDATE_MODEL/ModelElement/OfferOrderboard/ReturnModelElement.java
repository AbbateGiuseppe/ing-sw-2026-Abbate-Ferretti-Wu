package it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.OfferOrderboard;

import it.polimi.ingsw.gc49.client.view.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.client.view.mockupModel.MockupOffer;
import it.polimi.ingsw.gc49.client.view.mockupModel.MockupOrder;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.UpdateModelElement;

import java.util.List;

/**Used to send updates when the player goes back from the offerboard to the orderboard, usually gaining or losing food/points.
 * Combining offerboard, orderboard, food and points updates.*/
public class ReturnModelElement extends UpdateModelElement {
    private final List<MockupOffer> offerBoard;
    private final List<MockupOrder> orderBoard;
    private final int playerIndex;
    private final int newFood;
    private final int newPoints;

    public ReturnModelElement ( String actionInfo, List<MockupOffer> offerBoard, List<MockupOrder> orderBoard,
                                int playerIndex, int newFood, int newPoints ) {
        super(actionInfo);
        this.offerBoard = offerBoard;
        this.orderBoard = orderBoard;
        this.playerIndex = playerIndex;
        this.newFood = newFood;
        this.newPoints = newPoints;
    }

    @Override
    public void updateMockupModel ( MockupGame game ) {
        for(int i = 0; i < offerBoard.size(); i++) {
            game.setOfferPlayerIndex(i, offerBoard.get(i).getAssignedPlayerIndex());
        }
        for(int i = 0; i < orderBoard.size(); i++) {
            game.setOrderPlayerIndex(i, orderBoard.get(i).getAssignedPlayerIndex());
        }
        game.getPlayer(playerIndex).setFood(newFood);
        game.getPlayer(playerIndex).setPoints(newPoints);
    }
}
