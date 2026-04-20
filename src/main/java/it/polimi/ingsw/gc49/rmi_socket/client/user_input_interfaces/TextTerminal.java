package it.polimi.ingsw.gc49.rmi_socket.client.user_input_interfaces;

import it.polimi.ingsw.gc49.rmi_socket.VirtualServer;
import it.polimi.ingsw.gc49.datapacket.COMMAND.Command;
import it.polimi.ingsw.gc49.datapacket.Datapacket;

import java.util.Scanner;

public class TextTerminal extends UserInputInterface {
    private final TextParser textParser;

    public TextTerminal ( VirtualServer virtualServer ) {
        super( virtualServer );
        textParser = new TextParser();
    }

    @Override
    public void runInput() throws Exception {
        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            String inputLine = scan.nextLine();

            Datapacket dataPacket = textParser.parse( inputLine );
            switch (dataPacket.getDatapacketType()){
                case COMMAND:
                    virtualServer.sendCommand((Command) dataPacket);
                    break;
                default:
                    break;
            }
        }
    }
}
