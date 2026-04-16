package it.polimi.ingsw.gc49.rmi.server;

import it.polimi.ingsw.gc49.controller.massi.MassiController;
import it.polimi.ingsw.gc49.datapacket.COMMAND.Command;
import it.polimi.ingsw.gc49.rmi.client.VirtualClientRmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteServer;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class RmiServerSide extends UnicastRemoteObject implements VirtualServerRmi {
    public static final String mainServer = "MesosMainServer";
    private final Map<UUID, VirtualClientRmi> clients = new HashMap<>();
    private final Map<UUID, MassiController> controllers = new HashMap<>();

    public RmiServerSide() throws RemoteException {
        super();
    }

    public static void main(String[] args) throws RemoteException {
        VirtualServerRmi server = new RmiServerSide();

        int port = Integer.parseInt(args[0]);
        Registry registry = LocateRegistry.createRegistry(port);

        registry.rebind(mainServer, server);
    }

    @Override
    public void connect ( String nicknameClient, VirtualClientRmi client ) throws RemoteException {
        synchronized (this.clients) {
            UUID nameBasedId = UUID.nameUUIDFromBytes(nicknameClient.getBytes());
            controllers.put(nameBasedId, new MassiController(clients.size()) );
            this.clients.put(nameBasedId, client);
        }
    }

    @Override
    public void sendCommand ( Command command ) throws RemoteException {

    }
}
