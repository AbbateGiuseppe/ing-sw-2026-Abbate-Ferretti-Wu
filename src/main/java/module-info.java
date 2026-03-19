module it.polimi.ingsw.gc49 {
    requires javafx.controls;
    requires javafx.fxml;


    opens it.polimi.ingsw.gc49 to javafx.fxml;
    exports it.polimi.ingsw.gc49;
}