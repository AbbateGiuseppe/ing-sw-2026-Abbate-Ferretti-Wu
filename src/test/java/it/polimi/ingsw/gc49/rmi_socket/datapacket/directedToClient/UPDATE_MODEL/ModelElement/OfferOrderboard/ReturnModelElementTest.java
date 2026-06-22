package it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.OfferOrderboard;

import it.polimi.ingsw.gc49.client.view.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.client.view.mockupModel.MockupOffer;
import it.polimi.ingsw.gc49.client.view.mockupModel.MockupOrder;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.TestMockupFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReturnModelElementTest {

    @Test
    @DisplayName("updateMockupModel updates offers, orders, food and points of the returning player")
    void updateUpdatesEverything() {
        MockupGame game = TestMockupFactory.twoPlayerGame();
        // pre-assign Peppe to offer 0
        game.setOfferPlayerIndex(0, 0);

        new ReturnModelElement(
                "Peppe returned",
                List.of(new MockupOffer(0, 1, 0, null)),    // offer cleared
                List.of(new MockupOrder(2, false, 0, 0, 0)), // peppe on order 0
                0, 5, 7)
                .updateMockupModel(game);

        assertNull(game.getOfferBoard().get(0).getAssignedPlayerIndex());
        assertEquals(0, game.getOrderBoard().get(0).getAssignedPlayerIndex());
        assertEquals(5, game.getPlayer(0).getFood());
        assertEquals(7, game.getPlayer(0).getPoints());
    }
}
