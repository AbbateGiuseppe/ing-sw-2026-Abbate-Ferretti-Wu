package it.polimi.ingsw.gc49.View.mockupModel;

import java.util.List;

public class MockupTrack {
    private List<MockupPlayer> offerBoard;
    private List<MockupPlayer> orderBoard;

    public MockupTrack(List<MockupPlayer> offerBoard, List<MockupPlayer> orderBoard) {
        this.offerBoard = offerBoard;
        this.orderBoard = orderBoard;
    }

    //### setters
    public void setOfferBoard(List<MockupPlayer> offerBoard) {
        this.offerBoard = offerBoard;
    }
    public void setOrderBoard(List<MockupPlayer> orderBoard) {
        this.orderBoard = orderBoard;
    }

    //## getters
    public List<MockupPlayer> getOfferBoard() {
        return offerBoard;
    }
    public List<MockupPlayer> getOrderBoard() {
        return orderBoard;
    }
}
