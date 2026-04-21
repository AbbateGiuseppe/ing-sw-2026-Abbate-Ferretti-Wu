package it.polimi.ingsw.gc49.rmi_socket.client;

import it.polimi.ingsw.gc49.View.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.controller.massi.MassiPlayerActionEnum;
import it.polimi.ingsw.gc49.datapacket.COMMAND.Command;
import it.polimi.ingsw.gc49.datapacket.UPDATE_MODEL.UpdateModel;
import it.polimi.ingsw.gc49.datapacket.UPDATE_MODEL.UpdateModelElement;
import it.polimi.ingsw.gc49.rmi_socket.client.user_input_interfaces.TextTerminal;
import it.polimi.ingsw.gc49.rmi_socket.client.user_input_interfaces.UserInputInterface;
import it.polimi.ingsw.gc49.rmi_socket.server.ServerMultiplexer;
import it.polimi.ingsw.gc49.rmi_socket.server.VirtualServerRmi;
import it.polimi.ingsw.gc49.rmi_socket.server.FactoryServiceRmi;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;

public class RmiClientSide extends ClientSide implements VirtualClientRmi {
    VirtualServerRmi server;
    private static final String mainServer = ServerMultiplexer.mainServer;

    public RmiClientSide( String nickname, VirtualServerRmi server ) throws RemoteException {
        super(nickname);
        this.server = server;
    }

    public static void main( String[] args ) throws RemoteException, NotBoundException {
        int port = ServerMultiplexer.portRmi;
        String nickname = args[0];
        String host = null; //args[1];

        try {
            Registry registry = LocateRegistry.getRegistry(host, port); //null means "localhost"
            VirtualServerRmi server = ((FactoryServiceRmi) registry.lookup(mainServer)).connectPlayerRmi(nickname);
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
            inputInterface = new TextTerminal(server); //connect interface to server proxy
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
    public void initializeClientModel ( MockupGame mockupGame ) throws RemoteException {

    }

    @Override
    public void updateClientModel ( UpdateModel updateModel ) throws RemoteException {

    }

    @Override
    public void reportError ( String details ) throws RemoteException {

    }
}
