package it.polimi.ingsw.gc49.server.model.CardBoard;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.gc49.server.model.*;
import it.polimi.ingsw.gc49.server.model.Card.BuildingCard.*;
import it.polimi.ingsw.gc49.server.model.Card.Card;
import it.polimi.ingsw.gc49.server.model.Card.CharacterCard.*;
import it.polimi.ingsw.gc49.server.model.Card.EventCard.HuntingEvent;
import it.polimi.ingsw.gc49.server.model.Card.EventCard.PaintingEvent;
import it.polimi.ingsw.gc49.server.model.Card.EventCard.RitualEvent;
import it.polimi.ingsw.gc49.server.model.Card.EventCard.SustenanceEvent;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Manages the creation, shuffling, and dealing of all cards in the game.
 * <p>
 * The {@code Deck} class is responsible for loading card data from JSON files,
 * instantiating the correct Java objects based on the card types, grouping them by
 * {@link Era}, shuffling them, and providing them to the game during execution.
 * It manages two distinct decks: the Tribe deck (characters and events) and the Building deck.
 */
public class Deck implements Serializable {

    /** The main deck containing Character and Event cards. */
    private final ArrayList<Card> tribeDeck;

    /** The deck containing Building cards. */
    private final ArrayList<Card> buildingDeck;


    /** The central EventManager used to link Event cards to Building effects. */
    private final EventManager gameEventManager;
    private final QueueUpdatable queueUpdater;


    /**
     * Constructs and initializes a fully populated {@code Deck} for a specific game.
     * <p>
     * This constructor triggers the JSON parsing and deck building process for both
     * the Tribe and Building decks, tailoring the available cards to the specific
     * number of players in the game.
     *
     * @param game the {@link Game} instance this deck belongs to
     */
    public Deck( Game game ) {
        this.tribeDeck = new ArrayList<>();
        this.buildingDeck = new ArrayList<>();
        this.gameEventManager = game.getEventManager();
        this.queueUpdater = game;

        // qui costruisci fisicamente i mazzi in base al numero di giocatori
        TribeDeck(game.getNumOfPlayers());
        BuildingDeck(game.getNumOfPlayers());

    }


    /**
     * Constructs an empty {@code Deck}.
     * <p>
     * Useful for testing or scenarios where cards are added manually via {@link #addCard(Card)}.
     */
    public Deck(){
        this.tribeDeck = new ArrayList<>();
        this.buildingDeck = new ArrayList<>();
        this.gameEventManager = null;
        this.queueUpdater = null;
    }


    /**
     * Draws the top card from the Tribe deck.
     *
     * @return the drawn {@link Card} (Character or Event), or {@code null} if the deck is empty
     */
    public Card dealTribeCard() {
        if (tribeDeck.isEmpty()) {
            return null;
        }
        return tribeDeck.removeFirst();
    }

    /**
     * Draws the top card from the Building deck.
     *
     * @return the drawn {@link Card} (Building), or {@code null} if the deck is empty
     */
    public Card dealBuildingCard() {
        if (buildingDeck.isEmpty()) {
            return null;
        }
        return buildingDeck.removeFirst();
    }



    /**
     * Initializes the Tribe card deck by reading data from a JSON file.
     * <p>
     * This method follows these steps:
     * <ul>
     * <li><b>getClass().getResourceAsStream("/cards/tribe_cards.json"):</b> searches for the file inside the project's resources folder and returns an input stream. If the file does not exist, it throws an exception.</li>
     * <li><b>InputStream / InputStreamReader:</b> converts the byte stream into a character stream so the JSON can be read as text.</li>
     * <li><b>Gson:</b> a Google library used to convert the JSON text into Java objects ({@code JsonObject}).</li>
     * <li><b>Era Separation:</b> separates the cards into four lists based on their Era (First, Second, Third, Final).</li>
     * <li><b>Filtering:</b> filters out cards that require a higher minimum number of players than the current game.</li>
     * <li><b>Assembly:</b> creates the Java objects using a helper method, shuffles each Era independently, and assembles the final deck.</li>
     * </ul>
     * By doing this, each group of cards is shuffled and drawn in the correct chronological order during the game.
     *
     * @param numPlayers the number of players in the current game
     */
    private void TribeDeck(int numPlayers) {
        InputStream inputStream = getClass().getResourceAsStream("/cards/tribe_cards.json");
        if (inputStream == null) {
            throw new RuntimeException("File tribe_cards.json non trovato");
        }
        Gson gson = new Gson();
        JsonObject root = gson.fromJson(new InputStreamReader(inputStream), JsonObject.class);
        JsonArray cards = root.getAsJsonArray("cards");

        ArrayList<Card> era1Cards = new ArrayList<>();
        ArrayList<Card> era2Cards = new ArrayList<>();
        ArrayList<Card> era3Cards = new ArrayList<>();
        ArrayList<Card> finalEventCards = new ArrayList<>();

        for (JsonElement cardElement : cards) {
            JsonObject cardJson = cardElement.getAsJsonObject();
            int minPlayers = cardJson.get("minNumPlayers").getAsInt();
            if (numPlayers < minPlayers) {
                continue;
            }

            Card card = createTribeCardFromJson(cardJson);
            if (card == null) {
                continue;
            }

            Era era = card.getEra();
            switch (era) {
                case FIRST -> era1Cards.add(card);
                case SECOND -> era2Cards.add(card);
                case THIRD -> era3Cards.add(card);
                case THIRD_FINAL -> finalEventCards.add(card);
            }
        }

        Collections.shuffle(era1Cards);
        Collections.shuffle(era2Cards);
        Collections.shuffle(era3Cards);
        Collections.shuffle(finalEventCards);

        tribeDeck.addAll(era1Cards);
        tribeDeck.addAll(era2Cards);
        tribeDeck.addAll(era3Cards);
        tribeDeck.addAll(finalEventCards);
    }


