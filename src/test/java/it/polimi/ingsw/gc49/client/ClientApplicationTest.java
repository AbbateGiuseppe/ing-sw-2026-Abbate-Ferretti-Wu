package it.polimi.ingsw.gc49.client;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Tests for {@link ClientApplication}.
 * <p>
 * The class has a heavy static initializer that opens a JLine system terminal. In a
 * headless test environment this can throw {@link ExceptionInInitializerError}; when that
 * happens the tests are skipped via {@link org.junit.jupiter.api.Assumptions} rather than
 * failing. The runtime initialization is checked once in {@link #ensureClassLoadable()}.
 */
class ClientApplicationTest {

    private static boolean classLoaded;

    @BeforeAll
    static void ensureClassLoadable() {
        try {
            // touch a constant of the class to trigger its initialization
            Object unused = ClientApplication.mockups;
            classLoaded = unused != null;
        } catch (ExceptionInInitializerError | NoClassDefFoundError | RuntimeException e) {
            classLoaded = false;
        }
    }

    @Test
    @DisplayName("constructor sets the nickname")
    void constructorSetsNickname() {
        assumeTrue(classLoaded, "ClientApplication could not be initialized in this environment");
        ClientApplication app = new ClientApplication("Peppe");
        assertEquals("Peppe", app.nickname);
    }

    @Test
    @DisplayName("constructor accepts null nicknames")
    void constructorAcceptsNullNickname() {
        assumeTrue(classLoaded, "ClientApplication could not be initialized in this environment");
        ClientApplication app = new ClientApplication(null);
        assertNull(app.nickname);
    }

    @Test
    @DisplayName("ConnectionType enum has exactly RMI and SOCKET")
    void connectionTypeEnum() {
        assumeTrue(classLoaded, "ClientApplication could not be initialized in this environment");
        ClientApplication.ConnectionType[] values = ClientApplication.ConnectionType.values();
        assertEquals(2, values.length);
        assertEquals(ClientApplication.ConnectionType.RMI, ClientApplication.ConnectionType.valueOf("RMI"));
        assertEquals(ClientApplication.ConnectionType.SOCKET, ClientApplication.ConnectionType.valueOf("SOCKET"));
    }

    @Test
    @DisplayName("mockups is a non-null shared singleton")
    void mockupsIsSingleton() {
        assumeTrue(classLoaded, "ClientApplication could not be initialized in this environment");
        assertNotNull(ClientApplication.mockups);
        // calling twice must yield the same instance (it is final static)
        assertSame(ClientApplication.mockups, ClientApplication.mockups);
    }

    @Test
    @DisplayName("terminal and lineReader are initialized as non-null static fields")
    void terminalAndLineReaderInitialized() {
        assumeTrue(classLoaded, "ClientApplication could not be initialized in this environment");
        assertNotNull(ClientApplication.terminal);
        assertNotNull(ClientApplication.lineReader);
    }

    @Test
    @DisplayName("implements VirtualClient")
    void implementsVirtualClient() {
        assumeTrue(classLoaded, "ClientApplication could not be initialized in this environment");
        ClientApplication app = new ClientApplication("Peppe");
        assertInstanceOf(
                it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualClients.VirtualClient.class,
                app);
    }

    @Test
    @DisplayName("structural check: declared fields match the public API (works without class init)")
    void declaredFieldsExist() throws NoSuchFieldException {
        // these reflective lookups do NOT initialize the class
        Class<ClientApplication> klass = ClientApplication.class;

        Field nickname = klass.getDeclaredField("nickname");
        assertEquals(String.class, nickname.getType());
        assertTrue(Modifier.isPublic(nickname.getModifiers()));

        Field mockups = klass.getDeclaredField("mockups");
        assertTrue(Modifier.isStatic(mockups.getModifiers()));
        assertTrue(Modifier.isFinal(mockups.getModifiers()));

        Field localLanguage = klass.getDeclaredField("localLanguage");
        assertTrue(Modifier.isStatic(localLanguage.getModifiers()));

        Field terminal = klass.getDeclaredField("terminal");
        assertTrue(Modifier.isStatic(terminal.getModifiers()));
        assertTrue(Modifier.isFinal(terminal.getModifiers()));

        Field lineReader = klass.getDeclaredField("lineReader");
        assertTrue(Modifier.isStatic(lineReader.getModifiers()));
        assertTrue(Modifier.isFinal(lineReader.getModifiers()));
    }
}
