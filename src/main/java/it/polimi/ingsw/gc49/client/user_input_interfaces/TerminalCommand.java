package it.polimi.ingsw.gc49.client.user_input_interfaces;

import it.polimi.ingsw.gc49.client.view.Mockup;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualServer;

@FunctionalInterface
public interface TerminalCommand {
    void execute( TextTerminal terminalTerminal, ApplicationPhase terminalPhase, Mockup terminalMockups, String[] terminalParameters, VirtualServer terminalVirtualServer ) throws Exception;
}
