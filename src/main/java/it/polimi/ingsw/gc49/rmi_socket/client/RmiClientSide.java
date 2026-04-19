package it.polimi.ingsw.gc49.rmi_socket.client;

import it.polimi.ingsw.gc49.View.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.datapacket.UPDATE_MODEL.MockupModelDatapacketable;
import it.polimi.ingsw.gc49.rmi_socket.server.ServerMultiplexer;
import it.polimi.ingsw.gc49.rmi_socket.server.VirtualServerRmi;
import it.polimi.ingsw.gc49.rmi_socket.server.FactoryServiceRmi;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class RmiClientSide extends UnicastRemoteObject implements VirtualClientRmi {
    private final String nickname;
    private MockupGame mockupGame;
    VirtualServerRmi server;
    private static final String mainServer = ServerMultiplexer.mainServer;

    public RmiClientSide( String nickname, VirtualServerRmi server ) throws RemoteException {
        super();
        this.nickname = nickname;
        this.server = server;
    }

    public static void main( String[] args ) throws RemoteException, NotBoundException {
        int port = Integer.parseInt(args[0]);
        Registry registry = LocateRegistry.getRegistry(null, port); //null means "localhost"

        String nickname = args[1];
        VirtualServerRmi server = ((FactoryServiceRmi) registry.lookup(mainServer)).connectPlayerRmi(nickname);
    }

    @Override
    public void initializeClientModel ( MockupGame mockupGame ) throws RemoteException {

    }

    @Override
    public void updateClientModel ( List<MockupModelDatapacketable> updatesList ) throws RemoteException {

    }

    @Override
    public void reportError ( String details ) throws RemoteException {

    }
}
