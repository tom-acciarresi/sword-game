package it.unicam.cs.mpgc.rpg130730.util;

import it.unicam.cs.mpgc.rpg130730.AppLauncher;

/**
 * Implementers have access to the game loop `update()` method
 */
public interface Updatable {
    public default void subscribeToUpdates() {
        AppLauncher.subscribeToUpdates(this);
    };

    public void update(double timeDelta);
}
