package it.polimi.ingsw.gc49.rmi_socket.client;

import it.polimi.ingsw.gc49.View.mockupModel.MockupGame;

public abstract class ClientSide {
    protected final String nickname;
    protected MockupGame mockupGame;

    public ClientSide( String nickname ) {
        this.nickname = nickname;
    }
}
