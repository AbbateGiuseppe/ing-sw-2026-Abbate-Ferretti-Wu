package it.polimi.ingsw.gc49.rmi_socket.client.stub;

import it.polimi.ingsw.gc49.datapacket.DISCONNECT.DisconnectPacket;
import it.polimi.ingsw.gc49.datapacket.RECONNECT.ReconnectPacket;
import it.polimi.ingsw.gc49.rmi_socket.VirtualClient;
import it.polimi.ingsw.gc49.datapacket.COMMAND.CommandPacket;
import it.polimi.ingsw.gc49.datapacket.Datapacket;
import it.polimi.ingsw.gc49.datapacket.UPDATE_MODEL.UpdateModelPacket;
import it.polimi.ingsw.gc49.rmi_socket.VirtualServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SocketStub implements VirtualServer {
    final ObjectInputStream input;
    final ObjectOutputStream output;
    private volatile boolean running;

    public SocketStub(ObjectInputStream input, ObjectOutputStream output) {
        this.input = input;
        this.output = output;
    }


    public void runVirtualServer(VirtualClient client) throws IOException, ClassNotFoundException {
        //TODO: runVirtualServer??!
        running = true;

        Datapacket datapacket;
        try {
            while (running) { //TODO: && !Thread.currentThread().isInterrupted()
                try {
                    datapacket = (Datapacket) input.readObject();

                    switch(datapacket.getDatapacketType()){
                        case UPDATE_MODEL -> client.updateClientModel((UpdateModelPacket) datapacket);
                    }
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            stop();
        }
    }

    public void stop() {
        running = false;
    }

    @Override
    public void sendCommand ( CommandPacket commandPacket ) throws Exception {
        output.writeObject(commandPacket);
        output.flush();
    }

    @Override
    public void disconnect ( DisconnectPacket disconnectPacket ) throws Exception {
        output.writeObject(disconnectPacket);
        output.flush();
    }

    @Override
    public void reconnect ( ReconnectPacket reconnectPacket ) throws Exception {
        output.writeObject(reconnectPacket);
        output.flush();
    }
}
