package it.polimi.ingsw.gc49.rmi_socket.client.user_input_interfaces;

import it.polimi.ingsw.gc49.VirtualClient;
import it.polimi.ingsw.gc49.VirtualServer;

public abstract class UserInputInterface {
    protected VirtualServer virtualServer;

    public UserInputInterface ( VirtualServer virtualServer ) {
        this.virtualServer = virtualServer;
    }

    public void runInput() throws Exception {
        System.out.println("Interfaccia vuota?!");
    }
}
