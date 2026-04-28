package it.polimi.ingsw.gc49.rmi_socket.server;

import it.polimi.ingsw.gc49.rmi_socket.virtualServers.VirtualGameServer;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FactoryServiceRmi extends Remote {
    /**
     * Used to take control of the connection in RMI, authorize it when two players don't have the same nickname and give the client his connector.
     * @param nickname, the player's nickname;
     * @return The server's skeleton
     * @throws RemoteException an identical name is probably already connected.
     */
    VirtualGameServer connectPlayerRmi ( String nickname ) throws RemoteException;
}
