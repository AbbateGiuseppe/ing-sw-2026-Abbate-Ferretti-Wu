package it.polimi.ingsw.gc49.rmi_socket.datapacket;

import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the abstract {@link Datapacket}. A minimal subclass is used to instantiate it.
 */
class DatapacketTest {

    /** Minimal concrete subclass for instantiation. */
    private static class TestPacket extends Datapacket {
        TestPacket(DatapacketType type, ApplicationPhase phase) {
            super(type, phase);
        }
    }

    @Test
    @DisplayName("constructor stores datapacketType and applicationPhase as public final fields")
    void constructorStoresFields() {
        TestPacket packet = new TestPacket(Datapacket.DatapacketType.HEARTBEAT, ApplicationPhase.ANY);
        assertEquals(Datapacket.DatapacketType.HEARTBEAT, packet.datapacketType);
        assertEquals(Datapacket.DatapacketType.HEARTBEAT, packet.getDatapacketType());
        assertEquals(ApplicationPhase.ANY, packet.applicationPhase);
    }

    @Test
    @DisplayName("senderNickname is null by default and round-trips through its setter")
    void senderNicknameRoundTrip() {
        TestPacket packet = new TestPacket(Datapacket.DatapacketType.STRING, ApplicationPhase.ANY);
        assertNull(packet.getSenderNickname());
        packet.setSenderNickname("Peppe");
        assertEquals("Peppe", packet.getSenderNickname());
        packet.setSenderNickname(null);
        assertNull(packet.getSenderNickname());
    }

    @Test
    @DisplayName("Datapacket is Serializable")
    void datapacketIsSerializable() {
        TestPacket packet = new TestPacket(Datapacket.DatapacketType.HEARTBEAT, ApplicationPhase.ANY);
        assertInstanceOf(java.io.Serializable.class, packet);
    }

    @Test
    @DisplayName("DatapacketType has all 14 expected enum values")
    void datapacketTypeEnumValues() {
        Datapacket.DatapacketType[] values = Datapacket.DatapacketType.values();
        assertEquals(14, values.length);
        // verify a few representative values exist
        assertNotNull(Datapacket.DatapacketType.valueOf("INITIALIZE_MODEL"));
        assertNotNull(Datapacket.DatapacketType.valueOf("UPDATE_MODEL"));
        assertNotNull(Datapacket.DatapacketType.valueOf("ERROR"));
        assertNotNull(Datapacket.DatapacketType.valueOf("HEARTBEAT"));
    }

    @Test
    @DisplayName("DatapacketType.valueOf throws on an unknown name")
    void datapacketTypeUnknownThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> Datapacket.DatapacketType.valueOf("UNKNOWN"));
    }
}
