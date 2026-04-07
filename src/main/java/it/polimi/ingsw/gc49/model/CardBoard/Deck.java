package it.polimi.ingsw.gc49.model.CardBoard;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.gc49.model.Card.Card;
import it.polimi.ingsw.gc49.model.Card.TribeCards.CharacterCard.*;
import it.polimi.ingsw.gc49.model.Card.TribeCards.EventCard.*;
import it.polimi.ingsw.gc49.model.Card.BuildingCard.BuildingCard;
import it.polimi.ingsw.gc49.model.Era;
import it.polimi.ingsw.gc49.model.EventManager;
import it.polimi.ingsw.gc49.model.Game;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

public class Deck {

    private final ArrayList<Card> tribeDeck;
    private final ArrayList<Card> buildingDeck;
    private final EventManager gameEventManager;

    public Deck( Game game ) {
        this.tribeDeck = new ArrayList<>();
        this.buildingDeck = new ArrayList<>();
        this.gameEventManager = game.getEventManager();

        // qui costruisci fisicamente i mazzi in base al numero di giocatori
        TribeDeck(game.getNumOfPlayers());
        BuildingDeck(game.getNumOfPlayers());

    }

    // pesca la prossima carta Tribù (Personaggio/Eventi) dal mazzo
    public Card dealTribeCard() {
        if (tribeDeck.isEmpty()) {
            return null;
        }
        // top of deck = last element
        return tribeDeck.removeFirst();
    }

    // pesca la prossima carta Edificio dal mazzo
    public Card dealBuildingCard() {


        if (buildingDeck.isEmpty()) {
            return null;
        }
        return buildingDeck.removeFirst();
    }


    // --------- metodi di inizializzazione interni ---------
    /**
     * Inizializza il mazzo delle carte Tribù leggendo i dati da un file JSON.

     * Il metodo usa questi passaggi:
     * - getClass().getResourceAsStream("/cards/tribe_cards.json"):
     *   cerca il file dentro la cartella resources del progetto e restituisce uno stream
     *   di lettura; se il file non esiste, il risultato è null.

     * - InputStream:
     *   rapprsenta il flusso di byte del file letto da resources.

     * - new InputStreamReader(inputStream):
     *   converte lo stream di byte in stream di caratteri, così il JSON può essere letto
     *   come testo.

     * - Gson:
     *   libreria usata per convertire il testo JSON in oggetti Java.

     * - gson.fromJson(..., JsonObject.class):
     *   legge tutto il file JSON e lo trasforma in un oggetto JsonObject, che rappresenta
     *   il JSON principale.

     * - root.getAsJsonArray("cards"):
     *   estrae dall'oggetto principale l'array chiamato "cards", cioè la lista di tutte
     *   le carte definite nel file.

     * - era1Cards, era2Cards, era3Cards, finalEventCards:
     *   quattro liste separate in cui le carte vengono divise in base all'Era.

     * - for (JsonElement cardElement : cards):
     *   scorre tutte le carte presenti nel JSON una per una.

     * - cardElement.getAsJsonObject():
     *   converte ogni elemento dell'array in un JsonObject singolo, cioè la descrizione
     *   di una carta.

     * - cardJson.get("minPlayers").getAsInt():
     *   legge il numero minimo di giocatori richiesto per usare quella carta.

     * - if (numPlayers < minPlayers):
     *   scarta la carta se non è compatibile con il numero di giocatori della partita.

     * - createTribeCardFromJson(cardJson):
     *   metodo helper che costruisce la carta Java corretta leggendo il tipo e gli altri
     *   campi dal JSON.

     * - card.getEra():
     *   restituisce l'Era della carta appena creata.

     * - switch (era):
     *   inserisce la carta nella lista corrispondente alla sua Era.

     * In questo modo ogni gruppo di carte viene poi mescolato e usato separatamente
     * durante la partita.
     *
     * @param numPlayers numero di giocatori della partita corrente
     */

