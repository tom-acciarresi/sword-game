package it.unicam.cs.mpgc.rpg130730.environment;

import java.io.Serializable;

import org.jspecify.annotations.Nullable;

import it.unicam.cs.mpgc.rpg130730.util.datatypes.Vector2;

/**
 * Data necessary from traveling between Level A and Level B
 *
 * @param transitionLocationA - where doorway A should be placed
 * @param roomA               - what level A is
 * @param playerSpawnA        - where player should spawn in level A
 * @param transitionLocationB - where doorway B should be placed
 * @param roomB               - what level B is
 * @param playerSpawnB        - where player should spawn in level B
 *
 * @author Tommaso Acciarresi
 */
public record RoomTransitionData(
        Vector2 transitionLocationA,
        Level roomA,
        Vector2 playerSpawnA,
        Vector2 transitionLocationB,
        Level roomB,
        Vector2 playerSpawnB)
        implements Serializable {
    @Override
    public final @Nullable String toString() {
        return String.format("%s %s -> %s %s", transitionLocationA, roomA, transitionLocationB, roomB);
    }
}
