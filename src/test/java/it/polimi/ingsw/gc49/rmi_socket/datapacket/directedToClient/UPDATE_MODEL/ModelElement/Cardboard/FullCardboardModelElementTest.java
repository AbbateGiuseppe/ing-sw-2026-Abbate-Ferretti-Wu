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

class FullCardboardModelElementTest {

    @Test
    @DisplayName("updateMockupModel replaces every cardboard row and the deckTopEra; discards are appended")
    void updateReplacesEverything() {
        MockupGame game = TestMockupFactory.twoPlayerGame();
        // pre-existing state: nothing on the board, no discards
        assertEquals(0, game.getDiscards().size());

        List<Card> discards = List.of(new Hunter(false, Era.FIRST, 2, null));
        List<Card> upperLine = List.of(new Hunter(true, Era.FIRST, 2, null), new Hunter(false, Era.FIRST, 2, null));
        List<Card> lowerLine = List.of(new Hunter(false, Era.FIRST, 2, null));
        List<Card> upperBuilding = List.of();
        List<Card> lowerBuilding = List.of();

        FullCardboardModelElement el = new FullCardboardModelElement(
                "full", discards, Era.SECOND, upperLine, lowerLine, upperBuilding, lowerBuilding);
        el.updateMockupModel(game);

        assertEquals(Era.SECOND, game.getDeckTopEra());
        assertEquals(2, game.getUpperLine().size());
        assertEquals(1, game.getLowerLine().size());
        assertEquals(0, game.getUpperBuilding().size());
        assertEquals(0, game.getLowerBuilding().size());
        assertEquals(1, game.getDiscards().size());
    }

    @Test
    @DisplayName("updateMockupModel appends to existing discards instead of replacing them")
    void discardsAreAppended() {
        MockupGame game = TestMockupFactory.twoPlayerGame();
        game.addDiscards(List.of(new Hunter(false, Era.FIRST, 2, null)));
        // initial discards size = 1

        FullCardboardModelElement el = new FullCardboardModelElement(
                "full",
                List.of(new Hunter(false, Era.FIRST, 2, null), new Hunter(true, Era.FIRST, 2, null)),
                Era.FIRST, List.of(), List.of(), List.of(), List.of());
        el.updateMockupModel(game);

        assertEquals(3, game.getDiscards().size(), "1 pre-existing + 2 new = 3");
    }
}
