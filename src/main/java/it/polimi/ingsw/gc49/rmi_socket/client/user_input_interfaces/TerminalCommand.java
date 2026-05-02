package it.polimi.ingsw.gc49.rmi_socket.client.user_input_interfaces;

import it.polimi.ingsw.gc49.View.Mockup;

@FunctionalInterface
public interface TerminalCommand {
    void execute( Mockup terminalMockups, String[] terminalParameters);
}
