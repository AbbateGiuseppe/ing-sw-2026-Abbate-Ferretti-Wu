package it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualClients;

import java.rmi.Remote;

public interface VirtualClient extends Remote, VirtualGameClient, VirtualHallClient, VirtualRoomClient {
}
