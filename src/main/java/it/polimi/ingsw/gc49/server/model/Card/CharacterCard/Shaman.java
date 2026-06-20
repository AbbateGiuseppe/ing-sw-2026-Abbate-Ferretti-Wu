package it.polimi.ingsw.gc49.server.model.Card.CharacterCard;

import it.polimi.ingsw.gc49.client.view.RectangleAttributedString;
import it.polimi.ingsw.gc49.server.model.CharacterType;
import it.polimi.ingsw.gc49.server.model.DataBank;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.QueueUpdatable;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

public class Shaman extends CharacterCard {
    private final int numStars;

    public Shaman( int numStars, Era era, int minNumPlayers, QueueUpdatable queueUpdater ) {
        super(era, minNumPlayers, queueUpdater);
        this.numStars = numStars;
    }

    @Override
    public void updateDataBank(DataBank dataBank) {
        dataBank.addNumStar(numStars);
        dataBank.addCharacterCount(CharacterType.Shaman,1);
    }

    @Override
    public String toString() {
        return "Shaman {\n" +
                " era = " + era +
                ", numStars = " + numStars +
                "\n}";
    }

    @Override
    public String simpleToString () {
        return "SCIAMANO";
    }

    @Override
    public RectangleAttributedString getRectangleAttributedString () {
        return it.polimi.ingsw.gc49.client.view.TextCardRenderer.render(simpleToString(), era);
    }
}

