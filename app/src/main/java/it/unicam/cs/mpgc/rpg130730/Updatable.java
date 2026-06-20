package it.unicam.cs.mpgc.rpg130730;

/**
 * Implementers have access to the game loop `update()` method
 */
public interface Updatable {
    public default void subscribeToUpdates() {
        AppLauncher.objectsToUpdate.add(this);
    };

    public void update(double timeDelta);
}
