package it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers;

import it.polimi.ingsw.gc49.datapacket.directedToClient.INITIALIZE_HALL.InitializeHallPacket;
import it.polimi.ingsw.gc49.rmi_socket.server.proxies.PhasedProxyPlayer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.Heartbeatable;

import java.rmi.Remote;


public interface VirtualServer extends Remote, VirtualGameServer, VirtualHallServer, VirtualRoomServer {
    void syncPlayer(PhasedProxyPlayer p) throws Exception;
}
