package it.polimi.ingsw.gc49.rmi_socket.client;

import it.polimi.ingsw.gc49.View.mockupHall.MockupHall;
import it.polimi.ingsw.gc49.View.mockupHall.MockupRoom;
import it.polimi.ingsw.gc49.View.mockupModel.MockupGame;
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
    private PhasedProxyServer server;
    private ApplicationPhase currentApplicationPhase = ApplicationPhase.HALL;
    public final String nickname;
    private MockupGame mockupGame;
    private MockupHall mockupHall;
    private MockupRoom mockupRoom;
    private static final String mainServer = ServerMultiplexer.mainServer;
    private volatile boolean running;

    public ClientApplication ( String nickname ) {
        this.nickname = nickname;
    }

    public static void main( String[] args ) throws Exception {

        Scanner scan = new Scanner(System.in);
        System.out.println("Inserisci il tuo nomignolo");
        System.out.print("> ");
        String nickname = scan.nextLine();
        System.out.println("Premere 1 per la connessione RMI, Premere 2 per la connessione socket");
        System.out.print("> ");
        int connectionChoice = scan.nextInt();

        if(connectionChoice == 1) { //RMI
            int port = ServerMultiplexer.portRmi;
            String host = null; //args[1];

            try {
                Registry registry = LocateRegistry.getRegistry(host, port); //null means "localhost"

                ClientApplication runnableClient = new ClientApplication(nickname); //creating the client

                PhasedProxyServer phasedProxyServer = new RmiProxyServer(runnableClient); //creating the proxy, client side

                //exporting the proxy
                VirtualClient clientStub = (VirtualClient) UnicastRemoteObject.exportObject( phasedProxyServer, 0 ); //0 is a dynamic way to handle multiple client ports.
                //gaining the proxy on the server side
                VirtualServer server = ((FactoryServiceRmi) registry.lookup(mainServer)).connectPlayerRmi(nickname, clientStub);

                System.out.println("Connessione riuscita.");

                //connecting the proxy on this side to the server one
                phasedProxyServer.finishInitialization(server, null, null);
                //connecting the client to his client side proxy
                runnableClient.setServer(phasedProxyServer);

                //start heartbeat
                phasedProxyServer.startHeartbeat(); //


                //running the client
                runnableClient.runRmi();

            } catch (RemoteException e) {
                System.out.println("Connessione fallita.");
                System.out.println("Il Serviente ha restituito un'eccezione: " + e);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(connectionChoice == 2) { //Socket
            int port = ServerMultiplexer.portSocket;
            String host = null;

            Socket serverSocket = new Socket(host, port);
            ObjectInputStream input = new ObjectInputStream(serverSocket.getInputStream());
            ObjectOutputStream output = new ObjectOutputStream(serverSocket.getOutputStream());

            output.writeObject(nickname);
            output.flush();

            Object authorisation = input.readObject();

            if(authorisation instanceof RuntimeException) { // Meglio Exception generica o Runtime
                System.out.println("Connessione fallita.");
                serverSocket.close();
                return;
            } else {

                //TODO: add listeners

                ClientApplication runnableClient = new ClientApplication(nickname);

                PhasedProxyServer phasedProxyServer = new SocketProxyServer(runnableClient);

                //connecting the proxy on this side to the server one
                phasedProxyServer.finishInitialization(null, input, output);
                //connecting the client to his client side proxy
                runnableClient.setServer(phasedProxyServer);
                //start heartbeat
                phasedProxyServer.startHeartbeat();

                //running the client
                runnableClient.runSocket();
            }
        }else{
            System.out.println("Scelta non valida: chiusura imminente.");
            return;
        }


    }

    private void runRmi() throws Exception {
        Scanner scan = new Scanner(System.in);
        System.out.println("Premere 1 per l'interfaccia testuale, Premere 2 per l'interfaccia grafica");
        System.out.print("> ");
        int interfaceChoice = scan.nextInt();

        UserInputInterface inputInterface;
        if(interfaceChoice == 1) {
            System.out.println("Avvio dell'interfaccia testuale...");
            inputInterface = new TextTerminal(server, nickname); //connect interface to server proxy
        }else if(interfaceChoice == 2) {
            System.out.println("Avvio dell'interfaccia grafica...");
            System.out.println("ERRORE: INTERFACCIA NON ANCORA REALIZZATA! Chiusura imminente...");
            return;
        }else{
            System.out.println("Scelta non valida: chiusura imminente.");
            return;
        }

        inputInterface.runInput(); //run interface
    }

    public void setServer ( PhasedProxyServer server ) {
        this.server = server;
    }

    public void runSocket () throws Exception {
        Scanner scan = new Scanner(System.in);
        System.out.println("Premere 1 per l'interfaccia testuale, Premere 2 per l'interfaccia grafica");
        System.out.print("> ");
        int interfaceChoice = scan.nextInt();

        UserInputInterface inputInterface;
        if(interfaceChoice == 1) {
            System.out.println("Avvio dell'interfaccia testuale...");
            inputInterface = new TextTerminal(server, nickname); //connect interface to server proxy
        }else if(interfaceChoice == 2) {
            System.out.println("Avvio dell'interfaccia grafica...");
            System.out.println("ERRORE: INTERFACCIA NON ANCORA REALIZZATA! Chiusura imminente...");
            return;
        }else{
            System.out.println("Scelta non valida: chiusura imminente.");
            return;
        }

        //run server connection (input/output)
        new Thread(() -> {
            try {
                server.runVirtualServer();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();

        //TODO: add input listening methods on clientside.

        inputInterface.runInput(); //run interface
    }

    //### Client general methods
    @Override
    public void changePhaseClient ( ChangePhasePacket changePhasePacket ) throws Exception {
        currentApplicationPhase = changePhasePacket.newPhase;
    }

    @Override
    public void receiveHeartbeat() throws RemoteException {
        if (server != null) {
            server.reportActivity();
        }

    }

    //### Game called methods
    @Override
    public void initializeClientModel ( InitializeModelPacket initializeModelPacket ) throws RemoteException {
        mockupGame = initializeModelPacket.mockupModel;
    }
    @Override
    public void updateClientModel ( UpdateModelPacket updateModelPacket ) throws RemoteException {
        if(mockupGame != null) {
            updateModelPacket.updateTheMockupModel(mockupGame);
        }
    }
    @Override
    public void reportError ( ErrorPacket errorPacket ) throws RemoteException {
        //TODO: implement error reporting on the client's interface.
    }

    //### Hall called methods
    @Override
    public void initializeClientHall ( InitializeHallPacket initializeHallPacket ) throws Exception {
        mockupHall = initializeHallPacket.mockupHall;
    }
    @Override
    public void updateClientHall ( UpdateHallPacket updateHallPacket ) throws Exception {
        mockupHall = updateHallPacket.newMockupHall;
    }

    //### Room called methods
    @Override
    public void initializeClientRoom ( InitializeRoomPacket initializeRoomPacket ) throws Exception {
        mockupRoom = initializeRoomPacket.mockupRoom;
    }
    @Override
    public void updateClientRoom ( UpdateRoomPacket updateRoomPacket ) throws Exception {
        mockupRoom = updateRoomPacket.newMockupRoom;
    }

}
