package it.polimi.ingsw.gc49.server.rooms;

import it.polimi.ingsw.gc49.client.view.mockupHall.MockupRoom;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_ROOM.UpdateRoomPacket;
import it.polimi.ingsw.gc49.server.Hall;
import it.polimi.ingsw.gc49.server.ServerMultiplexer;
import it.polimi.ingsw.gc49.server.proxies.PhasedProxyPlayer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.Disconnectable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Room implements Disconnectable, Serializable {
    protected transient ServerMultiplexer server;
    protected final Hall hall;
    public final String roomName;
    public final int maxNumOfPlayers;
    protected final List<PhasedProxyPlayer> players = new ArrayList<>();

    public Room ( ServerMultiplexer server, Hall hall, String roomName, int maxNumOfPlayers ) {
        this.server = server;
        this.hall = hall;
        this.roomName = roomName;
        this.maxNumOfPlayers = maxNumOfPlayers;
    }
    public Room ( ServerMultiplexer server, Hall hall, String roomName, int maxNumOfPlayers, List<PhasedProxyPlayer> players ) {
        this.server = server;
        this.hall = hall;
        this.roomName = roomName;
        this.maxNumOfPlayers = maxNumOfPlayers;
        this.players.addAll(players);
    }

    public void enterPlayer ( PhasedProxyPlayer newPlayer ) throws Exception {
        System.out.println("Il giocatore " + newPlayer.nickname + " è entrato nella stanza " + roomName + ".");
        players.add(newPlayer);
    }
    protected abstract boolean canEnter();

    public int getNumConnectedPlayers () {
        return players.size();
    }

    protected PhasedProxyPlayer getPlayerByString ( String nickname ) {
        for( PhasedProxyPlayer player : players ) {
            if(nickname.equals(player.nickname)){
                return player;
            }
        }
        return null;
    }

    protected List<String> getListPlayerNicknames () {
        return players.stream()
                .map(player -> player.nickname)
                .collect(Collectors.toList());
    }

    public abstract MockupRoom giveMockupRoom ();

    protected void broadcastMockupRoom() throws Exception {
        UpdateRoomPacket updatedRoom = new UpdateRoomPacket(giveMockupRoom());

        for (PhasedProxyPlayer player : players) {
            try {
                player.updateClientRoom(updatedRoom);
            } catch (Exception e) {
                player.forceDisconnect();
                return; //already finished the broadcast in the forceDisconnect
            }
        }
    }

    public void setServer(ServerMultiplexer server){
        this.server=server;
    }

    /**
     * Verifica se la stanza è vuota.
     * @return true se non ci sono giocatori, false altrimenti.
     */
    public boolean isEmpty() {
        return players.isEmpty();
    }
}
