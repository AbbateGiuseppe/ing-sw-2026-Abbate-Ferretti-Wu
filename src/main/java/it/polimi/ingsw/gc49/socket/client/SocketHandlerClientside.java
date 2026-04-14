package it.polimi.ingsw.gc49.socket.client;

import it.polimi.ingsw.gc49.datapacket.COMMAND.Command;
import it.polimi.ingsw.gc49.datapacket.Datapacket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SocketHandlerClientside implements VirtualServerSocket {
    final SocketExecutorClientside client;
    final ObjectInputStream input;
    final ObjectOutputStream output;
    private volatile boolean running;

    public SocketHandlerClientside ( final SocketExecutorClientside client, final ObjectInputStream input, final ObjectOutputStream output) {
        this.client = client;
        this.input = input;
        this.output = output;
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

    public void stop() {
        running = false;
    }

    @Override
    public void sendCommand ( Command command ) throws Exception {
        output.writeObject(command);
        output.flush();
    }
}
