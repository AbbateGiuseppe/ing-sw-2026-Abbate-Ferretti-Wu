package it.polimi.ingsw.gc49.server.model.Card.CharacterCard;

import it.polimi.ingsw.gc49.client.view.ItaEngRectangleAttributedString;
import it.polimi.ingsw.gc49.client.view.RectangleAttributedString;
import it.polimi.ingsw.gc49.server.model.Era;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Covers the terminal-rendering methods ({@code getRectangleAttributedString},
 * {@code getItaEngRectangleAttributedString}) and {@code toString} for every concrete
 * {@link CharacterCard}. These methods contain no branching logic — they just build a fixed
 * 4x5 drawing — so a single invocation per card is enough to cover their lines.
 * <p>
 * For {@link Hunter}, {@link Shaman} and {@link Inventor} the variants (drumstick on/off,
 * different star counts, every invention type) are included to cover any internal switch.
 */
class CharacterCardRenderingTest {

    /** Every concrete character card variant. */
    private static List<Supplier<CharacterCard>> allCards() {
        List<Supplier<CharacterCard>> list = new ArrayList<>();
        // Hunter: both drumstick variants
        list.add(() -> new Hunter(false, Era.FIRST, 2, null));
        list.add(() -> new Hunter(true, Era.FIRST, 2, null));
        // Builder: representative values
        list.add(() -> new Builder(2, 5, Era.FIRST, 2, null));
        list.add(() -> new Builder(1, 3, Era.SECOND, 2, null));
        // Shaman: every numStars from 1 to 3
        list.add(() -> new Shaman(1, Era.FIRST, 2, null));
        list.add(() -> new Shaman(2, Era.SECOND, 2, null));
        list.add(() -> new Shaman(3, Era.THIRD, 2, null));
        // Artist
        list.add(() -> new Artist(Era.FIRST, 2, null));
        // Gatherer
        list.add(() -> new Gatherer(Era.FIRST, 2, null));
        // Inventor: every invention
        for (Invention inv : Invention.values()) {
            list.add(() -> new Inventor(inv, Era.FIRST, 2, null));
        }
        return list;
    }

    @Test
    @DisplayName("getRectangleAttributedString returns a 4x5 drawing for every card variant")
    void rectangleForEveryCard() {
        for (Supplier<CharacterCard> supplier : allCards()) {
            CharacterCard card = supplier.get();
            RectangleAttributedString rect = card.getRectangleAttributedString();
            assertNotNull(rect, card.getClass().getSimpleName() + " rectangle must not be null");
            assertEquals(4, rect.height, card.getClass().getSimpleName() + " height");
            assertEquals(5, rect.width, card.getClass().getSimpleName() + " width");
        }
    }

    @Test
    @DisplayName("getItaEngRectangleAttributedString returns ITA and ENG drawings for every card variant")
    void itaEngForEveryCard() {
        for (Supplier<CharacterCard> supplier : allCards()) {
            CharacterCard card = supplier.get();
            ItaEngRectangleAttributedString itaEng = card.getItaEngRectangleAttributedString();
            assertNotNull(itaEng, card.getClass().getSimpleName() + " itaEng must not be null");
            assertNotNull(itaEng.itaRectangleAttributedString);
            assertNotNull(itaEng.engRectangleAttributedString);
            assertEquals(4, itaEng.itaRectangleAttributedString.height);
            assertEquals(5, itaEng.itaRectangleAttributedString.width);
            assertEquals(4, itaEng.engRectangleAttributedString.height);
            assertEquals(5, itaEng.engRectangleAttributedString.width);
        }
    }

    @Test
    @DisplayName("toString is non-blank for every card variant")
    void toStringForEveryCard() {
        for (Supplier<CharacterCard> supplier : allCards()) {
            CharacterCard card = supplier.get();
            String s = card.toString();
            assertNotNull(s);
            assertFalse(s.isBlank(), card.getClass().getSimpleName() + " toString must not be blank");
        }
    }

    @Test
    @DisplayName("simpleToString returns the localized name for every card variant")
    void simpleToStringForEveryCard() {
        for (Supplier<CharacterCard> supplier : allCards()) {
            CharacterCard card = supplier.get();
            String s = card.simpleToString();
            assertNotNull(s);
            assertFalse(s.isBlank());
        }
    }
}
