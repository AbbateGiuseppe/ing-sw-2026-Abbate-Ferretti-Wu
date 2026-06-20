package it.polimi.ingsw.gc49.server.model.Card.BuildingCard;

import it.polimi.ingsw.gc49.client.view.ItaEngRectangleAttributedString;
import it.polimi.ingsw.gc49.client.view.RectangleAttributedString;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.Players.FoodAndPointsOneModelElement;
import it.polimi.ingsw.gc49.server.model.*;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

/**
 * Represents a specific type of {@link BuildingCard} that rewards the collection of matching inventor pairs.
 * <p>
 * This building grants <b>3 bonus food</b> to the owner whenever they successfully
 * obtain a pair of identical inventor cards. It establishes a baseline of existing pairs
 * at the moment of purchase to accurately track new completions.
 */
public class SamePairInventionsCard extends BuildingCard {
    /**
     * Constructs a new {@code SamePairInventionsCard}.
     *
     * @param buildingEvent the {@link BuildingEvent} that triggers this card's effect
     * @param pointsEndgame the base points awarded at the end of the game
     * @param foodPrice     the base food cost to acquire this building
     * @param era           the {@link Era} this card belongs to
     * @param minNumPlayers the minimum number of players required to include this card
     */
    public SamePairInventionsCard ( BuildingEvent buildingEvent, int pointsEndgame, int foodPrice, Era era, int minNumPlayers, QueueUpdatable queueUpdater ) {
        super(buildingEvent, pointsEndgame, foodPrice, era, minNumPlayers, queueUpdater);
    }


    /**
     * Executes the special effect of this building.
     * <p>
     * This method is invoked by the {@link EventManager}. It calculates the total number of
     * identical inventor pairs ({@link CharacterType#SamePairInventions}) owned by the player,
     * and adds 3 food to the player's supply for each pair.
     * <p>
     * <i>Note: If this event can be triggered multiple times, ensure the logic correctly uses
     * the baseline recorded in {@link #setOwner(Player)} to avoid double-rewarding old pairs.</i>
     */
    @Override
    public void onEventEffect() {
        owner.addFood(3 * owner.data.getCharacterCount(CharacterType.SamePairInventions));
        if(queueUpdater != null) {
            queueUpdater.queueUpdateModelElement(new FoodAndPointsOneModelElement(
                    "La carta " + simpleToString() + " si è attivata fornendo 3 di cibo a " + owner.getNickname(),
                    owner.getPlayerIndex(),
                    owner.getFood(),
                    owner.getPoints()
            ));
        }
    }

    /**
     * Assigns this building to a specific player and initializes the invention tracking.
     * <p>
     * Overrides the default {@link BuildingCard#setOwner(Player)} behavior. In addition
     * to setting the owner, it calls {@code owner.data.recordInventions()} to record
     * the current number of same-pair inventions. This serves as the baseline for future comparisons.
     *
     * @param owner the {@link Player} who now owns the card
     */
    @Override
    protected void setOwner( Player owner) {
        super.setOwner(owner);
        // Record the current number of the same pair inventions for future comparision
        owner.data.recordInventions();
    }

    /**
     * Provides a detailed string representation of the card, including its stats and effect description.
     *
     * @return a multi-line {@link String} describing the card's attributes and the inventor pair effect
     */
    @Override
    public String toString() {
        return "SamePairInventionsCard {\n" +
                " era = " + era +
                ", foodPrice = " + foodPrice +
                ", pointsEndgame = " + pointsEndgame +
                ",\n effect = get 3 bonus food whenever the player obtains a pair of same inventors" +
                "\n}";
    }


    /**
     * Provides a localized, concise name for this specific building card.
     *
     * @return a simple one-line {@link String} ("EDIFICIO (stracibo da inventori)")
     */
    @Override
    public String simpleToString () {
        return "EDIFICIO (stracibo da inventori)";
    }


    /**
     * Generates a visually formatted ASCII-art representation of the card for the terminal UI.
     * <p>
     * The generated drawing has a fixed height of 2 and a width of 10. It uses green styling
     * for the borders and includes symbols to represent the endgame points (♦), food cost (♥),
     * and the specific bonus food reward for inventor pairs ((=)3♥ I).
     *
     * @return a {@link RectangleAttributedString} containing the colored terminal UI graphics
     */
    @Override
    public RectangleAttributedString getRectangleAttributedString () {
        AttributedString attributedString = new AttributedStringBuilder()
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN)).append("╔")
                .style(AttributedStyle.DEFAULT).append(String.valueOf(pointsEndgame)).append("♦")
                .append("    ").append(String.valueOf(foodPrice)).append("♥")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN)).append("╗")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN)).append("╚")
                .style(AttributedStyle.DEFAULT).append("(=)3♥  I")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN)).append("╝")
                .toAttributedString();
        int height = 2;
        int width = 10;
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
