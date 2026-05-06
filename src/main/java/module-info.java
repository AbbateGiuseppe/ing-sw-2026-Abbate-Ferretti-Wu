module it.polimi.ingsw.gc49 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.rmi;
    requires org.jline;

    opens it.polimi.ingsw.gc49 to javafx.fxml;
    //exports it.polimi.ingsw.gc49;
    exports it.polimi.ingsw.gc49.model.States;
    opens it.polimi.ingsw.gc49.model.States to javafx.fxml;
    exports it.polimi.ingsw.gc49.model.Track;
    opens it.polimi.ingsw.gc49.model.Track to javafx.fxml;
    exports it.polimi.ingsw.gc49.model;
    opens it.polimi.ingsw.gc49.model to javafx.fxml;
    exports it.polimi.ingsw.gc49.rmi_socket.server to java.rmi;
    exports it.polimi.ingsw.gc49.rmi_socket.client to java.rmi;
    exports it.polimi.ingsw.gc49.rmi_socket.client.proxies to java.rmi;
    exports it.polimi.ingsw.gc49.rmi_socket.client.user_input_interfaces to java.rmi;
    exports it.polimi.ingsw.gc49.datapacket.directedToServer.GAME_phase.COMMAND;
    exports it.polimi.ingsw.gc49.datapacket.directedToServer.GAME_phase.DISCONNECT;
    exports it.polimi.ingsw.gc49.datapacket.directedToClient.ERROR;
    exports it.polimi.ingsw.gc49.datapacket.directedToClient.INITIALIZE_MODEL;
    exports it.polimi.ingsw.gc49.datapacket.directedToClient.UPDATE_MODEL;
    exports it.polimi.ingsw.gc49.rmi_socket.server.rooms to java.rmi;
    exports it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualClients;
    opens it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualClients to javafx.fxml;
    exports it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers;
    opens it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers to javafx.fxml;
    exports it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.adapters;
    opens it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.adapters to javafx.fxml;
    opens it.polimi.ingsw.gc49.rmi_socket.virtualMethods to javafx.fxml;
    exports it.polimi.ingsw.gc49.rmi_socket.virtualMethods;
}