package it.polimi.ingsw.gc49.client.gui;

import it.polimi.ingsw.gc49.client.ClientApplication;
import it.polimi.ingsw.gc49.client.view.Mockup;
import it.polimi.ingsw.gc49.client.view.mockupHall.MockupHall;
import it.polimi.ingsw.gc49.client.view.mockupHall.MockupRoom;
import it.polimi.ingsw.gc49.client.view.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.UpdateModelElement;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualServer;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.util.List;

public class MainFrame extends JFrame {
    private static final String CONNECT = "CONNECT";
    private static final String HALL = "HALL";
    private static final String ROOM = "ROOM";
    private static final String GAME = "GAME";

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cards = new JPanel(cardLayout);
    private final JTextArea logArea = new JTextArea(3, 80);

    private final ConnectPanel connectPanel;
    private final HallPanel hallPanel;
    private final RoomPanel roomPanel;
    private final GamePanel gamePanel;

    public MainFrame(ClientApplication application, Mockup mockups, ImageAssetManager imageAssetManager) {
        super("MESOS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1100, 760));
        setPreferredSize(new Dimension(1280, 780));

        connectPanel = new ConnectPanel(application);
        hallPanel = new HallPanel(this);
        roomPanel = new RoomPanel(this);
        gamePanel = new GamePanel(this, imageAssetManager);

        cards.add(connectPanel, CONNECT);
        cards.add(hallPanel, HALL);
        cards.add(roomPanel, ROOM);
        cards.add(gamePanel, GAME);

        logArea.setEditable(false);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setPreferredSize(new Dimension(1280, 72));
        logScroll.setMinimumSize(new Dimension(320, 50));

        setLayout(new BorderLayout());
        add(cards, BorderLayout.CENTER);
        add(logScroll, BorderLayout.SOUTH);

        cardLayout.show(cards, CONNECT);
        pack();
        setLocationRelativeTo(null);
    }

    public void setConnectionInProgress(boolean inProgress) {
        connectPanel.setConnectionInProgress(inProgress);
    }

    public void setVirtualServer(VirtualServer virtualServer) {
        hallPanel.setVirtualServer(virtualServer);
        roomPanel.setVirtualServer(virtualServer);
        gamePanel.setVirtualServer(virtualServer);
    }

    public void setNickname(String nickname) {
        setTitle("MESOS - " + nickname);
        gamePanel.setNickname(nickname);
    }

    public void showConnect() {
        cardLayout.show(cards, CONNECT);
    }

    public void showPhase(ApplicationPhase phase) {
        if (phase == ApplicationPhase.HALL) {
            cardLayout.show(cards, HALL);
        } else if (phase == ApplicationPhase.ROOM) {
            cardLayout.show(cards, ROOM);
        } else if (phase == ApplicationPhase.GAME) {
            gamePanel.showWaitingForGameState();
            cardLayout.show(cards, GAME);
        }
    }

    public void refreshHall(MockupHall hall) {
        hallPanel.refresh(hall);
    }

    public void refreshRoom(MockupRoom room) {
        roomPanel.refresh(room);
    }

    public void refreshGame(MockupGame game) {
        gamePanel.refresh(game);
    }

    public void refreshChangedGame(MockupGame game, List<Class<? extends UpdateModelElement>> changedElements) {
        gamePanel.refreshChanged(game, changedElements);
    }

    public void showEventResolution(List<String> eventResolutionSummaries) {
        if (eventResolutionSummaries == null || eventResolutionSummaries.isEmpty()) {
            return;
        }
        String message = String.join("\n", eventResolutionSummaries);
        appendLog("Event resolution:\n" + message);
        gamePanel.showEventResolution(eventResolutionSummaries);
    }

    public void appendLog(String text) {
        if (text == null || text.isBlank()) {
            return;
        }
        logArea.append(text);
        if (!text.endsWith("\n")) {
            logArea.append("\n");
        }
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    public void showError(String title, String message) {
        gamePanel.clearCommandPending();
        appendLog("ERROR - " + title + ": " + message);
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }
}
