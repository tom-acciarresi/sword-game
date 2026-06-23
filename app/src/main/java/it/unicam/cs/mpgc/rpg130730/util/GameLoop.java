package it.unicam.cs.mpgc.rpg130730.util;

import java.util.ArrayList;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * @author Tommaso Acciarresi
 */
public class GameLoop {
    private static ArrayList<Updatable> objectsToUpdate = new ArrayList<Updatable>();

    public static double timeDelta;

    public static void startLoop(Stage stage) {
        Timeline loop = new Timeline(
                new KeyFrame(Duration.seconds(1.0 / GlobalConstants.TARGET_FRAMERATE),
                        e -> updateObjects(1.0 / GlobalConstants.TARGET_FRAMERATE)));
        loop.setCycleCount(Animation.INDEFINITE);
        loop.play();
    }

    /**
     * Called 60 times per second (hopefully) at `timeDelta` intervals
     */
    private static void updateObjects(double timeDelta) {
        GameLoop.timeDelta = timeDelta;

        for (Updatable object : objectsToUpdate) {
            if (object != null)
                object.update(timeDelta);
            else
                System.err.println("Null updatable object");
        }
    }

    public static boolean subscribeToUpdates(Updatable obj) {
        return objectsToUpdate.add(obj);
    }

    public static boolean unsubscribeToUpdates(Updatable obj) {
        return objectsToUpdate.remove(obj);
    }

    public static ArrayList<Updatable> getObjectsToUpdate() {
        return objectsToUpdate;
    }
}
