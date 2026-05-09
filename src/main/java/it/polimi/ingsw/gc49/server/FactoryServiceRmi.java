package it.polimi.ingsw.gc49.server;

import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualClients.VirtualClient;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualServer;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FactoryServiceRmi extends Remote {
    /**
     * Used to take control of the connection in RMI, authorize it when two players don't have the same nickname and give the client his connector.
     * @param nickname, the player's nickname;
     * @return The server's skeleton
     * @throws RemoteException an identical name is probably already connected.
     */
    VirtualServer connectPlayerRmi ( String nickname, VirtualClient clientStub ) throws Exception;

    /**
     * Used by the client to check if there is such a server on an IP address.
     * @return true, always
     */
    boolean ping() throws Exception;
}
