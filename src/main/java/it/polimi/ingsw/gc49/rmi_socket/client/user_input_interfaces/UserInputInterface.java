package it.polimi.ingsw.gc49.rmi_socket.client.user_input_interfaces;

import it.polimi.ingsw.gc49.View.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualServer;

public abstract class UserInputInterface {
    protected VirtualServer virtualServer;
    protected MockupGame model;

    public UserInputInterface (VirtualServer virtualServer, MockupGame model ) {
        this.virtualServer = virtualServer;
        this.model = model;
    }

    public void runInput() throws Exception {
        System.out.println("Interfaccia vuota?!");
    }
}