    /**
     * Initializes the Building card deck by reading data from a JSON file.
     * <p>
     * Follows the same logic as {@link #TribeDeck(int)}, reading the file
     * {@code /cards/building_cards.json} and assembling the decks by era.
     *
     * @param numPlayers the number of players in the current game
     */
    private void BuildingDeck(int numPlayers) {
        InputStream inputStream = getClass().getResourceAsStream("/cards/building_cards.json");

        if (inputStream == null) {
            throw new RuntimeException("File building_cards.json non trovato");
        }

        Gson gson = new Gson();
        JsonObject root = gson.fromJson(new InputStreamReader(inputStream), JsonObject.class);
        JsonArray cards = root.getAsJsonArray("cards");

        ArrayList<Card> era1Buildings = new ArrayList<>();
        ArrayList<Card> era2Buildings = new ArrayList<>();
        ArrayList<Card> era3Buildings = new ArrayList<>();

        for (JsonElement cardElement : cards) {
            JsonObject cardJson = cardElement.getAsJsonObject();

            int minPlayers = cardJson.get("minNumPlayers").getAsInt();
            if (numPlayers < minPlayers) {
                continue;
            }

            BuildingCard card = createBuildingCardFromJson(cardJson);
            if (card == null) {
                continue;
            }

            Era era = card.getEra();
            switch (era) {
                case FIRST -> era1Buildings.add(card);
                case SECOND -> era2Buildings.add(card);
                case THIRD -> era3Buildings.add(card);
                case THIRD_FINAL -> {}
            }
        }

        Collections.shuffle(era1Buildings);
        Collections.shuffle(era2Buildings);
        Collections.shuffle(era3Buildings);

        //empty random not going to be used cards
        switch (numPlayers) {
            case 2:
                while (era1Buildings.size() > 1) {
                    era1Buildings.removeFirst();
                }
                while (era2Buildings.size() > 2) {
                    era2Buildings.removeFirst();
                }
                while (era3Buildings.size() > 3) {
                    era3Buildings.removeFirst();
                }
                break;
            case 3:
                while (era1Buildings.size() > 2) {
                    era1Buildings.removeFirst();
                }
                while (era2Buildings.size() > 2) {
                    era2Buildings.removeFirst();
                }
                while (era3Buildings.size() > 4) {
                    era3Buildings.removeFirst();
                }
                break;
            case 4:
                while (era1Buildings.size() > 2) {
                    era1Buildings.removeFirst();
                }
                while (era2Buildings.size() > 3) {
                    era2Buildings.removeFirst();
                }
                while (era3Buildings.size() > 4) {
                    era3Buildings.removeFirst();
                }
                break;
            case 5:
                while (era1Buildings.size() > 2) {
                    era1Buildings.removeFirst();
                }
                while (era2Buildings.size() > 3) {
                    era2Buildings.removeFirst();
                }
                while (era3Buildings.size() > 5) {
                    era3Buildings.removeFirst();
                }
                break;
        }

        buildingDeck.addAll(era1Buildings);
        buildingDeck.addAll(era2Buildings);
        buildingDeck.addAll(era3Buildings);
    }

