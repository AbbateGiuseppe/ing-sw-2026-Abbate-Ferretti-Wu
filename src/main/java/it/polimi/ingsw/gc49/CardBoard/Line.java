package it.polimi.ingsw.gc49.CardBoard;

import it.polimi.ingsw.gc49.Card.BuildingCard.BuildingCard;
import it.polimi.ingsw.gc49.Card.Card;
import it.polimi.ingsw.gc49.Card.TribeCards.CharacterCard.CharacterCard;
import it.polimi.ingsw.gc49.Card.TribeCards.EventCard.EventCard;
import it.polimi.ingsw.gc49.Era;
import it.polimi.ingsw.gc49.Player;

import java.util.ArrayList;
import java.util.List;


public class Line {
    /// Per la macchina a stati finiti
    private Era currentEra = Era.FIRST; //chiedi
    private boolean eraChanged = false;
    private Era nextEra = null;


    /// file sopra e sotto
    private final ArrayList<Card> upperLine;
    private final ArrayList<Card> lowerLine;
    private final ArrayList<Card> upperBuilding;
    private final ArrayList<Card> lowerBuilding;
    private final Deck deck;


    /// Costruttore
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
        resolveEvents(lowerLine);
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

        /*for (int i = 0; i < buildingsToPlace; i++) {   /// chiedere come fare
            Card building = deck.dealBuildingCard();
            if (building == null) break;       // nessuna carta edificio rimasta
            upperBuilding.add(building);
        }*/

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

        /*// ordina: stesso tipo per Era crescente, Sostentamento per ultimo [file:3]
        events.sort((e1, e2) -> {
            boolean s1 = e1.getType() == EventType.SUSTENTAMENTO;
            boolean s2 = e2.getType() == EventType.SUSTENTAMENTO;
            if (s1 && !s2) return 1;
            if (!s1 && s2) return -1;
            return Integer.compare(e1.getEraIndex(), e2.getEraIndex());
        });

        for (EventCard e : events) {
            e.resolve(// Model o lista giocatori );
        }*/
    }

}