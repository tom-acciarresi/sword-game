package it.unicam.cs.mpgc.rpg130730;

/**
 * Subscribers to this inferface can be updated roughly 60 times per second
 *
 * @author Tommaso Acciarresi
 */
public interface Updatable {
    public default void subscribeToUpdates() {
        GameLoop.startUpdating(this);
    };

    public default void unsubscribeFromUpdates() {
        GameLoop.stopUpdating(this);
    };

    public void update(double timeDelta);
}
