package it.unicam.cs.mpgc.rpg130730.util;

/**
 * Implementers have access to the game loop `update()` method
 *
 * @author Tommaso Acciarresi
 */
public interface Updatable {
    default void subscribeToUpdates() {
        GameLoop.subscribeToUpdates(this);
    };

    default void unsubscribeToUpdates() {
        if (GameLoop.getObjectsToUpdate().contains(this))
            GameLoop.unsubscribeToUpdates(this);
    };

    void update(double timeDelta);
}
