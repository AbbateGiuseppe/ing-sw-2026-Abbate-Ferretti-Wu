package it.polimi.ingsw.gc49.client.gui;

import it.polimi.ingsw.gc49.client.view.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.client.view.mockupModel.MockupOffer;
import it.polimi.ingsw.gc49.client.view.mockupModel.MockupOrder;
import it.polimi.ingsw.gc49.client.view.mockupModel.MockupPlayer;
import it.polimi.ingsw.gc49.server.model.BuildingEvent;
import it.polimi.ingsw.gc49.server.model.Card.BuildingCard.BonusHuntingCard;
import it.polimi.ingsw.gc49.server.model.Card.Card;
import it.polimi.ingsw.gc49.server.model.Card.CharacterCard.Artist;
import it.polimi.ingsw.gc49.server.model.Card.CharacterCard.Builder;
import it.polimi.ingsw.gc49.server.model.Card.CharacterCard.Hunter;
import it.polimi.ingsw.gc49.server.model.Card.EventCard.HuntingEvent;
import it.polimi.ingsw.gc49.server.model.Card.EventCard.PaintingEvent;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.Totem;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertTrue;

class GamePanelRenderSmokeTest {
    private static final int PANEL_WIDTH = 1100;
    private static final int PANEL_HEIGHT = 688;

    @Test
    void rendersBoardWithCardImages() throws Exception {
        System.setProperty("java.awt.headless", "true");
        AtomicReference<BufferedImage> rendered = new AtomicReference<>();

        SwingUtilities.invokeAndWait(() -> {
            ImageAssetManager imageAssetManager = new ImageAssetManager();
            GamePanel panel = new GamePanel(null, imageAssetManager);
            panel.setSize(PANEL_WIDTH, PANEL_HEIGHT);
            panel.setBounds(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
            panel.refresh(mockGame());
            layoutTree(panel);

            BufferedImage image = new BufferedImage(PANEL_WIDTH, PANEL_HEIGHT, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = image.createGraphics();
            panel.printAll(graphics);
            graphics.dispose();
            rendered.set(image);
        });

        BufferedImage image = rendered.get();
        Path output = Path.of("target", "gui-render-smoke.png");
        Files.createDirectories(output.getParent());
        ImageIO.write(image, "png", output.toFile());

        assertTrue(hasEnoughColorVariation(image), "Rendered GUI should not be blank");
    }

    @Test
    void cardTooltipIncludesSubclassDetails() throws Exception {
        GamePanel panel = new GamePanel(null, new ImageAssetManager());

        String hunterTooltip = tooltipHtml(panel, new Hunter(true, Era.FIRST, 2, null));
        assertTrue(hunterTooltip.contains("Drumstick: true"));
        assertTrue(hunterTooltip.contains("Effect"));

        String paintingTooltip = tooltipHtml(panel, new PaintingEvent(0, 1, 2, null, Era.FIRST, 2, null));
        assertTrue(paintingTooltip.contains("Threshold: 0"));
        assertTrue(paintingTooltip.contains("Plus Points: 1"));
        assertTrue(paintingTooltip.contains("Minus Points: 2"));

        String buildingTooltip = tooltipHtml(panel, new BonusHuntingCard(BuildingEvent.HUNTING_EVENT, 5, 3, Era.FIRST, 2, null));
        assertTrue(buildingTooltip.contains("Building Event: HUNTING_EVENT"));
        assertTrue(buildingTooltip.contains("Points Endgame: 5"));
        assertTrue(buildingTooltip.contains("Food Price: 3"));
    }

    @Test
    void playerAreaAllowsVerticalScrollingWhenCardsOverflow() throws Exception {
        AtomicReference<JScrollPane> scrollPane = new AtomicReference<>();
        SwingUtilities.invokeAndWait(() -> {
            GamePanel panel = new GamePanel(null, new ImageAssetManager());
            panel.setSize(PANEL_WIDTH, PANEL_HEIGHT);
            panel.refresh(mockGame());
            layoutTree(panel);
            scrollPane.set(findScrollPane(panel, "players-scroll"));
        });

        assertTrue(scrollPane.get() != null, "Players scroll pane should exist");
        assertTrue(scrollPane.get().getVerticalScrollBarPolicy() == JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                "Players area should show a vertical scrollbar when content does not fit");
    }

    private JScrollPane findScrollPane(Component component, String name) {
        if (component instanceof JScrollPane scrollPane && name.equals(scrollPane.getName())) {
            return scrollPane;
        }
        if (component instanceof Container container) {
            for (Component child : container.getComponents()) {
                JScrollPane found = findScrollPane(child, name);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }

    private String tooltipHtml(GamePanel panel, Card card) throws Exception {
        Method method = GamePanel.class.getDeclaredMethod("cardHtml", Card.class, int.class);
        method.setAccessible(true);
        return (String) method.invoke(panel, card, 0);
    }

    private MockupGame mockGame() {
        MockupPlayer ada = new MockupPlayer("Ada", 0, true, Totem.ORANGE, 5, 2, new ArrayList<>(), new ArrayList<>(), 0, 0);
        ada.addCharacterCard(new Hunter(true, Era.FIRST, 2, null));
        ada.addCharacterCard(new Artist(Era.FIRST, 2, null));
        MockupPlayer ben = new MockupPlayer("Ben", 1, true, Totem.BLUE, 3, 0, new ArrayList<>(), new ArrayList<>(), 0 ,0);
        ben.addBuildingCard(new BonusHuntingCard(BuildingEvent.HUNTING_EVENT, 5, 3, Era.FIRST, 2, null));

        return new MockupGame(
                List.of(ada, ben),
                Era.FIRST,
                List.of(
                        new Hunter(true, Era.FIRST, 2, null),
                        new Artist(Era.FIRST, 2, null),
                        new Builder(1, 2, Era.FIRST, 2, null)
                ),
                List.of(
                        new HuntingEvent(1, null, Era.FIRST, 2, null),
                        new Hunter(false, Era.FIRST, 2, null)
                ),
                List.of(new BonusHuntingCard(BuildingEvent.HUNTING_EVENT, 4, 2, Era.FIRST, 2, null)),
                List.of(new BonusHuntingCard(BuildingEvent.HUNTING_EVENT, 5, 3, Era.FIRST, 2, null)),
                List.of(new MockupOffer(0, 1, 0, null), new MockupOffer(0, 0, 1, 1)),
                List.of(new MockupOrder(1, false, 0, 0, 0), new MockupOrder(0, true, 1, 2, null))
        );
    }

    private boolean hasEnoughColorVariation(BufferedImage image) {
        Set<Integer> colors = new HashSet<>();
        for (int y = 10; y < image.getHeight(); y += 20) {
            for (int x = 10; x < image.getWidth(); x += 20) {
                colors.add(image.getRGB(x, y));
            }
        }
        return colors.size() > 20;
    }

    private void layoutTree(Component component) {
        if (component instanceof Container container) {
            container.doLayout();
            for (Component child : container.getComponents()) {
                layoutTree(child);
            }
        }
    }
}
