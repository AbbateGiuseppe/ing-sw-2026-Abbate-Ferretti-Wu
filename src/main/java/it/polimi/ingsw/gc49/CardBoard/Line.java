package it.polimi.ingsw.gc49.CardBoard;

import it.polimi.ingsw.gc49.Card.BuildingCard.BuildingCard;
import it.polimi.ingsw.gc49.Card.Card;
import it.polimi.ingsw.gc49.Card.TribeCards.CharacterCard.CharacterCard;
import it.polimi.ingsw.gc49.Card.TribeCards.EventCard.EventCard;
import it.polimi.ingsw.gc49.Era;
import it.polimi.ingsw.gc49.Model;
import it.polimi.ingsw.gc49.Player;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.sort;


public class Line {
    /// Per la macchina a stati finiti
    private Era currentEra = Era.FIRST; //chiedi
    private boolean eraChanged = false;
    private Era newEra = Era.SECOND;
    private Model model;
    private int numPlayers;




    /// file sopra e sotto
    private final ArrayList<Card> upperLine;
    private final ArrayList<Card> lowerLine;
    private final ArrayList<Card> upperBuilding;
    private final ArrayList<Card> lowerBuilding;
    private final Deck deck;




    /// Costruttore
    public Line(Model model) {
        this.model = model;
        this.numPlayers = model.getNumOfPlayers();
        this.deck = new Deck(numPlayers);
        this.upperLine = new ArrayList<>();
        this.lowerLine = new ArrayList<>();
        this.upperBuilding = new ArrayList<>();
        this.lowerBuilding = new ArrayList<>();
        }


        public Card dealTribeCard() {
            return deck.dealTribeCard();      // implementalo in Deck
        }

        public Card dealBuildingCard() {
            return deck.dealBuildingCard();   // implementalo in Deck
        }


    public Card drawUpperCharacter(int cardIndex) {
        if (cardIndex < 0 || cardIndex >= upperLine.size()) {
            return null;
            }

        Card picked = upperLine.get(cardIndex);
        if (!(picked instanceof CharacterCard)) {
            return null;                        // sicurezza, nel caso ci finisca altro
        }

        upperLine.remove(cardIndex);               // rimuovo DAVVERO dall'ArrayList
        return picked;
    }

    public Card drawUpperBuilding(int cardIndex, Player drawingPlayer) {
        if (cardIndex < 0 || cardIndex>= upperBuilding.size()) {
            return null;
        }

        Card picked = upperBuilding.get(cardIndex);
        if (!(picked instanceof BuildingCard)) {
            return null;
        }

        BuildingCard building = (BuildingCard) picked;

        // check sul cibo: se non ne ho abbastanza, non posso prenderla
        if (!building.canGet(drawingPlayer)) {  //chiedi ezcheng
            return null;                       // NON rimuovo dalla lista
        }

        upperBuilding.remove(cardIndex);           // ora la tolgo dalla board
        return building;
    }


    public Card drawLowerCharacter(int cardIndex) {
        if (cardIndex < 0 || cardIndex >= lowerLine.size()) {
            return null;
        }

        Card picked = lowerLine.get(cardIndex);
        if (!(picked instanceof CharacterCard)) {
            return null;
        }

        lowerLine.remove(cardIndex);

        return picked;
    }

    public Card drawLowerBuilding(int cardIndex, Player drawingPlayer) {
        if (cardIndex < 0 || cardIndex >= lowerBuilding.size()) {
            return null;
        }

        Card picked = lowerBuilding.get(cardIndex);
        if (!(picked instanceof BuildingCard)) {
            return null;
        }

        BuildingCard building = (BuildingCard) picked;


        if (!building.canGet(drawingPlayer)) {
            return null;
        }

        lowerBuilding.remove(cardIndex);
        return building;
    }


/// metodi per gli stati finiti
    public boolean hasEraChanged() {
        return eraChanged;
    }
    public Era getNextEra() {
        return nextEra;
    }
    public void clearEraChange() {
        eraChanged = false;
        nextEra = null;
    }
    public Era getCurrentEra() {
        return currentEra;
    }

/// tutti gli end

    public void endRound(int numPlayers) {
        resolveEvent(lowerLine, model.getPlayers());   ///parlare a Max
        lowerLine.clear();
        lowerLine.addAll(upperLine);
        upperLine.clear();

        Era previousEra = currentEra;

        for (int i = 0; i < numPlayers + 4; i++) {
            Card drawn = deck.dealTribeCard();
            if (drawn == null) break;

            Era cardEra = drawn.getEra();
            if (!cardEra.equals(previousEra)) {
                // segna che c'è stato un cambio era, ma NON fai ancora endEra
                currentEra = cardEra;
                eraChanged = true;
                nextEra = cardEra;
                previousEra = cardEra;
            }

            upperLine.add(drawn);
        }
    }

    public void endEra() {
        // 1) Se si passa all’Era III: scarta eventuali edifici nella fila inferiore
        if (newEra == Era.THIRD) {
            lowerBuilding.clear();
        }

        // 2) Sposta gli edifici dalla fila superiore alla fila inferiore
        //    (succede quando inizi Era II o III)
        lowerBuilding.addAll(upperBuilding);
        upperBuilding.clear();

        // 3) Aggiungi nella fila superiore gli edifici dell’Era appena iniziata,
        //    in numero dipendente da numPlayers (tabella del regolamento) [file:3]
        // ///capire come fare

        for (int i = 0; i < buildingsToPlace; i++) {   /// chiedere come fare
            Card building = deck.dealBuildingCard();
            if (building == null) break;       // nessuna carta edificio rimasta
            upperBuilding.add(building);
        }

        // aggiorna stato interno e resetta il flag di cambio era
        currentEra = newEra;
        eraChanged = false;
        newEra = Era.THIRD;
    }

    public void endGame() {
        // 1) Risolvi prima gli eventi nella fila inferiore
        resolveEvent(lowerLine, model.getPlayers());
        // 2) Poi risolvi quelli eventualmente nella fila superiore
        resolveEvent(upperLine, model.getPlayers());

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

}