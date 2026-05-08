package it.polimi.ingsw.gc49.server;

import it.polimi.ingsw.gc49.rmi_socket.datapacket.STRING.StringPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.ANY_phase.DISCONNECT.DisconnectPacket;
import it.polimi.ingsw.gc49.server.proxies.PhasedProxyPlayer;
import it.polimi.ingsw.gc49.server.proxies.RmiProxyPlayer;
import it.polimi.ingsw.gc49.server.proxies.SocketProxyPlayer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.Disconnectable;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualClients.VirtualClient;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualServer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.adapters.VirtualHallServerAdapter;

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

public class ServerMultiplexer extends UnicastRemoteObject implements FactoryServiceRmi, Disconnectable {
    public static final String mainServer = "MesosMainServer";
    public static final int portSocket = 2001;
    public static final int portRmi = 2002;
    private ServerSocket serverSocket;
    private static final Map<String, PhasedProxyPlayer> clients = new HashMap<>();
    private static final Hall hall = new Hall();
    private final int port;

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
                throw new RuntimeException(e);
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

                proxy.sendString(new StringPacket("Connessione riuscita."));

                new Thread(() -> {
                    runVirtualClient(proxy);
                }).start();

                return (VirtualServer) UnicastRemoteObject.exportObject(proxy, port);

            } else {
                //finds the existing proxy and converts it to the newly chosen connection technology
                PhasedProxyPlayer existingProxy = clients.get(nickname).convertToRmi();
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

                    proxy.sendString(new StringPacket("Connessione riuscita."));

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
        synchronized (clients) {
            String disconnectedNickname = disconnectPacket.getSenderNickname();
            clients.remove(disconnectedNickname); //completely removes the player from the server list.
        }
    }
}
