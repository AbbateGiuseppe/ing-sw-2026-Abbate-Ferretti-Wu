package it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL;

import it.polimi.ingsw.gc49.client.view.mockupModel.MockupGame;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class UpdateModelElementTest {

    /** Minimal subclass for instantiation. */
    private static class TestElement extends UpdateModelElement {
        boolean updateCalled = false;

        TestElement(String actionInfo) {
            super(actionInfo);
        }

        @Override
        public void updateMockupModel(MockupGame game) {
            updateCalled = true;
        }
    }

    @Test
    @DisplayName("constructor stores the actionInfo")
    void constructorStoresActionInfo() throws ReflectiveOperationException {
        TestElement element = new TestElement("did something");
        Field f = UpdateModelElement.class.getDeclaredField("actionInfo");
        f.setAccessible(true);
        assertEquals("did something", f.get(element));
    }

    @Test
    @DisplayName("UpdateModelElement is Serializable")
    void isSerializable() {
        assertInstanceOf(java.io.Serializable.class, new TestElement("x"));
    }

    @Test
    @DisplayName("updateMockupModel is dispatched to the subclass")
    void updateMockupModelDispatches() {
        TestElement element = new TestElement("x");
        element.updateMockupModel(TestMockupFactory.twoPlayerGame());
        assertTrue(element.updateCalled);
    }

    @Test
    @DisplayName("null actionInfo is accepted")
    void nullActionInfoAccepted() {
        assertDoesNotThrow(() -> new TestElement(null));
    }
}
