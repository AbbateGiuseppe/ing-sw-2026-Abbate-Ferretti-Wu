package it.polimi.ingsw.gc49.rmi_socket.server;

import it.polimi.ingsw.gc49.rmi_socket.server.proxies.PhasedProxyPlayer;
import it.polimi.ingsw.gc49.rmi_socket.server.proxies.RmiProxyPlayer;
import it.polimi.ingsw.gc49.rmi_socket.server.proxies.SocketProxyPlayer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualClients.VirtualClient;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualServer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.adapters.VirtualHallServerAdapter;

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
    private static final Map<String, PhasedProxyPlayer> clients = new HashMap<>();
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
            } catch (Exception e) {
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
    public VirtualServer connectPlayerRmi ( String nickname, VirtualClient clientStub ) throws Exception {
        synchronized (clients) {
            if(!clients.containsKey(nickname)) {
                PhasedProxyPlayer proxy = new RmiProxyPlayer(
                        this, nickname, ApplicationPhase.HALL, new VirtualHallServerAdapter(hall),
                        clientStub
                );
                System.out.println(proxy.nickname + " is connected");
                clients.put(nickname, proxy); //store the player in the clients-list.
                hall.enterPlayer(proxy); //enter the player into the hall

                proxy.startNetworkHealthChecks(); // check heartbeat

                return (VirtualServer) UnicastRemoteObject.exportObject(proxy, port);

            } else {
                if (clients.containsKey(nickname)) {
                    PhasedProxyPlayer existing = clients.get(nickname);
                    if (existing.isRunning()) throw new RemoteException("Already logged");
                    System.out.println(existing.nickname + " is now reconnected");

                    // update stub of old proxy
                    ((RmiProxyPlayer) existing).updateClientStub(clientStub);
                    existing.startNetworkHealthChecks();

                    // export old proxy already existing
                    existing.refreshClientState();
                    return (VirtualServer) existing;
                } else {
                    throw new RemoteException("Player Already Online");
                }
            }
        }
    }

    private void runSocketServer() throws Exception {
        Socket clientSocket;
        while ((clientSocket = this.serverSocket.accept()) != null) {
            ObjectOutputStream socketOutput = new ObjectOutputStream(clientSocket.getOutputStream());
            socketOutput.flush();
            ObjectInputStream socketInput = new ObjectInputStream(clientSocket.getInputStream());

            String nickname = (String) socketInput.readObject();
            synchronized (clients) {
                if(!clients.containsKey(nickname)) {
                    PhasedProxyPlayer proxy = new SocketProxyPlayer(
                            this, nickname, ApplicationPhase.HALL, new VirtualHallServerAdapter(hall),
                            socketInput, socketOutput
                    );
                    System.out.println(proxy.nickname + " is connected");

                    new Thread(() -> {
                        try {
                            proxy.runVirtualClient();
                        } catch (SocketException e) {
                            System.out.println("Connection lost with " + proxy.nickname);
                        }
                    }).start();


                    socketOutput.writeObject(new String("Connessione riuscita."));
                    socketOutput.flush();
                    // control heartbeat
                    proxy.startNetworkHealthChecks();

                    clients.put(nickname, proxy); //store the player in the clients-list.
                    hall.enterPlayer(proxy); //enter the player into the hall
                } else {
                    if (clients.containsKey(nickname)) {
                        PhasedProxyPlayer existingProxy = clients.get(nickname);

                        if (!existingProxy.isRunning()) {
                            if (existingProxy instanceof SocketProxyPlayer && !existingProxy.isRunning()) {
                                ((SocketProxyPlayer) existingProxy).updateStreams(socketInput, socketOutput);
                                System.out.println(existingProxy.nickname + " now reconnected");

                                new Thread(() -> {
                                    try {
                                        existingProxy.runVirtualClient();
                                    } catch (SocketException e) {
                                        System.out.println("Connection Lost");
                                    }
                                }).start();

                                existingProxy.startNetworkHealthChecks();
                                existingProxy.refreshClientState();
                            }
                        } else {
                            socketOutput.writeObject(new RuntimeException("Player already connected!"));
                            return;
                        }
                    } else {
                        socketOutput.writeObject(new RuntimeException("Nomignolo già esistente e connesso. Prego, cambiarlo."));
                        socketOutput.flush();
                    }
                }
            }
        }
    }
}
