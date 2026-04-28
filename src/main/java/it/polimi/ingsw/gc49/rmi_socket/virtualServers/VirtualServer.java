package it.polimi.ingsw.gc49.rmi_socket.virtualServers;

import java.rmi.Remote;

public interface VirtualServer extends Remote, VirtualGameServer, VirtualHallServer, VirtualRoomServer {
}
