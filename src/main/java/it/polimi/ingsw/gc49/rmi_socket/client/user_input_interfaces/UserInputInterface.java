package it.polimi.ingsw.gc49.rmi_socket.client.user_input_interfaces;

import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualServer;

public abstract class UserInputInterface {
    protected VirtualServer virtualServer;
    protected final String nickname; //<-- Will need this to reestablish a fallen connection

    public UserInputInterface ( VirtualServer virtualServer, String nickname ) {
        this.virtualServer = virtualServer;
        this.nickname = nickname;
    }

    public void runInput() throws Exception {
        System.out.println("Interfaccia vuota?!");
    }
}
