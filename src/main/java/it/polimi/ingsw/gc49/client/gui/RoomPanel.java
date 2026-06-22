package it.polimi.ingsw.gc49.client.gui;

import it.polimi.ingsw.gc49.client.view.mockupHall.MockupRoom;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.ROOM_phase.ROOM_COMMAND.LEAVE.RoomLeavePacket;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualServer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

public class RoomPanel extends JPanel {
    private static final Color FELT = new Color(29, 82, 61);
    private static final Color FELT_DARK = new Color(18, 48, 39);
    private static final Color PAPER = new Color(246, 236, 214);
    private static final Color GOLD = new Color(214, 164, 74);

    private final MainFrame mainFrame;
    private VirtualServer virtualServer;
    private final JLabel roomLabel = new JLabel("Room");
    private final JLabel statusLabel = new JLabel("Waiting for players");
    private final JPanel playersPanel = new JPanel();

    public RoomPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout(14, 14));
        setBorder(new EmptyBorder(18, 18, 18, 18));
        setBackground(FELT);

        add(header(), BorderLayout.NORTH);
        add(waitingTable(), BorderLayout.CENTER);
        add(actions(), BorderLayout.SOUTH);
    }

    public void setVirtualServer(VirtualServer virtualServer) {
        this.virtualServer = virtualServer;
    }

    public void refresh(MockupRoom room) {
        playersPanel.removeAll();
        if (room == null) {
            roomLabel.setText("Room");
            statusLabel.setText("Waiting for players");
            playersPanel.add(empty("No players yet"));
            revalidate();
            repaint();
            return;
        }
        roomLabel.setText(room.roomName);
        statusLabel.setText(room.connectedPlayers.size() + "/" + room.maxNumOfPlayers + " players - " + room.type);
        for (String player : room.connectedPlayers) {
            playersPanel.add(playerCard(player));
        }
        revalidate();
        repaint();
    }

    private Component header() {
        JPanel header = new JPanel(new BorderLayout(8, 0));
        header.setOpaque(false);
        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        roomLabel.setForeground(new Color(255, 238, 190));
        roomLabel.setFont(new Font(Font.SERIF, Font.BOLD, 42));
        statusLabel.setForeground(new Color(240, 230, 204));
        statusLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        text.add(roomLabel);
        text.add(statusLabel);
        header.add(text, BorderLayout.WEST);
        return header;
    }

    private Component waitingTable() {
        JPanel table = new JPanel(new BorderLayout(12, 12));
        table.setBackground(new Color(36, 105, 77));
        table.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 45), 1),
                new EmptyBorder(18, 18, 18, 18)
        ));

        JLabel title = new JLabel("PLAYERS AT THE TABLE");
        title.setForeground(new Color(255, 238, 190));
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
        table.add(title, BorderLayout.NORTH);

        playersPanel.setOpaque(false);
        playersPanel.setLayout(new GridLayout(0, 3, 14, 14));
        table.add(new JScrollPane(playersPanel), BorderLayout.CENTER);
        return table;
    }

    private Component actions() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 6));
        bar.setBackground(FELT_DARK);
        bar.setBorder(new EmptyBorder(10, 10, 10, 10));
        JButton leaveButton = tableButton("Leave Room");
        leaveButton.addActionListener(event -> leaveRoom());
        bar.add(leaveButton);
        return bar;
    }

    private Component playerCard(String player) {
        JPanel card = new JPanel(new BorderLayout(8, 8));
        card.setBackground(PAPER);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(110, 82, 52), 2),
                new EmptyBorder(12, 12, 12, 12)
        ));
        JLabel name = new JLabel(player, SwingConstants.CENTER);
        name.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        name.setForeground(new Color(45, 36, 28));
        card.add(name, BorderLayout.CENTER);
        return card;
    }

    private Component empty(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setForeground(new Color(255, 255, 255, 210));
        label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        return label;
    }

    private JButton tableButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(GOLD);
        button.setForeground(Color.BLACK);
        button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void leaveRoom() {
        if (virtualServer == null) {
            mainFrame.showError("Not connected", "Connect to a server first.");
            return;
        }
        Thread thread = new Thread(() -> {
            try {
                virtualServer.leaveRoom(new RoomLeavePacket());
            } catch (Exception e) {
                SwingUtilities.invokeLater(() ->
                        mainFrame.showError("Leave room failed", e.getMessage() == null ? e.toString() : e.getMessage()));
            }
        }, "gui-room-command");
        thread.setDaemon(true);
        thread.start();
    }
}
