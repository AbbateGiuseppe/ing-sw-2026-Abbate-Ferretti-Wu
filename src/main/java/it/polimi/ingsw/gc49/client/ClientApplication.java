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
import it.polimi.ingsw.gc49.client.gui.GuiInputInterface;
import it.polimi.ingsw.gc49.client.gui.ImageAssetManager;
import it.polimi.ingsw.gc49.client.gui.MainFrame;
import it.polimi.ingsw.gc49.client.user_input_interfaces.TextTerminal;
import it.polimi.ingsw.gc49.client.user_input_interfaces.UserInputInterface;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.ANY_phase.DISCONNECT.DisconnectPacket;
import it.polimi.ingsw.gc49.server.FactoryServiceRmi;
import it.polimi.ingsw.gc49.server.ServerMultiplexer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualClients.VirtualClient;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualServer;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import javax.swing.SwingUtilities;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
    public enum ConnectionType {
        RMI,
        SOCKET
    }
    public String nickname;
    public static final Mockup mockups = new Mockup();
    private static final String mainServer = ServerMultiplexer.mainServer;
    private static UserInputInterface inputInterface;
    private MainFrame mainFrame;
    public static ItaEngString.Language localLanguage;
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
    public static final LineReader lineReader = LineReaderBuilder.builder().terminal(terminal).build();

    public ClientApplication ( String nickname ) {
        this.nickname = nickname;
    }

    private ClientApplication () {
        this.nickname = null;
    }

    public static void main ( String[] args ) {

        String host;

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

        inputInterface = chooseInputInterface(localLanguage);
        if (inputInterface != null) {

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

    private void run() throws Exception {
        //was used for socket, now used for both, also starts heartbeat.
        new Thread(() -> {
            try {
                server.runVirtualServer();
            } catch (SocketException e) {
                server.handleServerOffline();
            }
        }).start();

        if(inputInterface != null) {
            inputInterface.runInput(); //run interface
        }
    }

    public void setServer ( PhasedProxyServer server ) {
        this.server = server;
    }

    private static UserInputInterface chooseInputInterface( ItaEngString.Language localLanguage ) {
        ItaEngString CHOOSE_INTERFACE_01 = new ItaEngString("Premere 1 per l'interfaccia testuale, Premere 2 per l'interfaccia grafica", "Press 1 for the textual interface, Press 2 for the graphical interface");
        ItaEngString CHOOSE_INTERFACE_02 = new ItaEngString("Avvio dell'interfaccia testuale...", "Launching the textual interface...");
        ItaEngString CHOOSE_INTERFACE_03 = new ItaEngString("Avvio dell'interfaccia grafica...", "Launching the graphical interface...");
        ItaEngString CHOOSE_INTERFCAE_04 = new ItaEngString("SCEGLI UN NUMERO TRA 1 e 2! Riprova", "CHOOSE A NUMBER BETWEEN 1 and 2! Try again");
        ItaEngString CHOOSE_INTERFACE_05 = new ItaEngString("NUMERO NON VALIDO! Riprova", "INVALID NUMBER! Try again");


        while(true) {
            try{
                terminal.writer().println(CHOOSE_INTERFACE_01.print(localLanguage));
                int interfaceChoice = Integer.parseInt(lineReader.readLine("> "));

                if (interfaceChoice == 1) {
                    terminal.writer().println(CHOOSE_INTERFACE_02.print(localLanguage));
                    inputInterface = new TextTerminal(server, ApplicationPhase.ANY); //connect interface to server proxy
                    return inputInterface;
                } else if (interfaceChoice == 2) {
                    terminal.writer().println(CHOOSE_INTERFACE_03.print(localLanguage));
                    launchGui();
                    return null;
                } else {
                    terminal.writer().println(CHOOSE_INTERFCAE_04.print(localLanguage));
                }
            } catch (NumberFormatException e) {
                terminal.writer().println(CHOOSE_INTERFACE_05.print(localLanguage));
            }
        }
    }
    public static void launchGui() {
        SwingUtilities.invokeLater(() -> {
            ClientApplication application = new ClientApplication();
            application.initializeGui();
            application.showGui();
        });
    }
    private void initializeGui() {
        ImageAssetManager imageAssetManager = new ImageAssetManager();
        this.mainFrame = new MainFrame(this, mockups, imageAssetManager);
        inputInterface = new GuiInputInterface(null, ApplicationPhase.ANY, mainFrame);
        this.mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                disconnectBeforeExit();
            }
        });
        mainFrame.appendLog("Loaded " + imageAssetManager.getCardImageCount()
                + " card images from the customer graphics folder.");
        mainFrame.appendLog("First card asset: " + imageAssetManager.getFirstCardImagePath());
    }
    private void showGui() {
        mainFrame.setVisible(true);
    }



    private boolean isGuiMode() {
        return mainFrame != null;
    }


    public void guiConnect ( String host, String nickname, ConnectionType connectionType) {
        String normalizedHost = host == null || host.trim().isEmpty() ? "localhost" : host.trim();
        String normalizedNickname = nickname == null ? "" : nickname.trim();
        if (normalizedNickname.isEmpty()) {
            mainFrame.showError("Connection error", "Choose a nickname before connecting.");
            return;
        }

        mainFrame.setConnectionInProgress(true);
        mainFrame.appendLog("Connecting to " + normalizedHost + " as " + normalizedNickname + " with " + connectionType + "...");

        Thread connector = new Thread(() -> {
            try {
                if (connectionType == ConnectionType.RMI) {
                    connectRmi(normalizedHost, normalizedNickname);
                } else {
                    connectSocket(normalizedHost, normalizedNickname);
                }
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    mainFrame.setConnectionInProgress(false);
                    mainFrame.showError("Connection failed", e.getMessage() == null ? e.toString() : e.getMessage());
                });
            }
        }, "gui-connector");
        connector.setDaemon(true);
        connector.start();
    }

    private void connectRmi(String host, String nickname) throws Exception {
        Registry registry = LocateRegistry.getRegistry(host, ServerMultiplexer.portRmi);
        FactoryServiceRmi factory = (FactoryServiceRmi) registry.lookup(ServerMultiplexer.mainServer);
        if (!factory.ping()) {
            throw new IllegalStateException("Server did not answer ping.");
        }

        this.nickname = nickname;
        PhasedProxyServer phasedProxyServer = new RmiProxyServer(this) {
            @Override
            public void handleServerOffline() {
                running = false;
                scheduler.shutdownNow();
                ClientApplication.this.handleServerOffline();
            }
        };
        VirtualClient clientStub = (VirtualClient) UnicastRemoteObject.exportObject(phasedProxyServer, 0);
        VirtualServer serverStub = factory.connectPlayerRmi(nickname, clientStub);
        phasedProxyServer.finishInitialization(serverStub, null, null);
        finishGuiConnection(phasedProxyServer);
    }

    private void connectSocket(String host, String nickname) throws Exception {
        Socket socket = new Socket(host, ServerMultiplexer.portSocket);
        ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
        output.writeObject(nickname);
        output.flush();

        this.nickname = nickname;
        PhasedProxyServer phasedProxyServer = new SocketProxyServer(this) {
            @Override
            public void handleServerOffline() {
                running = false;
                scheduler.shutdownNow();
                ClientApplication.this.handleServerOffline();
            }
        };
        phasedProxyServer.finishInitialization(null, input, output);
        finishGuiConnection(phasedProxyServer);
    }

    private void finishGuiConnection(PhasedProxyServer phasedProxyServer) {
        server = phasedProxyServer;
        inputInterface.setVirtualServer(phasedProxyServer);
        mainFrame.setVirtualServer(phasedProxyServer);
        mainFrame.setNickname(nickname);

        Thread serverReader = new Thread(() -> {
            try {
                phasedProxyServer.runVirtualServer();
            } catch (SocketException e) {
                handleServerOffline();
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> mainFrame.showError("Connection error", e.getMessage() == null ? e.toString() : e.getMessage()));
            }
        }, "gui-server-reader");
        serverReader.setDaemon(true);
        serverReader.start();

        SwingUtilities.invokeLater(() -> {
            mainFrame.setConnectionInProgress(false);
            mainFrame.appendLog("Connected.");
        });
    }

    private void handleServerOffline() {
        if (!isGuiMode()) {
            return;
        }
        SwingUtilities.invokeLater(() -> {
            mainFrame.showError("Server offline", "The server stopped responding.");
            mainFrame.showConnect();
        });
    }

    private void disconnectBeforeExit() {
        if (server == null) {
            return;
        }
        try {
            server.disconnect(new DisconnectPacket());
        } catch (Exception ignored) {
            // Closing the window must not hang if the server is already gone.
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
        if (isGuiMode()) {
            SwingUtilities.invokeLater(() -> mainFrame.refreshGame(mockups.getGame()));
        } else {
            inputInterface.show();
        }
    }
    @Override
    public void updateClientModel ( UpdateModelPacket updateModelPacket ) throws RemoteException {
        if (isGuiMode()) {
            if (mockups.getGame() != null) {
                UpdateModelPacket.UpdateResult result = updateModelPacket.updateTheMockupModelAndGetResult(mockups.getGame(), inputInterface);
                SwingUtilities.invokeLater(() -> mainFrame.refreshChangedGame(mockups.getGame(), result.changedElements()));
            }
        } else {
            if(mockups.getGame() != null) {
                updateModelPacket.updateTheMockupModel(mockups.getGame(), inputInterface);
            }
            inputInterface.show();
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
        if (isGuiMode()) {
            SwingUtilities.invokeLater(() -> mainFrame.refreshHall(mockups.getHall()));
        } else {
            inputInterface.show();
        }
    }
    @Override
    public void updateClientHall ( UpdateHallPacket updateHallPacket ) {
        mockups.setHall(updateHallPacket.newMockupHall);
        if (isGuiMode()) {
            SwingUtilities.invokeLater(() -> mainFrame.refreshHall(mockups.getHall()));
        } else {
            inputInterface.show();
        }
    }

    //### Room called methods
    @Override
    public void initializeClientRoom ( InitializeRoomPacket initializeRoomPacket ) {
        mockups.setRoom(initializeRoomPacket.mockupRoom);
        if (isGuiMode()) {
            SwingUtilities.invokeLater(() -> mainFrame.refreshRoom(mockups.getRoom()));
        } else {
            inputInterface.show();
        }
    }
    @Override
    public void updateClientRoom ( UpdateRoomPacket updateRoomPacket ) {
        mockups.setRoom(updateRoomPacket.newMockupRoom);
        if (isGuiMode()) {
            SwingUtilities.invokeLater(() -> mainFrame.refreshRoom(mockups.getRoom()));
        } else {
            inputInterface.show();
        }
    }
}
