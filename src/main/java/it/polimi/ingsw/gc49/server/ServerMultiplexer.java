package it.polimi.ingsw.gc49.server;

import it.polimi.ingsw.gc49.rmi_socket.datapacket.STRING.StringPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.ANY_phase.DISCONNECT.DisconnectPacket;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.adapters.VirtualGameServerAdapter;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.adapters.VirtualServerAdapter;
import it.polimi.ingsw.gc49.server.proxies.PhasedProxyPlayer;
import it.polimi.ingsw.gc49.server.proxies.RmiProxyPlayer;
import it.polimi.ingsw.gc49.server.proxies.SocketProxyPlayer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.Disconnectable;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualClients.VirtualClient;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualServer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.adapters.VirtualHallServerAdapter;
import it.polimi.ingsw.gc49.server.rooms.PlayingRoom;
import it.polimi.ingsw.gc49.server.rooms.Room;

import java.io.*;
import java.net.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The {@code ServerMultiplexer} class acts as the main entry point for the server application.
 * It manages both RMI and Socket connections simultaneously, multiplexing incoming clients.
 * It handles player connection, reconnection, and disconnection, while routing them
 * into the main {@link Hall}. It also manages the asynchronous persistence of the server state
 * to recover from potential crashes.
 */
public class ServerMultiplexer extends UnicastRemoteObject implements FactoryServiceRmi, Disconnectable {
    /** The binding name used for the RMI Registry. */
    public static final String mainServer = "MesosMainServer";

    /** The default port used for Socket connections. */
    public static final int portSocket = 2001;

    /** The default port used for RMI connections. */
    public static final int portRmi = 2002;


    private transient ServerSocket serverSocket;

    /** Thread-safe map containing all the connected or disconnected players known to the server. */
    private static final Map<String, PhasedProxyPlayer> clients = new HashMap<>();

    /** The main lobby (Hall) where players are placed upon connection. */
    private static Hall hall = new Hall();


    private final int port;
    private VirtualServerAdapter recoverRoom;

    /** Executor responsible for handling background state-saving tasks asynchronously. */
    private static final ExecutorService persistenceExecutor = Executors.newSingleThreadExecutor();

    /**
     * Constructs a {@code ServerMultiplexer} for handling RMI connections.
     * Initializes the server port and sets the server reference in the Hall.
     *
     * @param port the RMI port to export the object on.
     * @throws RemoteException if the object could not be exported.
     */
    public ServerMultiplexer (int port) throws RemoteException {
        super();
        this.port = port;
        hall.setServer(this);
    }

    /**
     * Constructs a {@code ServerMultiplexer} for handling Socket connections.
     *
     * @param port         the port associated with the server connection.
     * @param serverSocket the {@link ServerSocket} used to listen for incoming client sockets.
     * @throws RemoteException if an RMI-related error occurs during initialization.
     */
    public ServerMultiplexer(int port, ServerSocket serverSocket) throws RemoteException {
        super();
        this.port = port;
        this.serverSocket = serverSocket;
    }

