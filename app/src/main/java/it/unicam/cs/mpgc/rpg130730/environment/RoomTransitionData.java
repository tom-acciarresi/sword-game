package it.unicam.cs.mpgc.rpg130730.environment;

import it.unicam.cs.mpgc.rpg130730.util.datatypes.Vector2;

public record RoomTransitionData(
        Vector2 transitionLocationA,
        Level roomA,
        Vector2 playerSpawnA,
        Vector2 transitionLocationB,
        Level roomB,
        Vector2 playerSpawnB)
        implements java.io.Serializable {
    @Override
    public final @org.jspecify.annotations.Nullable String toString() {
        return String.format("%s %s -> %s %s", transitionLocationA, roomA, transitionLocationB, roomB);
    }
}
