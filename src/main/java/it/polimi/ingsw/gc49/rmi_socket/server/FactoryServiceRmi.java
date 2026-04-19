package it.polimi.ingsw.gc49.rmi_socket.server;

import it.polimi.ingsw.gc49.rmi_socket.server.connectors.RmiConnectorServerSide;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FactoryServiceRmi extends Remote {
    VirtualServerRmi connectPlayerRmi ( String nickname ) throws RemoteException;
}
