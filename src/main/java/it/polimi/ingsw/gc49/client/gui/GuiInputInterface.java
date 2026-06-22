package it.polimi.ingsw.gc49.client.gui;

import it.polimi.ingsw.gc49.client.user_input_interfaces.UserInputInterface;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.ERROR.ErrorPacket;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualServer;

import javax.swing.SwingUtilities;

public class GuiInputInterface extends UserInputInterface {
    private final MainFrame mainFrame;

    public GuiInputInterface(VirtualServer virtualServer, ApplicationPhase phase, MainFrame mainFrame) {
        super(virtualServer, phase);
        this.mainFrame = mainFrame;
    }

    @Override
    public void printString(String string) {
        SwingUtilities.invokeLater(() -> mainFrame.appendLog(string));
    }

    @Override
    public void printErrorPacket(ErrorPacket errorPacket) {
        SwingUtilities.invokeLater(() -> {
            mainFrame.showError(errorPacket.errorTitle, errorPacket.errorContent);
            if (errorPacket.forceDisconnection) {
                mainFrame.showConnect();
            }
        });
    }

    @Override
    public void setCurrentPhase(ApplicationPhase phase) {
        super.setCurrentPhase(phase);
        SwingUtilities.invokeLater(() -> {
            mainFrame.appendLog("Phase: " + phase);
            mainFrame.showPhase(phase);
        });
    }

    @Override
    public void show() {
        SwingUtilities.invokeLater(() -> mainFrame.refreshGame(mockups.getGame()));
    }
}
