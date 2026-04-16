package it.polimi.ingsw.gc49.rmi.client;

import it.polimi.ingsw.gc49.View.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.datapacket.UPDATE_MODEL.MockupModelDatapacketable;
import it.polimi.ingsw.gc49.rmi.server.RmiServerSide;
import it.polimi.ingsw.gc49.rmi.server.VirtualServerRmi;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class RmiClientSide extends UnicastRemoteObject implements VirtualClientRmi {
    private final String nickname;
    private MockupGame mockupGame;
    private final VirtualServerRmi server;
    private static final String mainServer = RmiServerSide.mainServer;

    public RmiClientSide( VirtualServerRmi server, String nickname ) throws RemoteException {
        super();
        this.server = server;
        this.nickname = nickname;
    }

    public static void main(String[] args) throws RemoteException, NotBoundException {
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        Registry registry = LocateRegistry.getRegistry(host, port);

        VirtualServerRmi server = (VirtualServerRmi) registry.lookup(mainServer);
        String nickname = args[2];

        new RmiClientSide(server, nickname).run();
    }

    private void run() throws RemoteException {
        this.server.connect(nickname, this);
        //TODO: running.
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