    public static void main ( String[] args ) {
        try {
            ServerMultiplexer loader = new ServerMultiplexer(portRmi);
            loadState(loader);

            if (hall == null) {
                hall = new Hall();
                hall.setServer(loader);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        try {
            String localIP = InetAddress.getLocalHost().getHostAddress();
            System.out.println("Local IP Address: " + localIP);
        } catch (UnknownHostException e) {
            System.out.println("Could not get local hostname: " + e.getMessage());
        }


        // ============================================================
        //SOCKET
        // ============================================================

        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(portSocket);
                new ServerMultiplexer(portSocket, serverSocket).runSocketServer();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();

        // ============================================================
        //RMI
        // ============================================================

        new Thread(() -> {
            try {
                FactoryServiceRmi serverRmi = new ServerMultiplexer(portRmi);

                Registry registry = LocateRegistry.createRegistry(portRmi);

                registry.rebind(mainServer, serverRmi);
            } catch ( RemoteException e ) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    /**
     * Simple ping method used by clients to check if the server is responsive.
     *
     * @return {@code true} if the server is reachable.
     */
    @Override
    public boolean ping() {
        return true;
    }

    /**
     * Connects a new player or reconnects an existing one using RMI technology.
     * If the player is already registered but disconnected, it converts the connection
     * to RMI and restores their session.
     *
     * @param nickname   the chosen username of the player.
     * @param clientStub the RMI stub representing the virtual client.
     * @return the {@link VirtualServer} interface exposed to the client.
     * @throws RemoteException if a player with the same nickname is already actively connected.
     * @throws Exception       if an error occurs during proxy creation or export.
     */
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

                proxy.sendString(new StringPacket("Connessione riuscita."));

                new Thread(() -> {
                    runVirtualClient(proxy);
                }).start();

                return (VirtualServer) UnicastRemoteObject.exportObject(proxy, port);

            } else {
                PhasedProxyPlayer existingProxy = clients.get(nickname);
                if (existingProxy.isConnected()) {
                    throw new RemoteException("A player with such a nickname is already connected. Please, change it.");
                } else {

                    //finds the existing proxy and converts it to the newly chosen connection technology
                    PhasedProxyPlayer convertedProxy = existingProxy.convertToRmi();
                    clients.remove(nickname);
                    clients.put(nickname, convertedProxy);

                    new Thread(() -> {
                        try {
                            Thread.sleep(200);

                            convertedProxy.reconnect(clientStub, null, null);
                            runVirtualClient(convertedProxy);

                            convertedProxy.sendString(new StringPacket("Riconnessione riuscita."));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }).start();

                    return (VirtualServer) UnicastRemoteObject.exportObject(convertedProxy, port);
                }
            }
        }
    }

    /**
     * Continuously listens for incoming Socket connections.
     * Manages new player connections and disconnections/reconnections seamlessly,
     * similarly to the RMI counterpart.
     *
     * @throws Exception if an I/O error occurs while accepting or setting up streams.
     */
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

                    proxy.sendString(new StringPacket("Connected Successfully."));

                    new Thread(() -> {
                        runVirtualClient(proxy);
                    }).start();

                    clients.put(nickname, proxy); //store the player in the clients-list.
                    hall.enterPlayer(proxy); //enter the player into the hall
                } else {


                    // ============================================================
                    //RESILIENCE
                    // ============================================================

                    //finds the existing proxy and converts it to the newly chosen connection technology
                    PhasedProxyPlayer existingProxy = clients.get(nickname);

                    if (existingProxy.isConnected()) {
                        socketOutput.writeObject(new StringPacket("A player with such a nickname is already connected. Please, change it."));
                        socketOutput.flush();
                        return;
                    } else {
                        //finds the existing proxy and converts it to the newly chosen connection technology
                        PhasedProxyPlayer convertedProxy = existingProxy.convertToSocket();
                        clients.remove(nickname);
                        clients.put(nickname, convertedProxy);

                        convertedProxy.reconnect(null, socketInput, socketOutput);

                        System.out.println(convertedProxy.nickname + " is reconnected");

                        convertedProxy.sendString(new StringPacket("Riconnessione riuscita."));

                        new Thread(() -> {
                            runVirtualClient(convertedProxy);
                        }).start();
                    }
                }
            }
        }
    }

    /**
     * Starts the client proxy listener loop.
     * If a {@link SocketException} is caught, it triggers an automatic disconnection
     * for that specific player.
     *
     * @param proxy the {@link PhasedProxyPlayer} representing the client to run.
     */
    private void runVirtualClient ( PhasedProxyPlayer proxy ) {
        try {
            proxy.runVirtualClient();
        } catch (SocketException e) {
            try {
                DisconnectPacket disconnectPacket = new DisconnectPacket();
                disconnectPacket.setSenderNickname(proxy.nickname);
                proxy.disconnect(disconnectPacket);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }



    // ============================================================
    //DISCONNECTION
    // ============================================================



    /**
     * Handles the explicit disconnection of a client.
     * Currently not fully implemented.
     *
     * @param disconnectPacket the packet containing disconnection details.
     * @throws Exception if an error occurs during disconnection.
     */
    @Override
    public void disconnect ( DisconnectPacket disconnectPacket ) throws Exception {
    }



    // ============================================================
    //PERSISTENCE
    // ============================================================


    /**
     * Submits a request to save the server state asynchronously.
     * This avoids blocking the main thread while persisting data to the disk.
     */
    public static void saveStateAsync() {
        // Chiama il metodo sincrono che scrive su file
        persistenceExecutor.submit(ServerMultiplexer::saveState);
    }



    /**
     * Synchronously saves the current state of the {@link Hall} and all connected clients
     * into a serialized file ({@code server_state.ser}).
     */
    public static synchronized void saveState() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("server_state.ser"))) {
            oos.writeObject(hall);
            oos.writeObject(clients);
            System.out.println("[PERSISTENCE] Snapshot saved.");
        } catch (IOException e) {
            System.err.println("[PERSISTENCE] Check Serializable: " + e.getMessage());
        }
    }



    /**
     * Attempts to load a previously saved server state from disk ({@code server_state.ser}).
     * Restores the hall, reconnects internal proxy states, and resumes paused game rooms.
     *
     * @param newServer the newly instantiated {@code ServerMultiplexer} to inject into
     * the recovered objects (replacing transient references).
     */
    private static void loadState(ServerMultiplexer newServer) {
        File file = new File("server_state.ser");
        if (!file.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            hall = (Hall) ois.readObject();
            Map<String, PhasedProxyPlayer> savedClients = (Map<String, PhasedProxyPlayer>) ois.readObject();
            clients.putAll(savedClients);

            // 1. Reanimate Hall
            hall.setServer(newServer);

            // 2. Reanimate Proxy
            for (PhasedProxyPlayer p : clients.values()) {
                p.resumeAfterServerCrash(newServer);
            }

            // 3. Reanimate Matches using Map<String, Room>
            Map<String, Room> roomsMap = hall.getRooms(); // La tua Map<String, Room>
            for (Room room : roomsMap.values()) {

                room.setServer(newServer);

                if (room instanceof PlayingRoom) {
                    PlayingRoom pRoom = (PlayingRoom) room;

                    new Thread(pRoom::runGame).start();
                    System.out.println("[RECOVERY] Match restarted " + pRoom.roomName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
