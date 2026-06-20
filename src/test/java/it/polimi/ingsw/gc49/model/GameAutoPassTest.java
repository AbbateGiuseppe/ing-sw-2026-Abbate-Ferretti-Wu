package it.polimi.ingsw.gc49.model;

import it.polimi.ingsw.gc49.server.model.Card.CharacterCard.Gatherer;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.Game;
import it.polimi.ingsw.gc49.server.model.Player;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameAutoPassTest {

    @Test
    void autoPassesWhenCurrentPlayerHasNoAvailableCardsForRemainingActions() {
        Game game = new Game(2, List.of("a1", "a2"), "test-room");
        Player player = game.getPlayers().get(0);
        player.setDrawableUpper(1);
        player.setDrawableLower(0);
        game.setCurrentPlayer(player);
        game.setCurrentPlayerIndex(player.getPlayerIndex());

        game.getCardBoard().getLine().getUpperLine().clear();
        game.getCardBoard().getLine().getUpperBuilding().clear();
        game.getCardBoard().getLine().getLowerLine().clear();
        game.getCardBoard().getLine().getLowerBuilding().clear();

        assertTrue(game.autoPassCurrentPlayerIfNoAvailableCards());
        assertFalse(player.hasActionsLeft());
    }

    @Test
    void doesNotAutoPassWhenARemainingActionCanDrawACard() {
        Game game = new Game(2, List.of("a1", "a2"), "test-room");
        Player player = game.getPlayers().get(0);
        player.setDrawableUpper(1);
        player.setDrawableLower(0);
        game.setCurrentPlayer(player);
        game.setCurrentPlayerIndex(player.getPlayerIndex());

        game.getCardBoard().getLine().getUpperLine().clear();
        game.getCardBoard().getLine().getUpperBuilding().clear();
        game.getCardBoard().getLine().getLowerLine().clear();
        game.getCardBoard().getLine().getLowerBuilding().clear();
        game.getCardBoard().getLine().getUpperLine().add(new Gatherer(Era.first(), 2, game));

        assertFalse(game.autoPassCurrentPlayerIfNoAvailableCards());
        assertTrue(player.hasActionsLeft());
    }
}
