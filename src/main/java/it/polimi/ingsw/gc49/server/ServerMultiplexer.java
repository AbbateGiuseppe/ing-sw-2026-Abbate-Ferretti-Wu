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

public class ServerMultiplexer extends UnicastRemoteObject implements FactoryServiceRmi, Disconnectable {
    public static final String mainServer = "MesosMainServer";
    public static final int portSocket = 2001;
    public static final int portRmi = 2002;
    private transient ServerSocket serverSocket;
    private static final Map<String, PhasedProxyPlayer> clients = new HashMap<>();
    private static Hall hall = new Hall();
    private final int port;
    private VirtualServerAdapter recoverRoom;
    // Executor per gestire i salvataggi in background
    private static final ExecutorService persistenceExecutor = Executors.newSingleThreadExecutor();

    /**
     * Constructor for rmi server
     */
    public ServerMultiplexer (int port) throws RemoteException {
        super();
        this.port = port;
        hall.setServer(this);
    }

    /**
     * Constructor for socket server
     * @param serverSocket, the serverSocket used to listen to new sockets connections.
     */
    public ServerMultiplexer(int port, ServerSocket serverSocket) throws RemoteException {
        super();
        this.port = port;
        this.serverSocket = serverSocket;
    }

    public static void main ( String[] args ) {
        // Ci serve un'istanza per "rianimare" i campi transient (il riferimento al server)
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
            InetAddress localHost = InetAddress.getLocalHost();
            System.out.println("Local IP Address: " + localHost.getHostAddress());
        } catch (UnknownHostException e) {
            System.out.println("Could not get local hostname: " + e.getMessage());
        }

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
                throw new RuntimeException(e);
            }
        }).start();
    }

    @Override
    public boolean ping() {
        return true;
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

                proxy.sendString(new StringPacket("Connessione riuscita."));

                new Thread(() -> {
                    runVirtualClient(proxy);
                }).start();

                return (VirtualServer) UnicastRemoteObject.exportObject(proxy, port);

            } else {
                //finds the existing proxy and converts it to the newly chosen connection technology
                PhasedProxyPlayer existingProxy = clients.get(nickname).convertToSocket();
                clients.remove(nickname);
                clients.put(nickname, existingProxy);

                if (existingProxy.isConnected()) {
                    throw new RemoteException("A player with such a nickname is already connected. Please, change it.");
                } else {
                    existingProxy.reconnect(clientStub, null, null);
                    System.out.println(existingProxy.nickname + " is reconnected");

                    existingProxy.sendString(new StringPacket("Riconnessione riuscita."));

                    new Thread(() -> {
                        runVirtualClient(existingProxy);
                    }).start();

                    return (VirtualServer) UnicastRemoteObject.exportObject(existingProxy, port);
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

                    proxy.sendString(new StringPacket("Connected Successfully."));

                    new Thread(() -> {
                        runVirtualClient(proxy);
                    }).start();

                    clients.put(nickname, proxy); //store the player in the clients-list.
                    hall.enterPlayer(proxy); //enter the player into the hall
                } else {
                    //finds the existing proxy and converts it to the newly chosen connection technology
                    PhasedProxyPlayer existingProxy = clients.get(nickname).convertToSocket();
                    clients.remove(nickname);
                    clients.put(nickname, existingProxy);

                    if (existingProxy.isConnected()) {
                        socketOutput.writeObject(new StringPacket("A player with such a nickname is already connected. Please, change it."));
                        socketOutput.flush();
                        return;
                    } else {
                        existingProxy.reconnect(null, socketInput, socketOutput);

                        System.out.println(existingProxy.nickname + " is reconnected");

                        existingProxy.sendString(new StringPacket("Riconnessione riuscita."));

                        new Thread(() -> {
                            runVirtualClient(existingProxy);
                        }).start();
                    }
                }
            }
        }
    }

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

    @Override
    public void disconnect ( DisconnectPacket disconnectPacket ) throws Exception {
    }

    //salvataggio stati
    public static void saveStateAsync() {
        // Chiama il metodo sincrono che scrive su file
        persistenceExecutor.submit(ServerMultiplexer::saveState);
    }

    public static synchronized void saveState() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("server_state.ser"))) {
            oos.writeObject(hall);
            oos.writeObject(clients);
            System.out.println("[PERSISTENCE] Snapshot saved.");
        } catch (IOException e) {
            System.err.println("[PERSISTENCE] Controlla se tutto è Serializable: " + e.getMessage());
        }
    }

    private static void loadState(ServerMultiplexer newServer) {
        File file = new File("server_state.ser");
        if (!file.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            hall = (Hall) ois.readObject();
            Map<String, PhasedProxyPlayer> savedClients = (Map<String, PhasedProxyPlayer>) ois.readObject();
            clients.putAll(savedClients);

            // 1. Rianimiamo la Hall
            hall.setServer(newServer);

            // 2. Rianimiamo i Proxy
            for (PhasedProxyPlayer p : clients.values()) {
                p.resumeAfterServerCrash(newServer);
            }

            // 3. Rianimiamo le Partite usando la Map<String, Room>
            Map<String, Room> roomsMap = hall.getRooms(); // La tua Map<String, Room>
            for (Room room : roomsMap.values()) {
                // Iniettiamo il nuovo server in ogni stanza (era transient)
                room.setServer(newServer);

                if (room instanceof PlayingRoom) {
                    PlayingRoom pRoom = (PlayingRoom) room;
                    // Facciamo ripartire il gameLoop() in un nuovo thread
                    new Thread(pRoom::runGame).start();
                    System.out.println("[RECOVERY] Match restarted " + pRoom.roomName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
