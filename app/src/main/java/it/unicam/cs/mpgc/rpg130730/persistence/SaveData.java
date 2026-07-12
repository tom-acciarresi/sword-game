package it.unicam.cs.mpgc.rpg130730.persistence;

import it.unicam.cs.mpgc.rpg130730.environment.Level;
import it.unicam.cs.mpgc.rpg130730.util.datatypes.Vector2;

public record SaveData(Level level, Vector2 pos, double health, int kills) implements java.io.Serializable {
    @Override
    public final @org.jspecify.annotations.Nullable String toString() {
        return String.format("level: %s, health: %.1f, kills: %d", level.toString(), health, kills);
    }
}
