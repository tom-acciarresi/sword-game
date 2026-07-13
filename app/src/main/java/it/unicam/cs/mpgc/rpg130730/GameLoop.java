package it.unicam.cs.mpgc.rpg130730;

import java.util.HashSet;
import java.util.Set;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

public class GameLoop {
    private static Set<Updatable> objectsToUpdate = new HashSet<Updatable>();

    private static double timeDelta;

    // #region get-set
    public static void startUpdating(Updatable obj) {
        objectsToUpdate.add(obj);
    }

    public static void stopUpdating(Updatable obj) {
        objectsToUpdate.remove(obj);
    }

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

    private static void updateObjects(double timeDelta) {
        GameLoop.timeDelta = timeDelta;

        new HashSet<Updatable>(objectsToUpdate).stream().forEach(o -> o.update(timeDelta));
    }
}
