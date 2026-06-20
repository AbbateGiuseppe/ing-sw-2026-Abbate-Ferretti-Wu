package it.polimi.ingsw.gc49.client.user_input_interfaces;

import it.polimi.ingsw.gc49.ItaEngAttributedStringBuilder;
import it.polimi.ingsw.gc49.ItaEngString;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;

import java.util.EnumMap;
import java.util.Map;

import static it.polimi.ingsw.gc49.client.user_input_interfaces.TextTerminal.HELP_TIMEOUT;

public class Manual {
    public static final Map<ApplicationPhase, ItaEngAttributedStringBuilder> manual = new EnumMap<>(ApplicationPhase.class);

    static {
        // Command entries
        //GENERAL/ANY commands
        for(ApplicationPhase applicationPhase : ApplicationPhase.values()){
            manual.put(applicationPhase, new ItaEngAttributedStringBuilder(
                    new AttributedStringBuilder()
                        .append(String.format("%72s\n", "comandi GENERALI:"))
                        .append(manualFormat("aiuto", "Mostra questo messaggio d'aiuto per " + HELP_TIMEOUT + " secondi"))
                        .append(manualFormat("disconnetti", "Ti disconnette dal serviente ed esce dall'applicazione"))
                    ,
                    new AttributedStringBuilder()
                        .append(String.format("%72s\n", "GENERAL commands:"))
                        .append(manualFormat("help", "Displays this help message for " + HELP_TIMEOUT + " seconds"))
                        .append(manualFormat("disconnect", "Disconnects you from the server and exits the application"))
                    )
            );
        }

        //HALL commands
        manual.get(ApplicationPhase.HALL).itaString
                .append(manualSeparator()).append("\n")
                .append(String.format("%70s\n", "comandi dell'ATRIO:"))
                .append(manualFormat("crea/entra [\"il nome della tua sala\"] (num. giocatori)", "Crea/Entra in una sala d'attesa"));
        manual.get(ApplicationPhase.HALL).engString
                .append(manualSeparator()).append("\n")
                .append(String.format("%70s\n", "HALL commands:"))
                .append(manualFormat("create/join [\"your room name\"] (num. players)", "Creates/Joins a waiting room"));

        //ROOM commands
        manual.get(ApplicationPhase.ROOM).itaString
                .append(manualSeparator()).append("\n")
                .append(String.format("%70s\n", "comandi da SALA:"))
                .append(manualFormat("esci", "Esce dalla sala attuale"));
        manual.get(ApplicationPhase.ROOM).engString
                .append(manualSeparator()).append("\n")
                .append(String.format("%70s\n", "ROOM commands:"))
                .append(manualFormat("leave", "Leaves the current room"));
        //GAME commands
        manual.get(ApplicationPhase.GAME).itaString
                .append(manualSeparator()).append("\n")
                .append(String.format("%70s\n", "comandi in PARTITA:"))
                .append(manualFormat("legenda", "Mostra una legenda che spiega i simboli della plancia"))
                .append(manualFormat("totemo [orange/white/blue/black/yellow]", "Scegli il totemo specificato"))
                .append(manualFormat("offerta [indice dell'offerta]", "Scegli l'offerta specificata"))
                .append(manualFormat("pesca [i/s] [p/e] [indice della carta]", "Pesca dai righi inferiori o superiori,"))
                .append(manualFormatSecondLine("la carta indicizzata nel rigo dei personaggi o edifici."))
                .append(manualFormat("leggi [i/s] [p/e] [indice della carta]", "Leggi dai righi inferiori o superiori,"))
                .append(manualFormatSecondLine("la carta indicizzata nel rigo dei personaggi o edifici."))
                .append(manualFormat("giocatore [indice del giocatore]", "Mostra la tribù del giocatore scelto"));
        manual.get(ApplicationPhase.GAME).engString
                .append(manualSeparator()).append("\n")
                .append(String.format("%70s\n", "GAME commands:"))
                .append(manualFormat("legend", "Displays a legend that explains game board's symbols"))
                .append(manualFormat("totem [orange/white/blue/black/yellow]", "Choose the specified totem"))
                .append(manualFormat("offer [offer index]", "Choose the specified offer"))
                .append(manualFormat("draw [l/u] [c/b] [card index]", "Draw from upper or lower lines,"))
                .append(manualFormatSecondLine("the indexed card from the character or the building line."))
                .append(manualFormat("read [l/u] [c/b] [card index]", "Read the description from upper or lower lines,"))
                .append(manualFormatSecondLine("of the indexed card from the character or the building line."))
                .append(manualFormat("player [player index]", "Displays the specified player's stats"));


        for(ApplicationPhase applicationPhase : ApplicationPhase.values()) {
            manual.get(applicationPhase).itaString.append(manualSeparator());
            manual.get(applicationPhase).engString.append(manualSeparator());
        }

    }

    private static String manualSeparator () { return " "+"-".repeat(127); }
    private static String manualFormat ( String command, String description ) {return " " + String.format(" %60s | %s\n", command, description);}
    private static String manualFormatSecondLine ( String description ) {return " " + String.format(" %60s \\ %s\n", "", description);}


    public AttributedString print ( ApplicationPhase applicationPhase, ItaEngString.Language language ) {
        return manual.get(applicationPhase).print(language).toAttributedString();
    }
}
