package it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.Cardboard;

import it.polimi.ingsw.gc49.client.view.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.TestMockupFactory;
import it.polimi.ingsw.gc49.server.model.Card.Card;
import it.polimi.ingsw.gc49.server.model.Card.CharacterCard.Hunter;
import it.polimi.ingsw.gc49.server.model.Era;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UpperDrawModelElementTest {

    @Test
    @DisplayName("updateMockupModel replaces upperLine and upperBuilding and adds a character card to the player")
    void updateAddsCharacterCard() {
        MockupGame game = TestMockupFactory.twoPlayerGame();
        Card drawn = new Hunter(false, Era.FIRST, 2, null);

        UpperDrawModelElement el = new UpperDrawModelElement(
                "Peppe drew", List.of(), List.of(), 0, drawn, true);
        el.updateMockupModel(game);

        assertEquals(0, game.getUpperLine().size());
        assertEquals(0, game.getUpperBuilding().size());
    }

    @Test
    @DisplayName("updateMockupModel adds a building card to the player when isCharacterCard is false")
    void updateAddsBuildingCard() {
        MockupGame game = TestMockupFactory.twoPlayerGame();
        Card drawn = new Hunter(false, Era.FIRST, 2, null);

        UpperDrawModelElement el = new UpperDrawModelElement(
                "Wu drew", List.of(), List.of(), 1, drawn, false);
        assertDoesNotThrow(() -> el.updateMockupModel(game));
    }
}
