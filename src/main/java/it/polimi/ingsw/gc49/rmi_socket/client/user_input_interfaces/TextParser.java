package it.polimi.ingsw.gc49.rmi_socket.client.user_input_interfaces;

import it.polimi.ingsw.gc49.controller.massi.MassiPlayerActionEnum;
import it.polimi.ingsw.gc49.datapacket.COMMAND.Command;
import it.polimi.ingsw.gc49.datapacket.Datapacket;

public class TextParser {

    public static Datapacket parse(String text) {
        Datapacket datapacket = new Command(MassiPlayerActionEnum.CHOOSE_OFFER, 2);

        return datapacket;
    }
}
