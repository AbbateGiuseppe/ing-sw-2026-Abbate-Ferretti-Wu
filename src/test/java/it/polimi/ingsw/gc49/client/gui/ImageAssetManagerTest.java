package it.polimi.ingsw.gc49.client.gui;

import it.polimi.ingsw.gc49.server.model.BuildingEvent;
import it.polimi.ingsw.gc49.server.model.Card.BuildingCard.BonusFoodEndTurnCard;
import it.polimi.ingsw.gc49.server.model.Card.BuildingCard.BonusPointsByClassEndGameCard;
import it.polimi.ingsw.gc49.server.model.Card.Card;
import it.polimi.ingsw.gc49.server.model.Card.CharacterCard.Builder;
import it.polimi.ingsw.gc49.server.model.Card.CharacterCard.Hunter;
import it.polimi.ingsw.gc49.server.model.Card.CharacterCard.Invention;
import it.polimi.ingsw.gc49.server.model.Card.CharacterCard.Inventor;
import it.polimi.ingsw.gc49.server.model.Card.EventCard.HuntingEvent;
import it.polimi.ingsw.gc49.server.model.CardBoard.Deck;
import it.polimi.ingsw.gc49.server.model.CharacterType;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.EventManager;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ImageAssetManagerTest {
    private final ImageAssetManager imageAssetManager = new ImageAssetManager();

    @Test
    void loadsCustomerGraphicsPack() {
        assertTrue(imageAssetManager.getCustomerAssetCount() >= 60);
        assertTrue(imageAssetManager.getCardImageCount() >= 50);
    }

    @Test
    void mapsNamedCustomerGraphicsToCards() throws Exception {
        assertNamedImage(new Builder(1, 2, Era.FIRST, 2, null), "Builder 1 2.jpg");
        assertNamedImage(new Hunter(true, Era.FIRST, 2, null), "Hunter true.jpg");
        assertNamedImage(new Inventor(Invention.ARROWHEAD, Era.FIRST, 2, null), "Inventor arrowhead.jpg");
        assertNamedImage(new HuntingEvent(1, new EventManager(), Era.FIRST, 2, null), "HuntingEvent 1.jpg");
        assertNamedImage(new BonusFoodEndTurnCard(BuildingEvent.TURN_END, 3, 3, Era.FIRST, 2, null),
                "3 3 BonusFoodEndTurnCard.jpg");
        assertNamedImage(new BonusPointsByClassEndGameCard(
                        CharacterType.Builder, 4, BuildingEvent.GAME_END, 3, 6, Era.THIRD, 2, null),
                "3 6 BonusPointsByClassEndGameCard Builder 4.jpg");
    }



    @SuppressWarnings("unchecked")
    private void assertNamedImage(Card card, String expectedFileName) throws Exception {
        Method method = ImageAssetManager.class.getDeclaredMethod("findNamedGraphicImage", Card.class);
        method.setAccessible(true);
        Optional<Path> path = (Optional<Path>) method.invoke(imageAssetManager, card);
        assertTrue(path.isPresent(), "Missing image for " + card.getClass().getSimpleName());
        assertTrue(path.get().getFileName().toString().equalsIgnoreCase(expectedFileName),
                "Expected " + expectedFileName + " but found " + path.get());
    }
}
