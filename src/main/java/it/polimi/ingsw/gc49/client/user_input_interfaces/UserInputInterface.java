package it.polimi.ingsw.gc49.client.user_input_interfaces;

import it.polimi.ingsw.gc49.client.view.Mockup;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualServer;

public abstract class UserInputInterface {
    protected VirtualServer virtualServer;
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
    public void setVirtualServer ( VirtualServer virtualServer ) {
        this.virtualServer = virtualServer;
    }

    public abstract void printString ( String string );
}
