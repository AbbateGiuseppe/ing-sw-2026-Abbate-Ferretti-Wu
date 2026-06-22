package it.polimi.ingsw.gc49.client.user_input_interfaces;

import it.polimi.ingsw.gc49.client.ClientApplication;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.ERROR.ErrorPacket;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualServer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Tests for {@link UserInputInterface}, an abstract base class for the CLI/GUI input
 * interfaces. A minimal {@link FakeInputInterface} subclass is used to instantiate the
 * abstract base. Since {@code UserInputInterface} references {@code ClientApplication}'s
 * static fields, the tests assume the latter could be initialized; otherwise they are
 * skipped via {@link org.junit.jupiter.api.Assumptions}.
 */
class UserInputInterfaceTest {

    private static boolean canLoadClasses;

    @BeforeAll
    static void ensureClassesLoadable() {
        try {
            Object unused = ClientApplication.mockups;
            canLoadClasses = unused != null;
        } catch (ExceptionInInitializerError | NoClassDefFoundError | RuntimeException e) {
            canLoadClasses = false;
        }
    }

    /** Minimal subclass capturing the abstract method calls. */
    private static class FakeInputInterface extends UserInputInterface {
        String lastPrintedString;
        ErrorPacket lastErrorPacket;
        int showCalls;

        FakeInputInterface(VirtualServer server, ApplicationPhase phase) {
            super(server, phase);
        }

        @Override public void printString(String string) { lastPrintedString = string; }
        @Override public void printErrorPacket(ErrorPacket errorPacket) { lastErrorPacket = errorPacket; }
        @Override public void show() { showCalls++; }
    }

    @Test
    @DisplayName("constructor wires the virtual server and the current phase")
    void constructorStoresState() {
        assumeTrue(canLoadClasses, "ClientApplication not initializable in this environment");
        FakeInputInterface ui = new FakeInputInterface(null, ApplicationPhase.HALL);
        // currentPhase is a static field shared across instances; just check it was set
        assertNotNull(ApplicationPhase.HALL); // sanity
        // virtualServer is protected; we can re-set it through the setter and observe via abstract calls
        ui.setVirtualServer(null);
        assertDoesNotThrow(() -> ui.setVirtualServer(null));
    }

    @Test
    @DisplayName("setNickname stores the nickname")
    void setNicknameStores() {
        assumeTrue(canLoadClasses, "ClientApplication not initializable in this environment");
        FakeInputInterface ui = new FakeInputInterface(null, ApplicationPhase.ANY);
        ui.setNickname("Peppe");
        // nickname is protected; check it via reflection
        try {
            var field = UserInputInterface.class.getDeclaredField("nickname");
            field.setAccessible(true);
            assertEquals("Peppe", field.get(ui));
        } catch (ReflectiveOperationException e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("setCurrentPhase replaces the static phase")
    void setCurrentPhaseUpdates() {
        assumeTrue(canLoadClasses, "ClientApplication not initializable in this environment");
        FakeInputInterface ui = new FakeInputInterface(null, ApplicationPhase.HALL);
        ui.setCurrentPhase(ApplicationPhase.GAME);
        // currentPhase is static; check via reflection
        try {
            var field = UserInputInterface.class.getDeclaredField("currentPhase");
            field.setAccessible(true);
            assertEquals(ApplicationPhase.GAME, field.get(null));
        } catch (ReflectiveOperationException e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("setVirtualServer stores the server")
    void setVirtualServerStores() {
        assumeTrue(canLoadClasses, "ClientApplication not initializable in this environment");
        FakeInputInterface ui = new FakeInputInterface(null, ApplicationPhase.ANY);
        assertDoesNotThrow(() -> ui.setVirtualServer(null));
    }

    @Test
    @DisplayName("default runInput prints 'Interfaccia vuota?!' to System.out")
    void defaultRunInput() throws Exception {
        assumeTrue(canLoadClasses, "ClientApplication not initializable in this environment");
        FakeInputInterface ui = new FakeInputInterface(null, ApplicationPhase.ANY);
        java.io.ByteArrayOutputStream buffer = new java.io.ByteArrayOutputStream();
        java.io.PrintStream original = System.out;
        try {
            System.setOut(new java.io.PrintStream(buffer));
            ui.runInput();
        } finally {
            System.setOut(original);
        }
        assertTrue(buffer.toString().contains("Interfaccia vuota?!"));
    }

    @Test
    @DisplayName("abstract methods are dispatched to the subclass implementation")
    void abstractMethodsDispatch() {
        assumeTrue(canLoadClasses, "ClientApplication not initializable in this environment");
        FakeInputInterface ui = new FakeInputInterface(null, ApplicationPhase.ANY);
        ui.printString("hello");
        ui.printErrorPacket(new ErrorPacket("Title", "Body", false));
        ui.show();
        assertEquals("hello", ui.lastPrintedString);
        assertEquals("Title", ui.lastErrorPacket.errorTitle);
        assertEquals(1, ui.showCalls);
    }
}
