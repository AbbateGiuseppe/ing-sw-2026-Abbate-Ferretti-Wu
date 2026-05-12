package it.polimi.ingsw.gc49.client;

import it.polimi.ingsw.gc49.client.view.Mockup;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.STRING.StringPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.CHANGE_PHASE.ChangePhasePacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.ERROR.ErrorPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.INITIALIZE_MODEL.InitializeModelPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.UpdateModelPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.INITIALIZE_HALL.InitializeHallPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_HALL.UpdateHallPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.INITIALIZE_ROOM.InitializeRoomPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_ROOM.UpdateRoomPacket;
import it.polimi.ingsw.gc49.client.proxies.PhasedProxyServer;
import it.polimi.ingsw.gc49.client.proxies.RmiProxyServer;
import it.polimi.ingsw.gc49.client.proxies.SocketProxyServer;
import it.polimi.ingsw.gc49.client.user_input_interfaces.TextTerminal;
import it.polimi.ingsw.gc49.client.user_input_interfaces.UserInputInterface;
import it.polimi.ingsw.gc49.server.FactoryServiceRmi;
import it.polimi.ingsw.gc49.server.ServerMultiplexer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualClients.VirtualClient;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualServer;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ClientApplication implements VirtualClient {
    private static PhasedProxyServer server;
    public final String nickname;
    private static final Mockup mockups = new Mockup();
    private static final String mainServer = ServerMultiplexer.mainServer;
    private static UserInputInterface inputInterface;
    public static final Terminal terminal;
    static {
        try {
            terminal = TerminalBuilder.builder().system(true).provider("ffm").build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static final LineReader lineReader = LineReaderBuilder.builder().terminal(terminal).build();

    public ClientApplication ( String nickname ) {
        this.nickname = nickname;
    }

    public static void main ( String[] args ) {

        String host;
        inputInterface = chooseInputInterface();
        if (inputInterface != null) {

            while (true) {
                try {
                    host = lineReader.readLine("Inserire l'indirizzo IP del serviente (lasciare vuoto se in locale): ");
                    Registry registry = LocateRegistry.getRegistry(host, ServerMultiplexer.portRmi);
                    if ( ((FactoryServiceRmi)registry.lookup(mainServer)).ping() ){
                        terminal.writer().println("Serviente esistente..");
                        break;
                    } else {
                        terminal.writer().println("??? COME HAI FFATTO?!");
                        throw new RuntimeException("???");
                    }
                } catch (Exception e) {
                    terminal.writer().println("Serviente non trovato. Ritenta");
                }
            }

            int connectionChoice;
            while(true) {
                try {
                    terminal.writer().println("Premere 1 per la connessione RMI, Premere 2 per la connessione socket");
                    connectionChoice = Integer.parseInt(lineReader.readLine("> "));
                    if(connectionChoice == 1 || connectionChoice == 2) {
                        break;
                    }else{
                        terminal.writer().println("SCEGLI UN NUMERO TRA 1 e 2! Riprova");
                    }
                } catch (NumberFormatException e) {
                    terminal.writer().println("NUMERO NON VALIDO! Riprova");
                }
            }
            terminal.writer().println("Inserisci il tuo nomignolo");
            String nickname = lineReader.readLine("> ");
            inputInterface.setNickname(nickname);

            try {
                if (connectionChoice == 1) { //RMI
                    int port = ServerMultiplexer.portRmi;

                    Registry registry = LocateRegistry.getRegistry(host, port); //null means "localhost"

                    ClientApplication runnableClient = new ClientApplication(nickname); //creating the client

                    PhasedProxyServer phasedProxyServer = new RmiProxyServer(runnableClient); //creating the proxy, client side

                    //exporting the proxy
                    VirtualClient clientStub = (VirtualClient) UnicastRemoteObject.exportObject(phasedProxyServer, 0); //0 is a dynamic way to handle multiple client ports.
                    //gaining the proxy on the server side
                    VirtualServer server = ((FactoryServiceRmi) registry.lookup(mainServer)).connectPlayerRmi(nickname, clientStub);

                    //connecting the proxy on this side to the server one
                    phasedProxyServer.finishInitialization(server, null, null);

                    //connecting the interface to the server proxy
                    inputInterface.setVirtualServer(phasedProxyServer);
                    //connecting the client to his client side proxy
                    runnableClient.setServer(phasedProxyServer);

                    //running the client
                    runnableClient.run();


                } else { //Socket, connectionChoice == 2
                    int port = ServerMultiplexer.portSocket;

                    Socket serverSocket = new Socket(host, port);

                    ObjectInputStream input = new ObjectInputStream(serverSocket.getInputStream());
                    ObjectOutputStream output = new ObjectOutputStream(serverSocket.getOutputStream());
                    //sends the nickname to authorise the connection
                    output.writeObject(nickname);
                    output.flush();

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
            } catch (Exception e) {
                terminal.writer().println("Connessione fallita.");
            }
        }


    }

    private void run() throws Exception {
        //was used for socket, now used for both, also starts heartbeat.
        new Thread(() -> {
            try {
                server.runVirtualServer();
            } catch (SocketException e) {
                server.handleServerOffline();
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

        while(true) {
            try{
                terminal.writer().println("Premere 1 per l'interfaccia testuale, Premere 2 per l'interfaccia grafica");
                int interfaceChoice = Integer.parseInt(lineReader.readLine("> "));

                if (interfaceChoice == 1) {
                    terminal.writer().println("Avvio dell'interfaccia testuale...");
                    return inputInterface = new TextTerminal(server, mockups, ApplicationPhase.ANY); //connect interface to server proxy
                } else if (interfaceChoice == 2) {
                    terminal.writer().println("Avvio dell'interfaccia grafica...");
                    terminal.writer().println("ERRORE: INTERFACCIA NON ANCORA REALIZZATA! Chiusura imminente...");
                    System.exit(0);
                    return null;
                } else {
                    terminal.writer().println("SCEGLI UN NUMERO TRA 1 e 2! Riprova");
                }
            } catch (NumberFormatException e) {
                terminal.writer().println("NUMERO NON VALIDO! Riprova");
            }
        }
    }

    //### Client general methods
    @Override
    public void changePhaseClient ( ChangePhasePacket changePhasePacket ) throws Exception {
        inputInterface.setCurrentPhase(changePhasePacket.newPhase);
    }
    @Override
    public void sendString ( StringPacket stringPacket ) {
        inputInterface.printString(stringPacket.string);
    }

    //### Game called methods
    @Override
    public void initializeClientModel ( InitializeModelPacket initializeModelPacket ) throws RemoteException {
        mockups.setGame(initializeModelPacket.mockupModel);
    }
    @Override
    public void updateClientModel ( UpdateModelPacket updateModelPacket ) throws RemoteException {
        if(mockups.getGame() != null) {
            updateModelPacket.updateTheMockupModel(mockups.getGame(), inputInterface);
        }
    }
    @Override
    public void reportError ( ErrorPacket errorPacket ) throws RemoteException {
        inputInterface.printErrorPacket(errorPacket);
    }

    //### Hall called methods
    @Override
    public void initializeClientHall ( InitializeHallPacket initializeHallPacket ) {
        mockups.setHall(initializeHallPacket.mockupHall);
    }
    @Override
    public void updateClientHall ( UpdateHallPacket updateHallPacket ) {
        mockups.setHall(updateHallPacket.newMockupHall);
    }

    //### Room called methods
    @Override
    public void initializeClientRoom ( InitializeRoomPacket initializeRoomPacket ) {
        mockups.setRoom(initializeRoomPacket.mockupRoom);
    }
    @Override
    public void updateClientRoom ( UpdateRoomPacket updateRoomPacket ) {
        mockups.setRoom(updateRoomPacket.newMockupRoom);
    }
}
