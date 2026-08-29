package it.unicam.cs.mpgc.rpg130730.entities;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.jspecify.annotations.Nullable;

import it.unicam.cs.mpgc.rpg130730.environment.RoomTransition;
import it.unicam.cs.mpgc.rpg130730.environment.Tile;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;

/**
 * Keeps track of collidables and determines provides methods to determine
 * collision
 *
 * @author Tommaso Acciarresi
 */
public class CollisionSystem {
    private static @Nullable CollisionSystem instance;

    private Set<Tile> collTiles = new HashSet<Tile>();
    private Set<Enemy> enemies = new HashSet<Enemy>();
    private Set<RoomTransition> transitions = new HashSet<RoomTransition>();

    // #region get-set
    public static CollisionSystem getInstance() {
        if (instance == null) {
            instance = new CollisionSystem();
        }
        return Objects.requireNonNull(instance);
    }

    public boolean addCollidableTile(Tile tile) {
        return collTiles.add(tile);
    }

    public boolean removeCollidableTile(Tile tile) {
        return collTiles.remove(tile);
    }

    public boolean addEnemy(Enemy enemy) {
        return enemies.add(enemy);
    }

    public boolean removeEnemy(Enemy enemy) {
        return enemies.remove(enemy);
    }

    public boolean addRoomTransition(RoomTransition roomTransition) {
        return transitions.add(roomTransition);
    }

    public boolean removeRoomTransition(RoomTransition roomTransition) {
        return transitions.remove(roomTransition);
    }

    public boolean collidesWithTiles(Bounds bounds) {
        return new HashSet<Tile>(collTiles).stream().anyMatch(tile -> {
            Bounds tileBounds = tile.getBoundsInParent();
            return tileBounds.intersects(bounds);
        });
    }

    public @Nullable Enemy collidesWithEnemy(Bounds bounds) {
        Optional<Enemy> enemyOptional = new HashSet<Enemy>(enemies).stream().filter(e -> {
            BoundingBox enemyBounds = e.getCollisionBounds();
            return enemyBounds.intersects(bounds);
        }).findFirst();
        return enemyOptional.isPresent() ? enemyOptional.get() : null;

    }

    public @Nullable RoomTransition enteredTransition(Bounds bounds) {
        Optional<RoomTransition> transitionOptional = new HashSet<RoomTransition>(transitions).stream()
                .filter(roomTransition -> {
                    Bounds transitionBounds = roomTransition.getBoundsInParent();
                    return transitionBounds.intersects(bounds);
                }).findFirst();
        return transitionOptional.isPresent() ? transitionOptional.get() : null;
    }
}
