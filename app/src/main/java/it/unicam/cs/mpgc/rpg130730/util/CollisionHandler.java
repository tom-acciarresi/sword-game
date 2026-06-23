package it.unicam.cs.mpgc.rpg130730.util;

import java.util.ArrayList;

public class CollisionHandler {
    public static ArrayList<Collider> colliders = new ArrayList<Collider>();

    public static void checkCollisions() {
        // Round-robin
        for (int i = 0; i < colliders.size() - 2; i++) {
            for (int j = i + 1; j < colliders.size() - 1; j++) {
                if (colliders.get(i).intersects(colliders.get(j).getBoundsInLocal())) {
                    return;
                }
            }
        }
    }
}
