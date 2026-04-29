package it.polimi.ingsw.gc49.rmi_socket.client;

import it.polimi.ingsw.gc49.View.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.datapacket.directedToClient.ANY_phase.CHANGE_PHASE.ChangePhasePacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.ANY_phase.ERROR.ErrorPacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.GAME_phase.INITIALIZE_MODEL.InitializeModelPacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.GAME_phase.UPDATE_MODEL.UpdateModelPacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.HALL_phase.HALL_CLIENT.INITIALIZE.HallClientInitializePacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.HALL_phase.HALL_CLIENT.UPDATE.HallClientUpdatePacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.ROOM_phase.ROOM_CLIENT.INITIALIZE.RoomClientInitializePacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.ROOM_phase.ROOM_CLIENT.UPDATE.RoomClientUpdatePacket;
import it.polimi.ingsw.gc49.rmi_socket.client.proxies.PhasedProxyServer;
import it.polimi.ingsw.gc49.rmi_socket.client.proxies.RmiProxyServer;
import it.polimi.ingsw.gc49.rmi_socket.client.proxies.SocketProxyServer;
import it.polimi.ingsw.gc49.rmi_socket.client.user_input_interfaces.TextTerminal;
import it.polimi.ingsw.gc49.rmi_socket.client.user_input_interfaces.UserInputInterface;
import it.polimi.ingsw.gc49.rmi_socket.server.FactoryServiceRmi;
import it.polimi.ingsw.gc49.rmi_socket.server.ServerMultiplexer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualClients.VirtualClient;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class ClientApplication implements VirtualClient {
    public PhasedProxyServer server;
    protected final String nickname;
    protected MockupGame mockupGame;
    protected static final String mainServer = ServerMultiplexer.mainServer;
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
            String host = null; //args[1];

            Socket serverSocket = new Socket(host, port);

            ObjectInputStream input = new ObjectInputStream(serverSocket.getInputStream());
            ObjectOutputStream output = new ObjectOutputStream(serverSocket.getOutputStream());
            //sends the nickname to authorise the connection
            output.writeObject(nickname);
            output.flush();
            //waits for authorisation
            Object authorisation = input.readObject();

            if(authorisation instanceof RuntimeException) {
                System.out.println("Connessione fallita.");
                System.err.println("Il Serviente ha restituito un'eccezione: " + authorisation);
            } else {
                System.out.println("Il Serviente ha restituito: " + authorisation);

                //TODO: add listeners

                ClientApplication runnableClient = new ClientApplication(nickname);

                PhasedProxyServer phasedProxyServer = new SocketProxyServer(runnableClient);

                //connecting the proxy on this side to the server one
                phasedProxyServer.finishInitialization(null, input, output);
                //connecting the client to his client side proxy
                runnableClient.setServer(phasedProxyServer);

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

    }

    //### Game called methods
    @Override
    public void initializeClientModel ( InitializeModelPacket initializeModelPacket ) throws RemoteException {

    }
    @Override
    public void updateClientModel ( UpdateModelPacket updateModelPacket ) throws RemoteException {

    }
    @Override
    public void reportError ( ErrorPacket errorPacket ) throws RemoteException {

    }

    //### Hall called methods
    @Override
    public void initializeClientHall ( HallClientInitializePacket hallClientInitializePacket ) throws Exception {

    }
    @Override
    public void updateClientHall ( HallClientUpdatePacket hallClientUpdatePacket ) throws Exception {

    }

    //### Room called methods
    @Override
    public void initializeClientRoom ( RoomClientInitializePacket roomClientInitializePacket ) throws Exception {

    }
    @Override
    public void updateClientRoom ( RoomClientUpdatePacket roomClientUpdatePacket ) throws Exception {

    }
}
