package it.unicam.cs.mpgc.rpg130730;

import java.util.HashSet;
import java.util.Set;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

public class GameLoop {
    private static Set<Updatable> objectsToUpdate = new HashSet<Updatable>();
    private static Set<Updatable> objectsToAdd = new HashSet<Updatable>();
    private static Set<Updatable> objectsToRemove = new HashSet<Updatable>();

    private static double timeDelta;

    // #region set-get
    public static double getTimeDelta() {
        return timeDelta;
    }
    // #endregion

    public static void initialize() {
        Timeline loop = new Timeline(new KeyFrame(
                javafx.util.Duration.seconds(1.0 / Launcher.TARGET_FRAMERATE),
                e -> updateObjects(1.0 / Launcher.TARGET_FRAMERATE)));

        loop.setCycleCount(Animation.INDEFINITE);
        loop.play();
    }

    public static void startUpdating(Updatable obj) {
        objectsToAdd.add(obj);
    }

    public static void stopUpdating(Updatable obj) {
        objectsToRemove.add(obj);
    }

    private static void updateObjects(double timeDelta) {
        GameLoop.timeDelta = timeDelta;

        objectsToUpdate.addAll(objectsToAdd);
        objectsToAdd.clear();
        objectsToUpdate.removeAll(objectsToRemove);
        objectsToRemove.clear();

        objectsToUpdate.stream().forEach(o -> o.update(timeDelta));
    }
}
