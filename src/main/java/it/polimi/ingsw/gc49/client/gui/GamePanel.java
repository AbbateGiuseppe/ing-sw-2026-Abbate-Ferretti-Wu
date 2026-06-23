package it.polimi.ingsw.gc49.client.gui;

import it.polimi.ingsw.gc49.client.view.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.client.view.mockupModel.MockupOffer;
import it.polimi.ingsw.gc49.client.view.mockupModel.MockupOrder;
import it.polimi.ingsw.gc49.client.view.mockupModel.MockupPlayer;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.Cardboard.FullCardboardModelElement;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.Cardboard.LowerDrawModelElement;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.Cardboard.UpperDrawModelElement;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.EndGameModelElement;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.GameStatusModelElement;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.OfferOrderboard.OfferOrderboardModelElement;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.OfferOrderboard.OrderboardModelElement;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.OfferOrderboard.ReturnModelElement;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.Players.CurrentPlayerModelElement;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.Players.FoodAndPointsAllModelElement;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.Players.FoodAndPointsOneModelElement;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.Players.PlayersModelElement;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.Players.TotemModelElement;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.UpdateModelElement;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.GAME_phase.COMMAND.CommandPacket;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualServer;
import it.polimi.ingsw.gc49.server.controller.PlayerActionEnum;
import it.polimi.ingsw.gc49.server.model.Card.BuildingCard.BuildingCard;
import it.polimi.ingsw.gc49.server.model.Card.Card;
import it.polimi.ingsw.gc49.server.model.Card.CharacterCard.Builder;
import it.polimi.ingsw.gc49.server.model.Card.CharacterCard.CharacterCard;
import it.polimi.ingsw.gc49.server.model.Card.EventCard.EventCard;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.States.State;
import it.polimi.ingsw.gc49.server.model.Totem;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.JTextArea;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class GamePanel extends JPanel {
    private static final String WAITING_VIEW = "WAITING";
    private static final String BOARD_VIEW = "BOARD";
    private static final Color FELT = new Color(31, 83, 63);
    private static final Color FELT_DARK = new Color(18, 48, 39);
    private static final Color WOOD = new Color(96, 55, 31);
    private static final Color PANEL = new Color(245, 235, 212);
    private static final Color PANEL_DARK_TEXT = new Color(44, 35, 28);
    private static final Color GOLD = new Color(215, 169, 74);
    private static final Color CARD_CHARACTER = new Color(238, 210, 153);
    private static final Color CARD_BUILDING = new Color(199, 190, 173);
    private static final Color CARD_EVENT = new Color(116, 118, 124);
    private static final int MARKET_CARD_WIDTH = 132;
    private static final int MARKET_CARD_HEIGHT = 186;
    private static final int MARKET_ROW_HEIGHT = 198;
    private static final int COLLECTED_CARD_WIDTH = 88;
    private static final int COLLECTED_CARD_HEIGHT = 124;
    private static final int[][] OFFER_CARD_SOURCE_RECTS = {
            {630, 80, 1037, 748},    // A
            {1037, 80, 1444, 748},   // B
            {1444, 80, 1852, 748},   // C
            {118, 768, 552, 1438},   // D
            {630, 768, 1037, 1438},  // E
            {1037, 768, 1444, 1438}, // F
            {1444, 768, 1852, 1438}  // G
    };
    private static final char[] OFFER_TILE_LETTERS = {'A', 'B', 'C', 'D', 'E', 'F', 'G'};
    private static final int[] TWO_PLAYER_OFFER_TILES = {1, 2, 4, 5};
    private static final int[] THREE_PLAYER_OFFER_TILES = {1, 2, 3, 4, 5};
    private static final int[] FOUR_PLAYER_OFFER_TILES = {1, 2, 3, 4, 5, 6};
    private static final int[] FIVE_PLAYER_OFFER_TILES = {0, 1, 2, 3, 4, 5, 6};

    private final MainFrame mainFrame;
    private final ImageAssetManager imageAssetManager;
    private VirtualServer virtualServer;
    private String localNickname;
    private int currentPlayerIndex = -1;
    private String currentPhaseName = "Setup";
    private MockupPlayer currentPlayerView;
    private MockupGame latestGame;
    private boolean commandPending;

    private final JLabel currentPlayerLabel = new JLabel("Waiting for game state");
    private final JLabel phaseLabel = new JLabel("Setup");
    private final JButton passTurnButton = new JButton("Pass Turn");
    private final JButton backToHallButton = new JButton("Back to Hall");
    private final JTextArea eventResolutionText = new JTextArea("");
    private final JLabel finalStandingsLabel = new JLabel("");
    private final JPanel playersPanel = transparentPanel();
    private final JPanel upperLinePanel = rowPanel();
    private final JPanel lowerLinePanel = rowPanel();
    private TrackBandPanel trackPanel;
    private final CardLayout contentLayout = new CardLayout();
    private final JPanel contentPanel = transparentPanel(contentLayout);
    private final Optional<BufferedImage> offerTrackSheet;
    private int playerCount;

    public GamePanel(MainFrame mainFrame, ImageAssetManager imageAssetManager) {
        this.mainFrame = mainFrame;
        this.imageAssetManager = imageAssetManager;
        this.offerTrackSheet = imageAssetManager.findOfferTrackSheet();
        setLayout(new BorderLayout(10, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(FELT);

        add(buildTopBar(), BorderLayout.NORTH);
        contentPanel.add(buildWaitingPanel(), WAITING_VIEW);
        contentPanel.add(buildGameSurface(), BOARD_VIEW);
        add(contentPanel, BorderLayout.CENTER);
        contentLayout.show(contentPanel, WAITING_VIEW);
    }

    public void setVirtualServer(VirtualServer virtualServer) {
        this.virtualServer = virtualServer;
    }

    public void setNickname(String nickname) {
        this.localNickname = nickname;
        updateActionButtonStates();
    }

    public void refresh(MockupGame game) {
        latestGame = game;
        commandPending = false;
        playerCount = game == null || game.getPlayers() == null ? 0 : game.getPlayers().size();
        clear(playersPanel);
        clear(upperLinePanel);
        clear(lowerLinePanel);

        if (game == null) {
            showWaitingForGameState();
            return;
        }

        contentLayout.show(contentPanel, BOARD_VIEW);
        renderHeaderAndStatus(game);

        renderPlayers(game.getPlayers());
        renderOrderBoard(game.getOrderBoard());
        renderOfferBoard(game.getOfferBoard());
        renderMarketRow(upperLinePanel, game.getUpperLine(), PlayerActionEnum.DRAW_UPPER_CHARACTER,
                game.getUpperBuilding(), PlayerActionEnum.DRAW_UPPER_BUILDING);
        renderMarketRow(lowerLinePanel, game.getLowerLine(), PlayerActionEnum.DRAW_LOWER_CHARACTER,
                game.getLowerBuilding(), PlayerActionEnum.DRAW_LOWER_BUILDING);

        revalidate();
        repaint();
    }

    public void refreshChanged(MockupGame game, List<Class<? extends UpdateModelElement>> changedElements) {
        if (game == null || changedElements == null || changedElements.isEmpty()) {
            refresh(game);
            return;
        }

        latestGame = game;
        commandPending = false;
        playerCount = game.getPlayers() == null ? 0 : game.getPlayers().size();
        contentLayout.show(contentPanel, BOARD_VIEW);

        boolean cardsChanged = changedElements.contains(FullCardboardModelElement.class)
                || changedElements.contains(UpperDrawModelElement.class)
                || changedElements.contains(LowerDrawModelElement.class);
        boolean playersChanged = changedElements.contains(PlayersModelElement.class)
                || changedElements.contains(TotemModelElement.class)
                || changedElements.contains(FoodAndPointsAllModelElement.class)
                || changedElements.contains(FoodAndPointsOneModelElement.class)
                || changedElements.contains(ReturnModelElement.class)
                || changedElements.contains(UpperDrawModelElement.class)
                || changedElements.contains(LowerDrawModelElement.class)
                || changedElements.contains(EndGameModelElement.class);
        boolean currentChanged = changedElements.contains(CurrentPlayerModelElement.class);
        boolean statusChanged = changedElements.contains(GameStatusModelElement.class)
                || changedElements.contains(EndGameModelElement.class);
        boolean offerChanged = changedElements.contains(OfferOrderboardModelElement.class)
                || changedElements.contains(ReturnModelElement.class);
        boolean orderChanged = changedElements.contains(OrderboardModelElement.class)
                || changedElements.contains(OfferOrderboardModelElement.class)
                || changedElements.contains(ReturnModelElement.class);
        boolean trackTotemChanged = changedElements.contains(TotemModelElement.class)
                || changedElements.contains(ReturnModelElement.class);
        boolean headerChanged = cardsChanged || playersChanged || currentChanged || statusChanged;

        if (headerChanged) {
            renderHeaderAndStatus(game);
        }

        if (playersChanged || currentChanged) {
            clear(playersPanel);
            renderPlayers(game.getPlayers());
            refreshComponent(playersPanel);
        }
        if (orderChanged || trackTotemChanged) {
            renderOrderBoard(game.getOrderBoard());
            refreshComponent(trackPanel);
        }
        if (offerChanged || trackTotemChanged) {
            renderOfferBoard(game.getOfferBoard());
            refreshComponent(trackPanel);
        }
        if (cardsChanged || playersChanged || currentChanged) {
            clear(upperLinePanel);
            clear(lowerLinePanel);
            renderMarketRow(upperLinePanel, game.getUpperLine(), PlayerActionEnum.DRAW_UPPER_CHARACTER,
                    game.getUpperBuilding(), PlayerActionEnum.DRAW_UPPER_BUILDING);
            renderMarketRow(lowerLinePanel, game.getLowerLine(), PlayerActionEnum.DRAW_LOWER_CHARACTER,
                    game.getLowerBuilding(), PlayerActionEnum.DRAW_LOWER_BUILDING);
            refreshComponent(upperLinePanel);
            refreshComponent(lowerLinePanel);
        }
        if (headerChanged) {
            currentPlayerLabel.repaint();
            phaseLabel.repaint();
            finalStandingsLabel.repaint();
        }
    }

    private void renderHeaderAndStatus(MockupGame game) {
        currentPlayerIndex = game.getCurrentPlayerIndex();
        currentPhaseName = game.getGameState() == null ? "" : game.getGameState().toString();
        currentPlayerView = findPlayer(game.getPlayers(), currentPlayerIndex);
        currentPlayerLabel.setText("Current player: " + currentPlayerName() + "  Era: " + eraText(game.getDeckTopEra()));
        phaseLabel.setText(currentPhaseName);
        finalStandingsLabel.setText(toHtmlBlock(game.getFinalStandings()));
        finalStandingsLabel.setVisible(game.getFinalStandings() != null && !game.getFinalStandings().isBlank());
        updateActionButtonStates();
    }

    public void showWaitingForGameState() {
        currentPlayerIndex = -1;
        currentPlayerView = null;
        currentPhaseName = "Waiting";
        currentPlayerLabel.setText("Waiting for game state");
        phaseLabel.setText("Waiting");
        eventResolutionText.setText("");
        finalStandingsLabel.setText("");
        finalStandingsLabel.setVisible(false);
        updateActionButtonStates();
        contentLayout.show(contentPanel, WAITING_VIEW);
        revalidate();
        repaint();
    }

    private Component buildTopBar() {
        RoundedPanel top = new RoundedPanel(WOOD, 14);
        top.setLayout(new BorderLayout(12, 0));
        top.setBorder(BorderFactory.createEmptyBorder(7, 12, 7, 12));

        JLabel title = new JLabel("MESOS");
        title.setForeground(new Color(255, 239, 197));
        title.setFont(new Font(Font.SERIF, Font.BOLD, 28));

        currentPlayerLabel.setForeground(Color.WHITE);
        currentPlayerLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        phaseLabel.setOpaque(true);
        phaseLabel.setBackground(new Color(255, 239, 197));
        phaseLabel.setForeground(new Color(64, 42, 25));
        phaseLabel.setBorder(BorderFactory.createEmptyBorder(4, 9, 4, 9));
        phaseLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));

        JPanel titleBox = transparentPanel();
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.X_AXIS));
        titleBox.add(title);
        titleBox.add(Box.createHorizontalStrut(18));
        titleBox.add(currentPlayerLabel);
        titleBox.add(Box.createHorizontalStrut(12));
        titleBox.add(phaseLabel);

        top.add(titleBox, BorderLayout.WEST);
        top.add(buildActionBar(), BorderLayout.EAST);
        return top;
    }

    private Component buildActionBar() {
        JPanel actions = transparentPanel();
        actions.setLayout(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        passTurnButton.setFocusPainted(false);
        passTurnButton.setBackground(new Color(255, 239, 197));
        passTurnButton.setForeground(new Color(64, 42, 25));
        passTurnButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        passTurnButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 120), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        passTurnButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        passTurnButton.setToolTipText("Skip the remaining offer actions for the current player");
        passTurnButton.addActionListener(event -> sendCommand(PlayerActionEnum.PASS_TURN));
        styleTopActionButton(backToHallButton, new Color(255, 239, 197), new Color(64, 42, 25));
        backToHallButton.setToolTipText("Return all players to the hall");
        backToHallButton.addActionListener(event -> sendCommand(PlayerActionEnum.RETURN_TO_HALL));
        actions.add(backToHallButton);
        addTotemButton(actions, "Orange", Totem.ORANGE, new Color(229, 128, 48));
        addTotemButton(actions, "White", Totem.WHITE, new Color(244, 240, 223));
        addTotemButton(actions, "Blue", Totem.BLUE, new Color(64, 137, 210));
        addTotemButton(actions, "Black", Totem.BLACK, new Color(38, 38, 42));
        addTotemButton(actions, "Yellow", Totem.YELLOW, new Color(231, 196, 66));
        updateActionButtonStates();
        return actions;
    }

    private void styleTopActionButton(JButton button, Color background, Color foreground) {
        button.setFocusPainted(false);
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 120), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void updateActionButtonStates() {
        boolean executeOffers = currentPhaseName.equals(State.States.OFFER_EXECUTION.toString());
        boolean gameEnd = currentPhaseName.equals(State.States.GAME_END.toString());
        boolean hasRemainingOfferActions = currentPlayerView != null
                && (currentPlayerView.getDrawableUpper() > 0 || currentPlayerView.getDrawableLower() > 0);
        boolean isLocalPlayerTurn = localNickname == null
                || localNickname.isBlank()
                || (currentPlayerView != null && localNickname.equals(currentPlayerView.getNickname()));
        boolean enabled = executeOffers
                && hasRemainingOfferActions
                && isLocalPlayerTurn
                && !commandPending
                && virtualServer != null;

        passTurnButton.setVisible(executeOffers);
        passTurnButton.setEnabled(enabled);
        passTurnButton.setCursor(Cursor.getPredefinedCursor(enabled ? Cursor.HAND_CURSOR : Cursor.DEFAULT_CURSOR));
        if (!executeOffers) {
            passTurnButton.setToolTipText("Available while executing offers");
        } else if (!isLocalPlayerTurn) {
            passTurnButton.setToolTipText("Only the current player's window can pass");
        } else if (!hasRemainingOfferActions) {
            passTurnButton.setToolTipText("No remaining offer actions");
        } else {
            passTurnButton.setToolTipText("Skip the remaining offer actions for the current player");
        }
        boolean endActionEnabled = gameEnd && !commandPending && virtualServer != null;
        backToHallButton.setVisible(gameEnd);
        backToHallButton.setEnabled(endActionEnabled);
        backToHallButton.setCursor(Cursor.getPredefinedCursor(endActionEnabled ? Cursor.HAND_CURSOR : Cursor.DEFAULT_CURSOR));
    }

    private Component buildWaitingPanel() {
        RoundedPanel panel = new RoundedPanel(new Color(36, 105, 77), 24);
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JPanel center = transparentPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("Waiting for game state", SwingConstants.CENTER);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setForeground(new Color(255, 239, 197));
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 28));

        JLabel detail = new JLabel("Synchronizing the board from the server.", SwingConstants.CENTER);
        detail.setAlignmentX(Component.CENTER_ALIGNMENT);
        detail.setForeground(new Color(255, 255, 255, 190));
        detail.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));

        center.add(Box.createVerticalGlue());
        center.add(title);
        center.add(Box.createVerticalStrut(10));
        center.add(detail);
        center.add(Box.createVerticalGlue());
        panel.add(center, BorderLayout.CENTER);
        return panel;
    }

    private Component buildGameSurface() {
        JPanel surface = transparentPanel(new BorderLayout(8, 0));
        JScrollPane boardScroll = styledScroll(buildBoard());
        boardScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        boardScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        boardScroll.getHorizontalScrollBar().setUnitIncrement(24);
        boardScroll.getVerticalScrollBar().setUnitIncrement(24);
        surface.add(boardScroll, BorderLayout.CENTER);
        surface.add(buildStatusRail(), BorderLayout.EAST);
        return surface;
    }

    private Component buildStatusRail() {
        RoundedPanel rail = new RoundedPanel(FELT_DARK, 18);
        rail.setLayout(new BoxLayout(rail, BoxLayout.Y_AXIS));
        rail.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        rail.setPreferredSize(new Dimension(230, 380));
        rail.setMinimumSize(new Dimension(210, 260));

        eventResolutionText.setEditable(false);
        eventResolutionText.setLineWrap(true);
        eventResolutionText.setWrapStyleWord(true);
        eventResolutionText.setOpaque(false);
        eventResolutionText.setForeground(Color.WHITE);
        eventResolutionText.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        finalStandingsLabel.setForeground(Color.WHITE);
        finalStandingsLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        finalStandingsLabel.setVisible(false);

        JScrollPane eventResolutionScroll = styledScroll(eventResolutionText);
        eventResolutionScroll.setPreferredSize(new Dimension(206, 220));
        rail.add(section("Event Resolution", eventResolutionScroll));
        rail.add(Box.createVerticalStrut(6));
        rail.add(section("Final", finalStandingsLabel));
        rail.add(Box.createVerticalGlue());
        return rail;
    }

    public void showEventResolution(List<String> eventResolutionSummaries) {
        if (eventResolutionSummaries == null || eventResolutionSummaries.isEmpty()) {
            return;
        }
        eventResolutionText.setText(String.join("\n", eventResolutionSummaries));
        eventResolutionText.setCaretPosition(0);
    }

    private Component buildBoard() {
        RoundedPanel board = new RoundedPanel(new Color(36, 105, 77), 24) {
            @Override
            protected void paintComponent(Graphics graphics) {
                super.paintComponent(graphics);
                Graphics2D g = (Graphics2D) graphics.create();
                g.setColor(new Color(255, 255, 255, 22));
                for (int x = 32; x < getWidth(); x += 80) {
                    g.drawLine(x, 0, x - 80, getHeight());
                }
                g.dispose();
            }
        };
        board.setLayout(new BorderLayout());
        board.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JPanel market = transparentPanel();
        market.setLayout(new BoxLayout(market, BoxLayout.Y_AXIS));
        market.add(cardRowScroll(upperLinePanel, MARKET_ROW_HEIGHT));
        market.add(Box.createVerticalStrut(3));
        market.add(trackBand());
        market.add(Box.createVerticalStrut(3));
        market.add(cardRowScroll(lowerLinePanel, MARKET_ROW_HEIGHT));

        JPanel center = transparentPanel(new BorderLayout(0, 6));
        center.add(market, BorderLayout.NORTH);
        center.add(buildPlayersArea(), BorderLayout.CENTER);
        board.add(center, BorderLayout.CENTER);
        return board;
    }

    private Component trackBand() {
        trackPanel = new TrackBandPanel(offerTrackSheet.orElse(null));
        trackPanel.setLayout(null);
        trackPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 136));
        trackPanel.setPreferredSize(new Dimension(900, 136));
        return trackPanel;
    }

    private JScrollPane cardRowScroll(JPanel row, int height) {
        JScrollPane scroll = styledScroll(row);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scroll.setPreferredSize(new Dimension(900, height));
        scroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
        return scroll;
    }

    private Component buildPlayersArea() {
        RoundedPanel rail = new RoundedPanel(FELT_DARK, 18);
        rail.setLayout(new BorderLayout());
        rail.setBorder(BorderFactory.createEmptyBorder(7, 8, 7, 8));
        rail.setPreferredSize(new Dimension(900, 292));
        rail.setMinimumSize(new Dimension(620, 230));

        playersPanel.setLayout(new GridLayout(1, 1, 8, 0));
        JScrollPane playersScroll = styledScroll(playersPanel);
        playersScroll.setName("players-scroll");
        playersScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        playersScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        playersScroll.getHorizontalScrollBar().setUnitIncrement(18);
        playersScroll.getVerticalScrollBar().setUnitIncrement(18);
        rail.add(section("Tribes", playersScroll), BorderLayout.CENTER);
        return rail;
    }

    private void renderPlayers(List<MockupPlayer> players) {
        if (players == null || players.isEmpty()) {
            playersPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 0));
            addEmpty(playersPanel, "No players yet");
            return;
        }
        playersPanel.setLayout(new BoxLayout(playersPanel, BoxLayout.X_AXIS));
        for (MockupPlayer player : players) {
            playersPanel.add(playerTile(player));
            playersPanel.add(Box.createHorizontalStrut(8));
        }
    }

    private Component playerTile(MockupPlayer player) {
        Color accent = totemColor(player.getTotem());
        RoundedPanel tile = new RoundedPanel(PANEL, 12);
        tile.setLayout(new BorderLayout(6, 3));
        tile.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(player.getPlayerIndex() == currentPlayerIndex ? GOLD : new Color(120, 101, 79), 2),
                BorderFactory.createEmptyBorder(5, 6, 5, 6)
        ));
        tile.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel name = new JLabel(player.getPlayerIndex() + "  " + abbreviate(player.getNickname(), 14));
        name.setToolTipText(player.getNickname());
        name.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
        name.setForeground(PANEL_DARK_TEXT);

        JLabel totem = new JLabel(player.getTotem() == null ? "-" : abbreviate(player.getTotem().toString(), 6), SwingConstants.CENTER);
        totem.setOpaque(true);
        totem.setBackground(accent);
        totem.setForeground(textFor(accent));
        totem.setBorder(BorderFactory.createEmptyBorder(3, 6, 3, 6));
        totem.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));

        JPanel header = transparentPanel(new BorderLayout(8, 0));
        header.add(name, BorderLayout.CENTER);
        header.add(totem, BorderLayout.EAST);

        JPanel stats = transparentPanel();
        stats.setLayout(new FlowLayout(FlowLayout.LEFT, 7, 0));
        stats.add(stat("Food", player.getFood()));
        stats.add(stat("Points", player.getPoints()));

        JPanel body = transparentPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        stats.setAlignmentX(Component.LEFT_ALIGNMENT);
        body.add(stats);
        body.add(Box.createVerticalStrut(3));
        JPanel collectedCards = collectedCardsPanel(player);
        collectedCards.setAlignmentX(Component.LEFT_ALIGNMENT);
        body.add(collectedCards);

        int cardTotal = cardCount(player.getCharacterCards()) + cardCount(player.getBuildingCards());
        int preferredWidth = Math.max(300, 116 + cardTotal * (COLLECTED_CARD_WIDTH + 6));
        int preferredHeight = Math.max(188, header.getPreferredSize().height + body.getPreferredSize().height + 24);
        tile.setPreferredSize(new Dimension(preferredWidth, Math.max(238, preferredHeight)));
        tile.setMinimumSize(new Dimension(240, 224));

        tile.add(header, BorderLayout.NORTH);
        tile.add(body, BorderLayout.CENTER);
        return tile;
    }

    private JLabel stat(String label, int value) {
        JLabel stat = new JLabel(label + ": " + value);
        stat.setForeground(PANEL_DARK_TEXT);
        stat.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
        return stat;
    }

    private JPanel collectedCardsPanel(MockupPlayer player) {
        JPanel row = transparentPanel();
        row.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 0));
        addCollectedCardGroup(row, "Char", player.getCharacterCards());
        row.add(Box.createHorizontalStrut(10));
        addCollectedCardGroup(row, "Build", player.getBuildingCards());
        return row;
    }

    private void addCollectedCardGroup(JPanel row, String label, List<Card> cards) {
        JLabel title = new JLabel(label + ":");
        title.setForeground(PANEL_DARK_TEXT);
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));
        title.setPreferredSize(new Dimension(42, COLLECTED_CARD_HEIGHT));
        row.add(title);
        renderCollectedCards(row, cards);
        if (cards == null || cards.isEmpty()) {
            JLabel empty = new JLabel("none");
            empty.setForeground(new Color(91, 75, 57));
            empty.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
            empty.setPreferredSize(new Dimension(46, COLLECTED_CARD_HEIGHT));
            row.add(empty);
        }
    }

    private void renderCollectedCards(JPanel panel, List<Card> cards) {
        if (cards == null) {
            return;
        }
        for (Card card : cards) {
            panel.add(collectedCardView(card));
        }
    }

    private int cardCount(List<Card> cards) {
        return cards == null ? 0 : cards.size();
    }

    private Component collectedCardView(Card card) {
        JLabel image = new JLabel();
        image.setHorizontalAlignment(SwingConstants.CENTER);
        image.setVerticalAlignment(SwingConstants.CENTER);
        image.setPreferredSize(new Dimension(COLLECTED_CARD_WIDTH, COLLECTED_CARD_HEIGHT));
        image.setMinimumSize(new Dimension(COLLECTED_CARD_WIDTH, COLLECTED_CARD_HEIGHT));
        image.setBorder(BorderFactory.createLineBorder(new Color(80, 60, 45), 1));
        image.setOpaque(true);
        image.setBackground(card instanceof BuildingCard ? CARD_BUILDING : CARD_CHARACTER);
        imageAssetManager.findIcon(card, COLLECTED_CARD_WIDTH - 2, COLLECTED_CARD_HEIGHT - 2).ifPresentOrElse(image::setIcon,
                () -> {
                    image.setText(shortCardName(card));
                    image.setForeground(PANEL_DARK_TEXT);
                    image.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 8));
                });
        image.setToolTipText("<html>" + cardHtml(card, -1) + "</html>");
        return image;
    }

    private void renderOrderBoard(List<MockupOrder> orderBoard) {
        if (trackPanel != null) {
            trackPanel.setOrderBoard(orderBoard);
        }
    }

    private void renderOfferBoard(List<MockupOffer> offerBoard) {
        if (trackPanel != null) {
            trackPanel.setOfferBoard(offerBoard);
        }
    }

    private void renderMarketRow(JPanel panel, List<Card> tribeCards, PlayerActionEnum tribeAction,
                                 List<Card> buildingCards, PlayerActionEnum buildingAction) {
        boolean hasTribeCards = tribeCards != null && !tribeCards.isEmpty();
        boolean hasBuildingCards = buildingCards != null && !buildingCards.isEmpty();
        if (!hasTribeCards && !hasBuildingCards) {
            addEmpty(panel, "No cards");
            return;
        }
        if (hasTribeCards) {
            renderCards(panel, tribeCards, tribeAction, true);
        }
        if (hasTribeCards && hasBuildingCards) {
            panel.add(Box.createHorizontalStrut(16));
        }
        if (hasBuildingCards) {
            renderCards(panel, buildingCards, buildingAction, false);
        }
    }

    private void renderCards(JPanel panel, List<Card> cards, PlayerActionEnum action, boolean tribeLine) {
        if (cards == null || cards.isEmpty()) {
            addEmpty(panel, "No cards");
            return;
        }
        for (int i = 0; i < cards.size(); i++) {
            panel.add(cardButton(cards.get(i), i, action, tribeLine));
        }
    }

    private JButton cardButton(Card card, int index, PlayerActionEnum action, boolean tribeLine) {
        boolean event = card instanceof EventCard;
        boolean building = card instanceof BuildingCard;
        boolean character = card instanceof CharacterCard;
        Color bg = event ? CARD_EVENT : building ? CARD_BUILDING : CARD_CHARACTER;

        Optional<Image> cardImage = imageAssetManager.findCardImage(card, MARKET_CARD_WIDTH - 6, MARKET_CARD_HEIGHT - 6);
        JButton button = new ImageCardButton(cardImage.orElse(null), bg, event);
        button.setPreferredSize(new Dimension(MARKET_CARD_WIDTH, MARKET_CARD_HEIGHT));
        button.setMinimumSize(new Dimension(MARKET_CARD_WIDTH, MARKET_CARD_HEIGHT));
        button.setMaximumSize(new Dimension(MARKET_CARD_WIDTH, MARKET_CARD_HEIGHT));
        button.setFocusPainted(false);
        boolean legalNow = isLegalCardAction(card, action, tribeLine);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(legalNow ? GOLD : event ? Color.LIGHT_GRAY : new Color(80, 60, 45), legalNow ? 3 : 2),
                BorderFactory.createEmptyBorder(3, 3, 3, 3)
        ));

        button.setToolTipText("<html>" + cardHtml(card, index) + "</html>");

        if (event) {
            button.setCursor(Cursor.getDefaultCursor());
            button.setToolTipText("<html>" + cardHtml(card, index) + "<br><br><b>Event cards cannot be taken</b></html>");
            return button;
        }

        boolean validType = tribeLine ? character : building;
        if (!validType) {
            button.setCursor(Cursor.getDefaultCursor());
            button.setToolTipText("<html>" + cardHtml(card, index) + "<br><br><b>This card cannot be selected from this row</b></html>");
            return button;
        }

        if (!legalNow) {
            button.setEnabled(false);
            button.setCursor(Cursor.getDefaultCursor());
            button.setToolTipText("<html>" + cardHtml(card, index) + "<br><br><b>Not available for the current action</b></html>");
            return button;
        }

        if (commandPending) {
            button.setEnabled(false);
            button.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            button.setToolTipText("<html>" + cardHtml(card, index) + "<br><br><b>Waiting for the board to update</b></html>");
            return button;
        }

        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addActionListener(eventClick -> sendCommand(action, index));
        return button;
    }

    private boolean isLegalCardAction(Card card, PlayerActionEnum action, boolean tribeLine) {
        if (!currentPhaseName.equals(State.States.OFFER_EXECUTION.toString()) || currentPlayerView == null) {
            return false;
        }
        if (card instanceof EventCard) {
            return false;
        }
        boolean typeMatches = tribeLine ? card instanceof CharacterCard : card instanceof BuildingCard;
        if (!typeMatches) {
            return false;
        }
        boolean upperAction = action == PlayerActionEnum.DRAW_UPPER_CHARACTER || action == PlayerActionEnum.DRAW_UPPER_BUILDING;
        boolean lowerAction = action == PlayerActionEnum.DRAW_LOWER_CHARACTER || action == PlayerActionEnum.DRAW_LOWER_BUILDING;
        if (upperAction && currentPlayerView.getDrawableUpper() <= 0) {
            return false;
        }
        if (lowerAction && currentPlayerView.getDrawableLower() <= 0) {
            return false;
        }
        if (card instanceof BuildingCard buildingCard) {
            int effectivePrice = Math.max(0, buildingCard.getFoodPrice() - currentBuildingDiscount());
            return currentPlayerView.getFood() >= effectivePrice;
        }
        return true;
    }

    private int currentBuildingDiscount() {
        if (currentPlayerView == null) {
            return 0;
        }
        int discount = 0;
        for (Card card : currentPlayerView.getCharacterCards()) {
            if (card instanceof Builder builder) {
                discount += builder.getBuildingDiscount();
            }
        }
        return discount;
    }

    private String offerLetter(int index, int offerCount) {
        int sourceIndex = offerSourceIndex(index, offerCount);
        if (sourceIndex < 0 || sourceIndex >= OFFER_TILE_LETTERS.length) {
            return String.valueOf((char) ('A' + index));
        }
        return String.valueOf(OFFER_TILE_LETTERS[sourceIndex]);
    }

    private int offerSourceIndex(int index, int offerCount) {
        int[] sourceIndexes = offerSourceIndexes(offerCount);
        if (sourceIndexes.length == 0) {
            return index;
        }
        return index >= 0 && index < sourceIndexes.length ? sourceIndexes[index] : -1;
    }

    private int[] offerSourceIndexes(int offerCount) {
        if (offerCount == TWO_PLAYER_OFFER_TILES.length) {
            return TWO_PLAYER_OFFER_TILES;
        }
        if (offerCount == THREE_PLAYER_OFFER_TILES.length) {
            return THREE_PLAYER_OFFER_TILES;
        }
        if (offerCount == FOUR_PLAYER_OFFER_TILES.length) {
            return FOUR_PLAYER_OFFER_TILES;
        }
        if (offerCount == FIVE_PLAYER_OFFER_TILES.length) {
            return FIVE_PLAYER_OFFER_TILES;
        }
        return new int[0];
    }

    private String eraText(Era era) {
        if (era == null) {
            return "?";
        }
        if (era == Era.FIRST) {
            return "1";
        }
        if (era == Era.SECOND) {
            return "2";
        }
        return "3";
    }

    private MockupPlayer findPlayer(List<MockupPlayer> players, int playerIndex) {
        if (players == null) {
            return null;
        }
        for (MockupPlayer player : players) {
            if (player.getPlayerIndex() == playerIndex) {
                return player;
            }
        }
        return null;
    }

    private String currentPlayerName() {
        if (currentPlayerView == null) {
            return currentPlayerIndex < 0 ? "-" : String.valueOf(currentPlayerIndex);
        }
        return currentPlayerView.getPlayerIndex() + " " + currentPlayerView.getNickname();
    }

    private String toHtmlLine(String text, int maxChars) {
        if (text == null || text.isBlank()) {
            return "<html>-</html>";
        }
        String escaped = escape(text);
        if (escaped.length() <= maxChars) {
            return "<html>" + escaped + "</html>";
        }
        StringBuilder builder = new StringBuilder("<html>");
        int index = 0;
        while (index < escaped.length()) {
            int end = Math.min(escaped.length(), index + maxChars);
            builder.append(escaped, index, end);
            if (end < escaped.length()) {
                builder.append("<br>");
            }
            index = end;
        }
        return builder.append("</html>").toString();
    }

    private String toHtmlBlock(String text) {
        if (text == null || text.isBlank()) {
            return "<html>-</html>";
        }
        return "<html>" + escape(text).replace("\n", "<br>") + "</html>";
    }

    private String cardHtml(Card card, int index) {
        String type = card instanceof CharacterCard ? "CHARACTER" : card instanceof BuildingCard ? "BUILDING" : card instanceof EventCard ? "EVENT" : "CARD";
        StringBuilder builder = new StringBuilder();
        builder.append("<b>").append(type);
        if (index >= 0) {
            builder.append(" #").append(index);
        }
        builder.append("</b><br>");
        builder.append(escape(safeSimpleToString(card))).append("<br><br>");
        builder.append("Era ").append(card.getEra()).append("<br>");
        builder.append("Min ").append(card.getMinNumPlayers());
        if (card instanceof BuildingCard buildingCard) {
            int basePrice = buildingCard.getFoodPrice();
            int discount = currentBuildingDiscount();
            int effectivePrice = Math.max(0, basePrice - discount);
            builder.append("<br>Food ").append(basePrice);
            if (currentPlayerView != null && discount > 0) {
                builder.append("<br>Builder discount -").append(discount);
                builder.append("<br><b>You pay ").append(effectivePrice).append("</b>");
            }
        }
        appendEffectSummary(builder, card);
        appendCardDetails(builder, card);
        if (card instanceof EventCard) {
            builder.append("<br><b>LOCKED</b>");
        }
        return builder.toString();
    }

    private void appendEffectSummary(StringBuilder builder, Card card) {
        String summary = effectSummary(card);
        if (!summary.isBlank()) {
            builder.append("<br><br><b>Effect</b><br>").append(escape(summary));
        }
    }

    private String effectSummary(Card card) {
        String className = card.getClass().getSimpleName();
        return switch (className) {
            case "Artist" -> "Counts as 1 Artist for painting events and end game artist scoring.";
            case "Gatherer" -> "Counts as 1 Gatherer and gives sustenance discount +3.";
            case "Hunter" -> "Counts as 1 Hunter. If drumstick is true, on draw gain food equal to owned Hunters.";
            case "Builder" -> "Adds building discount and builder end game points.";
            case "Inventor" -> "Counts as 1 Inventor and adds the shown invention.";
            case "Shaman" -> "Counts as 1 Shaman and adds the shown stars.";
            case "HuntingEvent" -> "Each player gains food equal to Hunters and points per Hunter.";
            case "PaintingEvent" -> "Players with Artists greater than threshold gain points per Artist; others lose points.";
            case "RitualEvent" -> "Most stars gain points; fewest stars lose points.";
            case "SustenanceEvent" -> "Players pay food for characters after discounts; missing food costs points.";
            case "BonusHuntingCard" -> "During hunting events, owner gains extra food and points from Hunters.";
            case "BonusPaintingCard" -> "During painting events, owner gains food from Artists.";
            case "SustainDiscountByClassCard" -> "During sustenance, owner discounts food by the shown character type.";
            case "ShamanicImmunityCard" -> "During ritual events, owner can ignore shamanic point loss.";
            case "DoubleShamanPointsCard" -> "During ritual/end game, owner can double shaman-related pending points.";
            case "ShamanicThreeStarCard" -> "During ritual events, owner gains extra stars for ritual comparison.";
            case "BonusFoodEndTurnCard" -> "At turn end, owner gains food when returned to a food order slot.";
            case "OneMoreCardCard" -> "At turn end, owner may gain an extra draw action.";
            case "CharacterSetCompleteFoodCard" -> "On draw events, owner gains food when all character classes are present.";
            case "SamePairInventionsCard" -> "On draw events, owner gains food from matching invention pairs.";
            case "CharacterSetCompletePointEndGameCard" -> "At game end, owner gains points when all character classes are present.";
            case "BonusPointsByClassEndGameCard" -> "At game end, owner gains points per shown character type.";
            case "DoubleBuilderPointsCard" -> "At game end, owner can double builder points.";
            case "TwentyFiveBonusPointsEndGame" -> "At game end, owner can gain a 25 point food bonus.";
            default -> "";
        };
    }

    private void appendCardDetails(StringBuilder builder, Card card) {
        List<Field> fields = detailFields(card.getClass());
        if (fields.isEmpty()) {
            return;
        }

        builder.append("<br><br><b>Details</b>");
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object value = field.get(card);
                builder.append("<br>")
                        .append(escape(fieldLabel(field.getName())))
                        .append(": ")
                        .append(escape(formatFieldValue(value)));
            } catch (Exception ignored) {
                // Tooltip details are best effort; the card stays usable if a field is inaccessible.
            }
        }
    }

    private List<Field> detailFields(Class<?> cardClass) {
        List<Class<?>> hierarchy = new ArrayList<>();
        Class<?> current = cardClass;
        while (current != null && Card.class.isAssignableFrom(current) && current != Card.class) {
            hierarchy.add(current);
            current = current.getSuperclass();
        }
        Collections.reverse(hierarchy);

        List<Field> fields = new ArrayList<>();
        for (Class<?> type : hierarchy) {
            for (Field field : type.getDeclaredFields()) {
                if (shouldShowDetailField(field)) {
                    fields.add(field);
                }
            }
        }
        return fields;
    }

    private boolean shouldShowDetailField(Field field) {
        int modifiers = field.getModifiers();
        if (Modifier.isStatic(modifiers) || Modifier.isTransient(modifiers)) {
            return false;
        }
        String name = field.getName();
        return !name.equals("queueUpdater")
                && !name.equals("eventManager")
                && !name.equals("owner");
    }

    private String fieldLabel(String fieldName) {
        StringBuilder label = new StringBuilder();
        for (int i = 0; i < fieldName.length(); i++) {
            char character = fieldName.charAt(i);
            if (i > 0 && Character.isUpperCase(character)) {
                label.append(' ');
            }
            label.append(i == 0 ? Character.toUpperCase(character) : character);
        }
        return label.toString();
    }

    private String formatFieldValue(Object value) {
        if (value == null) {
            return "-";
        }
        return String.valueOf(value);
    }

    private void addTotemButton(JPanel panel, String label, Totem totem, Color color) {
        JButton button = gameButton(label, color, textFor(color));
        button.addActionListener(event -> sendCommand(PlayerActionEnum.CHOOSE_TOTEM, totem));
        panel.add(button);
    }

    private JButton gameButton(String text, Color background, Color foreground) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 120), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JPanel section(String title, Component content) {
        RoundedPanel panel = new RoundedPanel(new Color(255, 255, 255, 28), 16);
        panel.setLayout(new BorderLayout(6, 6));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        panel.add(sectionLabel(title), BorderLayout.NORTH);
        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    private JLabel sectionLabel(String text) {
        JLabel label = new JLabel(text.toUpperCase());
        label.setForeground(new Color(255, 238, 190));
        label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
        return label;
    }

    private JScrollPane styledScroll(Component component) {
        JScrollPane scroll = new JScrollPane(component);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        return scroll;
    }

    private static JPanel transparentPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        return panel;
    }

    private static JPanel transparentPanel(java.awt.LayoutManager layout) {
        JPanel panel = new JPanel(layout);
        panel.setOpaque(false);
        return panel;
    }

    private static JPanel rowPanel() {
        JPanel panel = transparentPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 4));
        return panel;
    }

    private void clear(JPanel panel) {
        panel.removeAll();
    }

    private void refreshComponent(Component component) {
        if (component != null) {
            component.revalidate();
            component.repaint();
        }
    }

    private void addEmpty(JPanel panel, String text) {
        JLabel label = new JLabel(text);
        label.setForeground(new Color(255, 255, 255, 190));
        label.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        panel.add(label);
    }

    private Color totemColor(Totem totem) {
        switch (totem) {
            case ORANGE -> {
                return new Color(229, 128, 48);
            }
            case WHITE -> {
                return new Color(244, 240, 223);
            }
            case BLUE -> {
                return new Color(64, 137, 210);
            }
            case BLACK -> {
                return new Color(38, 38, 42);
            }
            case YELLOW -> {
                return new Color(231, 196, 66);
            }
            default -> {
                return new Color(132, 121, 103);
            }
        }
    }

    private Color textFor(Color color) {
        int luminance = (int) (0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue());
        return luminance > 160 ? Color.BLACK : Color.WHITE;
    }

    private String safeSimpleToString(Card card) {
        try {
            return card.simpleToString();
        } catch (Exception e) {
            return card.getClass().getSimpleName();
        }
    }

    private String shortCardName(Card card) {
        String name = safeSimpleToString(card);
        if (name.length() <= 6) {
            return name;
        }
        return name.substring(0, 5) + ".";
    }

    private String abbreviate(String text, int maxLength) {
        if (text == null) {
            return "";
        }
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, Math.max(1, maxLength - 1)) + ".";
    }

    private String escape(String text) {
        return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    private void sendCommand(PlayerActionEnum action, int index) {
        if (beginCommand()) {
            runCommand(action.toString(), () -> virtualServer.sendCommand(new CommandPacket(action, index)));
        }
    }

    private void sendCommand(PlayerActionEnum action, Totem totem) {
        if (beginCommand()) {
            runCommand(action.toString(), () -> virtualServer.sendCommand(new CommandPacket(action, totem)));
        }
    }

    private void sendCommand(PlayerActionEnum action) {
        if (beginCommand()) {
            runCommand(action.toString(), () -> virtualServer.sendCommand(new CommandPacket(action)));
        }
    }

    public void clearCommandPending() {
        commandPending = false;
        refreshButtonsAfterPendingChange();
    }

    private boolean beginCommand() {
        if (commandPending) {
            return false;
        }
        commandPending = true;
        refreshButtonsAfterPendingChange();
        return true;
    }

    private void refreshButtonsAfterPendingChange() {
        updateActionButtonStates();
        if (latestGame != null) {
            clear(upperLinePanel);
            clear(lowerLinePanel);
            renderMarketRow(upperLinePanel, latestGame.getUpperLine(), PlayerActionEnum.DRAW_UPPER_CHARACTER,
                    latestGame.getUpperBuilding(), PlayerActionEnum.DRAW_UPPER_BUILDING);
            renderMarketRow(lowerLinePanel, latestGame.getLowerLine(), PlayerActionEnum.DRAW_LOWER_CHARACTER,
                    latestGame.getLowerBuilding(), PlayerActionEnum.DRAW_LOWER_BUILDING);
        }
        revalidate();
        repaint();
    }

    private void runCommand(String label, ThrowingRunnable runnable) {
        if (virtualServer == null) {
            commandPending = false;
            mainFrame.showError("Not connected", "Connect to a server first.");
            return;
        }
        Thread thread = new Thread(() -> {
            try {
                runnable.run();
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    commandPending = false;
                    mainFrame.showError(label + " failed", e.getMessage() == null ? e.toString() : e.getMessage());
                });
            }
        }, "gui-game-command");
        thread.setDaemon(true);
        thread.start();
    }

    private interface ThrowingRunnable {
        void run() throws Exception;
    }

    private static class RoundedPanel extends JPanel {
        private final Color background;
        private final int radius;

        private RoundedPanel(Color background, int radius) {
            this.background = background;
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(background);
            g.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g.dispose();
            super.paintComponent(graphics);
        }
    }

    private class TrackBandPanel extends JPanel {
        private final BufferedImage sheet;
        private List<MockupOrder> orderBoard = List.of();
        private List<MockupOffer> offerBoard = List.of();
        private int hoveredOfferSlot = -1;

        private TrackBandPanel(BufferedImage sheet) {
            this.sheet = sheet;
            setOpaque(false);
            setToolTipText("");
            MouseAdapter offerMouseHandler = new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent event) {
                    int offerSlot = offerSlotAt(event.getX(), event.getY());
                    if (offerSlot >= 0 && canChooseOfferSlot(offerSlot)) {
                        sendCommand(PlayerActionEnum.CHOOSE_OFFER, offerSlot);
                    }
                }

                @Override
                public void mouseMoved(MouseEvent event) {
                    updateHoveredOfferSlot(event.getX(), event.getY());
                }

                @Override
                public void mouseExited(MouseEvent event) {
                    updateHoveredOfferSlot(-1, -1);
                }
            };
            addMouseListener(offerMouseHandler);
            addMouseMotionListener(offerMouseHandler);
        }

        private void setOrderBoard(List<MockupOrder> orderBoard) {
            this.orderBoard = orderBoard == null ? List.of() : new ArrayList<>(orderBoard);
            repaint();
        }

        private void setOfferBoard(List<MockupOffer> offerBoard) {
            this.offerBoard = offerBoard == null ? List.of() : new ArrayList<>(offerBoard);
            repaint();
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

            int width = getWidth();
            int height = getHeight();
            if (sheet != null) {
                paintComposedTrack(g, width, height);
            } else {
                g.setPaint(new GradientPaint(0, 0, new Color(34, 127, 150),
                        width, height, new Color(232, 118, 43)));
                g.fillRoundRect(0, 0, width, height, 18, 18);

                int slots = Math.max(offerBoard.size(), 1);
                int orderWidth = Math.max(86, Math.min(118, width / 10));
                int offerWidth = Math.max(1, width - orderWidth - 4);
                int slotWidth = Math.max(60, offerWidth / slots);
                g.setColor(new Color(255, 255, 255, 80));
                g.setStroke(new BasicStroke(2f));
                for (int i = 1; i < slots; i++) {
                    int x = orderWidth + 4 + i * slotWidth;
                    g.drawLine(x, 10, x, height - 10);
                }

                g.setColor(new Color(255, 255, 255, 85));
                for (int i = 0; i < slots; i++) {
                    int x = orderWidth + 4 + i * slotWidth + slotWidth / 2 - 22;
                    g.drawRoundRect(x, 26, 44, 20, 6, 6);
                    g.drawRoundRect(x - 4, height - 42, 52, 20, 6, 6);
                }
            }

            paintOrderSlotGuides(g, width, height);
            paintOfferHover(g, width, height);
            paintTrackTotems(g, width, height);
            g.dispose();
            super.paintComponent(graphics);
        }

        private void paintComposedTrack(Graphics2D g, int width, int height) {
            int paddingY = Math.max(4, height / 18);
            int trackHeight = height - paddingY * 2;
            int orderWidth = Math.max(86, Math.min(118, width / 10));
            int gap = 4;
            int offerWidth = width - orderWidth - gap;

            drawOrderBoard(g, 0, paddingY, orderWidth, trackHeight);

            drawVisibleOfferCards(g, orderWidth + gap, paddingY, offerWidth, trackHeight);
        }

        private void drawOrderBoard(Graphics2D g, int x, int y, int width, int height) {
            Optional<BufferedImage> orderSheet = imageAssetManager.findOrderBoardSheet(orderBoardPlayerCount());
            if (orderSheet.isPresent()) {
                int[] source = orderBoardSourceRect(orderBoardPlayerCount());
                BufferedImage image = orderSheet.get();
                drawImageCropped(g, image, source[0], source[1], source[2], source[3], x, y, width, height);
                return;
            }
            drawCropped(g, 118, 80, 552, 748, x, y, width, height);
        }

        private void paintOrderSlotGuides(Graphics2D g, int width, int height) {
            int count = orderBoard.size();
            if (count <= 0) {
                return;
            }
            int paddingY = Math.max(4, height / 18);
            int trackHeight = height - paddingY * 2;
            int orderWidth = Math.max(86, Math.min(118, width / 10));
            for (int i = 0; i < count; i++) {
                int centerY = paddingY + orderSlotY(i, count, trackHeight);
                g.setColor(new Color(255, 239, 178, 70));
                g.fillOval(orderWidth / 2 - 18, centerY - 12, 36, 24);
                g.setColor(new Color(69, 39, 24, 190));
                g.setStroke(new BasicStroke(2f));
                g.drawOval(orderWidth / 2 - 18, centerY - 12, 36, 24);
            }
        }

        private void drawCropped(Graphics2D g, int sx1, int sy1, int sx2, int sy2,
                                 int dx, int dy, int dw, int dh) {
            drawImageCropped(g, sheet, sx1, sy1, sx2, sy2, dx, dy, dw, dh);
        }

        private void drawImageCropped(Graphics2D g, BufferedImage sourceImage, int sx1, int sy1, int sx2, int sy2,
                                      int dx, int dy, int dw, int dh) {
            g.drawImage(sourceImage, dx, dy, dx + dw, dy + dh, sx1, sy1, sx2, sy2, null);
        }

        private void drawVisibleOfferCards(Graphics2D g, int x, int y, int width, int height) {
            int[] sourceIndexes = offerSourceIndexes(offerBoard.size());
            if (sourceIndexes.length == 0) {
                return;
            }

            int cardGap = Math.max(3, width / 260);
            int slotCount = sourceIndexes.length;
            int baseWidth = Math.max(1, (width - cardGap * (slotCount - 1)) / slotCount);
            int cursor = x;
            for (int i = 0; i < slotCount; i++) {
                int sourceIndex = sourceIndexes[i];
                if (sourceIndex < 0 || sourceIndex >= OFFER_CARD_SOURCE_RECTS.length) {
                    continue;
                }
                int cardWidth = i == slotCount - 1 ? x + width - cursor : baseWidth;
                int[] source = OFFER_CARD_SOURCE_RECTS[sourceIndex];
                drawCropped(g, source[0], source[1], source[2], source[3], cursor, y, cardWidth, height);
                cursor += cardWidth + cardGap;
            }
        }

        private void paintTrackTotems(Graphics2D g, int width, int height) {
            int paddingY = Math.max(4, height / 18);
            int trackHeight = height - paddingY * 2;
            int orderWidth = Math.max(86, Math.min(118, width / 10));

            for (int i = 0; i < orderBoard.size(); i++) {
                MockupPlayer player = playerFor(orderBoard.get(i).getAssignedPlayerIndex());
                if (player == null) {
                    continue;
                }
                int x = orderWidth / 2 - 11;
                int y = paddingY + orderSlotY(i, orderBoard.size(), trackHeight) - 28;
                paintTotem(g, x, y, 22, 48, totemColor(player.getTotem()));
            }

            for (int i = 0; i < offerBoard.size(); i++) {
                MockupPlayer player = playerFor(offerBoard.get(i).getAssignedPlayerIndex());
                if (player == null) {
                    continue;
                }
                int[] bounds = offerSlotBounds(i, width, height);
                int x = bounds[0] + (bounds[2] - bounds[0]) / 2 - 11;
                int y = bounds[1] + (bounds[3] - bounds[1]) / 2 - 24;
                paintTotem(g, x, y, 22, 48, totemColor(player.getTotem()));
            }
        }

        private void paintOfferHover(Graphics2D g, int width, int height) {
            if (hoveredOfferSlot < 0 || !canChooseOfferSlot(hoveredOfferSlot)) {
                return;
            }
            int[] bounds = offerSlotBounds(hoveredOfferSlot, width, height);
            int arc = Math.max(6, Math.min(14, (bounds[3] - bounds[1]) / 5));
            g.setColor(new Color(255, 239, 178, 72));
            g.fillRoundRect(bounds[0], bounds[1], bounds[2] - bounds[0], bounds[3] - bounds[1], arc, arc);
            g.setColor(new Color(255, 244, 194, 210));
            g.setStroke(new BasicStroke(2f));
            g.drawRoundRect(bounds[0], bounds[1], bounds[2] - bounds[0], bounds[3] - bounds[1], arc, arc);
        }

        private int orderSlotY(int index, int count, int trackHeight) {
            double[] slotPositions = orderBoardSlotPositions(orderBoardPlayerCount());
            if (index >= 0 && index < slotPositions.length) {
                return (int) Math.round(trackHeight * slotPositions[index]);
            }
            if (count <= 1) {
                return trackHeight / 2;
            }
            double top = 0.17;
            double bottom = 0.72;
            return (int) Math.round(trackHeight * (top + (bottom - top) * index / (count - 1)));
        }

        private int orderBoardPlayerCount() {
            if (playerCount >= 2 && playerCount <= 5) {
                return playerCount;
            }
            return Math.max(2, Math.min(5, orderBoard.size()));
        }

        private int[] orderBoardSourceRect(int count) {
            switch (count) {
                case 2:
                    return new int[] {1418, 140, 1852, 809};
                case 3:
                    return new int[] {116, 140, 552, 809};
                case 4:
                    return new int[] {1418, 80, 1852, 748};
                case 5:
                    return new int[] {118, 80, 552, 748};
                default:
                    return new int[] {118, 80, 552, 748};
            }
        }

        private double[] orderBoardSlotPositions(int count) {
            switch (count) {
                case 2:
                    return new double[] {0.245, 0.545};
                case 3:
                    return new double[] {0.18, 0.38, 0.78};
                case 4:
                    return new double[] {0.18, 0.37, 0.57, 0.78};
                case 5:
                    return new double[] {0.17, 0.35, 0.52, 0.68, 0.84};
                default:
                    return new double[0];
            }
        }

        private int offerSlotAt(int mouseX, int mouseY) {
            int count = offerBoard.size();
            if (count == 0 || mouseX < 0 || mouseY < 0) {
                return -1;
            }

            int width = getWidth();
            int height = getHeight();
            for (int i = 0; i < count; i++) {
                int[] bounds = offerSlotBounds(i, width, height);
                if (mouseX >= bounds[0] && mouseX <= bounds[2] && mouseY >= bounds[1] && mouseY <= bounds[3]) {
                    return i;
                }
            }
            return -1;
        }

        private int[] offerSlotBounds(int offerSlot, int width, int height) {
            int count = offerBoard.size();
            if (offerSlot < 0 || offerSlot >= count) {
                return new int[] {0, 0, 0, 0};
            }
            int paddingY = Math.max(4, height / 18);
            int trackHeight = height - paddingY * 2;
            int orderWidth = Math.max(86, Math.min(118, width / 10));
            int gap = 4;
            int offerWidth = width - orderWidth - gap;
            int cardGap = Math.max(3, offerWidth / 260);
            int baseWidth = Math.max(1, (offerWidth - cardGap * (count - 1)) / count);
            int left = orderWidth + gap + offerSlot * (baseWidth + cardGap);
            int right = offerSlot == count - 1 ? orderWidth + gap + offerWidth : left + baseWidth;
            int horizontalPad = Math.max(2, (right - left) / 24);
            int verticalPad = Math.max(2, trackHeight / 22);
            return new int[] {
                    left + horizontalPad,
                    paddingY + verticalPad,
                    right - horizontalPad,
                    paddingY + trackHeight - verticalPad
            };
        }

        private boolean isEmptyOfferSlot(int offerSlot) {
            return offerSlot >= 0 && offerSlot < offerBoard.size()
                    && offerBoard.get(offerSlot).getAssignedPlayerIndex() == null;
        }

        private void updateHoveredOfferSlot(int mouseX, int mouseY) {
            int nextHoveredOfferSlot = offerSlotAt(mouseX, mouseY);
            if (nextHoveredOfferSlot >= 0 && !canChooseOfferSlot(nextHoveredOfferSlot)) {
                nextHoveredOfferSlot = -1;
            }
            if (hoveredOfferSlot != nextHoveredOfferSlot) {
                hoveredOfferSlot = nextHoveredOfferSlot;
                setCursor(hoveredOfferSlot >= 0
                        ? Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
                        : Cursor.getDefaultCursor());
                repaint();
            }
        }

        @Override
        public String getToolTipText(MouseEvent event) {
            int offerSlot = offerSlotAt(event.getX(), event.getY());
            if (offerSlot < 0) {
                return null;
            }
            MockupPlayer assignedPlayer = playerFor(offerBoard.get(offerSlot).getAssignedPlayerIndex());
            if (assignedPlayer == null) {
                return currentPhaseName.equals(State.States.OFFER_CHOOSING.toString())
                        ? "Choose offer " + offerLetter(offerSlot, offerBoard.size())
                        : "Offer " + offerLetter(offerSlot, offerBoard.size());
            }
            return "Offer " + offerLetter(offerSlot, offerBoard.size()) + ": " + assignedPlayer.getNickname();
        }

        private boolean canChooseOfferSlot(int offerSlot) {
            return currentPhaseName.equals(State.States.OFFER_CHOOSING.toString()) && isEmptyOfferSlot(offerSlot);
        }

        private MockupPlayer playerFor(Integer playerIndex) {
            if (latestGame == null || playerIndex == null) {
                return null;
            }
            return findPlayer(latestGame.getPlayers(), playerIndex);
        }
    }

    private static class ImageCardButton extends JButton {
        private final Image cardImage;
        private final Color fallbackColor;
        private final boolean locked;

        private ImageCardButton(Image cardImage, Color fallbackColor, boolean locked) {
            this.cardImage = cardImage;
            this.fallbackColor = fallbackColor;
            this.locked = locked;
            setText("");
            setContentAreaFilled(false);
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

            int width = getWidth();
            int height = getHeight();
            g.setColor(new Color(48, 34, 24));
            g.fillRoundRect(0, 0, width, height, 4, 4);

            int margin = 3;
            int imageHeight = height - margin * 2;
            if (cardImage != null) {
                g.drawImage(cardImage, margin, margin, width - margin * 2, imageHeight, null);
            } else {
                g.setColor(fallbackColor);
                g.fillRoundRect(margin, margin, width - margin * 2, imageHeight, 4, 4);
            }

            if (locked) {
                g.setColor(new Color(0, 0, 0, 110));
                g.fillRoundRect(margin, margin, width - margin * 2, imageHeight, 4, 4);
            }

            if (getModel().isRollover() && isEnabled()) {
                g.setColor(new Color(255, 238, 190, 90));
                g.fillRoundRect(0, 0, width, height, 4, 4);
            }
            g.dispose();
        }
    }

    private static void paintTotem(Graphics2D g, int x, int y, int width, int height, Color color) {
        Color shadow = new Color(Math.max(0, color.getRed() - 70),
                Math.max(0, color.getGreen() - 70),
                Math.max(0, color.getBlue() - 70));
        g.setColor(new Color(0, 0, 0, 70));
        g.fillRoundRect(x + 4, y + 6, width, height, 9, 9);
        g.setColor(color);
        g.fillRoundRect(x, y, width, height, 9, 9);
        g.setColor(shadow);
        g.setStroke(new BasicStroke(3f));
        int center = x + width / 2;
        g.drawLine(center, y + 18, center, y + height - 8);
        g.drawLine(x + 7, y + height / 2, center, y + height - 17);
        g.drawLine(x + width - 7, y + height / 2, center, y + height - 17);
        g.drawRect(x + 7, y + 10, 5, 7);
        g.drawRect(x + width - 12, y + 10, 5, 7);
    }

    private static void drawCentered(Graphics2D g, String text, int width, int y) {
        int textWidth = g.getFontMetrics().stringWidth(text);
        g.drawString(text, Math.max(4, (width - textWidth) / 2), y);
    }

}
