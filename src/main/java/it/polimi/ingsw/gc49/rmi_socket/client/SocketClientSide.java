package it.polimi.ingsw.gc49.rmi_socket.client;

import it.polimi.ingsw.gc49.datapacket.ERROR.ErrorPacket;
import it.polimi.ingsw.gc49.datapacket.INITIALIZE_MODEL.InitializeModelPacket;
import it.polimi.ingsw.gc49.datapacket.UPDATE_MODEL.UpdateModelPacket;
import it.polimi.ingsw.gc49.rmi_socket.VirtualClient;
import it.polimi.ingsw.gc49.rmi_socket.client.stub.SocketStub;
import it.polimi.ingsw.gc49.rmi_socket.client.user_input_interfaces.TextTerminal;
import it.polimi.ingsw.gc49.rmi_socket.client.user_input_interfaces.UserInputInterface;
import it.polimi.ingsw.gc49.rmi_socket.server.ServerMultiplexer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class SocketClientSide extends ClientSide implements VirtualClient {
    private final SocketStub server;
    private volatile boolean running;

    public SocketClientSide(String nickname, SocketStub server) {
        super(nickname);
        this.server = server;
    }

    public static void main(String[] args) throws Exception {
        int port = ServerMultiplexer.portSocket;
        String nickname = args[0];
        String host = null; //args[1];

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
                server.runVirtualServer(this);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }).start();

        //TODO: add input listening methods on clientside.

        inputInterface.runInput(); //run interface
    }

    @Override
    public void initializeClientModel ( InitializeModelPacket initializeModelPacket ) throws Exception {

    }

    @Override
    public void updateClientModel ( UpdateModelPacket updateModelPacket ) throws Exception {

    }

    @Override
    public void reportError ( ErrorPacket errorPacket ) throws Exception {

    }

}