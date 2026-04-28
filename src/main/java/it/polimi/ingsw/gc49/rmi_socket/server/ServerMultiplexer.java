package it.polimi.ingsw.gc49.rmi_socket.server;

import it.polimi.ingsw.gc49.rmi_socket.server.connectors.ReferencedProxyPlayer;
import it.polimi.ingsw.gc49.rmi_socket.server.connectors.proxyPlayer.inHall.ProxyPlayerHallRmi;
import it.polimi.ingsw.gc49.rmi_socket.server.connectors.proxyPlayer.inHall.ProxyPlayerHallSocket;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualClients.VirtualClient;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualHallServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class ServerMultiplexer extends UnicastRemoteObject implements FactoryServiceRmi {
    public static final String mainServer = "MesosMainServer";
    public static final int portSocket = 2001;
    public static final int portRmi = 2002;
    private ServerSocket serverSocket;
    private static final Map<String, ReferencedProxyPlayer> clients = new HashMap<>();
    private static final Hall hall = new Hall();
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

        //SOCKET
        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(portSocket);
                new ServerMultiplexer(portSocket, serverSocket).runSocketServer();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }).start();

        //RMI
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
    public VirtualHallServer connectPlayerRmi ( String nickname, VirtualClient clientStub ) throws RemoteException {
        synchronized (clients) {
            if(!clients.containsKey(nickname)) {
                ReferencedProxyPlayer proxy = new ReferencedProxyPlayer(new ProxyPlayerHallRmi(this, nickname, hall, clientStub));
                clients.put(nickname, proxy); //store the player in the clients-list.
                hall.enterPlayer(proxy); //enter the player into the hall

                return (VirtualHallServer) UnicastRemoteObject.exportObject(proxy, port);
            } else {
                throw new RemoteException("Nomignolo già esistente e connesso. Prego, cambiarlo.");
            }
        }
    }

    private void runSocketServer() throws IOException, ClassNotFoundException {
        Socket clientSocket;
        while ((clientSocket = this.serverSocket.accept()) != null) {
            ObjectInputStream socketInput = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream socketOutput = new ObjectOutputStream(clientSocket.getOutputStream());

            String nickname = (String) socketInput.readObject();
            synchronized (clients) {
                if(!clients.containsKey(nickname)) {
                    ReferencedProxyPlayer proxy = new ReferencedProxyPlayer(
                            new ProxyPlayerHallSocket(this, nickname, hall, socketInput, socketOutput)
                    );

                    clients.put(nickname, proxy);

                    new Thread(() -> {
                        try {
                            proxy.getProxy().runVirtualClient();
                        } catch (SocketException e) {
                            System.out.println("Connessione con " + proxy.getProxy().nickname + " persa");
                        }
                    }).start();

                    socketOutput.writeObject(new String("Connessione riuscita."));
                    socketOutput.flush();
                } else {
                    socketOutput.writeObject(new RuntimeException("Nomignolo già esistente e connesso. Prego, cambiarlo."));
                    socketOutput.flush();
                }
            }
        }
    }
}
