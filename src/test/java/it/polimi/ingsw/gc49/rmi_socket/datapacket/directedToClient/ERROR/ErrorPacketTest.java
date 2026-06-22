package it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.ERROR;

import it.polimi.ingsw.gc49.rmi_socket.datapacket.Datapacket;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ErrorPacketTest {

    @Test
    @DisplayName("constructor stores title, content, and the disconnection flag")
    void constructorStoresFields() {
        ErrorPacket packet = new ErrorPacket("Error", "Something broke", true);
        assertEquals("Error", packet.errorTitle);
        assertEquals("Something broke", packet.errorContent);
        assertTrue(packet.forceDisconnection);
    }

    @Test
    @DisplayName("forceDisconnection round trip via the constructor flag")
    void forceDisconnectionBothValues() {
        ErrorPacket disconnects = new ErrorPacket("T", "C", true);
        ErrorPacket softError = new ErrorPacket("T", "C", false);
        assertTrue(disconnects.forceDisconnection);
        assertFalse(softError.forceDisconnection);
    }

    @Test
    @DisplayName("the packet is tagged as ERROR with applicationPhase ANY")
    void packetTags() {
        ErrorPacket packet = new ErrorPacket("T", "C", false);
        assertEquals(Datapacket.DatapacketType.ERROR, packet.datapacketType);
        assertEquals(ApplicationPhase.ANY, packet.applicationPhase);
    }

    @Test
    @DisplayName("null title and content are accepted")
    void acceptsNulls() {
        ErrorPacket packet = new ErrorPacket(null, null, false);
        assertNull(packet.errorTitle);
        assertNull(packet.errorContent);
    }

    @Test
    @DisplayName("ErrorPacket is a Datapacket")
    void isDatapacket() {
        assertInstanceOf(Datapacket.class, new ErrorPacket("t", "c", false));
    }
}
