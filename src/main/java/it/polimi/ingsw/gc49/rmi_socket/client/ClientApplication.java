package it.polimi.ingsw.gc49.rmi_socket.client;

import it.polimi.ingsw.gc49.View.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.rmi_socket.server.ServerMultiplexer;

public abstract class ClientApplication {
    protected final String nickname;
    protected MockupGame mockupGame;
    protected static final String mainServer = ServerMultiplexer.mainServer;

    public ClientApplication ( String nickname ) {
        this.nickname = nickname;
    }
}
