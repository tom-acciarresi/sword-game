package it.unicam.cs.mpgc.rpg130730;

import java.util.HashMap;
import java.util.Map;

import it.unicam.cs.mpgc.rpg130730.environment.Level;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class InputMap {
    private static Map<KeyCode, Boolean> currentlyPressedKeys = new HashMap<KeyCode, Boolean>();

    public static void initialize(javafx.scene.Node inputListeningNode) {
        inputListeningNode.getScene().setOnKeyPressed(onKeyPressed());
        inputListeningNode.getScene().setOnKeyReleased(onKeyReleased());
    }

    // #region set-get
    public static boolean isKeyPressed(KeyBind keyBind) {
        return currentlyPressedKeys.getOrDefault(keyBind.keyCode(), false);
    }

    private static void setKeyPressed(KeyCode keyCode, boolean b) {
        currentlyPressedKeys.put(keyCode, b);
    }
    // #endregion

    private static EventHandler<? super KeyEvent> onKeyPressed() {
        return e -> {
            KeyCode key = e.getCode();
            if (key == null)
                throw new NullPointerException(key + "is not a valid keycode");

            if (key == KeyBind.QUIT.keyCode()) {
                Launcher.saveAndQuit();
            }

            // TODO: just for debug
            if (key == KeyCode.DIGIT1) {
                Launcher.getSceneManager().loadLevel(Level.ROOM_1);
            }
            if (key == KeyCode.DIGIT2) {
                Launcher.getSceneManager().loadLevel(Level.ROOM_2);
            }

            InputMap.setKeyPressed(key, true);
        };
    }

    private static EventHandler<? super KeyEvent> onKeyReleased() {
        return e -> {
            KeyCode code = e.getCode();
            if (code == null)
                throw new NullPointerException(code + "is not a valid keycode");

            InputMap.setKeyPressed(code, false);
        };
    }
}
