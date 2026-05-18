package it.polimi.ingsw.gc49.server.model.CardBoard;

import it.polimi.ingsw.gc49.server.model.Card.Card;
import it.polimi.ingsw.gc49.server.model.Card.TribeCards.EventCard.EventCard;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.Game;
import it.polimi.ingsw.gc49.server.model.Player;
import it.polimi.ingsw.gc49.server.model.States.EraEndedException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.sort;


public class Line implements Serializable {
    /// Per la macchina a stati finiti
    private Era currentEra = Era.first(); //chiedi
    private boolean eraChanged = false;
    private Era newEra = currentEra.next();
    private Game game;
    private int numPlayers;
    private List<Player> playerList;


    /// file sopra e sotto
    private final List<Card> upperLine;
    private final List<Card> lowerLine;
    private final List<Card> upperBuilding;
    private final List<Card> lowerBuilding;
    private final Deck deck;




    /// Costruttore
    public Line( Game game, Deck deck) {
        this.game = game;
        this.numPlayers = game.getNumOfPlayers();
        this.playerList = game.getPlayers();
        this.deck = deck;
        this.upperLine = new ArrayList<>();
        this.lowerLine = new ArrayList<>();
        this.upperBuilding = new ArrayList<>();
        this.lowerBuilding = new ArrayList<>();

        int upperPlaced = 0;
        int lowerPlaced = 0;

        while (lowerPlaced < numPlayers + 1) {
            Card drawn = deck.dealTribeCard();
            if (drawn == null) break;

            if(drawn.getClass().isInstance(EventCard.class)) {
                upperLine.add(drawn);
                upperPlaced++;
            } else {
                lowerLine.add(drawn);
                lowerPlaced++;
            }
        }

        while (upperPlaced < numPlayers + 4) {
            Card drawn = deck.dealTribeCard();
            if (drawn == null) break;

            upperLine.add(drawn);
            upperPlaced++;
        }

        int buildingsToPlace = deck.getBuildingsToPlace(numPlayers, currentEra);
        for (int i = 0; i < buildingsToPlace; i++) {
            Card building = deck.dealBuildingCard();
            if (building == null) break;
            upperBuilding.add(building);
        }
    }


        public Card dealTribeCard() {
            return deck.dealTribeCard();      // implementalo in Deck
        }

        public Card dealBuildingCard() {
            return deck.dealBuildingCard();   // implementalo in Deck
        }


    public Card drawUpperCharacter(int cardIndex, Player drawingPlayer) {
        if (cardIndex < 0 || cardIndex >= upperLine.size()) {
            return null;
        }

        Card picked = upperLine.get(cardIndex);
        if (!picked.canGet(drawingPlayer)) {
            return null;
        }else{
            upperLine.remove(cardIndex); //actually removing the card from the line
            return picked;
        }
    }
    public Card drawLowerCharacter(int cardIndex, Player drawingPlayer) {
        if (cardIndex < 0 || cardIndex >= lowerLine.size()) {
            return null;
        }

        Card picked = lowerLine.get(cardIndex);
        if (!picked.canGet(drawingPlayer)) {
            return null;
        }else {
            lowerLine.remove(cardIndex); //actually removing the card from the line
            return picked;
        }
    }

    public Card drawUpperBuilding(int cardIndex, Player drawingPlayer) {
        if (cardIndex < 0 || cardIndex>= upperBuilding.size()) {
            return null;
        }

        Card pickedBuilding = upperBuilding.get(cardIndex);
        if (!pickedBuilding.canGet(drawingPlayer)) {
            return null;
        }else{
            upperBuilding.remove(cardIndex); //actually removing the card from the line
            return pickedBuilding;
        }
    }
    public Card drawLowerBuilding(int cardIndex, Player drawingPlayer) {
        if (cardIndex < 0 || cardIndex >= lowerBuilding.size()) {
            return null;
        }

        Card pickedBuilding = lowerBuilding.get(cardIndex);
        if (!pickedBuilding.canGet(drawingPlayer)) {
            return null;
        }else {
            lowerBuilding.remove(cardIndex); //actually removing the card from the line
            return pickedBuilding;
        }
    }


/// metodi per gli stati finiti
    public boolean hasEraChanged() {
        return eraChanged;
    }
    public Era getNewEra() {
        return newEra;
    }
    public void clearEraChange() {
        eraChanged = false;
        newEra = null;
    }
    public Era getCurrentEra() {
        return currentEra;
    }

/// tutti gli end

    public void endRound(int numPlayers) throws EraEndedException {
        resolveEvent(lowerLine, playerList);   ///parlare a Max
        lowerLine.clear();
        lowerLine.addAll(upperLine);
        upperLine.clear();

        Era previousEra = currentEra;
        Era lastCardEra = currentEra;

        for (int i = 0; i < numPlayers + 4; i++) {
            Card drawn = deck.dealTribeCard();
            if (drawn == null) break;

            upperLine.add(drawn);

            lastCardEra = drawn.getEra();
        }
        if (!lastCardEra.equals(previousEra)) {
            // segna che c'è stato un cambio era, ma NON fai ancora endEra
            currentEra = lastCardEra;
            eraChanged = true;
            newEra = lastCardEra;
            throw new EraEndedException("Era ended");
        }
    }

    public void endEra() {
        // 1) Se si passa all'Era III: scarta eventuali edifici nella fila inferiore
        if (newEra == Era.THIRD) {
            lowerBuilding.clear();
        }

        // 2) Sposta gli edifici dalla fila superiore alla fila inferiore
        //    (succede quando inizi Era II o III)
        lowerBuilding.addAll(upperBuilding);
        upperBuilding.clear();

        // 3) Aggiungi nella fila superiore gli edifici dell'Era appena iniziata,
        //    in numero dipendente da numPlayers (tabella del regolamento) [file:3]

        int buildingsToPlace = deck.getBuildingsToPlace(numPlayers, newEra);
        for (int i = 0; i < buildingsToPlace; i++) {
            Card building = deck.dealBuildingCard();
            if (building == null) break;       // nessuna carta edificio rimasta
            upperBuilding.add(building);
        }

        // aggiorna stato interno e resetta il flag di cambio era
        currentEra = newEra;
        eraChanged = false;
    }

    public void endGame() {
        // 1) Risolvi prima gli eventi nella fila inferiore
        resolveEvent(lowerLine, playerList);
        // 2) Poi risolvi quelli eventualmente nella fila superiore
        resolveEvent(upperLine, playerList);

        // 3) Se vuoi, svuoti tutto il board (opzionale)
        lowerLine.clear();
        upperLine.clear();
        lowerBuilding.clear();
        upperBuilding.clear();
    }

    private void resolveEvent(List<Card> line, List<Player> playerList) {
        List<EventCard> events = new ArrayList<>();

        for (Card c : line) {
            if (c instanceof EventCard) {
                events.add((EventCard) c);
            }
        }
        // ordina: stesso tipo per Era crescente, Sostentamento per ultimo [file:3]
        sort(events);

        for (EventCard e : events) {
            e.resolveEvent(playerList);
        }
    }

    //### getters
    public List<Card> getUpperLine() { return upperLine; }
    public List<Card> getLowerLine() { return lowerLine; }
    public List<Card> getUpperBuilding() { return upperBuilding; }
    public List<Card> getLowerBuilding() { return lowerBuilding; }
}
