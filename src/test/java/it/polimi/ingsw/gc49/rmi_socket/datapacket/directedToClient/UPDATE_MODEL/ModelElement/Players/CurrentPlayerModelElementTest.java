package it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.Players;

import it.polimi.ingsw.gc49.client.view.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.TestMockupFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CurrentPlayerModelElementTest {

    @Test
    @DisplayName("updateMockupModel switches the currentPlayerIndex and toggles ofTurn on the two players")
    void updateSwitchesCurrentPlayer() {
        MockupGame game = TestMockupFactory.twoPlayerGame();
        // default current is 0 (Peppe)

        new CurrentPlayerModelElement("Wu's turn", 1).updateMockupModel(game);

        assertEquals(1, game.getCurrentPlayerIndex());
        assertFalse(game.getPlayer(0).isOfTurn());
        assertTrue(game.getPlayer(1).isOfTurn());
    }

    @Test
    @DisplayName("updateMockupModel back to player 0 toggles ofTurn the other way")
    void updateSwitchesBack() {
        MockupGame game = TestMockupFactory.twoPlayerGame();
        new CurrentPlayerModelElement("Wu's turn", 1).updateMockupModel(game);

        new CurrentPlayerModelElement("Peppe's turn", 0).updateMockupModel(game);

        assertEquals(0, game.getCurrentPlayerIndex());
        assertTrue(game.getPlayer(0).isOfTurn());
        assertFalse(game.getPlayer(1).isOfTurn());
    }
}
