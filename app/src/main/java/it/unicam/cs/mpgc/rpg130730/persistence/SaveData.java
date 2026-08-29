package it.unicam.cs.mpgc.rpg130730.persistence;

import org.jspecify.annotations.Nullable;

import it.unicam.cs.mpgc.rpg130730.environment.Level;
import it.unicam.cs.mpgc.rpg130730.util.datatypes.Vector2;

/**
 * SaveData
 *
 * @param level     - the room the player is in
 * @param playerPos - the coordinates of the player
 * @param health    - health points
 * @param kills     - amount of enemies killed
 *
 * @author Tommaso Acciarresi
 */
public record SaveData(Level level, Vector2 playerPos, double health, int kills) implements java.io.Serializable {
    @Override
    public final @Nullable String toString() {
        return String.format("level: %s, playerPos: %s, health: %.1f, kills: %d",
                level.toString(),
                playerPos,
                health,
                kills);
    }
}
