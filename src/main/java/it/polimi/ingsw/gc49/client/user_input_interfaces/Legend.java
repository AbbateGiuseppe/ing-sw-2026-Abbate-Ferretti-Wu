package it.polimi.ingsw.gc49.client.user_input_interfaces;

import it.polimi.ingsw.gc49.ItaEngAttributedStringBuilder;
import it.polimi.ingsw.gc49.ItaEngString;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

public class Legend {
    private static final ItaEngAttributedStringBuilder legend = new ItaEngAttributedStringBuilder(new AttributedStringBuilder(), new AttributedStringBuilder());

    static {
        legend.itaString.append(" Significato dei simboli:\n");
        legend.itaString.append(" ♥ cibo    | ░ spazio vuoto                          | tipi di carte per colore:\n");
        legend.itaString.append(" ♦ punti   | ▓ spazio occupato da un totemo          | ")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).append("╬")
                .style(AttributedStyle.DEFAULT).append(" carte personaggio\n");
        legend.itaString.append(" * stelle  |___                                      | ")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN)).append("╬")
                .style(AttributedStyle.DEFAULT).append(" carte edificio\n");
        legend.itaString.append(" ≥ a finpartita|_____________________________________| ")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.MAGENTA)).append("╬")
                .style(AttributedStyle.DEFAULT).append(" event card\n");
        legend.itaString.append(" ● a serie completata       |  tipi di personaggio:  |\n");
        legend.itaString.style(AttributedStyle.DEFAULT.foreground(AttributedStyle.RED)).append(" ▼")
                .style(AttributedStyle.DEFAULT).append(" carte pescabili inferiori|  A = artista           |\n");
        legend.itaString.style(AttributedStyle.DEFAULT.foreground(AttributedStyle.CYAN)).append(" ▲")
                .style(AttributedStyle.DEFAULT).append(" carte pescabili superiori|  Ca = cacciatore       |\n");
        legend.itaString.append(" ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯|  Co = costruttore      |\n");
        legend.itaString.append(" tipi d'evento:             |  I = inventore         |\n");
        legend.itaString.append(" ¤ pitture                  |  R = raccoglitore      |\n");
        legend.itaString.append(" § rituale                  |  S = sciamano          |\n");
        legend.itaString.append(" % caccia                   |\n");
        legend.itaString.append(" € sostentamento            |\n");

        legend.engString.append(" Symbols meaning:\n");
        legend.engString.append(" ♥ food    | ░ free space                       | card types by color:\n");
        legend.engString.append(" ♦ points  | ▓ space occupied by totem          | ")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).append("╬")
                .style(AttributedStyle.DEFAULT).append(" character card\n");
        legend.engString.append(" * stars   |__                                  | ")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN)).append("╬")
                .style(AttributedStyle.DEFAULT).append(" building card\n");
        legend.engString.append(" ≥ at end game|_________________________________| ")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.MAGENTA)).append("╬")
                .style(AttributedStyle.DEFAULT).append(" event card\n");
        legend.engString.append(" ● at set completion   |  character types:      |\n");
        legend.engString.style(AttributedStyle.DEFAULT.foreground(AttributedStyle.RED)).append(" ▼")
                .style(AttributedStyle.DEFAULT).append(" drawable lower cards|  A = artist            |\n");
        legend.engString.style(AttributedStyle.DEFAULT.foreground(AttributedStyle.CYAN)).append(" ▲")
                .style(AttributedStyle.DEFAULT).append(" drawable upper cards|  B = builder           |\n");
        legend.engString.append(" ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯|  G = gatherer          |\n");
        legend.engString.append(" event types:          |  H = hunter            |\n");
        legend.engString.append(" ¤ painting            |  I = inventor          |\n");
        legend.engString.append(" § ritual              |  S = shaman            |\n");
        legend.engString.append(" % hunt                |\n");
        legend.engString.append(" € sustenance          |\n");
    }

    public AttributedString print ( ItaEngString.Language language ) {
        return legend.print(language).toAttributedString();
    }
}
