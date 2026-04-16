package it.polimi.ingsw.gc49.rmi.server;

import it.polimi.ingsw.gc49.VirtualClient;
import it.polimi.ingsw.gc49.VirtualServer;
import it.polimi.ingsw.gc49.datapacket.COMMAND.Command;
import it.polimi.ingsw.gc49.rmi.client.VirtualClientRmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VirtualServerRmi extends Remote, VirtualServer {
    void connect( String nickname, VirtualClientRmi client ) throws RemoteException;

    @Override
    void sendCommand ( Command command ) throws RemoteException;
}
