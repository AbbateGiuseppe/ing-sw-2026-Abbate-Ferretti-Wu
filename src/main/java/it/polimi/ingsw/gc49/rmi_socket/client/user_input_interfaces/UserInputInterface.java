package it.polimi.ingsw.gc49.rmi_socket.client.user_input_interfaces;

import it.polimi.ingsw.gc49.View.Mockup;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualServer;

public abstract class UserInputInterface {
    protected final VirtualServer virtualServer;
    protected final Mockup mockups;
    protected static ApplicationPhase currentPhase;

    public UserInputInterface (VirtualServer virtualServer, Mockup mockups, ApplicationPhase phase ) {
        this.virtualServer = virtualServer;
        this.mockups = mockups;
        currentPhase = phase;
    }

    public void runInput() throws Exception {
        System.out.println("Interfaccia vuota?!");
    }

    public void setCurrentPhase ( ApplicationPhase phase ) {
        currentPhase = phase;
    }
}
