package it.polimi.ingsw.gc49.rmi_socket.server;

import it.polimi.ingsw.gc49.controller.massi.MassiController;
import it.polimi.ingsw.gc49.rmi_socket.server.connectors.ConnectorServerSide;
import it.polimi.ingsw.gc49.rmi_socket.server.connectors.RmiConnectorServerSide;
import it.polimi.ingsw.gc49.rmi_socket.server.connectors.SocketConnectorServerSide;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerMultiplexer extends UnicastRemoteObject implements FactoryServiceRmi {
    public static final String mainServer = "MesosMainServer";
    private ServerSocket serverSocket;
    private final Map<String, ConnectorServerSide> clients = new HashMap<>();
    private final int port;

    /**
     * Constructor for rmi server
     * @throws RemoteException
     */
    public ServerMultiplexer (int port) throws RemoteException {
        super();
        this.port = port;
    }

    /**
     * Constructor for socket server
     * @param serverSocket, the serverSocket used to listen to new sockets connections.
     * @throws RemoteException
     */
    public ServerMultiplexer(int port, ServerSocket serverSocket) throws RemoteException {
        super();
        this.port = port;
        this.serverSocket = serverSocket;
    }

    public static void main ( String[] args ) throws IOException {
        String host = null; //args[0];

        int portSocket = Integer.parseInt(args[1]);
        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(portSocket);
                new ServerMultiplexer(portSocket, serverSocket).runSocketServer();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }).start();

        int portRmi = Integer.parseInt(args[2]);
        new Thread(() -> {
            try {
                FactoryServiceRmi serverRmi = new ServerMultiplexer(portRmi);

                Registry registry = LocateRegistry.createRegistry(portRmi);

                registry.rebind(mainServer, serverRmi);
            } catch ( RemoteException e ) {
                new RuntimeException(e);
            }
        }).start();
    }

    @Override
    public VirtualServerRmi connectPlayerRmi ( String nickname ) throws RemoteException {
        synchronized (this.clients) {
            if(!clients.containsKey(nickname)) {
                RmiConnectorServerSide connector = new RmiConnectorServerSide(clients.size(), new MassiController(clients.size()), this);
                clients.put(nickname, connector);

                return (VirtualServerRmi) UnicastRemoteObject.exportObject(connector, port);
            } else {
                throw new RemoteException("Nomignolo già esistente e connesso. Prego, cambiarlo.");
            }
        }
    }

    private void runSocketServer() throws IOException, ClassNotFoundException {
        Socket clientSocket;
        while ((clientSocket = this.serverSocket.accept()) != null) {
            ObjectOutputStream socketTx = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream socketRx = new ObjectInputStream(clientSocket.getInputStream());

            String nickname = (String) socketRx.readObject();
            if(!clients.containsKey(nickname)) {
                synchronized (this.clients) {
                    SocketConnectorServerSide connector = new SocketConnectorServerSide(
                            clients.size(),
                            new MassiController(clients.size()),
                            this,
                            socketRx,
                            socketTx
                    );

                    clients.put(nickname, connector);

                    new Thread(() -> {
                        try {
                            connector.runVirtualClient();
                        } catch (IOException | ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }).start();
                }
                socketTx.writeObject(new String("Connessione riuscita."));
                socketTx.flush();

            } else {
                socketTx.writeObject(new RuntimeException("Nomignolo già esistente e connesso. Prego, cambiarlo."));
                socketTx.flush();
            }
        }
    }
}
