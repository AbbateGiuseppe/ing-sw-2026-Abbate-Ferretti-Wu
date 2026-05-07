package it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers;

import it.polimi.ingsw.gc49.rmi_socket.server.proxies.PhasedProxyPlayer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.Disconnectable;

import java.rmi.Remote;


public interface VirtualServer extends Remote, VirtualGameServer, VirtualHallServer, VirtualRoomServer, Disconnectable {

}
