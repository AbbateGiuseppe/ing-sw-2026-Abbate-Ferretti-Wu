package it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.CHANGE_PHASE;

import it.polimi.ingsw.gc49.rmi_socket.datapacket.Datapacket;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChangePhasePacketTest {

    @Test
    @DisplayName("constructor stores newPhase and tags the packet as CHANGE_PHASE")
    void constructorStoresFields() {
        ChangePhasePacket packet = new ChangePhasePacket(ApplicationPhase.ANY);
        assertEquals(ApplicationPhase.ANY, packet.newPhase);
        assertEquals(Datapacket.DatapacketType.CHANGE_PHASE, packet.datapacketType);
    }

    @Test
    @DisplayName("the packet's own applicationPhase is ANY (it applies to every phase)")
    void applicationPhaseIsAny() {
        ChangePhasePacket packet = new ChangePhasePacket(ApplicationPhase.ANY);
        assertEquals(ApplicationPhase.ANY, packet.applicationPhase);
    }

    @Test
    @DisplayName("ChangePhasePacket is a Datapacket")
    void isDatapacket() {
        assertInstanceOf(Datapacket.class, new ChangePhasePacket(ApplicationPhase.ANY));
    }
}
