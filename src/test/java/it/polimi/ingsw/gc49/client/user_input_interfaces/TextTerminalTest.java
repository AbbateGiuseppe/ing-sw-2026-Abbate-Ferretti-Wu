package it.polimi.ingsw.gc49.client.user_input_interfaces;

import it.polimi.ingsw.gc49.client.ClientApplication;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.ERROR.ErrorPacket;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Tests for {@link TextTerminal}.
 * <p>
 * {@code TextTerminal} requires a live JLine system terminal (initialized indirectly via
 * {@link ClientApplication}) to be usable. In a headless test environment most of its
 * behaviour cannot be exercised. Two strategies are used:
 * <ul>
 *   <li>Best-effort instantiation: if the JLine terminal cannot be created, all such tests
 *       are skipped via {@link org.junit.jupiter.api.Assumptions}.</li>
 *   <li>Structural reflection: the public API surface (constants and method signatures) is
 *       verified without initializing the class.</li>
 * </ul>
 */
class TextTerminalTest {

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

    // --- structural assertions (no class initialization) ---

    @Test
    @DisplayName("HELP_TIMEOUT and ERROR_TIMEOUT are public static final int constants equal to 10")
    void timeoutConstants() {
        // these are compile-time constants and do not require class initialization
        assertEquals(10, TextTerminal.HELP_TIMEOUT);
        assertEquals(10, TextTerminal.ERROR_TIMEOUT);
    }

    @Test
    @DisplayName("TextTerminal declares the expected public methods")
    void declaresExpectedMethods() throws NoSuchMethodException {
        Class<TextTerminal> klass = TextTerminal.class;

        Method printString = klass.getDeclaredMethod("printString", String.class);
        assertTrue(Modifier.isPublic(printString.getModifiers()));

        Method printErrorPacket = klass.getDeclaredMethod("printErrorPacket", ErrorPacket.class);
        assertTrue(Modifier.isPublic(printErrorPacket.getModifiers()));

        Method setCurrentPhase = klass.getDeclaredMethod("setCurrentPhase", ApplicationPhase.class);
        assertTrue(Modifier.isPublic(setCurrentPhase.getModifiers()));

        Method runInput = klass.getDeclaredMethod("runInput");
        assertTrue(Modifier.isPublic(runInput.getModifiers()));

        Method show = klass.getDeclaredMethod("show");
        assertTrue(Modifier.isPublic(show.getModifiers()));
    }

    @Test
    @DisplayName("TextTerminal extends UserInputInterface")
    void inheritance() {
        assertTrue(UserInputInterface.class.isAssignableFrom(TextTerminal.class));
    }

    // --- best-effort behaviour assertions ---

    @Test
    @DisplayName("constructor accepts (null server, ANY phase) without throwing when the terminal is available")
    void constructorBestEffort() {
        assumeTrue(canLoadClasses, "TextTerminal cannot be loaded without a system terminal");
        assertDoesNotThrow(() -> new TextTerminal(null, ApplicationPhase.ANY));
    }

    @Test
    @DisplayName("setCurrentPhase does not throw when the terminal is available")
    void setCurrentPhaseBestEffort() {
        assumeTrue(canLoadClasses, "TextTerminal cannot be loaded without a system terminal");
        TextTerminal terminal = new TextTerminal(null, ApplicationPhase.ANY);
        assertDoesNotThrow(() -> terminal.setCurrentPhase(ApplicationPhase.HALL));
        assertDoesNotThrow(() -> terminal.setCurrentPhase(ApplicationPhase.ROOM));
        assertDoesNotThrow(() -> terminal.setCurrentPhase(ApplicationPhase.GAME));
    }
}