    private void TribeDeck(int numPlayers) {
        // STEP 1: Carica il file JSON delle carte tribù
        // getClass().getResourceAsStream() cerca il file nella cartella resources del progetto
        // Il path "/cards/tribe_cards.json" corrisponde a src/main/resources/cards/tribe_cards.json
        InputStream inputStream = getClass().getResourceAsStream("/cards/tribe_cards.json");

        // Se il file non esiste, lancia un'eccezione per segnalare l'errore
        if (inputStream == null) {
            throw new RuntimeException("File tribe_cards.json non trovato");
        }

        // STEP 2: Parsing del JSON
        // Gson è una libreria di Google per leggere/scrivere JSON in Java
        Gson gson = new Gson();
        // Converte lo stream in un oggetto JSON. InputStreamReader legge il file come testo
        JsonObject root = gson.fromJson(new InputStreamReader(inputStream), JsonObject.class);
        // Estrae l'array "cards" dal JSON principale
        // Il JSON deve avere questa struttura: { "cards": [ {...}, {...}, ... ] }
        JsonArray cards = root.getAsJsonArray("cards");

        // STEP 3: Crea liste separate per ogni era
        // Questo permette di mescolare ogni era indipendentemente, come richiesto dalle regole
        ArrayList<Card> era1Cards = new ArrayList<>();
        ArrayList<Card> era2Cards = new ArrayList<>();
        ArrayList<Card> era3Cards = new ArrayList<>();
        ArrayList<Card> finalEventCards = new ArrayList<>();

        // STEP 4: Itera su tutte le carte definite nel JSON
        for (JsonElement cardElement : cards) {
            // Converte ogni elemento dell'array in un JsonObject (una singola carta)
            JsonObject cardJson = cardElement.getAsJsonObject();

            // STEP 5: Verifica compatibilità con il numero di giocatori
            // Ogni carta nel JSON ha un campo "minPlayers" che indica il numero minimo di giocatori
            // Esempio: se minPlayers = 3, la carta è usata solo in partite con 3+ giocatori
            int minPlayers = cardJson.get("minNumPlayers").getAsInt();
            if (numPlayers < minPlayers) {
                continue; // Salta questa carta perché non compatibile con la partita corrente
            }

            // STEP 6: Crea l'oggetto Java della carta dal JSON
            // Delega a un metodo helper che legge il tipo e i parametri della carta
            Card card = createTribeCardFromJson(cardJson);
            if (card == null) {
                continue; // Se la creazione fallisce (tipo non riconosciuto), salta
            }

            // STEP 7: Aggiungi la carta alla lista della sua era
            // Questo separa le carte per era, così possiamo mescolarle separatamente
            Era era = card.getEra();
            switch (era) {
                case FIRST -> era1Cards.add(card);
                case SECOND -> era2Cards.add(card);
                case THIRD -> era3Cards.add(card);
                case THIRD_FINAL -> finalEventCards.add(card);
            }
        }

        // STEP 8: Mescola ogni mazzetto separatamente
        // Collections.shuffle() mescola casualmente l'ordine delle carte in ogni lista
        // È importante farlo per ogni era separatamente per mantenere l'ordine delle ere
        Collections.shuffle(era1Cards);
        Collections.shuffle(era2Cards);
        Collections.shuffle(era3Cards);
        Collections.shuffle(finalEventCards);

        // STEP 9: Costruisci il mazzo finale nell'ordine corretto
        // dealTribeCard() pesca dall'ultima posizione, quindi l'ordine è:
        // - Era I in fondo (pescate per prime)
        // - Era II
        // - Era III
        // - Eventi Finali in cima (pescate per ultime)
        tribeDeck.addAll(era1Cards);
        tribeDeck.addAll(era2Cards);
        tribeDeck.addAll(era3Cards);
        tribeDeck.addAll(finalEventCards);
    }

