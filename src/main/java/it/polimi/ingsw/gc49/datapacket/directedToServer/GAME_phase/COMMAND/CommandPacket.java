package it.polimi.ingsw.gc49.datapacket.directedToServer.GAME_phase.COMMAND;

import it.polimi.ingsw.gc49.controller.massi.MassiPlayerActionEnum;
import it.polimi.ingsw.gc49.datapacket.Datapacket;
import it.polimi.ingsw.gc49.model.Totem;

public class CommandPacket extends Datapacket {
    private final MassiPlayerActionEnum action;
    private int index;
    private Totem totem;

    /**
     * Constructor for blank commands.
     * @param action, the player's action enum.
     */
    public CommandPacket ( MassiPlayerActionEnum action ) {
        super(DatapacketType.COMMAND);
        this.action = action;
    }

    /**
     * Constructor for indexed commands.
     * @param action, the player's action enum.
     * @param index, the action's chosen index.
     */
    public CommandPacket ( MassiPlayerActionEnum action, int index ) {
        super(DatapacketType.COMMAND);
        this.action = action;
        this.index = index;
    }

    /**
     * Constructor for totem choosing command.
     * @param action, the player's action enum.
     * @param totem, the chosen totem's enum.
     */
    public CommandPacket ( MassiPlayerActionEnum action, Totem totem ) {
        super(DatapacketType.COMMAND);
        this.action = action;
        this.totem = totem;
    }


    //### getters
    public MassiPlayerActionEnum getAction () {
        return action;
    }
    public int getIndex () {
        return index;
    }
    public Totem getTotem () {
        return totem;
    }
}