    /**
     * Creates a Tribe card (Character or Event) from its JSON representation.
     * <p>
     * This method is called for each card in the JSON and creates the corresponding Java object.
     *
     * @param cardJson the JSON object containing the card data
     * @return the created {@link Card}, or {@code null} if the type is unrecognized
     *
     * <p><b>HOW TO USE THIS METHOD:</b>
     * <ol>
     * <li>For each new card type you add to the JSON, you must add a case in the switch statement.</li>
     * <li>Read the specific parameters of the card from the JSON using {@code cardJson.get("fieldName")}.</li>
     * <li>Instantiate the card object with the read parameters.</li>
     * </ol>
     *
     * <p><b>JSON EXAMPLE for a Hunter card:</b>
     * <pre>{@code
     * {
     * "type": "Hunter",
     * "era": "FIRST",
     * "minNumPlayers": 2,
     * "drumstick": true
     * }
     * }</pre>
     */
    private Card createTribeCardFromJson(JsonObject cardJson) {
        // Legge il tipo di carta (es: "Hunter", "Gatherer", "HuntingEvent")
        String type = cardJson.get("type").getAsString();

        // Legge l'era come stringa (es: "FIRST", "SECOND") e la converte nell'enum Era
        String eraStr = cardJson.get("era").getAsString();
        Era era = Era.valueOf(eraStr);

        // Legge il numero minimo di giocatori
        int minNumPlayers = cardJson.get("minNumPlayers").getAsInt();

        Card card = null;

        // Switch sul tipo per creare la carta appropriata
        // Per ogni tipo, leggi i parametri specifici dal JSON e crea l'oggetto
        switch (type) {
            case "Hunter" -> {
                boolean drumstick = cardJson.get("drumstick").getAsBoolean();
                card = new Hunter(drumstick, era, minNumPlayers, queueUpdater);
            }
            case "Gatherer" -> {
                card = new Gatherer(era, minNumPlayers, queueUpdater);
            }
            case "Shaman" -> {
                int numStars = cardJson.get("numStars").getAsInt();
                card = new Shaman(numStars, era, minNumPlayers, queueUpdater);
            }
            case "Inventor" -> {
                String inventionStr = cardJson.get("invention").getAsString();
                Invention invention = Invention.valueOf(inventionStr);
                card = new Inventor(invention, era, minNumPlayers, queueUpdater);
            }
            case "Builder" -> {
                int buildingDiscount = cardJson.get("buildingDiscount").getAsInt();
                int numPoints = cardJson.get("numPoints").getAsInt();
                card = new Builder(buildingDiscount, numPoints, era, minNumPlayers, queueUpdater);
            }
            case "Artist" -> {
                card = new Artist(era, minNumPlayers, queueUpdater);
            }
            case "HuntingEvent" -> {
                int pointsPerHunter = cardJson.get("pointsPerHunter").getAsInt();
                card = new HuntingEvent(pointsPerHunter, gameEventManager, era, minNumPlayers, queueUpdater);
            }
            case "SustenanceEvent" -> {
                int minusPoints = cardJson.get("minusPoints").getAsInt();
                card = new SustenanceEvent(minusPoints, gameEventManager, era, minNumPlayers, queueUpdater);
            }
            case "PaintingEvent" -> {
                int threshold = cardJson.get("threshold").getAsInt();
                int plusPoints = cardJson.get("plusPoints").getAsInt();
                int minusPoints = cardJson.get("minusPoints").getAsInt();
                card = new PaintingEvent(threshold, plusPoints, minusPoints, gameEventManager, era, minNumPlayers, queueUpdater);
            }
            case "RitualEvent" -> {
                int plusPoints = cardJson.get("plusPoints").getAsInt();
                int minusPoints = cardJson.get("minusPoints").getAsInt();
                card = new RitualEvent(plusPoints, minusPoints, gameEventManager, era, minNumPlayers, queueUpdater);
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
     * Creates a Building card from its JSON representation.
     * <p>
     * This method is called for each building card in the JSON and creates the corresponding Java object.
     *
     * @param cardJson the JSON object containing the building card data
     * @return the created {@link BuildingCard}, or {@code null} if the type is unrecognized
     *
     * <p><b>HOW TO USE THIS METHOD:</b>
     * <ol>
     * <li>For each new building card type you add to the JSON, you must add a case in the switch statement.</li>
     * <li>Read any specific parameters of the card from the JSON using {@code cardJson.get("fieldName")}.</li>
     * <li>Instantiate the card object with the read parameters alongside the common ones (like era, food price, etc.).</li>
     * </ol>
     *
     * <p><b>JSON EXAMPLE for a BonusFoodEndTurnCard:</b>
     * <pre>{@code
     * {
     * "type": "BonusFoodEndTurnCard",
     * "era": "SECOND",
     * "pointsEndgame": 5,
     * "foodPrice": 2,
     * "minNumPlayers": 3,
     * "buildingEvent": "HUNTING"
     * }
     * }</pre>
     */
    private BuildingCard createBuildingCardFromJson(JsonObject cardJson) {
        // Legge il tipo di carta
        String type = cardJson.get("type").getAsString();

        // Legge l'era della carta
        String eraStr = cardJson.get("era").getAsString();
        Era era = Era.valueOf(eraStr);

        // Legge i parametri base della carta edificio
        int pointsEndgame = cardJson.get("pointsEndgame").getAsInt();
        int foodPrice = cardJson.get("foodPrice").getAsInt();
        int minNumPlayers = cardJson.get("minNumPlayers").getAsInt();

        // Legge il buildingEvent
        String buildingEventStr = cardJson.get("buildingEvent").getAsString();
        BuildingEvent buildingEvent = BuildingEvent.valueOf(buildingEventStr);

        BuildingCard card = null;

        switch (type) {
            case "BonusFoodEndTurnCard" -> {
                card = new BonusFoodEndTurnCard(buildingEvent, pointsEndgame, foodPrice, era, minNumPlayers, queueUpdater);
            }
            case "BonusHuntingCard" -> {
                card = new BonusHuntingCard(buildingEvent, pointsEndgame, foodPrice, era, minNumPlayers, queueUpdater);
            }
            case "BonusPaintingCard" -> {
                card = new BonusPaintingCard(buildingEvent, pointsEndgame, foodPrice, era, minNumPlayers, queueUpdater);
            }
            case "BonusPointsByClassEndGameCard" -> {
                String unitStr = cardJson.get("unit").getAsString();
                CharacterType unit = CharacterType.valueOf(unitStr);
                int pointsPerUnit = cardJson.get("pointsPerUnit").getAsInt();
                card = new BonusPointsByClassEndGameCard(unit, pointsPerUnit, buildingEvent, pointsEndgame, foodPrice, era, minNumPlayers, queueUpdater);
            }
            case "CharacterSetCompleteFoodCard" -> {
                card = new CharacterSetCompleteFoodCard(buildingEvent, pointsEndgame, foodPrice, era, minNumPlayers, queueUpdater);
            }
            case "CharacterSetCompletePointEndGameCard" -> {
                card = new CharacterSetCompletePointEndGameCard(buildingEvent, pointsEndgame, foodPrice, era, minNumPlayers, queueUpdater);
            }
            case "DoubleBuilderPointsCard" -> {
                card = new DoubleBuilderPointsCard(buildingEvent, pointsEndgame, foodPrice, era, minNumPlayers, queueUpdater);
            }
            case "DoubleShamanPointsCard" -> {
                card = new DoubleShamanPointsCard(buildingEvent, pointsEndgame, foodPrice, era, minNumPlayers, queueUpdater);
            }
            case "OneMoreCardCard" -> {
                card = new OneMoreCardCard(buildingEvent, pointsEndgame, foodPrice, era, minNumPlayers, queueUpdater);
            }
            case "SamePairInventionsCard" -> {
                card = new SamePairInventionsCard(buildingEvent, pointsEndgame, foodPrice, era, minNumPlayers, queueUpdater);
            }
            case "ShamanicImmunityCard" -> {
                card = new ShamanicImmunityCard(buildingEvent, pointsEndgame, foodPrice, era, minNumPlayers, queueUpdater);
            }
            case "ShamanicThreeStarCard" -> {
                card = new ShamanicThreeStarCard(buildingEvent, pointsEndgame, foodPrice, era, minNumPlayers, queueUpdater);
            }
            case "SustainDiscountByClassCard" -> {
                String unitStr = cardJson.get("unit").getAsString();
                CharacterType unit = CharacterType.valueOf(unitStr);
                card = new SustainDiscountByClassCard(unit, buildingEvent, pointsEndgame, foodPrice, era, minNumPlayers, queueUpdater);
            }
            case "TwentyFiveBonusPointsEndGame" -> {
                card = new TwentyFiveBonusPointsEndGame(buildingEvent, pointsEndgame, foodPrice, era, minNumPlayers, queueUpdater);
            }
            default -> {
                System.err.println("Tipo carta edificio non riconosciuto: " + type);
                return null;
            }
        }

        return card;
    }

    public Card getBuildingsToPlace(Era era) {
        try {
            Card newCard = buildingDeck.getFirst();
            if (newCard.getEra().equals(era)) {
                buildingDeck.removeFirst();
                return newCard;
            } else {
                return null;
            }
        } catch (RuntimeException e) {
            return null;
        }
    }
    public void addCard(Card card){
        tribeDeck.add(card);
    }

    public ArrayList<Card> getTribeDeck(){
        return tribeDeck;
    };

    public ArrayList<Card> getBuildingDeck(){
        return buildingDeck;
    };

    public EventManager getGameEventManager(){
        return gameEventManager;
    };

    public QueueUpdatable getQueueUpdater(){
        return queueUpdater;
    }

}
