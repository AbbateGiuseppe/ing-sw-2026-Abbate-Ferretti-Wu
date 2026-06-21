package it.polimi.ingsw.gc49.server.model.Card.EventCard;

import it.polimi.ingsw.gc49.client.view.ItaEngRectangleAttributedString;
import it.polimi.ingsw.gc49.client.view.RectangleAttributedString;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.Players.FoodAndPointsAllModelElement;
import it.polimi.ingsw.gc49.server.model.*;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

import java.util.List;

/**
 * Represents the Sustenance Event card in the game (Sostentamento).
 * <p>
 * Pay 1 Food for each Character card in
 * your tribe (Buildings do not count). If, after
 * paying all the Food you have, you have not managed
 * to feed all your Characters, you lose
 * the Prestige Points indicated on the Event card,
 * for each Character you have not fed
 * You cannot choose to lose Prestige Points by not paying
 * Food.
 * In any case, keep all your Characters, even if
 * you do not have enough Food to feed them all.
 * Remember: Each Collector provides a discount of 3 Food.
 * If there are multiple Events to resolve, Sustenance
 * must be resolved last.
 */
public class SustenanceEvent extends EventCard {
    /** The point penalty multiplier applied for each missing unit of food. */
    private final int minusPoints;

    /**
     * Constructs a new {@code SustenanceEvent} card.
     *
     * @param minusPoints  the point penalty inflicted per missing unit of food
     * @param eventManager the {@link EventManager} used to trigger related building effects
     * @param era          the {@link Era} this card belongs to
     * @param minNumPlayers the minimum number of players required to include this card in the deck
     */
    public SustenanceEvent( int minusPoints, EventManager eventManager, Era era, int minNumPlayers, QueueUpdatable queueUpdater ) {
        super(eventManager, era, minNumPlayers, queueUpdater);
        this.minusPoints = minusPoints;
    }

    /**
     * Resolves the logic and applies the effects of the Sustenance event to all players.
     * <p>
     * This execution follows a strict three-step process:
     * <ol>
     * <li><b>Initial Calculation:</b> For each player, the base food cost is calculated as
     * their total character count minus their sustenance discount (provided by Gatherers).
     * The cost is bounded to a minimum of 0 via {@code Math.max}.</li>
     * <li><b>Building Effects:</b> Invokes {@link BuildingEvent#SUSTENANCE_EVENT} via the
     * {@code eventManager}. This allows defensive buildings (like {@code SustainDiscountByClassCard})
     * to further reduce the pending {@code foodToPay}.</li>
     * <li><b>Resolution & Penalties:</b> The game checks if the player has enough food in
     * their inventory to cover the final cost. If they are short on food, the missing amount
     * is multiplied by {@link #minusPoints} to create a point penalty, and the food payment
     * is capped at their current food level. Finally, {@link Player#confirmToPay()} is executed.</li>
     * </ol>
     *
     * @param players the list of {@link Player}s participating in the event
     */
    @Override
    public void resolveEvent(List<Player> players) {
        for(Player player : players) {
            // the player has to pay food equal to the number of the charactercards he has
            // and subtract it by the discount of the gatherers
            player.setFoodToPay(Math.max(0, player.data.getNumCharacters() - player.data.getNumSustenanceDiscount()));
            player.setPointsToPay(0);
        }
        // Effect num 2
        eventManager.invokeEvent(BuildingEvent.SUSTENANCE_EVENT);

        for(Player player : players) {
            // if the player doesn't have enough food,subtract from his points
            if(player.getFood() < player.getFoodToPay()) {
                  player.setPointsToPay((player.getFoodToPay() - player.getFood()) * minusPoints);
                  player.setFoodToPay(player.getFood());
            }
            player.confirmToPay();
        }

        if(queueUpdater != null) {
            queueUpdater.queueUpdateModelElement(new FoodAndPointsAllModelElement(
                    "La carta evento " + simpleToString() + " si è attivata togliendo "
                            + minusPoints + " punti per ogni personaggio non sostentato",
                    FoodAndPointsAllModelElement.getNewFood(players),
                    FoodAndPointsAllModelElement.getNewPoints(players)
            ));
        }
    }

    /**
     * Provides a detailed string representation of the event card, including its stats.
     *
     * @return a multi-line {@link String} showing the era and the starvation penalty multiplier
     */
    @Override
    public String toString() {
        return "Sustenance {\n" +
                " era = " + era +
                ", minusPoints = " + minusPoints +
                "\n}";
    }

    /**
     * Provides a localized, concise name for this event card.
     *
     * @return a simple one-line {@link String} ("SOSTENTAMENTO")
     */
    @Override
    public String simpleToString () {
        return "SOSTENTAMENTO";
    }


    /**
     * Generates a visually formatted ASCII-art representation of the card for the terminal UI.
     * <p>
     * The generated drawing has a fixed height of 4 and a width of 5. It uses a magenta
     * border to visually distinguish Event cards, and includes the specific sustenance
     * symbol (│€│).
     *
     * @return a {@link RectangleAttributedString} containing the colored terminal UI graphics
     */
    @Override
    public RectangleAttributedString getRectangleAttributedString () {
        AttributedString attributedString = new AttributedStringBuilder()
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.MAGENTA)).append("╔═══╗")
                .append("║")
                .style(AttributedStyle.DEFAULT).append("│€│")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.MAGENTA)).append("║")
                .append("║")
                .style(AttributedStyle.DEFAULT).append("└─┘")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.MAGENTA)).append("║")
                .append("╚═══╝").toAttributedString();
        int height = 4;
        int width = 5;
        return new RectangleAttributedString(height, width, attributedString);
    }

    @Override
    public ItaEngRectangleAttributedString getItaEngRectangleAttributedString () {
        RectangleAttributedString globalRectangle = getRectangleAttributedString();
        return new ItaEngRectangleAttributedString(
                globalRectangle.height, globalRectangle.width, globalRectangle.attributedString,
                globalRectangle.height, globalRectangle.width, globalRectangle.attributedString);
    }
}
