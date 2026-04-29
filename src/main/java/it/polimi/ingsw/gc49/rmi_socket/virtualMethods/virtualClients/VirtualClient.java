package it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualClients;

import it.polimi.ingsw.gc49.datapacket.directedToClient.ANY_phase.CHANGE_PHASE.ChangePhasePacket;

import java.rmi.Remote;

public interface VirtualClient extends Remote, VirtualGameClient, VirtualHallClient, VirtualRoomClient {
    void changePhaseClient ( ChangePhasePacket changePhasePacket ) throws Exception;
}
