package it.polimi.ingsw.gc49.model;

import it.polimi.ingsw.gc49.server.model.Game;
import it.polimi.ingsw.gc49.server.model.Locks;
import it.polimi.ingsw.gc49.server.model.Player;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameDisconnectionResilienceTest {
    private static final String TIMEOUT_PROPERTY = "gc49.disconnectWinnerTimeoutMillis";

    @Test
    void onlyConnectedPlayerWinsAfterSuspensionTimeout() {
        String previousTimeout = System.getProperty(TIMEOUT_PROPERTY);
        System.setProperty(TIMEOUT_PROPERTY, "25");
        try {
            Game game = new Game(2, List.of("lorenzo", "diego"), "test-room");
            Player lorenzo = game.getPlayers().get(0);
            Player diego = game.getPlayers().get(1);
            diego.setConnected(false);

            synchronized (Locks.playerInput) {
                assertTrue(game.waitIfGameSuspendedByDisconnections());
            }

            assertTrue(game.hasForcedWinner());
            assertSame(lorenzo, game.getForcedWinner());
        } finally {
            restoreTimeoutProperty(previousTimeout);
        }
    }

    @Test
    void gameContinuesWhenAnotherPlayerReconnectsBeforeTimeout() throws Exception {
        String previousTimeout = System.getProperty(TIMEOUT_PROPERTY);
        System.setProperty(TIMEOUT_PROPERTY, "1000");
        try {
            Game game = new Game(2, List.of("lorenzo", "diego"), "test-room");
            Player diego = game.getPlayers().get(1);
            diego.setConnected(false);

            Thread reconnect = new Thread(() -> {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                synchronized (Locks.playerInput) {
                    diego.setConnected(true);
                    Locks.playerInput.notifyAll();
                }
            });

            synchronized (Locks.playerInput) {
                reconnect.start();
                assertFalse(game.waitIfGameSuspendedByDisconnections());
            }
            reconnect.join();

            assertFalse(game.hasForcedWinner());
            assertTrue(diego.isConnected());
        } finally {
            restoreTimeoutProperty(previousTimeout);
        }
    }

    @Test
    void reconnectedRemovedPlayerReturnsToOrderBoard() {
        Game game = new Game(3, List.of("a1", "a2", "a3"), "test-room");
        Player player = game.getPlayers().get(1);
        player.setConnected(false);
        player.setRemovedFromTrack(true);

        player.setConnected(true);
        game.restoreConnectedRemovedPlayersToTrack();

        assertFalse(player.isRemovedFromTrack());
        assertNotNull(player.getAssignedOrderSlot());
        assertSame(player, player.getAssignedOrderSlot().getAssignedPlayer());
    }

    @Test
    void disconnectedOfferChoiceIsSkippedAndPlayerLeavesTrack() {
        Game game = new Game(3, List.of("a1", "a2", "a3"), "test-room");
        Player player = game.getPlayers().get(1);
        player.setConnected(false);
        player.setDrawableUpper(1);
        player.setChoseAnOffer(false);

        game.skipDisconnectedOfferChoice(player);

        assertTrue(player.hasChosenAnOffer());
        assertFalse(player.hasActionsLeft());
        assertTrue(player.isRemovedFromTrack());
    }

    private static void restoreTimeoutProperty(String previousTimeout) {
        if (previousTimeout == null) {
            System.clearProperty(TIMEOUT_PROPERTY);
        } else {
            System.setProperty(TIMEOUT_PROPERTY, previousTimeout);
        }
    }
}
