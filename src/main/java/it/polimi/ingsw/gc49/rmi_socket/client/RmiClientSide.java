package it.polimi.ingsw.gc49.rmi_socket.client;

import it.polimi.ingsw.gc49.datapacket.ERROR.ErrorPacket;
import it.polimi.ingsw.gc49.datapacket.INITIALIZE_MODEL.InitializeModelPacket;
import it.polimi.ingsw.gc49.datapacket.UPDATE_MODEL.UpdateModelPacket;
import it.polimi.ingsw.gc49.rmi_socket.VirtualClient;
import it.polimi.ingsw.gc49.rmi_socket.VirtualServer;
import it.polimi.ingsw.gc49.rmi_socket.client.user_input_interfaces.TextTerminal;
import it.polimi.ingsw.gc49.rmi_socket.client.user_input_interfaces.UserInputInterface;
import it.polimi.ingsw.gc49.rmi_socket.server.ServerMultiplexer;
import it.polimi.ingsw.gc49.rmi_socket.server.FactoryServiceRmi;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class RmiClientSide extends ClientSide implements VirtualClient {
    VirtualServer server;
    private static final String mainServer = ServerMultiplexer.mainServer;

    public RmiClientSide( String nickname, VirtualServer server ) throws RemoteException {
        super(nickname);
        this.server = server;
    }

    public static void main( String[] args ) throws RemoteException, NotBoundException {
        int port = ServerMultiplexer.portRmi;
        String nickname = args[0];
        String host = null; //args[1];

        try {
            Registry registry = LocateRegistry.getRegistry(host, port); //null means "localhost"
            VirtualServer server = ((FactoryServiceRmi) registry.lookup(mainServer)).connectPlayerRmi(nickname);
            System.out.println("Connessione riuscita.");
            new RmiClientSide(nickname, server).run();
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

    @Override
    public void initializeClientModel ( InitializeModelPacket initializeModelPacket ) throws RemoteException {

    }

    @Override
    public void updateClientModel ( UpdateModelPacket updateModelPacket ) throws RemoteException {

    }

    @Override
    public void reportError ( ErrorPacket errorPacket ) throws RemoteException {

    }
}
