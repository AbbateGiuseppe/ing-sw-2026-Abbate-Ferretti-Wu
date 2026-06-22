package it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.Players;

import it.polimi.ingsw.gc49.client.view.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.client.view.mockupModel.MockupPlayer;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.TestMockupFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayersModelElementTest {

    @Test
    @DisplayName("updateMockupModel replaces the players list")
    void updateReplacesPlayers() {
        MockupGame game = TestMockupFactory.twoPlayerGame();
        MockupPlayer massi = new MockupPlayer("Massi", 0, 1, 1, null);

        new PlayersModelElement("snapshot", List.of(massi)).updateMockupModel(game);

        assertEquals(1, game.getPlayers().size());
        assertEquals("Massi", game.getPlayer(0).getNickname());
    }
}
