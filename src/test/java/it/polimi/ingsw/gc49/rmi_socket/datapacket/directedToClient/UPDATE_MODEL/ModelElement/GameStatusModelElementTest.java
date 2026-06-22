package it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement;

import it.polimi.ingsw.gc49.client.view.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.TestMockupFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameStatusModelElementTest {

    @Test
    @DisplayName("constructor accepts all fields")
    void constructionAcceptsFields() {
        GameStatusModelElement el = new GameStatusModelElement(
                "info", "GAME", "Peppe 30, Wu 25", 12, 5);
        assertNotNull(el);
    }

    @Test
    @DisplayName("updateMockupModel does not throw (the implementation is currently commented out)")
    void updateDoesNotThrow() {
        MockupGame game = TestMockupFactory.twoPlayerGame();
        GameStatusModelElement el = new GameStatusModelElement("info", "GAME", "...", 0, 0);
        assertDoesNotThrow(() -> el.updateMockupModel(game));
    }
}
