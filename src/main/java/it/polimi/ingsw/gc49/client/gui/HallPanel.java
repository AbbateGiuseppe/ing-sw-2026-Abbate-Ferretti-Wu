package it.polimi.ingsw.gc49.client.gui;

import it.polimi.ingsw.gc49.client.view.mockupHall.MockupHall;
import it.polimi.ingsw.gc49.client.view.mockupHall.MockupRoom;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.HALL_phase.HALL_COMMAND.CREATE.HallCreatePacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.HALL_phase.HALL_COMMAND.JOIN.HallJoinPacket;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualServer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

public class HallPanel extends JPanel {
    private static final Color FELT = new Color(29, 82, 61);
    private static final Color FELT_DARK = new Color(18, 48, 39);
    private static final Color PAPER = new Color(246, 236, 214);
    private static final Color GOLD = new Color(214, 164, 74);

    private final MainFrame mainFrame;
    private VirtualServer virtualServer;
    private final DefaultListModel<String> playersModel = new DefaultListModel<>();
    private final JPanel roomsPanel = new JPanel();
    private final JTextField roomNameField = new JTextField(16);
    private final JSpinner maxPlayersSpinner = new JSpinner(new SpinnerNumberModel(2, 2, 5, 1));

    public HallPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout(14, 14));
        setBorder(new EmptyBorder(18, 18, 18, 18));
        setBackground(FELT);

        add(header(), BorderLayout.NORTH);
        add(leftRail(), BorderLayout.WEST);
        add(roomBoard(), BorderLayout.CENTER);
        add(createBar(), BorderLayout.SOUTH);
    }

    public void setVirtualServer(VirtualServer virtualServer) {
        this.virtualServer = virtualServer;
    }

    public void refresh(MockupHall hall) {
        playersModel.clear();
        roomsPanel.removeAll();
        if (hall == null) {
            roomsPanel.add(empty("No rooms at this table yet"));
            revalidate();
            repaint();
            return;
        }
        if (hall.connectedPlayers != null) {
            hall.connectedPlayers.forEach(playersModel::addElement);
        }
        if (hall.rooms == null || hall.rooms.isEmpty()) {
            roomsPanel.add(empty("Create a room to begin"));
        } else {
            for (MockupRoom room : hall.rooms) {
                roomsPanel.add(roomCard(room));
            }
        }
        revalidate();
        repaint();
    }

    private Component header() {
        JPanel header = new JPanel(new BorderLayout(12, 0));
        header.setOpaque(false);

        JLabel title = new JLabel("MESOS HALL");
        title.setForeground(new Color(255, 238, 190));
        title.setFont(new Font(Font.SERIF, Font.BOLD, 34));
        header.add(title, BorderLayout.WEST);
        return header;
    }

    private Component leftRail() {
        JPanel rail = panel(FELT_DARK);
        rail.setLayout(new BorderLayout(8, 8));
        rail.setPreferredSize(new Dimension(260, 500));
        rail.add(sectionLabel("Players Online"), BorderLayout.NORTH);
        JList<String> playersList = new JList<>(playersModel);
        playersList.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        playersList.setBackground(PAPER);
        rail.add(new JScrollPane(playersList), BorderLayout.CENTER);
        return rail;
    }

    private Component roomBoard() {
        JPanel board = panel(new Color(36, 105, 77));
        board.setLayout(new BorderLayout(8, 8));
        board.add(sectionLabel("Open Tables"), BorderLayout.NORTH);
        roomsPanel.setOpaque(false);
        roomsPanel.setLayout(new GridLayout(0, 2, 14, 14));
        board.add(new JScrollPane(roomsPanel), BorderLayout.CENTER);
        return board;
    }

    private Component createBar() {
        JPanel bar = panel(FELT_DARK);
        bar.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 6));
        JLabel label = sectionLabel("Create Room");
        roomNameField.setText("Room");
        JButton createButton = tableButton("Create");
        bar.add(label);
        bar.add(roomNameField);
        bar.add(new JLabel("Players"));
        bar.add(maxPlayersSpinner);
        bar.add(createButton);
        createButton.addActionListener(event -> createRoom());
        return bar;
    }

    private Component roomCard(MockupRoom room) {
        JPanel card = panel(PAPER);
        card.setLayout(new BorderLayout(10, 10));
        card.setPreferredSize(new Dimension(280, 138));

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        JLabel name = new JLabel(room.roomName);
        name.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        name.setForeground(new Color(45, 36, 28));
        JLabel players = new JLabel(room.connectedPlayers.size() + "/" + room.maxNumOfPlayers + " players");
        JLabel status = new JLabel(String.valueOf(room.type));
        players.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        status.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        info.add(name);
        info.add(Box.createVerticalStrut(8));
        info.add(players);
        info.add(status);
        info.add(Box.createVerticalGlue());
        card.add(info, BorderLayout.CENTER);

        JButton join = tableButton("Join Table");
        if (room.type == MockupRoom.RoomType.PLAYING) {
            join.setText("In Game");
            join.setEnabled(false);
        } else {
            join.addActionListener(event -> runCommand("Join room", () -> virtualServer.joinRoom(new HallJoinPacket(room.roomName))));
        }
        card.add(join, BorderLayout.SOUTH);
        return card;
    }

    private Component empty(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setForeground(new Color(255, 255, 255, 210));
        label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        return label;
    }

    private JPanel panel(Color color) {
        JPanel panel = new JPanel();
        panel.setBackground(color);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 45), 1),
                new EmptyBorder(12, 12, 12, 12)
        ));
        return panel;
    }

    private JLabel sectionLabel(String text) {
        JLabel label = new JLabel(text.toUpperCase());
        label.setForeground(new Color(255, 238, 190));
        label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
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

    private void createRoom() {
        if (virtualServer == null) {
            mainFrame.showError("Not connected", "Connect to a server first.");
            return;
        }
        String roomName = roomNameField.getText().trim();
        if (roomName.isEmpty()) {
            mainFrame.showError("Room name", "Room name cannot be empty.");
            return;
        }
        int maxPlayers = (Integer) maxPlayersSpinner.getValue();
        runCommand("Create room", () -> virtualServer.createRoom(new HallCreatePacket(roomName, maxPlayers)));
    }

    private void runCommand(String label, ThrowingRunnable runnable) {
        if (virtualServer == null) {
            mainFrame.showError("Not connected", "Connect to a server first.");
            return;
        }
        Thread thread = new Thread(() -> {
            try {
                runnable.run();
            } catch (Exception e) {
                SwingUtilities.invokeLater(() ->
                        mainFrame.showError(label + " failed", e.getMessage() == null ? e.toString() : e.getMessage()));
            }
        }, "gui-hall-command");
        thread.setDaemon(true);
        thread.start();
    }

    private interface ThrowingRunnable {
        void run() throws Exception;
    }
}
