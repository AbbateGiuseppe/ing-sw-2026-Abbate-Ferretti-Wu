package it.polimi.ingsw.gc49.rmi_socket.server.connectors;

import it.polimi.ingsw.gc49.View.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.controller.massi.MassiController;
import it.polimi.ingsw.gc49.datapacket.COMMAND.Command;
import it.polimi.ingsw.gc49.datapacket.Datapacket;
import it.polimi.ingsw.gc49.datapacket.UPDATE_MODEL.MockupModelDatapacketable;
import it.polimi.ingsw.gc49.datapacket.UPDATE_MODEL.UpdateModel;
import it.polimi.ingsw.gc49.model.Game;
import it.polimi.ingsw.gc49.rmi_socket.client.stub.VirtualServerSocket;
import it.polimi.ingsw.gc49.rmi_socket.server.ServerMultiplexer;
import it.polimi.ingsw.gc49.rmi_socket.server.VirtualClientSocket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketException;
import java.util.List;

public class SocketConnectorServerSide extends ConnectorServerSide implements VirtualClientSocket {
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
                        case COMMAND -> controller.executeCommand( (Command) datapacket );
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
    public void initializeClientModel ( MockupGame mockupGame ) throws Exception {
        output.writeObject(mockupGame);
        output.flush();
    }

    @Override
    public void updateClientModel ( UpdateModel updateModel ) throws Exception {
        output.writeObject(updateModel);
        output.flush();
    }

    @Override
    public void reportError(String details) throws Exception {
        output.writeObject(details);
        output.flush();
    }
}
