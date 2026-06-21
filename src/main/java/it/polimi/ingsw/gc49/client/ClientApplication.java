package it.polimi.ingsw.gc49.client;

import it.polimi.ingsw.gc49.ItaEngString;
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

/**
 * The {@code ClientApplication} class is the main entry point for the client side of the game.
 * It handles the initial setup (language selection, server IP, connection type, nickname),
 * establishes the connection to the server (via RMI or Socket), and initializes the user interface.
 * It also implements {@link VirtualClient} to receive direct callbacks and model updates from the server.
 */
public class ClientApplication implements VirtualClient {

    /** The proxy used to communicate with the server. */
    private static PhasedProxyServer server;

    /** The player's chosen nickname. */
    public final String nickname;

    /** The local repository holding the current state of the game, hall, or room views. */
    public static final Mockup mockups = new Mockup();

    /** The registered name of the main server in the RMI Registry. */
    private static final String mainServer = ServerMultiplexer.mainServer;

    /** The active user interface (CLI or GUI). */
    private static UserInputInterface inputInterface;

    /** The language currently selected by the user. */
    public static ItaEngString.Language localLanguage;

    /** The JLine terminal instance used for rich console I/O. */
    public static final Terminal terminal;


    /** 'r' stands for reply, 'e' stands for error */
    private final static ItaEngString CONNECTION_01, CONNECTION_01_r, CONNECTION_01_e_01, CONNECTION_01_e_02;
    private final static ItaEngString CONNECTION_02, CONNECTION_02_e_01, CONNECTION_02_e_02;
    private final static ItaEngString CONNECTION_03, CONNECTION_03_e_01, CONNECTION_03_e_02;
    private final static ItaEngString CONNECTION_04_e;


    static {
        try {
            terminal = TerminalBuilder.builder().system(true).provider("ffm").build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        CONNECTION_01 = new ItaEngString("Inserire l'indirizzo IP del serviente (lasciare vuoto se in locale): ", "Insert the IP address of the server (leave empty if it's local): ");
        CONNECTION_01_r = new ItaEngString("Serviente esistente..", "Server found..");
        CONNECTION_01_e_01 = new ItaEngString("??? COME HAI FFATTO?!", "??? HOW DID YOU DDO THAT?!");
        CONNECTION_01_e_02 = new ItaEngString("Serviente non trovato. Ritenta", "Server not found. Try again");
        CONNECTION_02 = new ItaEngString("Premere 1 per la connessione RMI, Premere 2 per la connessione socket", "Press 1 for RMI connection, Press 2 for socket connection");
        CONNECTION_02_e_01 = new ItaEngString("SCEGLI UN NUMERO TRA 1 e 2! Riprova", "CHOOSE A NUMBER BETWEEN 1 and 2! Try again");
        CONNECTION_02_e_02 = new ItaEngString("NUMERO NON VALIDO! Riprova", "INVALID NUMBER! Try again");
        CONNECTION_03 = new ItaEngString("Inserisci il tuo nomignolo", "Insert your nickname");
        CONNECTION_03_e_01 = new ItaEngString("IL NOMIGNOLO NON PUO' ESSERE VUOTO!", "THE NICKNAME CANNOT BE EMPTY!");
        CONNECTION_03_e_02 = new ItaEngString("NOMIGNOLO TROPPO LUNGO! Usa un massimo di 15 caratteri", "NICKNAME TOO LONG! Use a maximum of 15 characters");
        CONNECTION_04_e = new ItaEngString("Connessione fallita.", "Connection failed.");
    }

    /** The JLine reader used to parse user input from the console. */
    public static final LineReader lineReader = LineReaderBuilder.builder().terminal(terminal).build();

    /**
     * Constructs a new {@code ClientApplication} with the given nickname.
     *
     * @param nickname the unique identifier chosen by the user.
     */
    public ClientApplication ( String nickname ) {
        this.nickname = nickname;
    }


    /**
     * The main execution method for the client application.
     * Guides the user through a setup wizard to establish the server connection.
     *
     * @param args command-line arguments (currently unused).
     */
    public static void main ( String[] args ) {

        String host;
        // 1. Language Selection
        while (true) {
            try {
                String localLanguageChoice = lineReader.readLine("Scegli la lingua/Choose the language [ITA/ENG]: ").toUpperCase();
                if (localLanguageChoice.equals("ITA") || localLanguageChoice.equals("ENG")) {
                    localLanguage = ItaEngString.Language.valueOf(localLanguageChoice);
                    break;
                } else {
                   throw new RuntimeException("Lingua inesistente");
                }
            } catch (Exception e) {
                terminal.writer().println("Scelta non valida/Invalid choice");
            }
        }

        // 2. Interface Selection
        inputInterface = chooseInputInterface(localLanguage);
        if (inputInterface != null) {

            // 3. Server IP Setup
            while (true) {
                try {
                    host = lineReader.readLine(CONNECTION_01.print(localLanguage));
                    Registry registry = LocateRegistry.getRegistry(host, ServerMultiplexer.portRmi);
                    if (((FactoryServiceRmi) registry.lookup(mainServer)).ping()) {
                        terminal.writer().println(CONNECTION_01_r.print(localLanguage));
                        break;
                    } else {
                        terminal.writer().println(CONNECTION_01_e_01.print(localLanguage));
                        throw new RuntimeException("???");
                    }
                } catch (Exception e) {
                    terminal.writer().println(CONNECTION_01_e_02.print(localLanguage));
                }
            }

            // 4. Connection Protocol Selection
            int connectionChoice;
            while (true) {
                try {
                    terminal.writer().println(CONNECTION_02.print(localLanguage));
                    connectionChoice = Integer.parseInt(lineReader.readLine("> "));
                    if (connectionChoice == 1 || connectionChoice == 2) {
                        break;
                    } else {
                        terminal.writer().println(CONNECTION_02_e_01.print(localLanguage));
                    }
                } catch (NumberFormatException e) {
                    terminal.writer().println(CONNECTION_02_e_02.print(localLanguage));
                }
            }
            // 5. Nickname Selection
            terminal.writer().println(CONNECTION_03.print(localLanguage));
            String nickname;
            while (true) {
                nickname = lineReader.readLine("> ");
                if ( nickname.length() <= 15 ) {
                    if (!nickname.isEmpty()) {
                        break;
                    } else {
                        terminal.writer().println(CONNECTION_03_e_01.print(localLanguage));
                    }
                } else {
                    terminal.writer().println(CONNECTION_03_e_02.print(localLanguage));
                }
            }
            inputInterface.setNickname(nickname);

            // 6. Connecting to the Server
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
                terminal.writer().println(CONNECTION_04_e.print(localLanguage));
            }
        }


    }

