package it.unicam.cs.mpgc.rpg130730;

public interface Updatable {
    public void subscribeToUpdates();

    public void update(double timeDelta);
}
