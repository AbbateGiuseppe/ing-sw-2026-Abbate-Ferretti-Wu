package it.polimi.ingsw.gc49.client.user_input_interfaces;

import it.polimi.ingsw.gc49.ItaEngString;
import org.jline.utils.AttributedString;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link Legend}. The class only exposes a single {@code print} method that
 * formats the legend in either Italian or English.
 */
class LegendTest {

    @Test
    @DisplayName("print(IT) returns a non-empty Italian legend mentioning the symbols")
    void printItalian() {
        Legend legend = new Legend();
        AttributedString out = legend.print(ItaEngString.Language.ITA);
        assertNotNull(out);
        String s = out.toString();
        assertFalse(s.isEmpty());
        assertTrue(s.contains("cibo"));
        assertTrue(s.contains("punti"));
    }

    @Test
    @DisplayName("print(EN) returns a non-empty English legend")
    void printEnglish() {
        Legend legend = new Legend();
        AttributedString out = legend.print(ItaEngString.Language.ENG);
        assertNotNull(out);
        String s = out.toString();
        assertFalse(s.isEmpty());
    }

    @Test
    @DisplayName("ITA and ENG legends differ")
    void itaAndEngDiffer() {
        Legend legend = new Legend();
        String ita = legend.print(ItaEngString.Language.ITA).toString();
        String eng = legend.print(ItaEngString.Language.ENG).toString();
        assertNotEquals(ita, eng);
    }
}
