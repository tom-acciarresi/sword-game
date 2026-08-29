package it.unicam.cs.mpgc.rpg130730.environment;

import org.jspecify.annotations.Nullable;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * A teleporter (doorway) between two levels
 *
 * @author Tommaso Acciarresi
 */
public class RoomTransition extends Rectangle {
    private RoomTransitionData transitionData;

    // #region constructors
    public RoomTransition(RoomTransitionData data) {
        transitionData = data;
        Color DOOR_COLOR = new Color(44 / 255.0, 29 / 255.0, 14 / 255.0, 1);
        setFill(DOOR_COLOR);
        setViewOrder(-1);
    }

    public RoomTransition(double x, double y, double width, double height, RoomTransitionData data) {
        this(data);
        setWidth(width);
        setHeight(height);
        setX(x);
        setY(y);
    }
    // #endregion

    // #region get-set
    public RoomTransitionData getTransitionData() {
        return transitionData;
    }
    // #endregion

    public void enter() {
        SceneManager sm = SceneManager.getInstance();
        if (sm.getCurrLevel().equals(transitionData.roomA())) {
            sm.loadLevel(transitionData.roomB());
            SceneManager.getInstance().getPlayer().setPosition(transitionData.playerSpawnB());
        }

        else {
            sm.loadLevel(transitionData.roomA());
            SceneManager.getInstance().getPlayer().setPosition(transitionData.playerSpawnA());
        }
    }

    @Override
    public @Nullable String toString() {
        return transitionData.toString();
    }
}
