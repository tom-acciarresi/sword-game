package it.unicam.cs.mpgc.rpg130730.util;

import javafx.scene.shape.Rectangle;

public class Collider extends Rectangle {
    private boolean enabled = true;

    public boolean canCollide() {
        return enabled;
    }

    public boolean isColliding() {
        return false;
    }
}
