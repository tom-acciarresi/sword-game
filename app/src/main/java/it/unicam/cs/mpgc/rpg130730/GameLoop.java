package it.unicam.cs.mpgc.rpg130730;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.jspecify.annotations.Nullable;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * 60 UPS game loop
 *
 * @author Tommaso Acciarresi
 */
public class GameLoop {
    // #region constants
    public static final int TARGET_FRAMERATE = 60;
    private static final double TIME_DELTA = 1.0 / TARGET_FRAMERATE;
    // #endregion

    private static @Nullable GameLoop instance;

    private Timeline loop;

    private Set<Updatable> objectsToUpdate = new HashSet<Updatable>();

    // #region constructors
    private GameLoop() {
        loop = new Timeline(new KeyFrame(
                Duration.seconds(TIME_DELTA),
                e -> updateObjects(TIME_DELTA)));

        loop.setCycleCount(Animation.INDEFINITE);
    };
    // #endregion

    // #region get-set
    public void startUpdating(Updatable obj) {
        objectsToUpdate.add(obj);
    }

    public void stopUpdating(Updatable obj) {
        objectsToUpdate.remove(obj);
    }

    // #endregion

    public static GameLoop getInstance() {
        if (instance == null)
            instance = new GameLoop();
        return Objects.requireNonNull(instance);
    }

    public void start() {
        loop.play();
    }

    public void pause() {
        loop.pause();
    }

    private void updateObjects(double timeDelta) {
        new HashSet<Updatable>(objectsToUpdate).stream().forEach(o -> o.update(TIME_DELTA));
    }
}
