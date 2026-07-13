package it.unicam.cs.mpgc.rpg130730.entities;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import it.unicam.cs.mpgc.rpg130730.environment.RoomTransition;
import it.unicam.cs.mpgc.rpg130730.environment.Tile;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;

public class CollisionSystem {
    private static Set<Tile> collTiles = new HashSet<Tile>();
    private static Set<Enemy> enemies = new HashSet<Enemy>();
    private static Set<RoomTransition> transitions = new HashSet<RoomTransition>();

    // #region get-set
    public static boolean addCollidableTile(Tile tile) {
        return collTiles.add(tile);
    }

    public static boolean removeCollidableTile(Tile tile) {
        return collTiles.remove(tile);
    }

    public static boolean addEnemy(Enemy enemy) {
        return enemies.add(enemy);
    }

    public static boolean removeEnemy(Enemy enemy) {
        return enemies.remove(enemy);
    }

    public static boolean addRoomTransition(RoomTransition roomTransition) {
        return transitions.add(roomTransition);
    }

    public static boolean removeRoomTransition(RoomTransition roomTransition) {
        return transitions.remove(roomTransition);
    }
    // #endregion

    public static boolean collidesWithTiles(Bounds bounds) {
        return new HashSet<Tile>(collTiles).stream().anyMatch(tile -> {
            Bounds tileBounds = tile.getBoundsInParent();
            return tileBounds.intersects(bounds);
        });
    }

    public static Optional<Enemy> collidesWithEnemy(Bounds bounds) {
        Optional<Enemy> enemyOptional = new HashSet<Enemy>(enemies).stream().filter(enemy -> {
            BoundingBox enemyBounds = enemy.getCollisionBounds();
            return enemyBounds.intersects(bounds);
        }).findFirst();

        if (enemyOptional == null)
            throw new NullPointerException();

        return enemyOptional;
    }

    public static Optional<RoomTransition> enteredTransition(Bounds bounds) {
        Optional<RoomTransition> transitionOptional = new HashSet<RoomTransition>(transitions).stream()
                .filter(roomTransition -> {
                    Bounds transitionBounds = roomTransition.getBoundsInParent();
                    return transitionBounds.intersects(bounds);
                }).findFirst();

        if (transitionOptional == null)
            throw new NullPointerException();

        return transitionOptional;
    }
}
