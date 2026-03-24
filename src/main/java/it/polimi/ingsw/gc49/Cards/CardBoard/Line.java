package it.polimi.ingsw.gc49.Cards.CardBoard;

import it.polimi.ingsw.gc49.Cards.BuildingCard.BuildingCard;
import it.polimi.ingsw.gc49.Cards.Card;
import it.polimi.ingsw.gc49.Era;

import java.util.ArrayList;
import java.util.List;


public class Line {
    private Era currentEra = ?; //chiedi
    private boolean eraChanged = false;
    private Era nextEra = null;
    private final ArrayList<Card> upperLine;
    private final ArrayList<Card> lowerLine;
    private final ArrayList<Card> upperBuilding;
    private final ArrayList<Card> lowerBuilding;
    private final Deck deck;

    public Line(int numPlayers) {

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


    public Card drawUpperCharacter(int index) {
        if (index < 0 || index >= upperLine.size()) {
            return null;
            }

        Card picked = upperLine.get(index);
        if (!(picked instanceof CharacterCard)) {
            return null;                        // sicurezza, nel caso ci finisca altro
        }

        upperLine.remove(index);               // rimuovo DAVVERO dall'ArrayList
        return picked;
    }

    public Card drawUpperBuilding(int index, int availableFood) {
        if (index < 0 || index >= upperBuilding.size()) {
            return null;
        }

        Card picked = upperBuilding.get(index);
        if (!(picked instanceof BuildingCard)) {
            return null;
        }

        BuildingCard building = (BuildingCard) picked;

        // check sul cibo: se non ne ho abbastanza, non posso prenderla
        if (building.getFoodCost() > availableFood) {  //chiedi ezcheng
            return null;                       // NON rimuovo dalla lista
        }

        upperBuilding.remove(index);           // ora la tolgo dalla board
        return building;
    }


    public Card drawLowerCharacter(int index) {
        if (index < 0 || index >= lowerLine.size()) {
            return null;
        }

        Card picked = lowerLine.get(index);
        if (!(picked instanceof CharacterCard)) {
            return null;
        }

        lowerLine.remove(index);

        return picked;
    }

    public Card drawLowerBuilding(int index, int availableFood) {
        if (index < 0 || index >= lowerBuilding.size()) {
            return null;
        }

        Card picked = lowerBuilding.get(index);
        if (!(picked instanceof BuildingCard)) {
            return null;
        }

        BuildingCard building = (BuildingCard) picked;


        if (building.getFoodCost() > availableFood) {
            return null;
        }
        /// chiedere di aggiungere cibo su carta edificio

        lowerBuilding.remove(index);
        return building;
    }



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
        resolveLowerLineEvents();
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

    public void endEra(Era newEra) {
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
        nextEra = null;
    }

    public void endGame() {
        // 1) Risolvi prima gli eventi nella fila inferiore
        resolveEvents(lowerLine);

        // 2) Poi risolvi quelli eventualmente nella fila superiore
        resolveEvents(upperLine);

        // 3) Se vuoi, svuoti tutto il board (opzionale)
        lowerLine.clear();
        upperLine.clear();
        lowerBuilding.clear();
        upperBuilding.clear();
    }

    private void resolveEvents(List<Card> line) {
        List<EventCard> events = new ArrayList<>();

        for (Card c : line) {
            if (c instanceof EventCard) {
                events.add((EventCard) c);
            }
        }

        // Ordina: stessi tipi per Era crescente, Sostentamento per ultimo
        events.sort((e1, e2) -> {
            boolean s1 = e1.getType() == EventType.SUSTENTAMENTO;
            boolean s2 = e2.getType() == EventType.SUSTENTAMENTO;
            if (s1 && !s2) return 1;   // e1 dopo e2
            if (!s1 && s2) return -1;  // e1 prima di e2
            return Integer.compare(e1.getEraIndex(), e2.getEraIndex());
        });


        for (EventCard e : events) {
            e.resolve(/* game state / lista giocatori */);
        }

    }





}