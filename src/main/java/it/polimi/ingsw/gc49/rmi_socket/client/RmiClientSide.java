package it.polimi.ingsw.gc49.rmi_socket.client;

import it.polimi.ingsw.gc49.View.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.controller.massi.MassiPlayerActionEnum;
import it.polimi.ingsw.gc49.datapacket.COMMAND.Command;
import it.polimi.ingsw.gc49.datapacket.UPDATE_MODEL.UpdateModel;
import it.polimi.ingsw.gc49.datapacket.UPDATE_MODEL.UpdateModelElement;
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
    private MockupGame mockupGame;
    VirtualServerRmi server;
    private static final String mainServer = ServerMultiplexer.mainServer;

    public RmiClientSide( String nickname, VirtualServerRmi server ) throws RemoteException {
        super(nickname);
        this.server = server;
    }

    public static void main( String[] args ) throws RemoteException, NotBoundException {
        int port = Integer.parseInt(args[0]);
        String nickname = args[1];

        try {
            Registry registry = LocateRegistry.getRegistry(null, port); //null means "localhost"
            VirtualServerRmi server = ((FactoryServiceRmi) registry.lookup(mainServer)).connectPlayerRmi(nickname);
            System.out.println("Connessione riuscita.");
            new RmiClientSide(nickname, server).run();
        } catch (RemoteException e) {
            System.out.println("Connessione fallita.");
            System.out.println("Il Serviente ha restituito un'eccezione: " + e);
        }
    }

    private void run() throws RemoteException {

        this.runCli();//TODO: running.
    }

    private void runCli() throws RemoteException {
        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            int command = scan.nextInt();

            if (command != 0) {
                server.sendCommand(new Command(MassiPlayerActionEnum.CHOOSE_OFFER, command));
            } else {

            }
        }
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
