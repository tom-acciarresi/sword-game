package it.unicam.cs.mpgc.rpg130730;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public final class GameLoop {
    private static List<Updatable> objectsToUpdate = new ArrayList<Updatable>();

    private static double timeDelta;

    public static void initialize() {
        Timeline loop = new Timeline(
                new KeyFrame(Duration.seconds(1.0 / Launcher.TARGET_FRAMERATE),
                        e -> updateObjects(1.0 / Launcher.TARGET_FRAMERATE)));
        loop.setCycleCount(Animation.INDEFINITE);
        loop.play();
    }

    public static double getTimeDelta() {
        return timeDelta;
    }

    public static boolean subscribeToUpdates(Updatable obj) {
        return objectsToUpdate.add(obj);
    }

    public static boolean unsubscribeToUpdates(Updatable obj) {
        return objectsToUpdate.remove(obj);
    }

    private static void updateObjects(double timeDelta) {
        GameLoop.timeDelta = timeDelta;

        for (Updatable object : objectsToUpdate) {
            object.update(timeDelta);
        }
    }

    public interface Updatable {
        default boolean subscribeToUpdates() {
            return GameLoop.subscribeToUpdates(this);
        };

        default boolean unsubscribeToUpdates() {
            return GameLoop.unsubscribeToUpdates(this);
        };

        void update(double timeDelta);
    }
}
