package it.unicam.cs.mpgc.rpg130730.entities;

public record EnemyData(double health, String identifier) {
    @Override
    public final @org.jspecify.annotations.Nullable String toString() {
        return String.format("health:%.1f, type:%s", health, identifier);
    }
}
