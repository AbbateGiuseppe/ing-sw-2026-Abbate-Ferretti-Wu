package it.polimi.ingsw.gc49.rmi_socket.virtualMethods;

import it.polimi.ingsw.gc49.ItaEngString;

public enum ApplicationPhase {
    ANY, GAME, HALL, ROOM;

    public String print ( ItaEngString.Language language ) {
        switch (language) {
            case ITA:
                return switch (this) {
                    case ANY -> "QUALUNQUE";
                    case GAME -> "PARTITA";
                    case HALL -> "ATRIO";
                    case ROOM -> "SALA";
                    default -> "";
                };
            case ENG:
                return switch (this) {
                    case ANY -> "ANY";
                    case GAME -> "GAME";
                    case HALL -> "HALL";
                    case ROOM -> "ROOM";
                    default -> "";
                };
            default:
                return "";
        }
    }
}
