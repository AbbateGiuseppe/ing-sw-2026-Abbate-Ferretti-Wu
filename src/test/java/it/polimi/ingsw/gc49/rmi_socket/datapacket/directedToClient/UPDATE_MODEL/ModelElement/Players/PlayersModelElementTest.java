package it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.Players;

import it.polimi.ingsw.gc49.client.view.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.client.view.mockupModel.MockupPlayer;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.TestMockupFactory;
import it.polimi.ingsw.gc49.server.model.Totem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayersModelElementTest {

    @Test
    @DisplayName("updateMockupModel replaces the players list")
    void updateReplacesPlayers() {
        MockupGame game = TestMockupFactory.twoPlayerGame();
        MockupPlayer peppe = new MockupPlayer("Peppe", 0, true, Totem.BLUE, 0, 0, new ArrayList<>(), new ArrayList<>(), 0, 0);

        new PlayersModelElement("snapshot", List.of(peppe)).updateMockupModel(game);

        assertEquals(1, game.getPlayers().size());
        assertEquals("Peppe", game.getPlayer(0).getNickname());
    }
}
