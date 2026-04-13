package it.polimi.ingsw.gc49.socket.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SocketExecutorServerside {
    final ServerSocket serverSocket;
    final List<SocketHandlerServerside> clients = new ArrayList<>();

    public SocketExecutorServerside ( ServerSocket serverSocket ) {
        this.serverSocket = serverSocket;
    }

    private void runServer() throws IOException {
        Socket clientSocket;
        while ((clientSocket = this.serverSocket.accept()) != null) {
            ObjectInputStream socketRx = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream socketTx = new ObjectOutputStream(clientSocket.getOutputStream());

            SocketHandlerServerside handler = new SocketHandlerServerside(
                    clients.size(),
                    this,
                    socketRx,
                    socketTx
            );

            synchronized (this.clients) {
                clients.add(handler);
            }

            new Thread(() -> {
                try {
                    handler.runVirtualView();
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
    }

    public static void main ( String[] args ) throws IOException {
        String host = args[0];
        int port = Integer.parseInt(args[1]);

        ServerSocket serverSocket = new ServerSocket(port);

        new SocketExecutorServerside(serverSocket).runServer();
    }
}
