package it.polimi.ingsw.gc49.server.model.Card.CharacterCard;

import it.polimi.ingsw.gc49.client.view.ItaEngRectangleAttributedString;
import it.polimi.ingsw.gc49.client.view.RectangleAttributedString;
import it.polimi.ingsw.gc49.server.model.CharacterType;
import it.polimi.ingsw.gc49.server.model.DataBank;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.QueueUpdatable;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

public class Builder extends CharacterCard {
    private final int buildingDiscount;
    private final int numPoints;

    public Builder(int buildingDiscount, int numPoints, Era era, int minNumPlayers, QueueUpdatable queueUpdater ) {
        super(era, minNumPlayers, queueUpdater);
        this.buildingDiscount = buildingDiscount;
        this.numPoints = numPoints;
    }

    @Override
    public void updateDataBank(DataBank dataBank) {
        dataBank.addCharacterCount(CharacterType.Builder,1);
        dataBank.addNumBuildingDiscount(buildingDiscount);
        dataBank.addNumBuilderPoints(numPoints);
    }

    @Override
    public String toString() {
        return "Builder {\n" +
                " era = " +  era +
                ", buildingDiscount = " + buildingDiscount +
                ", numPoints = " + numPoints +
                "\n}";
    }

    @Override
    public String simpleToString () {
        return "COSTRUTTORE";
    }

    @Override
    public RectangleAttributedString getRectangleAttributedString () {
        AttributedString attributedString = new AttributedStringBuilder()
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).append("╔═══╗")
                .append("║")
                .style(AttributedStyle.DEFAULT).append("B").append(String.valueOf(buildingDiscount)).append("♥")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).append("║")
                .append("║")
                .style(AttributedStyle.DEFAULT).append(" ").append(String.valueOf(numPoints)).append("♦")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).append("║")
                .append("╚═══╝").toAttributedString();
        int height = 4;
        int width = 5;
        return new RectangleAttributedString(height, width, attributedString);
    }

    @Override
    public ItaEngRectangleAttributedString getItaEngRectangleAttributedString () {
        AttributedString itaAttributedString = new AttributedStringBuilder()
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).append("╔═══╗")
                .append("║")
                .style(AttributedStyle.DEFAULT).append("Co").append("♥")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).append("║")
                .append("║")
                .style(AttributedStyle.DEFAULT).append(String.valueOf(numPoints)).append("♦").append(String.valueOf(buildingDiscount))
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).append("║")
                .append("╚═══╝").toAttributedString();
        AttributedString engAttributedString = new AttributedStringBuilder()
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).append("╔═══╗")
                .append("║")
                .style(AttributedStyle.DEFAULT).append("B").append(String.valueOf(buildingDiscount)).append("♥")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).append("║")
                .append("║")
                .style(AttributedStyle.DEFAULT).append(" ").append(String.valueOf(numPoints)).append("♦")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).append("║")
                .append("╚═══╝").toAttributedString();
        int height = 4;
        int width = 5;
        return new ItaEngRectangleAttributedString(
                height, width, itaAttributedString,
                height, width, engAttributedString);
    }
}
