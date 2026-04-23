package it.polimi.ingsw.gc49.rmi_socket.server.connectors;

import it.polimi.ingsw.gc49.controller.massi.MassiController;
import it.polimi.ingsw.gc49.datapacket.COMMAND.CommandPacket;
import it.polimi.ingsw.gc49.datapacket.Datapacket;
import it.polimi.ingsw.gc49.datapacket.ERROR.ErrorPacket;
import it.polimi.ingsw.gc49.datapacket.INITIALIZE_MODEL.InitializeModelPacket;
import it.polimi.ingsw.gc49.datapacket.UPDATE_MODEL.UpdateModelPacket;
import it.polimi.ingsw.gc49.model.Game;
import it.polimi.ingsw.gc49.rmi_socket.VirtualClient;
import it.polimi.ingsw.gc49.rmi_socket.server.ServerMultiplexer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketException;

public class SocketConnectorServerSide extends ConnectorServerSide implements VirtualClient {
    private final ObjectInputStream input;
    private final ObjectOutputStream output;
    private volatile boolean running;

    public SocketConnectorServerSide ( int clientLocalIndex, MassiController controller,
                                       ServerMultiplexer server, String nickname,
                                       ObjectInputStream input, ObjectOutputStream output ) {
        super( clientLocalIndex, controller, server, nickname );
        this.input = input;
        this.output = output;
    }

    public void connectControllerToModel ( Game game ) {
        controller.connectModel(game);
    }

    //TODO: runVirtualClient??!
    public void runVirtualClient() throws IOException, ClassNotFoundException {
        running = true;

        Datapacket datapacket;

        try {
            while (running) { //TODO: && !Thread.currentThread().isInterrupted()
                try {
                    datapacket = (Datapacket) input.readObject();

                    switch(datapacket.getDatapacketType()){
                        case COMMAND -> controller.executeCommand( (CommandPacket) datapacket );
                    }
                }catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }catch (SocketException e) {
            throw new SocketException(e);
        }finally {
            stop();
        }
    }

    public void stop() {
        running = false;
    }

    @Override
    public void initializeClientModel ( InitializeModelPacket initializeModelPacket ) throws Exception {
        output.writeObject(initializeModelPacket);
        output.flush();
    }

    @Override
    public void updateClientModel ( UpdateModelPacket updateModelPacket ) throws Exception {
        output.writeObject(updateModelPacket);
        output.flush();
    }

    @Override
    public void reportError ( ErrorPacket errorPacket ) throws Exception {
        output.writeObject(errorPacket);
        output.flush();
    }
}
