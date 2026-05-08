package it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.ERROR;

import it.polimi.ingsw.gc49.rmi_socket.datapacket.Datapacket;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;

public class ErrorPacket extends Datapacket {
    public final String errorTitle;
    public final String errorContent;
    public final boolean forceDisconnection;

    /**
     * Datapacket used by the server to send errors to the client.
     * @param errorTitle, the title/category of the error occurred on the server;
     * @param errorContent, the description of the error;
     * @param forceDisconnection, is set to true if the client has to disconnect after receiving the error.
     */
    public ErrorPacket ( String errorTitle, String errorContent, boolean forceDisconnection ) {
        super(DatapacketType.ERROR, ApplicationPhase.ANY);
        this.errorTitle = errorTitle;
        this.errorContent = errorContent;
        this.forceDisconnection = forceDisconnection;
    }
}
