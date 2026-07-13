package it.unicam.cs.mpgc.rpg130730.environment;

import it.unicam.cs.mpgc.rpg130730.Launcher;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

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
        SceneManager sm = Launcher.getSceneManager();
        if (sm.getCurrLevel().equals(transitionData.roomA())) {
            sm.loadLevel(transitionData.roomB());
            Launcher.getSceneManager().getPlayer().setPosition(transitionData.playerSpawnB());
        }

        else {
            sm.loadLevel(transitionData.roomA());
            Launcher.getSceneManager().getPlayer().setPosition(transitionData.playerSpawnA());
        }
    }

    @Override
    public @org.jspecify.annotations.Nullable String toString() {
        return transitionData.toString();
    }
}
