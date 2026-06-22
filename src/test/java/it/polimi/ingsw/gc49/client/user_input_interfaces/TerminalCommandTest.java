package it.polimi.ingsw.gc49.client.user_input_interfaces;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link TerminalCommand} is a {@code @FunctionalInterface}. There is no behaviour to test
 * beyond verifying that a lambda matching the signature compiles and can be invoked.
 */
class TerminalCommandTest {

    @Test
    @DisplayName("a lambda matching the signature can be created and invoked")
    void lambdaIsInvokable() throws Exception {
        AtomicBoolean called = new AtomicBoolean(false);
        TerminalCommand cmd = (terminalMethods, terminalPhase, terminalMockups, terminalParameters, virtualServer) ->
                called.set(true);

        // every parameter is allowed to be null because the lambda ignores them
        cmd.execute(null, null, null, new String[]{"x", "y"}, null);

        assertTrue(called.get(), "the lambda body must have been executed");
    }

    @Test
    @DisplayName("a TerminalCommand lambda may throw a checked Exception")
    void lambdaCanThrow() {
        TerminalCommand thrower = (a, b, c, d, e) -> {
            throw new Exception("boom");
        };
        assertThrows(Exception.class, () -> thrower.execute(null, null, null, new String[0], null));
    }
}
