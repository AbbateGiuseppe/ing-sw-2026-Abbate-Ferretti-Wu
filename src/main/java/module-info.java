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
    exports it.polimi.ingsw.gc49.rmi_socket;
    exports it.polimi.ingsw.gc49.datapacket.directedToServer.GAME_phase.COMMAND;
    exports it.polimi.ingsw.gc49.datapacket.directedToServer.GAME_phase.DISCONNECT;
    exports it.polimi.ingsw.gc49.datapacket.directedToClient.ANY_phase.ERROR;
    exports it.polimi.ingsw.gc49.datapacket.directedToClient.GAME_phase.INITIALIZE_MODEL;
    exports it.polimi.ingsw.gc49.datapacket.uncertain.RECONNECT;
    exports it.polimi.ingsw.gc49.datapacket.directedToClient.GAME_phase.UPDATE_MODEL;
    opens it.polimi.ingsw.gc49.rmi_socket to javafx.fxml;
    exports it.polimi.ingsw.gc49.rmi_socket.server.rooms to java.rmi;
    exports it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualClients;
    opens it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualClients to javafx.fxml;
    exports it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers;
    opens it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers to javafx.fxml;
}