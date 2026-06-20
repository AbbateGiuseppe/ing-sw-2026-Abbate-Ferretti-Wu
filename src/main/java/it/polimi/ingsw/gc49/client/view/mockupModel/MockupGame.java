package it.polimi.ingsw.gc49.client.view.mockupModel;

import it.polimi.ingsw.gc49.client.view.RectangleAttributedString;
import it.polimi.ingsw.gc49.server.model.Card.Card;
import it.polimi.ingsw.gc49.server.model.Era;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MockupGame implements Serializable {
    private int currentPlayerIndex = -1;
    private List<MockupPlayer> players;
    private final List<Card> discards = new ArrayList<>();
    private Era deckTopEra;
    private List<Card> upperLine;
    private List<Card> lowerLine;
    private List<Card> upperBuilding;
    private List<Card> lowerBuilding;
    private final List<MockupOffer> offerBoard;
    private final List<MockupOrder> orderBoard;
    private String phaseName = "Setup";
    private String latestAction = "";
    private String finalStandings = "";
    private int tribeDeckRemaining;
    private int buildingDeckRemaining;

    public MockupGame(List<MockupPlayer> players, Era deckTopEra, List<Card> upperLine, List<Card> lowerLine,
                      List<Card> upperBuilding, List<Card> lowerBuilding, List<MockupOffer> offerBoard,
                      List<MockupOrder> orderBoard) {
        this.players = players;
        this.deckTopEra = deckTopEra;
        this.upperLine = upperLine;
        this.lowerLine = lowerLine;
        this.upperBuilding = upperBuilding;
        this.lowerBuilding = lowerBuilding;
        this.offerBoard = offerBoard;
        for (MockupOffer offer : offerBoard) {
            offer.setGame(this);
        }
        this.orderBoard = orderBoard;
        for (MockupOrder order : orderBoard) {
            order.setGame(this);
        }
    }

    public void setPlayers(List<MockupPlayer> players) {
        this.players = players;
    }

    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        if (this.currentPlayerIndex >= 0 && this.currentPlayerIndex < players.size()) {
            players.get(this.currentPlayerIndex).setOfTurn(false);
        }
        this.currentPlayerIndex = currentPlayerIndex;
        if (currentPlayerIndex >= 0 && currentPlayerIndex < players.size()) {
            players.get(currentPlayerIndex).setOfTurn(true);
        }
    }

    public void setDiscards(List<Card> discards) {
        this.discards.clear();
        if (discards != null) {
            this.discards.addAll(discards);
        }
    }

    public void setDeckTopEra(Era deckTopEra) {
        this.deckTopEra = deckTopEra;
    }

    public void setUpperLine(List<Card> upperLine) {
        this.upperLine = upperLine;
    }

    public void setLowerLine(List<Card> lowerLine) {
        this.lowerLine = lowerLine;
    }

    public void setUpperBuilding(List<Card> upperBuilding) {
        this.upperBuilding = upperBuilding;
    }

    public void setLowerBuilding(List<Card> lowerBuilding) {
        this.lowerBuilding = lowerBuilding;
    }

    public void setOfferPlayerIndex(int offerIndex, Integer playerIndex) {
        if (offerIndex >= 0 && offerIndex < offerBoard.size()) {
            offerBoard.get(offerIndex).setAssignedPlayerIndex(playerIndex);
        }
    }

    public void setOrderPlayerIndex(int orderIndex, Integer playerIndex) {
        if (orderIndex >= 0 && orderIndex < orderBoard.size()) {
            orderBoard.get(orderIndex).setAssignedPlayerIndex(playerIndex);
        }
    }

    public void setPhaseName(String phaseName) {
        this.phaseName = phaseName == null ? "" : phaseName;
    }

    public void setLatestAction(String latestAction) {
        this.latestAction = latestAction == null ? "" : latestAction;
    }

    public void setFinalStandings(String finalStandings) {
        this.finalStandings = finalStandings == null ? "" : finalStandings;
    }

    public void setTribeDeckRemaining(int tribeDeckRemaining) {
        this.tribeDeckRemaining = tribeDeckRemaining;
    }

    public void setBuildingDeckRemaining(int buildingDeckRemaining) {
        this.buildingDeckRemaining = buildingDeckRemaining;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public List<MockupPlayer> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public MockupPlayer getPlayer(int i) {
        return players.get(i);
    }

    public List<Card> getDiscards() {
        return Collections.unmodifiableList(discards);
    }

    public Era getDeckTopEra() {
        return deckTopEra;
    }

    public List<Card> getUpperLine() {
        return Collections.unmodifiableList(upperLine);
    }

    public List<Card> getLowerLine() {
        return Collections.unmodifiableList(lowerLine);
    }

    public List<Card> getUpperBuilding() {
        return Collections.unmodifiableList(upperBuilding);
    }

    public List<Card> getLowerBuilding() {
        return Collections.unmodifiableList(lowerBuilding);
    }

    public List<MockupOrder> getOrderBoard() {
        return Collections.unmodifiableList(orderBoard);
    }

    public List<RectangleAttributedString> getOrderBoardRectangleStrings() {
        return orderBoard.stream().map(MockupOrder::getRectangleAttributedString).toList();
    }

    public List<MockupOffer> getOfferBoard() {
        return Collections.unmodifiableList(offerBoard);
    }

    public List<RectangleAttributedString> getOfferBoardRectangleStrings() {
        return offerBoard.stream().map(MockupOffer::getRectangleAttributedString).toList();
    }

    public String getPhaseName() {
        return phaseName;
    }

    public String getLatestAction() {
        return latestAction;
    }

    public String getFinalStandings() {
        return finalStandings;
    }

    public int getTribeDeckRemaining() {
        return tribeDeckRemaining;
    }

    public int getBuildingDeckRemaining() {
        return buildingDeckRemaining;
    }

    public void addDiscards(List<Card> discards) {
        setDiscards(discards);
    }
}
