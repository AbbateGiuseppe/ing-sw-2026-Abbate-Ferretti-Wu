package it.polimi.ingsw.gc49.datapacket.directedToServer.GAME_phase.COMMAND;

import it.polimi.ingsw.gc49.controller.PlayerActionEnum;
import it.polimi.ingsw.gc49.datapacket.Datapacket;
import it.polimi.ingsw.gc49.model.Totem;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;

public class CommandPacket extends Datapacket {
    private final PlayerActionEnum action;
    private int index;
    private Totem totem;

    /**
     * Constructor for blank commands.
     * @param action, the player's action enum.
     */
    public CommandPacket ( PlayerActionEnum action ) {
        super(DatapacketType.COMMAND, ApplicationPhase.GAME);
        this.action = action;
    }

    /**
     * Constructor for indexed commands.
     * @param action, the player's action enum.
     * @param index, the action's chosen index.
     */
    public CommandPacket ( PlayerActionEnum action, int index ) {
        super(DatapacketType.COMMAND, ApplicationPhase.GAME);
        this.action = action;
        this.index = index;
    }

    /**
     * Constructor for totem choosing command.
     * @param action, the player's action enum.
     * @param totem, the chosen totem's enum.
     */
    public CommandPacket ( PlayerActionEnum action, Totem totem ) {
        super(DatapacketType.COMMAND, ApplicationPhase.GAME);
        this.action = action;
        this.totem = totem;
    }


    //### getters
    public PlayerActionEnum getAction () {
        return action;
    }
    public int getIndex () {
        return index;
    }
    public Totem getTotem () {
        return totem;
    }
}
