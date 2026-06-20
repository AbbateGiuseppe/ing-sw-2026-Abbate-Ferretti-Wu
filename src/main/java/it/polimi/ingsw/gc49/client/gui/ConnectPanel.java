package it.polimi.ingsw.gc49.client.gui;

import it.polimi.ingsw.gc49.client.ClientApplication;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class ConnectPanel extends JPanel {
    private final JTextField hostField = new JTextField("localhost", 24);
    private final JTextField nicknameField = new JTextField(24);
    private final JComboBox<ClientApplication.ConnectionType> connectionTypeCombo =
            new JComboBox<>(ClientApplication.ConnectionType.values());
    private final JButton connectButton = new JButton("Connect");

    public ConnectPanel(ClientApplication application) {
        setLayout(new BorderLayout(18, 18));
        setBorder(new EmptyBorder(36, 42, 36, 42));
        setBackground(new Color(29, 82, 61));

        JPanel hero = new JPanel(new BorderLayout(16, 16));
        hero.setOpaque(false);
        JLabel title = new JLabel("MESOS");
        title.setForeground(new Color(255, 238, 190));
        title.setFont(new Font(Font.SERIF, Font.BOLD, 64));
        JPanel titleBox = new JPanel(new BorderLayout());
        titleBox.setOpaque(false);
        titleBox.add(title, BorderLayout.NORTH);
        hero.add(titleBox, BorderLayout.CENTER);
        add(hero, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(new Color(246, 236, 214));
        form.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(new Color(116, 75, 39), 3),
                new EmptyBorder(24, 28, 24, 28)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(9, 9, 9, 9);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        form.add(label("Server host"), gbc);
        gbc.gridx = 1;
        form.add(hostField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        form.add(label("Nickname"), gbc);
        gbc.gridx = 1;
        form.add(nicknameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        form.add(label("Connection"), gbc);
        gbc.gridx = 1;
        connectionTypeCombo.setSelectedItem(ClientApplication.ConnectionType.RMI);
        form.add(connectionTypeCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        connectButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
        connectButton.setBackground(new Color(214, 164, 74));
        connectButton.setForeground(Color.BLACK);
        form.add(connectButton, gbc);

        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        center.add(form);
        add(center, BorderLayout.CENTER);

        connectButton.addActionListener(event -> application.connect(
                hostField.getText(),
                nicknameField.getText(),
                (ClientApplication.ConnectionType) connectionTypeCombo.getSelectedItem()
        ));
    }

    private Component label(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        label.setForeground(new Color(45, 36, 28));
        return label;
    }

    public void setConnectionInProgress(boolean inProgress) {
        connectButton.setEnabled(!inProgress);
        hostField.setEnabled(!inProgress);
        nicknameField.setEnabled(!inProgress);
        connectionTypeCombo.setEnabled(!inProgress);
    }
}
