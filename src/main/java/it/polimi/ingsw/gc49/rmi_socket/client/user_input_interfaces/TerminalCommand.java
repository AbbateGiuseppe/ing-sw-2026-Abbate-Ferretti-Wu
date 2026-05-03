package it.polimi.ingsw.gc49.rmi_socket.client.user_input_interfaces;

import it.polimi.ingsw.gc49.View.Mockup;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;

@FunctionalInterface
public interface TerminalCommand {
    void execute( ApplicationPhase terminalPhase, Mockup terminalMockups, String[] terminalParameters);
}
