package it.polimi.ingsw.gc49.rmi_socket.virtualClients;

import java.rmi.Remote;

public interface VirtualClient extends Remote, VirtualGameClient, VirtualHallClient, VirtualRoomClient {
}
