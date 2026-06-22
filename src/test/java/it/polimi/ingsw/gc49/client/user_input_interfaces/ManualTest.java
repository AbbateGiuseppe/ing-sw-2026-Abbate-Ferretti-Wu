package it.polimi.ingsw.gc49.client.user_input_interfaces;

import it.polimi.ingsw.gc49.ItaEngString;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;
import org.jline.utils.AttributedString;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link Manual}. The class statically builds a per-{@link ApplicationPhase} manual
 * and exposes a single {@code print(phase, language)} method.
 */
class ManualTest {

    @Test
    @DisplayName("print returns a non-empty manual for every ApplicationPhase in ITA")
    void printForEveryPhaseItalian() {
        Manual manual = new Manual();
        for (ApplicationPhase phase : ApplicationPhase.values()) {
            AttributedString out = manual.print(phase, ItaEngString.Language.ITA);
            assertNotNull(out, phase + " ITA manual must not be null");
            assertFalse(out.toString().isEmpty(), phase + " ITA manual must not be empty");
        }
    }

    @Test
    @DisplayName("print returns a non-empty manual for every ApplicationPhase in ENG")
    void printForEveryPhaseEnglish() {
        Manual manual = new Manual();
        for (ApplicationPhase phase : ApplicationPhase.values()) {
            AttributedString out = manual.print(phase, ItaEngString.Language.ENG);
            assertNotNull(out, phase + " ENG manual must not be null");
            assertFalse(out.toString().isEmpty(), phase + " ENG manual must not be empty");
        }
    }

    @Test
    @DisplayName("the ITA manual contains 'comandi GENERALI' and the ENG manual contains 'GENERAL commands'")
    void manualsAreLocalized() {
        Manual manual = new Manual();
        for (ApplicationPhase phase : ApplicationPhase.values()) {
            String ita = manual.print(phase, ItaEngString.Language.ITA).toString();
            String eng = manual.print(phase, ItaEngString.Language.ENG).toString();
            assertTrue(ita.contains("GENERALI"), phase + " ITA must mention GENERALI");
            assertTrue(eng.contains("GENERAL"), phase + " ENG must mention GENERAL");
        }
    }

    @Test
    @DisplayName("ITA and ENG manuals are different for every phase")
    void manualsDiffer() {
        Manual manual = new Manual();
        for (ApplicationPhase phase : ApplicationPhase.values()) {
            String ita = manual.print(phase, ItaEngString.Language.ITA).toString();
            String eng = manual.print(phase, ItaEngString.Language.ENG).toString();
            assertNotEquals(ita, eng, phase + " ITA and ENG must differ");
        }
    }
}
