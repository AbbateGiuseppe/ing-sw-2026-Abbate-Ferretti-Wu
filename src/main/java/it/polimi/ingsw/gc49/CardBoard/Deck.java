package it.polimi.ingsw.gc49.CardBoard;

import it.polimi.ingsw.gc49.Card.Card;
import it.polimi.ingsw.gc49.Era;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {

    private final ArrayList<Card> tribeDeck;
    private final ArrayList<Card> buildingDeck;

    public Deck(int numPlayers) {
        this.tribeDeck = new ArrayList<>();
        this.buildingDeck = new ArrayList<>();

        // qui costruisci fisicamente i mazzi in base al numero di giocatori
        TribeDeck(numPlayers);
        BuildingDeck(numPlayers);

        flush(tribeDeck);
        flush(buildingDeck);
    }

    // pesca la prossima carta Tribù (Personaggio/Eventi) dal mazzo
    public Card dealTribeCard() {
        if (tribeDeck.isEmpty()) {
            return null;
        }
        // top of deck = last element
        return tribeDeck.remove(tribeDeck.size() - 1);
    }

    // pesca la prossima carta Edificio dal mazzo
    public Card dealBuildingCard() {


        if (buildingDeck.isEmpty()) {
            return null;
        }
        return buildingDeck.remove(buildingDeck.size() - 1);
    }

    // mescola una lista di carte e la restituisce
    private ArrayList<Card> flush(ArrayList<Card> deck) {
        Collections.shuffle(deck);
        return deck;
    }

    // --------- metodi di inizializzazione interni ---------

    private void TribeDeck(int numPlayers) {
        // TODO: qui bisogna creare le Card concrete seguendo le regole:
        // dividere per Era I / II / III + 2 carte Evento finale,
        // tenendo solo le carte valide per numPlayers.
        // Esempio molto semplificato:

        // tribeDeck.add(new HunterCard(...));
        // tribeDeck.add(new ShamanCard(...));
        // ...

        // alla fine lasciale nell'ordine "fisico" (Era I in cima ecc.),
        // poi flush() mescolerà ogni mazzetto come richiesto dalle regole [file:3].
    }

    private void BuildingDeck(int numPlayers) {
        // TODO: creare le carte Edificio per le tre Ere,
        // filtrando quelle non usate in base al numero di giocatori [file:3].
    }


}
