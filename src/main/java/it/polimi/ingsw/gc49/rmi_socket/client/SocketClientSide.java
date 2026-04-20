package it.polimi.ingsw.gc49.rmi_socket.client;

import it.polimi.ingsw.gc49.View.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.datapacket.UPDATE_MODEL.UpdateModel;
import it.polimi.ingsw.gc49.datapacket.UPDATE_MODEL.UpdateModelElement;
import it.polimi.ingsw.gc49.rmi_socket.client.stub.SocketStub;
import it.polimi.ingsw.gc49.rmi_socket.client.user_input_interfaces.TextTerminal;
import it.polimi.ingsw.gc49.rmi_socket.client.user_input_interfaces.UserInputInterface;
import it.polimi.ingsw.gc49.rmi_socket.server.VirtualClientSocket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class SocketClientSide extends ClientSide implements VirtualClientSocket {
    private final SocketStub server;
    private MockupGame mockupGame;
    private volatile boolean running;

    public SocketClientSide(String nickname, SocketStub server) {
        super(nickname);
        this.server = server;
    }

    public static void main(String[] args) throws Exception {
        String host = null; //args[0];
        int port = Integer.parseInt(args[1]);
        String nickname = args[2];

        Socket serverSocket = new Socket(host, port);

        ObjectInputStream socketRx = new ObjectInputStream(serverSocket.getInputStream());
        ObjectOutputStream socketTx = new ObjectOutputStream(serverSocket.getOutputStream());
        //sends the nickname to authorise the connection
        socketTx.writeObject(nickname);
        socketTx.flush();
        //waits for authorisation
        Object authorisation = socketRx.readObject();

        if(authorisation instanceof RuntimeException) {
            System.out.println("Connessione fallita.");
            System.err.println("Il Serviente ha restituito un'eccezione: " + authorisation);
        } else {
            System.out.println("Il Serviente ha restituito: " + authorisation);

            //TODO: add listeners

            SocketStub server = new SocketStub(socketRx, socketTx);
            new SocketClientSide(nickname, server).run();
        }
    }

    private void run() throws Exception {
        new Thread(() -> {
            try {
                server.runVirtualServer(this);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }).start();
        //TODO: add input listening methods on clientside.

        Scanner scan = new Scanner(System.in);
        System.out.println("Premere 1 per l'interfaccia testuale, Premere 2 per l'interfaccia grafica");
        System.out.print("> ");
        int interfaceChoice = scan.nextInt();

        UserInputInterface inputInterface;
        if(interfaceChoice == 1) {
            System.out.println("Avvio dell'interfaccia testuale...");
            inputInterface = new TextTerminal(server);
        }else if(interfaceChoice == 2) {
            System.out.println("Avvio dell'interfaccia grafica...");
            System.out.println("ERRORE: INTERFACCIA NON ANCORA REALIZZATA! Chiusura imminente...");
            return;
        }else{
            System.out.println("Scelta non valida: chiusura imminente.");
            return;
        }

        inputInterface.runInput();
    }

    @Override
    public void initializeClientModel ( MockupGame mockupGame ) throws Exception {

    }

    @Override
    public void updateClientModel ( UpdateModel updateModel ) throws Exception {

    }

    @Override
    public void reportError ( String details ) throws Exception {

    }

}