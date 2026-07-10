package it.unicam.cs.mpgc.rpg130730;

import java.util.HashSet;
import java.util.Set;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public final class GameLoop {
    private static Set<Updatable> objectsToUpdate = new HashSet<Updatable>();
    private static Set<Updatable> objectsToAdd = new HashSet<Updatable>();
    private static Set<Updatable> objectsToRemove = new HashSet<Updatable>();

    private static double timeDelta;

    public static void initialize() {
        Timeline loop = new Timeline(new KeyFrame(Duration.seconds(1.0 / Launcher.TARGET_FRAMERATE),
                e -> updateObjects(1.0 / Launcher.TARGET_FRAMERATE)));
        loop.setCycleCount(Animation.INDEFINITE);
        loop.play();
    }

    public static double getTimeDelta() {
        return timeDelta;
    }

    private static void updateObjects(double timeDelta) {
        GameLoop.timeDelta = timeDelta;

        objectsToUpdate.addAll(objectsToAdd);
        objectsToAdd.clear();
        objectsToUpdate.removeAll(objectsToRemove);
        objectsToRemove.clear();

        System.out.println(objectsToUpdate);
        objectsToUpdate.stream().forEach(o -> o.update(timeDelta));
    }

    public interface Updatable {
        default void subscribeToUpdates() {
            objectsToAdd.add(this);
        };

        default void unsubscribeFromUpdates() {
            objectsToRemove.add(this);
        };

        void update(double timeDelta);
    }
}
