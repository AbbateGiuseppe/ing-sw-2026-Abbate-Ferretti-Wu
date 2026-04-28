package it.polimi.ingsw.gc49.rmi_socket.client.user_input_interfaces;

import it.polimi.ingsw.gc49.rmi_socket.virtualServers.VirtualGameServer;

public abstract class UserInputInterface {
    protected VirtualGameServer virtualGameServer;
    protected final String nickname; //<-- Will need this to reestablish a fallen connection

    public UserInputInterface ( VirtualGameServer virtualGameServer, String nickname ) {
        this.virtualGameServer = virtualGameServer;
        this.nickname = nickname;
    }

    public void runInput() throws Exception {
        System.out.println("Interfaccia vuota?!");
    }
}
