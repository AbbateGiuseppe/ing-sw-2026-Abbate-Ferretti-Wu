package it.polimi.ingsw.gc49.rmi_socket.client;

import it.polimi.ingsw.gc49.View.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.controller.massi.MassiPlayerActionEnum;
import it.polimi.ingsw.gc49.datapacket.COMMAND.Command;
import it.polimi.ingsw.gc49.datapacket.Datapacket;
import it.polimi.ingsw.gc49.datapacket.UPDATE_MODEL.MockupModelDatapacketable;
import it.polimi.ingsw.gc49.rmi_socket.server.VirtualClientSocket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class SocketClientSide implements VirtualClientSocket, VirtualServerSocket {
    private final String nickname;
    private MockupGame mockupGame;
    final ObjectInputStream input;
    final ObjectOutputStream output;
    private volatile boolean running;

    public SocketClientSide(String nickname, ObjectInputStream input, ObjectOutputStream output) {
        this.nickname = nickname;
        this.input = input;
        this.output = output;
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
            System.err.println("Il Serviente ha restituito un'eccezione: " + authorisation);
        } else {
            System.out.println("Il Serviente ha restituito: " + authorisation);

            //TODO: add listeners

            new SocketClientSide(nickname, socketRx, socketTx).run();
        }
    }

    private void run() throws Exception {
        new Thread(() -> {
            try {
                runVirtualServer();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }).start();
        //TODO: add input listening methods on clientside.
        runCli();
    }

    public void runVirtualServer() throws IOException, ClassNotFoundException {
        //TODO: runVirtualServer??!
        running = true;

        Datapacket datapacket;
        try {
            while (running) { //TODO: && !Thread.currentThread().isInterrupted()
                try {
                    datapacket = (Datapacket) input.readObject();

                    switch(datapacket.getDatapacketType()){
                        case UPDATE_MODEL -> stop(); //TODO: update local model
                    }
                }catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            stop();
        }
    }

    private void runCli() throws Exception {
        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            int command = scan.nextInt();

            if (command != 0) {
                this.sendCommand(new Command(MassiPlayerActionEnum.CHOOSE_OFFER, 40));
            } else {

            }
        }
    }

    public void stop() {
        running = false;
    }

    @Override
    public void initializeClientModel ( MockupGame mockupGame ) throws Exception {

    }

    @Override
    public void updateClientModel ( List<MockupModelDatapacketable> updatesList ) throws Exception {

    }

    @Override
    public void reportError ( String details ) throws Exception {

    }

    @Override
    public void sendCommand ( Command command ) throws Exception {
        output.writeObject(command);
        output.flush();
    }
}