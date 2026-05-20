package it.polimi.ingsw.gc49.server.model.Card.BuildingCard;

import it.polimi.ingsw.gc49.client.view.RectangleAttributedString;
import it.polimi.ingsw.gc49.server.model.BuildingEvent;
import it.polimi.ingsw.gc49.server.model.CharacterType;
import it.polimi.ingsw.gc49.server.model.Era;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

public class SustainDiscountByClassCard extends BuildingCard {
    private final CharacterType unit;

    public SustainDiscountByClassCard ( CharacterType unit, BuildingEvent buildingEvent, int pointsEndgame, int foodPrice, Era era, int minNumPlayers ) {
        super(buildingEvent, pointsEndgame, foodPrice, era, minNumPlayers);
        this.unit = unit;
    }

    @Override
    public void onEventEffect() {
        owner.setFoodToPay(owner.getFoodToPay() - owner.data.getCharacterCount(unit));
    }

    @Override
    public String toString() {
        return "SustainDiscountByClassCard {\n" +
                " era = " + era +
                ", foodPrice = " + foodPrice +
                ", pointsEndgame = " + pointsEndgame +
                ",\n effect = get 1 food discount for each " + unit + " in possession during the sustenance event" +
                "\n}";
    }

    @Override
    public String simpleToString () {
        return "EDIFICIO (sconto sostentamento)";
    }

    @Override
    public RectangleAttributedString getRectangleAttributedString () {
        AttributedStringBuilder attributedString = new AttributedStringBuilder()
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN)).append("╔")
                .style(AttributedStyle.DEFAULT).append(String.valueOf(pointsEndgame)).append("♦")
                .append("    ").append(String.valueOf(foodPrice)).append("♥")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN)).append("╗")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN)).append("╚")
                .style(AttributedStyle.DEFAULT).append("♥x");
        switch(unit){
            case Artist -> attributedString.append("A");
            case Builder -> attributedString.append("B");
            case Gatherer -> attributedString.append("G");
            case Hunter -> attributedString.append("H");
            case Inventor -> attributedString.append("I");
            case Shaman -> attributedString.append("S");
        }
        attributedString.append("    €")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN)).append("╝");
        int height = 2;
        int width = 10;
        return new RectangleAttributedString(height, width, attributedString.toAttributedString());
    }
}
