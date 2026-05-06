package it.polimi.ingsw.gc49.rmi_socket.client;

import it.polimi.ingsw.gc49.View.Mockup;
import it.polimi.ingsw.gc49.datapacket.directedToClient.CHANGE_PHASE.ChangePhasePacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.ERROR.ErrorPacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.INITIALIZE_MODEL.InitializeModelPacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.UPDATE_MODEL.UpdateModelPacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.INITIALIZE_HALL.InitializeHallPacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.UPDATE_HALL.UpdateHallPacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.INITIALIZE_ROOM.InitializeRoomPacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.UPDATE_ROOM.UpdateRoomPacket;
import it.polimi.ingsw.gc49.rmi_socket.client.proxies.PhasedProxyServer;
import it.polimi.ingsw.gc49.rmi_socket.client.proxies.RmiProxyServer;
import it.polimi.ingsw.gc49.rmi_socket.client.proxies.SocketProxyServer;
import it.polimi.ingsw.gc49.rmi_socket.client.user_input_interfaces.TextTerminal;
import it.polimi.ingsw.gc49.rmi_socket.client.user_input_interfaces.UserInputInterface;
import it.polimi.ingsw.gc49.rmi_socket.server.FactoryServiceRmi;
import it.polimi.ingsw.gc49.rmi_socket.server.ServerMultiplexer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualClients.VirtualClient;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class ClientApplication implements VirtualClient {
    private static PhasedProxyServer server;
    public final String nickname;
    private static final Mockup mockups = new Mockup();
    private static final String mainServer = ServerMultiplexer.mainServer;
    private static UserInputInterface inputInterface;
    private volatile boolean running;

    public ClientApplication ( String nickname ) {
        this.nickname = nickname;
    }

    public static void main ( String[] args ) throws Exception {

        inputInterface = chooseInputInterface();
        if (inputInterface != null) {
            Scanner scan = new Scanner(System.in);
            int connectionChoice;
            while(true) {
                try {
                    System.out.println("Premere 1 per la connessione RMI, Premere 2 per la connessione socket");
                    System.out.print("> ");
                    connectionChoice = Integer.parseInt(scan.nextLine());
                    if(connectionChoice == 1 || connectionChoice == 2) {
                        break;
                    }else{
                        System.out.println("SCEGLI UN NUMERO TRA 1 e 2! Riprova");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("NUMERO NON VALIDO! Riprova");
                }
            };
            System.out.println("Inserisci il tuo nomignolo");
            System.out.print("> ");
            String nickname = scan.nextLine();

            if (connectionChoice == 1) { //RMI
                int port = ServerMultiplexer.portRmi;
                String host = null; //args[1];

                try {
                    Registry registry = LocateRegistry.getRegistry(host, port); //null means "localhost"

                    ClientApplication runnableClient = new ClientApplication(nickname); //creating the client

                    PhasedProxyServer phasedProxyServer = new RmiProxyServer(runnableClient); //creating the proxy, client side

                    //exporting the proxy
                    VirtualClient clientStub = (VirtualClient) UnicastRemoteObject.exportObject(phasedProxyServer, 0); //0 is a dynamic way to handle multiple client ports.
                    //gaining the proxy on the server side
                    VirtualServer server = ((FactoryServiceRmi) registry.lookup(mainServer)).connectPlayerRmi(nickname, clientStub);

                    System.out.println("Connessione riuscita.");

                    //connecting the proxy on this side to the server one
                    phasedProxyServer.finishInitialization(server, null, null);

                    //connecting the interface to the server proxy
                    inputInterface.setVirtualServer(phasedProxyServer);
                    //connecting the client to his client side proxy
                    runnableClient.setServer(phasedProxyServer);

                    //running the client
                    runnableClient.run();

                } catch (RemoteException e) {
                    System.out.println("Connessione fallita.");
                    System.out.println("Il Serviente ha restituito un'eccezione: " + e);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (connectionChoice == 2) { //Socket
                int port = ServerMultiplexer.portSocket;
                String host = null; //args[1];

                Socket serverSocket = new Socket(host, port);

                ObjectInputStream input = new ObjectInputStream(serverSocket.getInputStream());
                ObjectOutputStream output = new ObjectOutputStream(serverSocket.getOutputStream());
                //sends the nickname to authorise the connection
                output.writeObject(nickname);
                output.flush();
                //waits for authorisation
                Object authorisation = input.readObject();

                if (authorisation instanceof RuntimeException) {
                    System.out.println("Connessione fallita.");
                    System.err.println("Il Serviente ha restituito un'eccezione: " + authorisation);
                    serverSocket.close();
                } else {
                    System.out.println("Il Serviente ha restituito: " + authorisation);

                    //TODO: add listeners

                    ClientApplication runnableClient = new ClientApplication(nickname);

                    PhasedProxyServer phasedProxyServer = new SocketProxyServer(runnableClient);

                    //connecting the proxy on this side to the server one
                    phasedProxyServer.finishInitialization(null, input, output);

                    //connecting the interface to the server proxy
                    inputInterface.setVirtualServer(phasedProxyServer);
                    //connecting the client to his client side proxy
                    runnableClient.setServer(phasedProxyServer);

                    //running the client
                    runnableClient.run();
                }
            } else {
                System.out.println("Scelta non valida: chiusura imminente.");
                return;
            }
        }


    }

    private void run() throws Exception {
        //was used for socket, now used for both, also starts heartbeat.
        new Thread(() -> {
            try {
                server.runVirtualServer();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();

        //TODO: add input listening methods on clientside.
        if(inputInterface != null) {
            inputInterface.runInput(); //run interface
        }
    }

    public void setServer ( PhasedProxyServer server ) {
        this.server = server;
    }

    private static UserInputInterface chooseInputInterface() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Premere 1 per l'interfaccia testuale, Premere 2 per l'interfaccia grafica");
        System.out.print("> ");
        String interfaceChoice = scan.nextLine();

        if(interfaceChoice.equals("1")) {
            System.out.println("Avvio dell'interfaccia testuale...");
            return inputInterface = new TextTerminal(server, mockups, ApplicationPhase.ANY); //connect interface to server proxy
        }else if(interfaceChoice.equals("2")) {
            System.out.println("Avvio dell'interfaccia grafica...");
            System.out.println("ERRORE: INTERFACCIA NON ANCORA REALIZZATA! Chiusura imminente...");
            return null;
        }else{
            System.out.println("Scelta non valida: chiusura imminente.");
            return null;
        }
    }

    //### Client general methods
    @Override
    public void changePhaseClient ( ChangePhasePacket changePhasePacket ) throws Exception {
        inputInterface.setCurrentPhase(changePhasePacket.newPhase);
    }

    //### Game called methods
    @Override
    public void initializeClientModel ( InitializeModelPacket initializeModelPacket ) throws RemoteException {
        mockups.setGame(initializeModelPacket.mockupModel);
    }
    @Override
    public void updateClientModel ( UpdateModelPacket updateModelPacket ) throws RemoteException {
        if(mockups.getGame() != null) {
            updateModelPacket.updateTheMockupModel(mockups.getGame());
        }
    }
    @Override
    public void reportError ( ErrorPacket errorPacket ) throws RemoteException {
        //TODO: implement error reporting on the client's interface.
    }

    //### Hall called methods
    @Override
    public void initializeClientHall ( InitializeHallPacket initializeHallPacket ) throws Exception {
        mockups.setHall(initializeHallPacket.mockupHall);
    }
    @Override
    public void updateClientHall ( UpdateHallPacket updateHallPacket ) throws Exception {
        mockups.setHall(updateHallPacket.newMockupHall);
    }

    //### Room called methods
    @Override
    public void initializeClientRoom ( InitializeRoomPacket initializeRoomPacket ) throws Exception {
        mockups.setRoom(initializeRoomPacket.mockupRoom);
    }
    @Override
    public void updateClientRoom ( UpdateRoomPacket updateRoomPacket ) throws Exception {
        mockups.setRoom(updateRoomPacket.newMockupRoom);
    }
}
