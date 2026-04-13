package it.polimi.ingsw.gc49.socket.server;

import it.polimi.ingsw.gc49.View.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.datapacket.COMMAND.Command;
import it.polimi.ingsw.gc49.datapacket.Datapacket;
import it.polimi.ingsw.gc49.datapacket.UPDATE_MODEL.MockupModelDatapacketable;
import it.polimi.ingsw.gc49.controller.massi.MassiController;
import it.polimi.ingsw.gc49.model.Game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class SocketHandlerServerside implements VirtualClientSocket {
    private final int clientIndex;
    private final MassiController controller;
    private final SocketExecutorServerside server;
    private final ObjectInputStream input;
    private final ObjectOutputStream output;
    private volatile boolean running;

    public SocketHandlerServerside ( int clientIndex, SocketExecutorServerside server, ObjectInputStream input, ObjectOutputStream output) {
        this.clientIndex = clientIndex;
        controller = new MassiController(clientIndex);
        this.server = server;
        this.input = input;
        this.output = output;
    }

    public void connectControllerToModel ( Game game ) {
        controller.connectModel(game);
    }

    //TODO: runVirtualView??!
    public void runVirtualView() throws IOException, ClassNotFoundException {
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
    public void initializeClientModel ( MockupGame mockupGame ) throws Exception {
        output.writeObject(mockupGame);
        output.flush();
    }

    @Override
    public void updateClientModel ( List<MockupModelDatapacketable> updatesList ) throws Exception {
        output.writeObject(updatesList);
        output.flush();
    }

    @Override
    public void reportError(String details) throws Exception {
        output.writeObject(details);
        output.flush();
    }
}
