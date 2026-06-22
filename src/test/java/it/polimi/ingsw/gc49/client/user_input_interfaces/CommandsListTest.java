package it.polimi.ingsw.gc49.client.user_input_interfaces;

import it.polimi.ingsw.gc49.ItaEngString;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests that every constant declared in {@link CommandsList} is a non-null {@link ItaEngString}.
 * <p>
 * The class is a pure container of i18n string constants; the goal here is mostly coverage:
 * every {@code public static final ItaEngString} field is touched, and a few key commands are
 * checked for the expected localized values.
 */
class CommandsListTest {

    @Test
    @DisplayName("every public static final ItaEngString constant is non-null")
    void everyConstantIsNonNull() throws IllegalAccessException {
        for (Field f : CommandsList.class.getDeclaredFields()) {
            int mods = f.getModifiers();
            if (Modifier.isPublic(mods) && Modifier.isStatic(mods) && Modifier.isFinal(mods)
                    && f.getType() == ItaEngString.class) {
                Object value = f.get(null);
                assertNotNull(value, f.getName() + " must not be null");
            }
        }
    }

    @Test
    @DisplayName("the HELP command localizes to 'aiuto' (ITA) and 'help' (ENG)")
    void helpStrings() {
        assertEquals("aiuto", CommandsList.HELP.print(ItaEngString.Language.ITA));
        assertEquals("help", CommandsList.HELP.print(ItaEngString.Language.ENG));
    }

    @Test
    @DisplayName("the DRAW command localizes to 'pesca' (ITA) and 'draw' (ENG)")
    void drawStrings() {
        assertEquals("pesca", CommandsList.DRAW.print(ItaEngString.Language.ITA));
        assertEquals("draw", CommandsList.DRAW.print(ItaEngString.Language.ENG));
    }

    @Test
    @DisplayName("the TOTEM command localizes to 'totemo' (ITA) and 'totem' (ENG)")
    void totemStrings() {
        assertEquals("totemo", CommandsList.TOTEM.print(ItaEngString.Language.ITA));
        assertEquals("totem", CommandsList.TOTEM.print(ItaEngString.Language.ENG));
    }

    @Test
    @DisplayName("the LEGEND command localizes to 'legenda' (ITA) and 'legend' (ENG)")
    void legendStrings() {
        assertEquals("legenda", CommandsList.LEGEND.print(ItaEngString.Language.ITA));
        assertEquals("legend", CommandsList.LEGEND.print(ItaEngString.Language.ENG));
    }

    @Test
    @DisplayName("aliases share the same ItaEngString instance with their source command")
    void aliasesShareReferences() {
        assertSame(CommandsList.DRAW_p_01, CommandsList.READ_p_01);
        assertSame(CommandsList.DRAW_p_02, CommandsList.READ_p_02);
        assertSame(CommandsList.PLAYER_et, CommandsList.OFFER_et_01);
        assertSame(CommandsList.PLAYER_em, CommandsList.OFFER_em_01);
    }
}
