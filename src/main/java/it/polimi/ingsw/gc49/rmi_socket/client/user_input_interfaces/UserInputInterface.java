package it.polimi.ingsw.gc49.rmi_socket.client.user_input_interfaces;

import it.polimi.ingsw.gc49.View.Mockup;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualServer;

public abstract class UserInputInterface {
    protected VirtualServer virtualServer;
    protected Mockup mockups;

    public UserInputInterface (VirtualServer virtualServer, Mockup mockups ) {
        this.virtualServer = virtualServer;
        this.mockups = mockups;
    }

    public void runInput() throws Exception {
        System.out.println("Interfaccia vuota?!");
    }
}