    private void BuildingDeck(int numPlayers) {
        // STEP 1: Carica il file JSON delle carte edificio
        // Stesso meccanismo di TribeDeck: cerca il file in src/main/resources/cards/building_cards.json
        InputStream inputStream = getClass().getResourceAsStream("/cards/building_cards.json");

        // Se il file non esiste, lancia un'eccezione
        if (inputStream == null) {
            throw new RuntimeException("File building_cards.json non trovato");
        }

        // STEP 2: Parsing del JSON
        // Stessi passaggi di TribeDeck: usa Gson per leggere il file JSON
        Gson gson = new Gson();
        JsonObject root = gson.fromJson(new InputStreamReader(inputStream), JsonObject.class);
        JsonArray cards = root.getAsJsonArray("cards");

        // STEP 3: Crea liste separate per ogni era
        // Come per TribeDeck, serve per mescolare ogni era indipendentemente
        ArrayList<Card> era1Buildings = new ArrayList<>();
        ArrayList<Card> era2Buildings = new ArrayList<>();
        ArrayList<Card> era3Buildings = new ArrayList<>();

        // STEP 4: Itera su tutte le carte edificio nel JSON
        for (JsonElement cardElement : cards) {
            JsonObject cardJson = cardElement.getAsJsonObject();

            // STEP 5: Verifica compatibilità con numero di giocatori
            // Stesso controllo di TribeDeck: salta le carte che richiedono più giocatori
            int minPlayers = cardJson.get("minNumPlayers").getAsInt();
            if (numPlayers < minPlayers) {
                continue; // Salta questa carta
            }

            // STEP 6: Crea la carta edificio dal JSON
            BuildingCard card = createBuildingCardFromJson(cardJson);
            if (card == null) {
                continue; // Errore nella creazione, salta
            }

            // STEP 7: Aggiungi alla lista dell'era corrispondente
            Era era = card.getEra();
            switch (era) {
                case FIRST -> era1Buildings.add(card);
                case SECOND -> era2Buildings.add(card);
                case THIRD -> era3Buildings.add(card);
                case THIRD_FINAL -> {} // Gli edifici non hanno eventi finali, quindi ignoriamo questo caso
            }
        }

        // STEP 8: Mescola ogni mazzetto separatamente
        // Come per TribeDeck, mescoliamo ogni era indipendentemente
        Collections.shuffle(era1Buildings);
        Collections.shuffle(era2Buildings);
        Collections.shuffle(era3Buildings);

        // STEP 9: Costruisci il mazzo finale nell'ordine corretto
        // dealBuildingCard() pesca dall'ultima posizione, quindi:
        // Era I in fondo, Era II al centro, Era III in cima
        buildingDeck.addAll(era1Buildings);
        buildingDeck.addAll(era2Buildings);
        buildingDeck.addAll(era3Buildings);
    }

    /**
     * Crea una carta tribù (personaggio o evento) dal JSON.
     * Questo metodo è chiamato per ogni carta nel JSON e crea l'oggetto Java corrispondente.
     *
     * @param cardJson oggetto JSON con i dati della carta
     * @return la carta creata o null se il tipo non è riconosciuto
     *
     * COME USARE QUESTO METODO:
     * 1. Per ogni tipo di carta che aggiungi al JSON, devi aggiungere un case nello switch
     * 2. Leggi i parametri specifici della carta dal JSON usando cardJson.get("nomeCampo")
     * 3. Crea l'oggetto della carta con i parametri letti
     *
     * ESEMPIO JSON per una carta Hunter:
     * {
     *   "type": "Hunter",
     *   "era": "FIRST",
     *   "minPlayers": 2,
     *   "drumstick": true
     * }
     */
    private Card createTribeCardFromJson(JsonObject cardJson) {
        // Legge il tipo di carta (es: "Hunter", "Gatherer", "HuntingEvent")
        String type = cardJson.get("type").getAsString();

        // Legge l'era come stringa (es: "FIRST", "SECOND") e la converte nell'enum Era
        String eraStr = cardJson.get("era").getAsString();
        Era era = Era.valueOf(eraStr);

        Card card = null;

        // Switch sul tipo per creare la carta appropriata
        // Per ogni tipo, leggi i parametri specifici dal JSON e crea l'oggetto
        switch (type) {
            case "Hunter" -> {
                // Esempio completo: legge il parametro "drumstick" e crea un Hunter
                boolean drumstick = cardJson.get("drumstick").getAsBoolean();
                card = new Hunter(drumstick, era, 2);
            }
            case "Gatherer" -> {
                // TODO: Aggiungi qui la logica per creare un Gatherer
                // Esempio: boolean hasBasket = cardJson.get("hasBasket").getAsBoolean();
                //          card = new Gatherer(hasBasket);
            }
            case "Shaman" -> {
                // TODO: Aggiungi qui la logica per creare uno Shaman
                // Leggi i parametri necessari dal JSON e crea l'oggetto
            }
            case "Inventor" -> {
                // TODO: Aggiungi qui la logica per creare un Inventor
            }
            case "Builder" -> {
                // TODO: Aggiungi qui la logica per creare un Builder
            }
            case "Artist" -> {
                // TODO: Aggiungi qui la logica per creare un Artist
            }
            case "HuntingEvent" -> {
                // Esempio di evento: legge quanti punti per hunter e crea l'evento
                int pointsPerHunter = cardJson.get("pointsPerHunter").getAsInt();
                card = new HuntingEvent(pointsPerHunter, gameEventManager, era, 2);
            }
            case "SustenanceEvent" -> {
                // TODO: Aggiungi qui la logica per creare un SustenanceEvent
            }
            default -> {
                // Se il tipo non è riconosciuto, stampa un errore e ritorna null
                System.err.println("Tipo carta non riconosciuto: " + type);
                return null;
            }
        }

        return card;
    }