    /**
     * Starts the client application loop.
     * Triggers the virtual server listener (which handles incoming packets and heartbeats)
     * on a new thread, and then launches the main input interface for the user.
     *
     * @throws Exception if an initialization error occurs.
     */
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

    /**
     * Binds the application to the initialized server proxy.
     *
     * @param server the {@link PhasedProxyServer} managing the connection.
     */
    public void setServer ( PhasedProxyServer server ) {
        this.server = server;
    }


    /**
     * Prompts the user to select their preferred user interface (Text Terminal or GUI).
     *
     * @param localLanguage the language previously selected by the user.
     * @return the chosen {@link UserInputInterface}.
     */
    private static UserInputInterface chooseInputInterface( ItaEngString.Language localLanguage ) {
        ItaEngString CHOOSE_INTERFACE_01 = new ItaEngString("Premere 1 per l'interfaccia testuale, Premere 2 per l'interfaccia grafica", "Press 1 for the textual interface, Press 2 for the graphical interface");
        ItaEngString CHOOSE_INTERFACE_02 = new ItaEngString("Avvio dell'interfaccia testuale...", "Launching the textual interface...");
        ItaEngString CHOOSE_INTERFACE_03 = new ItaEngString("ERRORE: INTERFACCIA NON ANCORA REALIZZATA! Chiusura imminente...", "ERROR: INTERFACE NOT YET IMPLEMENTED! Closure imminent...");
        ItaEngString CHOOSE_INTERFCAE_04 = new ItaEngString("SCEGLI UN NUMERO TRA 1 e 2! Riprova", "CHOOSE A NUMBER BETWEEN 1 and 2! Try again");
        ItaEngString CHOOSE_INTERFACE_05 = new ItaEngString("NUMERO NON VALIDO! Riprova", "INVALID NUMBER! Try again");


        while(true) {
            try{
                terminal.writer().println(CHOOSE_INTERFACE_01.print(localLanguage));
                int interfaceChoice = Integer.parseInt(lineReader.readLine("> "));

                if (interfaceChoice == 1) {
                    terminal.writer().println("");
                    inputInterface = new TextTerminal(server, ApplicationPhase.ANY); //connect interface to server proxy
                    return inputInterface;
                } else if (interfaceChoice == 2) {
                    terminal.writer().println(CHOOSE_INTERFACE_02.print(localLanguage));
                    terminal.writer().println(CHOOSE_INTERFACE_03.print(localLanguage));
                    System.exit(0);
                    return null;
                } else {
                    terminal.writer().println(CHOOSE_INTERFCAE_04.print(localLanguage));
                }
            } catch (NumberFormatException e) {
                terminal.writer().println(CHOOSE_INTERFACE_05.print(localLanguage));
            }
        }
    }


