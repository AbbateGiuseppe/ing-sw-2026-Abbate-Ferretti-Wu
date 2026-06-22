package it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.Players;

import it.polimi.ingsw.gc49.client.view.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.TestMockupFactory;
import it.polimi.ingsw.gc49.server.model.Totem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TotemModelElementTest {

    @Test
    @DisplayName("updateMockupModel sets the chosen totem on the addressed player only")
    void updateSetsTotem() {
        MockupGame game = TestMockupFactory.twoPlayerGame();
        assertNull(game.getPlayer(0).getTotem());
        assertNull(game.getPlayer(1).getTotem());

        new TotemModelElement("Peppe picked BLUE", 0, Totem.BLUE).updateMockupModel(game);

        assertEquals(Totem.BLUE, game.getPlayer(0).getTotem());
        assertNull(game.getPlayer(1).getTotem(), "Wu must stay untouched");
    }

    @Test
    @DisplayName("updateMockupModel overwrites a previously chosen totem")
    void updateOverwritesTotem() {
        MockupGame game = TestMockupFactory.twoPlayerGame();
        game.getPlayer(0).setTotem(Totem.BLUE);

        new TotemModelElement("Peppe changed", 0, Totem.YELLOW).updateMockupModel(game);

        assertEquals(Totem.YELLOW, game.getPlayer(0).getTotem());
    }
}