    /**
     * Crea una carta edificio dal JSON.
     * Le carte edificio hanno una strategia che definisce il loro effetto.
     *
     * @param cardJson oggetto JSON con i dati della carta
     * @return la carta edificio creata o null in caso di errore
     *
     * COME USARE QUESTO METODO:
     * 1. Ogni BuildingCard ha parametri base: era, ppReward (punti vittoria), foodPrice (costo in cibo)
     * 2. Ogni carta ha anche una strategia che definisce il suo effetto speciale
     * 3. Dovrai leggere dal JSON quale strategia usare e i suoi parametri
     *
     * ESEMPIO JSON per una carta edificio:
     * {
     *   "era": "FIRST",
     *   "minPlayers": 2,
     *   "ppReward": 5,
     *   "foodPrice": 3,
     *   "strategyType": "BonusFoodAndPP",
     *   "strategyParams": {
     *     "characterType": "Hunter",
     *     "foodBonus": 1,
     *     "ppBonus": 1
     *   }
     * }
     */
    private BuildingCard createBuildingCardFromJson(JsonObject cardJson) {
        // Legge l'era della carta
        String eraStr = cardJson.get("era").getAsString();
        Era era = Era.valueOf(eraStr);

        // Legge i parametri base della carta edificio
        int ppReward = cardJson.get("ppReward").getAsInt();  // Punti vittoria base
        int foodPrice = cardJson.get("foodPrice").getAsInt(); // Costo in cibo per acquistarla

        // TODO: Implementa la creazione della strategia dal JSON
        // Dovrai leggere il campo "strategyType" e creare la strategia appropriata
        // Esempio:
        // String strategyType = cardJson.get("strategyType").getAsString();
        // BuildingStrategyInterface strategy = switch(strategyType) {
        //     case "BonusFoodAndPP" -> createBonusFoodAndPPStrategy(cardJson.getAsJsonObject("strategyParams"));
        //     case "OtherStrategy" -> createOtherStrategy(cardJson.getAsJsonObject("strategyParams"));
        //     default -> null;
        // };

//        BuildingStrategyInterface strategy = null; // Per ora null, da implementare

        // Crea la carta con i parametri letti
//        BuildingCard card = new BuildingCard(strategy, ppReward, foodPrice);
//        card.setEra(era);

//        return card;
        return null;
    }

    /**
     * Restituisce il numero di edifici da piazzare sulla board per una data era e numero di giocatori.
     * Questo metodo centralizza la logica di quante building card devono essere distribuite
     * all'inizio di ogni era secondo le regole del gioco.
     *
     * @param numPlayers numero di giocatori (2-5)
     * @param era l'era corrente
     * @return numero di carte edificio da piazzare
     */
    public int getBuildingsToPlace(int numPlayers, Era era) {
        return switch (numPlayers) {
            case 2 -> switch (era) {
                case FIRST -> 1;
                case SECOND -> 2;
                case THIRD -> 3;
                case THIRD_FINAL -> 0;
            };
            case 3 -> switch (era) {
                case FIRST -> 2;
                case SECOND -> 2;
                case THIRD -> 4;
                case THIRD_FINAL -> 0;
            };
            case 4 -> switch (era) {
                case FIRST -> 2;
                case SECOND -> 3;
                case THIRD -> 4;
                case THIRD_FINAL -> 0;
            };
            case 5 -> switch (era) {
                case FIRST -> 2;
                case SECOND -> 3;
                case THIRD -> 5;
                case THIRD_FINAL -> 0;
            };
            default -> throw new IllegalArgumentException("Numero giocatori non valido: " + numPlayers);
        };
    }

}