    // ============================================================
    // ### Client general methods
    // ============================================================

    /**
     * Updates the application's local phase (e.g., from HALL to ROOM to GAME).
     *
     * @param changePhasePacket the packet containing the new application phase.
     * @throws Exception if an error occurs while updating the interface.
     */
    @Override
    public void changePhaseClient ( ChangePhasePacket changePhasePacket ) throws Exception {
        inputInterface.setCurrentPhase(changePhasePacket.newPhase);
    }

    /**
     * Prints a direct string message received from the server to the user interface.
     *
     * @param stringPacket the packet containing the server's message.
     */
    @Override
    public void sendString ( StringPacket stringPacket ) {
        inputInterface.printString(stringPacket.string);
    }

    // ============================================================
    // ### Game called methods
    // ============================================================

    /**
     * Initializes the client's local game model mockup and refreshes the view.
     *
     * @param initializeModelPacket the packet containing the initial game state.
     * @throws RemoteException if a network communication error occurs.
     */
    @Override
    public void initializeClientModel ( InitializeModelPacket initializeModelPacket ) throws RemoteException {
        mockups.setGame(initializeModelPacket.mockupModel);
        inputInterface.show();
    }

    /**
     * Applies a specific update to the client's existing game model mockup and refreshes the view.
     *
     * @param updateModelPacket the packet detailing the required updates.
     * @throws RemoteException if a network communication error occurs.
     */
    @Override
    public void updateClientModel ( UpdateModelPacket updateModelPacket ) throws RemoteException {
        if(mockups.getGame() != null) {
            updateModelPacket.updateTheMockupModel(mockups.getGame(), inputInterface);
        }
        inputInterface.show();
    }

    /**
     * Displays an in-game error notification to the user.
     *
     * @param errorPacket the packet containing the error details.
     * @throws RemoteException if a network communication error occurs.
     */
    @Override
    public void reportError ( ErrorPacket errorPacket ) throws RemoteException {
        inputInterface.printErrorPacket(errorPacket);
    }

    // ============================================================
    // ### Hall called methods
    // ============================================================

    /**
     * Initializes the client's local hall model mockup and refreshes the view.
     *
     * @param initializeHallPacket the packet containing the initial hall state.
     */
    @Override
    public void initializeClientHall ( InitializeHallPacket initializeHallPacket ) {
        mockups.setHall(initializeHallPacket.mockupHall);
        inputInterface.show();
    }

    /**
     * Completely replaces the local hall mockup with a fresh one from the server and refreshes the view.
     *
     * @param updateHallPacket the packet containing the updated hall state.
     */
    @Override
    public void updateClientHall ( UpdateHallPacket updateHallPacket ) {
        mockups.setHall(updateHallPacket.newMockupHall);
        inputInterface.show();
    }

    // ============================================================
    // ### Room called methods
    // ============================================================

    /**
     * Initializes the client's local waiting room model mockup and refreshes the view.
     *
     * @param initializeRoomPacket the packet containing the initial room state.
     */
    @Override
    public void initializeClientRoom ( InitializeRoomPacket initializeRoomPacket ) {
        mockups.setRoom(initializeRoomPacket.mockupRoom);
        inputInterface.show();
    }

    /**
     * Completely replaces the local room mockup with a fresh one from the server and refreshes the view.
     *
     * @param updateRoomPacket the packet containing the updated room state.
     */
    @Override
    public void updateClientRoom ( UpdateRoomPacket updateRoomPacket ) {
        mockups.setRoom(updateRoomPacket.newMockupRoom);
        inputInterface.show();
    }
}
