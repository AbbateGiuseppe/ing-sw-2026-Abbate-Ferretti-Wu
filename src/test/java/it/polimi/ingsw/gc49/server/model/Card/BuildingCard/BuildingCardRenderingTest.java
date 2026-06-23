package it.polimi.ingsw.gc49.server.model.Card.BuildingCard;

import it.polimi.ingsw.gc49.client.view.ItaEngRectangleAttributedString;
import it.polimi.ingsw.gc49.client.view.RectangleAttributedString;
import it.polimi.ingsw.gc49.server.model.BuildingEvent;
import it.polimi.ingsw.gc49.server.model.CharacterType;
import it.polimi.ingsw.gc49.server.model.Era;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Covers the terminal-rendering methods ({@code getRectangleAttributedString},
 * {@code getItaEngRectangleAttributedString}) and {@code toString} for every concrete
 * {@link BuildingCard}. These methods contain no branching logic — they just build a fixed
 * 2x10 drawing — so a single invocation per card is enough to cover their lines.
 */
class BuildingCardRenderingTest {

    /** Every concrete building card, built with representative constructor values. */
    private static List<Supplier<BuildingCard>> allCards() {
        return List.of(
                () -> new BonusFoodEndTurnCard(BuildingEvent.TURN_END, 1, 5, Era.FIRST, 2, null),
                () -> new BonusHuntingCard(BuildingEvent.HUNTING_EVENT, 3, 5, Era.SECOND, 2, null),
                () -> new BonusPaintingCard(BuildingEvent.PAINTING_EVENT, 6, 5, Era.SECOND, 2, null),
                () -> new BonusPointsByClassEndGameCard(CharacterType.Hunter, 3, BuildingEvent.GAME_END, 8, 8, Era.THIRD, 2, null),
                () -> new CharacterSetCompleteFoodCard(BuildingEvent.DRAW_EVENT, 3, 4, Era.FIRST, 2, null),
                () -> new CharacterSetCompletePointEndGameCard(BuildingEvent.GAME_END, 6, 5, Era.SECOND, 2, null),
                () -> new DoubleBuilderPointsCard(BuildingEvent.GAME_END, 4, 6, Era.SECOND, 2, null),
                () -> new DoubleShamanPointsCard(BuildingEvent.RITUAL_EVENT, 0, 7, Era.SECOND, 2, null),
                () -> new OneMoreCardCard(BuildingEvent.DRAW_EVENT, 3, 9, Era.THIRD, 2, null),
                () -> new SamePairInventionsCard(BuildingEvent.DRAW_EVENT, 4, 3, Era.FIRST, 2, null),
                () -> new ShamanicImmunityCard(BuildingEvent.RITUAL_EVENT, 2, 5, Era.FIRST, 2, null),
                () -> new ShamanicThreeStarCard(BuildingEvent.RITUAL_EVENT, 4, 6, Era.SECOND, 2, null),
                () -> new SustainDiscountByClassCard(CharacterType.Gatherer, BuildingEvent.SUSTENANCE_EVENT, 4, 4, Era.FIRST, 2, null),
                () -> new TwentyFiveBonusPointsEndGame(BuildingEvent.GAME_END, 25, 10, Era.THIRD, 2, null)
        );
    }

    @Test
    void rectangleForEveryCard() {
        for (Supplier<BuildingCard> supplier : allCards()) {
            BuildingCard card = supplier.get();
            RectangleAttributedString rect = card.getRectangleAttributedString();
            assertNotNull(rect, card.getClass().getSimpleName() + " rectangle must not be null");
            assertEquals(2, rect.height, card.getClass().getSimpleName() + " height");
            assertEquals(10, rect.width, card.getClass().getSimpleName() + " width");
        }
    }

    @Test
    void itaEngForEveryCard() {
        for (Supplier<BuildingCard> supplier : allCards()) {
            BuildingCard card = supplier.get();
            ItaEngRectangleAttributedString itaEng = card.getItaEngRectangleAttributedString();
            assertNotNull(itaEng, card.getClass().getSimpleName() + " itaEng must not be null");
            assertNotNull(itaEng.itaRectangleAttributedString);
            assertNotNull(itaEng.engRectangleAttributedString);
            assertEquals(2, itaEng.itaRectangleAttributedString.height);
            assertEquals(10, itaEng.itaRectangleAttributedString.width);
            assertEquals(2, itaEng.engRectangleAttributedString.height);
            assertEquals(10, itaEng.engRectangleAttributedString.width);
        }
    }

    @Test
    void toStringForEveryCard() {
        for (Supplier<BuildingCard> supplier : allCards()) {
            BuildingCard card = supplier.get();
            String s = card.toString();
            assertNotNull(s);
            assertFalse(s.isBlank(), card.getClass().getSimpleName() + " toString must not be blank");
        }
    }

    @Test
    void simpleToStringForEveryCard() {
        for (Supplier<BuildingCard> supplier : allCards()) {
            BuildingCard card = supplier.get();
            String s = card.simpleToString();
            assertNotNull(s);
            assertTrue(s.startsWith("EDIFICIO"), card.getClass().getSimpleName() + ": " + s);
        }
    }
}
