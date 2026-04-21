package it.polimi.ingsw.gc49.rmi_socket.server;

import it.polimi.ingsw.gc49.rmi_socket.server.connectors.RmiConnectorServerSide;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FactoryServiceRmi extends Remote {
    /**
     * Used to take control of the connection in RMI, authorize it when two players don't have the same nickname and give the client his connector.
     * @param nickname, the player's nickname;
     * @return The server's skeleton
     * @throws RemoteException
     */
    VirtualServerRmi connectPlayerRmi ( String nickname ) throws RemoteException;
}
