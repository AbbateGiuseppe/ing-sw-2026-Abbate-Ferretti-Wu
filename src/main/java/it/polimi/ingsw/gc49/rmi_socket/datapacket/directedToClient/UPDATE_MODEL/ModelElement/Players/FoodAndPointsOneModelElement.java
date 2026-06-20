package it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.Players;

import it.polimi.ingsw.gc49.client.view.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.UpdateModelElement;

public class FoodAndPointsOneModelElement extends UpdateModelElement {
    private final int playerIndex;
    private final int newFood;
    private final int newPoints;

    public FoodAndPointsOneModelElement ( String actionInfo, int playerIndex, int newFood, int newPoints ) {
        super(actionInfo);
        this.playerIndex = playerIndex;
        this.newFood = newFood;
        this.newPoints = newPoints;
    }

    public int getPlayerIndex () {
        return playerIndex;
    }

    public int getNewFood () {
        return newFood;
    }

    public int getNewPoints () {
        return newPoints;
    }

    @Override
    public void updateMockupModel ( MockupGame game ) {
        game.getPlayer(playerIndex).setFood(newFood);
        game.getPlayer(playerIndex).setPoints(newPoints);
    }
}
