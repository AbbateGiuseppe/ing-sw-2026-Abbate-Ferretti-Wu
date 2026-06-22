package it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.OfferOrderboard;

import it.polimi.ingsw.gc49.client.view.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.client.view.mockupModel.MockupOffer;
import it.polimi.ingsw.gc49.client.view.mockupModel.MockupOrder;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.TestMockupFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OfferOrderboardModelElementTest {

    @Test
    @DisplayName("updateMockupModel propagates the assigned player indices to every offer and order slot")
    void updatePropagatesIndices() {
        MockupGame game = TestMockupFactory.twoPlayerGame();

        // craft a snapshot with player indices set
        MockupOffer offer0 = new MockupOffer(0, 1, 0, 1); // assigned to Wu
        MockupOrder order0 = new MockupOrder(2, false, 0, 0, 0); // assigned to Peppe

        new OfferOrderboardModelElement("info", List.of(offer0), List.of(order0))
                .updateMockupModel(game);

        assertEquals(1, game.getOfferBoard().get(0).getAssignedPlayerIndex());
        assertEquals(0, game.getOrderBoard().get(0).getAssignedPlayerIndex());
    }

    @Test
    @DisplayName("updateMockupModel clears assigned indices when the snapshot has null")
    void updateClearsWhenNull() {
        MockupGame game = TestMockupFactory.twoPlayerGame();
        // pre-assign
        game.setOfferPlayerIndex(0, 1);
        game.setOrderPlayerIndex(0, 0);

        new OfferOrderboardModelElement("clear",
                List.of(new MockupOffer(0, 1, 0, null)),
                List.of(new MockupOrder(2, false, 0, 0, null)))
                .updateMockupModel(game);

        assertNull(game.getOfferBoard().get(0).getAssignedPlayerIndex());
        assertNull(game.getOrderBoard().get(0).getAssignedPlayerIndex());
    }
}
