package it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.Players;

import it.polimi.ingsw.gc49.client.view.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.UpdateModelElement;
import it.polimi.ingsw.gc49.server.model.Player;

import java.util.ArrayList;
import java.util.List;

public class FoodAndPointsAllModelElement extends UpdateModelElement {
    private final List<Integer> newFood;
    private final List<Integer> newPoints;

    public FoodAndPointsAllModelElement ( String actionInfo, List<Integer> newFood, List<Integer> newPoints ) {
        super(actionInfo);
        this.newFood = newFood;
        this.newPoints = newPoints;
    }

    public List<Integer> getNewFood () {
        return newFood;
    }

    public List<Integer> getNewPoints () {
        return newPoints;
    }

    @Override
    public void updateMockupModel ( MockupGame game ) {
        for(int i = 0; i < newFood.size(); i++) {
            game.getPlayer(i).setFood(newFood.get(i));
            game.getPlayer(i).setPoints(newPoints.get(i));
        }
    }

    public static List<Integer> getNewFood( List<Player> players ){
        List<Integer> newFood = new ArrayList<>();
        for(Player player : players){
            newFood.add(player.getPlayerIndex(), player.getFood());
        }

        return newFood;
    }

    public static List<Integer> getNewPoints( List<Player> players ){
        List<Integer> newPoints = new ArrayList<>();
        for(Player player : players){
            newPoints.add(player.getPlayerIndex(), player.getPoints());
        }

        return newPoints;
    }
}
