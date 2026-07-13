package it.unicam.cs.mpgc.rpg130730;

public interface Updatable {
    public default void subscribeToUpdates() {
        GameLoop.startUpdating(this);
    };

    public default void unsubscribeFromUpdates() {
        GameLoop.stopUpdating(this);
    };

    public void update(double timeDelta);
}
