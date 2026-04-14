package it.polimi.ingsw.gc49.socket.client;

import java.io.*;
import java.net.Socket;

public class SocketExecutorClientside {
    private final SocketHandlerClientside serverHandler;

    public SocketExecutorClientside (ObjectInputStream input, ObjectOutputStream output) {
        serverHandler = new SocketHandlerClientside(this, input, output);
    }

    private void runClient() throws IOException {
        new Thread(() -> {
            try {
                serverHandler.runVirtualServer();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }).start();
        //TODO: add input listening methods on clientside.
    }

    public static void main(String[] args) throws IOException {
        String host = args[0];
        int port = Integer.parseInt(args[1]);

        Socket serverSocket = new Socket(host, port);

        ObjectInputStream socketRx = new ObjectInputStream(serverSocket.getInputStream());
        ObjectOutputStream socketTx = new ObjectOutputStream(serverSocket.getOutputStream());

        //TODO: add listeners

        new SocketExecutorClientside(socketRx, socketTx).runClient();
    }
}
