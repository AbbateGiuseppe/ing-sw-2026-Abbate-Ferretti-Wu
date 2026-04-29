package it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers;

import it.polimi.ingsw.gc49.datapacket.directedToClient.ANY_phase.CHANGE_PHASE.ChangePhasePacket;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VirtualServer extends Remote, VirtualGameServer, VirtualHallServer, VirtualRoomServer {
}
