package it.polimi.ingsw.gc49.server.model;

import java.io.Serializable;

public class Locks implements Serializable {
    public final LockObject playerInput = new LockObject();
    public final LockObject broadcastLock = new LockObject();
}
