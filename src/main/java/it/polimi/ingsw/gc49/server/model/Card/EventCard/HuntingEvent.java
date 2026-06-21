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
 * Represents the Hunting Event card in the game.
 * <p>
 * Player earn 1 Food and earn the Prestige Points
 * indicated on the Event card for each Hunter
 * in their tribe.
 * This event also acts as a trigger point for specific building cards that modify
 * hunting outcomes.
 */
public class HuntingEvent extends EventCard {

    /** The amount of base points awarded to a player for each Hunter they possess. */
    private final int pointsPerHunter;

    /**
     * Constructs a new {@code HuntingEvent} card.
     *
     * @param pointsPerHunter the multiplier for the points awarded per Hunter
     * @param eventManager    the {@link EventManager} used to trigger related building effects
     * @param era             the {@link Era} this card belongs to
     * @param minNumPlayers   the minimum number of players required to include this card in the deck
     */
    public HuntingEvent( int pointsPerHunter, EventManager eventManager, Era era, int minNumPlayers, QueueUpdatable queueUpdater ) {
        super(eventManager, era, minNumPlayers, queueUpdater);
        this.pointsPerHunter = pointsPerHunter;
    }


    /**
     * Resolves the logic and applies the effects of the Hunting event to all players.
     * <p>
     * This execution follows a three-step process:
     * <ol>
     * <li><b>Initial Setup:</b> Calculates the base rewards for each player. It sets negative
     * pending penalties (which act as positive rewards in the game's logic) for both food
     * (1 per Hunter) and points ({@link #pointsPerHunter} per Hunter).</li>
     * <li><b>Building Effects:</b> Invokes {@link BuildingEvent#HUNTING_EVENT} via the
     * {@code eventManager}. This allows specific buildings (like the BonusHuntingCard)
     * to step in and further reduce penalties or increase these rewards.</li>
     * <li><b>Finalization:</b> Iterates through all players and calls {@link Player#confirmToPay()}
     * to officially apply the net food and points to their totals.</li>
     * </ol>
     *
     * @param players the list of {@link Player}s participating in the event
     */
    @Override
    public void resolveEvent(List<Player> players) {
        for(Player player : players) {
            player.setFoodToPay(-player.data.getCharacterCount(CharacterType.Hunter));
            player.setPointsToPay(-player.data.getCharacterCount(CharacterType.Hunter) * pointsPerHunter);
        }
        eventManager.invokeEvent(BuildingEvent.HUNTING_EVENT);

        // Finalize the change on food and points of the player
        for(Player player : players) {
            player.confirmToPay();
        }

        if(queueUpdater != null) {
            queueUpdater.queueUpdateModelElement(new FoodAndPointsAllModelElement(
                    "La carta evento " + simpleToString() + " si è attivata fornendo "
                            + pointsPerHunter + " punti per ogni cacciatore",
                    FoodAndPointsAllModelElement.getNewFood(players),
                    FoodAndPointsAllModelElement.getNewPoints(players)
            ));
        }
    }

    /**
     * Provides a detailed string representation of the event card, including its stats.
     *
     * @return a multi-line {@link String} showing the era and the points multiplier per hunter
     */
    @Override
    public String toString() {
        return "Hunt {\n" +
                " era = " + era +
                ", pointsPerHunter = " + pointsPerHunter +
                "\n}";
    }

    /**
     * Provides a localized, concise name for this event card.
     *
     * @return a simple one-line {@link String} ("CACCIA")
     */
    @Override
    public String simpleToString () {
        return "CACCIA";
    }

    /**
     * Generates a visually formatted ASCII-art representation of the card for the terminal UI.
     * <p>
     * The generated drawing has a fixed height of 4 and a width of 5. It uses a magenta
     * border to visually distinguish Event cards from standard buildings or characters,
     * and includes the specific hunting symbol (│%│).
     *
     * @return a {@link RectangleAttributedString} containing the colored terminal UI graphics
     */
    @Override
    public RectangleAttributedString getRectangleAttributedString () {
        AttributedString attributedString = new AttributedStringBuilder()
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.MAGENTA)).append("╔═══╗")
                .append("║")
                .style(AttributedStyle.DEFAULT).append("│%│")
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
