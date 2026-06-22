package it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.Players;

import it.polimi.ingsw.gc49.client.view.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.TestMockupFactory;
import it.polimi.ingsw.gc49.server.model.Player;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FoodAndPointsAllModelElementTest {

    @Test
    @DisplayName("updateMockupModel applies the food/points lists position-by-position")
    void updateAppliesAll() {
        MockupGame game = TestMockupFactory.twoPlayerGame();

        new FoodAndPointsAllModelElement(
                "snapshot", List.of(7, 9), List.of(3, 4))
                .updateMockupModel(game);

        assertEquals(7, game.getPlayer(0).getFood());
        assertEquals(3, game.getPlayer(0).getPoints());
        assertEquals(9, game.getPlayer(1).getFood());
        assertEquals(4, game.getPlayer(1).getPoints());
    }

    @Test
    @DisplayName("getNewFood collects the food values indexed by playerIndex")
    void getNewFoodCollects() {
        Player peppe = new Player("Peppe", 0);
        Player wu = new Player("Wu", 1);
        peppe.setFood(5);
        wu.setFood(8);

        List<Integer> result = FoodAndPointsAllModelElement.getNewFood(List.of(peppe, wu));

        assertEquals(2, result.size());
        assertEquals(5, result.get(0));
        assertEquals(8, result.get(1));
    }

    @Test
    @DisplayName("getNewPoints collects the points values indexed by playerIndex")
    void getNewPointsCollects() {
        Player peppe = new Player("Peppe", 0);
        Player wu = new Player("Wu", 1);
        peppe.setPoints(2);
        wu.setPoints(11);

        List<Integer> result = FoodAndPointsAllModelElement.getNewPoints(List.of(peppe, wu));

        assertEquals(2, result.get(0));
        assertEquals(11, result.get(1));
    }
}
