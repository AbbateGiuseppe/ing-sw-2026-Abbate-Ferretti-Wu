module it.polimi.ingsw.gc49 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.rmi;

    opens it.polimi.ingsw.gc49 to javafx.fxml;
    exports it.polimi.ingsw.gc49;
    exports it.polimi.ingsw.gc49.model.States;
    opens it.polimi.ingsw.gc49.model.States to javafx.fxml;
    exports it.polimi.ingsw.gc49.model.Track;
    opens it.polimi.ingsw.gc49.model.Track to javafx.fxml;
    exports it.polimi.ingsw.gc49.model;
    opens it.polimi.ingsw.gc49.model to javafx.fxml;
    exports it.polimi.ingsw.gc49.rmi_socket.server to java.rmi;
    exports it.polimi.ingsw.gc49.rmi_socket.client to java.rmi;
    exports it.polimi.ingsw.gc49.rmi_socket.client.stub to java.rmi;
    exports it.polimi.ingsw.gc49.rmi_socket.client.user_input_interfaces to java.rmi;
}