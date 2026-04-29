package it.polimi.ingsw.gc49.rmi_socket.client;

import it.polimi.ingsw.gc49.datapacket.directedToClient.ANY_phase.CHANGE_PHASE.ChangePhasePacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.ANY_phase.ERROR.ErrorPacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.HALL_phase.HALL_CLIENT.INITIALIZE.HallClientInitializePacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.HALL_phase.HALL_CLIENT.UPDATE.HallClientUpdatePacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.GAME_phase.INITIALIZE_MODEL.InitializeModelPacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.ROOM_phase.ROOM_CLIENT.INITIALIZE.RoomClientInitializePacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.ROOM_phase.ROOM_CLIENT.UPDATE.RoomClientUpdatePacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.GAME_phase.UPDATE_MODEL.UpdateModelPacket;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualClients.VirtualClient;
import it.polimi.ingsw.gc49.rmi_socket.client.user_input_interfaces.TextTerminal;
import it.polimi.ingsw.gc49.rmi_socket.client.user_input_interfaces.UserInputInterface;
import it.polimi.ingsw.gc49.rmi_socket.server.ServerMultiplexer;
import it.polimi.ingsw.gc49.rmi_socket.server.FactoryServiceRmi;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualServer;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class RmiClientApplication extends ClientApplication implements VirtualClient {
    VirtualServer server;
    //protected static final String mainServer = ServerMultiplexer.mainServer;

    public RmiClientApplication ( String nickname ) throws RemoteException {
        super(nickname);
    }

    public static void main( String[] args ) throws RemoteException, NotBoundException {
        int port = ServerMultiplexer.portRmi;
        String nickname = args[0];
        String host = null; //args[1];

        try {
            Registry registry = LocateRegistry.getRegistry(host, port); //null means "localhost"

            RmiClientApplication runnableClient = new RmiClientApplication(nickname);

            VirtualClient clientStub = (VirtualClient) UnicastRemoteObject.exportObject( runnableClient, 0 ); //0 is a dynamic way to handle multiple client ports.
            VirtualServer server = ((FactoryServiceRmi) registry.lookup(mainServer)).connectPlayerRmi(nickname, clientStub);

            System.out.println("Connessione riuscita.");
            runnableClient.setServer(server);
            runnableClient.run();

        } catch (RemoteException e) {
            System.out.println("Connessione fallita.");
            System.out.println("Il Serviente ha restituito un'eccezione: " + e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void run() throws Exception {
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

    public void setServer ( VirtualServer server ) {
        this.server = server;
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
