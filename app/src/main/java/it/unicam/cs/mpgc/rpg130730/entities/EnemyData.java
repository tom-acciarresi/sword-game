package it.unicam.cs.mpgc.rpg130730.entities;

import org.jspecify.annotations.Nullable;

/**
 * EnemyData
 *
 * @param health     - enemy hp
 * @param identifier - string that represents the enemy type uniquely
 *
 * @author Tommaso Acciarresi
 */
public record EnemyData(double health, String identifier) {
    @Override
    public final @Nullable String toString() {
        return String.format("health:%.1f, type:%s", health, identifier);
    }
}
