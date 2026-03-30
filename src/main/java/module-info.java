module it.polimi.ingsw.gc49 {
    requires javafx.controls;
    requires javafx.fxml;
    requires it.polimi.ingsw.gc49;


    opens it.polimi.ingsw.gc49 to javafx.fxml;
    exports it.polimi.ingsw.gc49;
    exports it.polimi.ingsw.gc49.States;
    opens it.polimi.ingsw.gc49.States to javafx.fxml;
    exports it.polimi.ingsw.gc49.Track;
    opens it.polimi.ingsw.gc49.Track to javafx.fxml;
}