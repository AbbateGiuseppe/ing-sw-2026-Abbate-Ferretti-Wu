package it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.Players;

import it.polimi.ingsw.gc49.client.view.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.TestMockupFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FoodAndPointsOneModelElementTest {

    @Test
    @DisplayName("updateMockupModel sets food and points of the addressed player only")
    void updateOnlyOnePlayer() {
        MockupGame game = TestMockupFactory.twoPlayerGame();
        game.getPlayer(1).setFood(99);
        game.getPlayer(1).setPoints(99);

        new FoodAndPointsOneModelElement("Peppe gained", 0, 6, 4)
                .updateMockupModel(game);

        assertEquals(6, game.getPlayer(0).getFood());
        assertEquals(4, game.getPlayer(0).getPoints());
        // Wu untouched
        assertEquals(99, game.getPlayer(1).getFood());
        assertEquals(99, game.getPlayer(1).getPoints());
    }
}
