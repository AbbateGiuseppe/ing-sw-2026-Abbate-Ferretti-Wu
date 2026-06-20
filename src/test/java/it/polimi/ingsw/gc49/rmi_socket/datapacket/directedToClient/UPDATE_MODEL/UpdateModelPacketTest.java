package it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL;

import it.polimi.ingsw.gc49.client.view.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.client.view.mockupModel.MockupOffer;
import it.polimi.ingsw.gc49.client.view.mockupModel.MockupOrder;
import it.polimi.ingsw.gc49.client.view.mockupModel.MockupPlayer;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.Cardboard.LowerDrawModelElement;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.Cardboard.UpperDrawModelElement;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.OfferOrderboard.ReturnModelElement;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.Players.CurrentPlayerModelElement;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.Players.FoodAndPointsAllModelElement;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.Players.FoodAndPointsOneModelElement;
import it.polimi.ingsw.gc49.server.model.BuildingEvent;
import it.polimi.ingsw.gc49.server.model.Card.BuildingCard.BonusHuntingCard;
import it.polimi.ingsw.gc49.server.model.Card.Card;
import it.polimi.ingsw.gc49.server.model.Card.CharacterCard.Hunter;
import it.polimi.ingsw.gc49.server.model.Card.EventCard.HuntingEvent;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.Totem;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UpdateModelPacketTest {
    @Test
    void returnsPopupSummariesAndUpdatesSinglePlayerFoodAndPoints() {
        MockupGame game = gameWithPlayers();
        UpdateModelPacket packet = new UpdateModelPacket();

        packet.addUpdateElement(new FoodAndPointsOneModelElement("Hunter event", 0, 5, 4));
        UpdateModelPacket.UpdateResult result = packet.updateTheMockupModelAndGetResult(game, null);

        assertEquals(5, game.getPlayer(0).getFood());
        assertEquals(4, game.getPlayer(0).getPoints());
        assertTrue(result.changedElements().contains(FoodAndPointsOneModelElement.class));
        assertEquals(List.of("Ada: Food +2 (3 -> 5), Points +4 (0 -> 4)"), result.foodAndPointSummaries());
        assertEquals(List.of(), result.eventResolutionSummaries());
    }

    @Test
    void returnsPopupSummariesAndUpdatesAllPlayersFoodAndPoints() {
        MockupGame game = gameWithPlayers();
        UpdateModelPacket packet = new UpdateModelPacket();

        packet.addUpdateElement(new FoodAndPointsAllModelElement(
                "Round end event",
                List.of(4, 1),
                List.of(2, -3)
        ));
        UpdateModelPacket.UpdateResult result = packet.updateTheMockupModelAndGetResult(game, null);

        assertEquals(4, game.getPlayer(0).getFood());
        assertEquals(2, game.getPlayer(0).getPoints());
        assertEquals(1, game.getPlayer(1).getFood());
        assertEquals(-3, game.getPlayer(1).getPoints());
        assertTrue(result.changedElements().contains(FoodAndPointsAllModelElement.class));
        assertEquals(List.of(
                "Ada: Food +1 (3 -> 4), Points +2 (0 -> 2)",
                "Ben: Food -1 (2 -> 1), Points -4 (1 -> -3)"
        ), result.foodAndPointSummaries());
        assertEquals(List.of(), result.eventResolutionSummaries());
    }

    @Test
    void returnsEventResolutionOnlyForEventCardFoodAndPointsAllUpdates() {
        MockupGame game = gameWithPlayers(List.of(new HuntingEvent(1, null, Era.FIRST, 2, null)));
        UpdateModelPacket packet = new UpdateModelPacket();

        packet.addUpdateElement(new FoodAndPointsAllModelElement(
                "La carta evento CACCIA si e' attivata fornendo 1 punti per ogni cacciatore",
                List.of(4, 2),
                List.of(2, 1)
        ));
        UpdateModelPacket.UpdateResult result = packet.updateTheMockupModelAndGetResult(game, null);

        assertEquals(List.of(
                "Event: CACCIA",
                "Ada: Food 4 (+1), Points 2 (+2)",
                "Ben: Food 2 (0), Points 1 (0)"
        ), result.eventResolutionSummaries());
    }

    @Test
    void currentPlayerUpdateCarriesRemainingDrawActions() {
        MockupGame game = gameWithPlayers();
        UpdateModelPacket packet = new UpdateModelPacket();

        packet.addUpdateElement(new CurrentPlayerModelElement("Ben turn", 1, 2, 1));
        UpdateModelPacket.UpdateResult result = packet.updateTheMockupModelAndGetResult(game, null);

        assertEquals(1, game.getCurrentPlayerIndex());
        assertEquals(2, game.getPlayer(1).getDrawableUpper());
        assertEquals(1, game.getPlayer(1).getDrawableLower());
        assertTrue(result.changedElements().contains(CurrentPlayerModelElement.class));
    }

    @Test
    void returnUpdateCarriesOrderSlotFoodAndTotemPosition() {
        MockupGame game = gameWithPlayers();
        UpdateModelPacket packet = new UpdateModelPacket();

        packet.addUpdateElement(new ReturnModelElement(
                "Ada returned to an order slot",
                List.of(new MockupOffer(0, 0, 0, null)),
                List.of(new MockupOrder(2, false, 0, 0, 0)),
                0,
                5,
                0
        ));
        UpdateModelPacket.UpdateResult result = packet.updateTheMockupModelAndGetResult(game, null);

        assertEquals(5, game.getPlayer(0).getFood());
        assertEquals(0, game.getPlayer(0).getPoints());
        assertEquals(0, game.getOrderBoard().get(0).getAssignedPlayerIndex());
        assertTrue(result.changedElements().contains(ReturnModelElement.class));
        assertEquals(List.of("Ada: Food +2 (3 -> 5), Points 0 (0 -> 0)"), result.foodAndPointSummaries());
    }

    @Test
    void drawUpdatesCarryPlayerCardsResourcesAndActionsWithoutPopupSummary() {
        MockupGame game = gameWithPlayers();
        UpdateModelPacket packet = new UpdateModelPacket();
        Hunter hunter = new Hunter(false, Era.FIRST, 2, null);

        packet.addUpdateElement(new UpperDrawModelElement(
                "Ada drew a hunter",
                List.of(),
                List.of(),
                0,
                hunter,
                true,
                1,
                0,
                6,
                -1
        ));
        UpdateModelPacket.UpdateResult result = packet.updateTheMockupModelAndGetResult(game, null);

        assertEquals(List.of(hunter), game.getPlayer(0).getCharacterCards());
        assertEquals(6, game.getPlayer(0).getFood());
        assertEquals(-1, game.getPlayer(0).getPoints());
        assertEquals(1, game.getPlayer(0).getDrawableUpper());
        assertEquals(0, game.getPlayer(0).getDrawableLower());
        assertTrue(result.changedElements().contains(UpperDrawModelElement.class));
        assertEquals(List.of(), result.foodAndPointSummaries());
        assertEquals(List.of(), result.eventResolutionSummaries());
    }

    @Test
    void lowerDrawUpdatesBuildingCardsResourcesAndActions() {
        MockupGame game = gameWithPlayers();
        UpdateModelPacket packet = new UpdateModelPacket();
        BonusHuntingCard building = new BonusHuntingCard(BuildingEvent.HUNTING_EVENT, 5, 3, Era.FIRST, 2, null);

        packet.addUpdateElement(new LowerDrawModelElement(
                "Ada drew a building",
                List.of(),
                List.of(),
                0,
                building,
                false,
                0,
                1,
                1,
                0
        ));
        packet.updateTheMockupModelAndGetResult(game, null);

        assertEquals(List.of(building), game.getPlayer(0).getBuildingCards());
        assertEquals(1, game.getPlayer(0).getFood());
        assertEquals(0, game.getPlayer(0).getPoints());
        assertEquals(0, game.getPlayer(0).getDrawableUpper());
        assertEquals(1, game.getPlayer(0).getDrawableLower());
    }

    private MockupGame gameWithPlayers() {
        return gameWithPlayers(List.of());
    }

    private MockupGame gameWithPlayers(List<Card> lowerLine) {
        List<MockupPlayer> players = new ArrayList<>();
        players.add(new MockupPlayer("Ada", 0, 3, 0, Totem.ORANGE));
        players.add(new MockupPlayer("Ben", 1, 2, 1, Totem.BLUE));
        return new MockupGame(
                players,
                Era.FIRST,
                List.of(),
                lowerLine,
                List.of(),
                List.of(),
                List.of(new MockupOffer(0, 0, 0, null)),
                List.of(new MockupOrder(0, false, 0, 0, null))
        );
    }
}
