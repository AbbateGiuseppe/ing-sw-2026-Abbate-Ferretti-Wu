package it.polimi.ingsw.gc49.client.user_input_interfaces;

import it.polimi.ingsw.gc49.ItaEngString;
import it.polimi.ingsw.gc49.client.ClientApplication;
import it.polimi.ingsw.gc49.client.view.Mockup;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.ERROR.ErrorPacket;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualServer;

public abstract class UserInputInterface {
    protected VirtualServer virtualServer;
    protected static Mockup mockups;
    protected static ApplicationPhase currentPhase;
    protected static ItaEngString.Language localLanguage;
    protected String nickname;
    protected boolean ofTurn = false;

    public UserInputInterface (VirtualServer virtualServer, ApplicationPhase phase ) {
        this.virtualServer = virtualServer;
        mockups = ClientApplication.mockups;
        currentPhase = phase;
    }

    public void runInput() throws Exception {
        System.out.println("Interfaccia vuota?!");
    }

    public void setNickname(String nickname) { this.nickname = nickname; }
    public void setCurrentPhase ( ApplicationPhase phase ) {
        currentPhase = phase;
    }
    public void setVirtualServer ( VirtualServer virtualServer ) {
        this.virtualServer = virtualServer;
    }
    public void setLocalLanguage ( ItaEngString.Language localLanguage ) { UserInputInterface.localLanguage = localLanguage; }

    public abstract void printString ( String string );
    public abstract void printErrorPacket ( ErrorPacket errorPacket );
    public abstract void show ();
}
