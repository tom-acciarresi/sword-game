package it.unicam.cs.mpgc.rpg130730;

import java.util.HashMap;
import java.util.Map;

import it.unicam.cs.mpgc.rpg130730.environment.SceneManager.Level;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class InputMap {
    public static enum KeyBind {
        QUIT(KeyCode.ESCAPE),
        DOWN(KeyCode.S),
        UP(KeyCode.W),
        RIGHT(KeyCode.D),
        LEFT(KeyCode.A),
        ATTACK_L(KeyCode.LEFT),
        ATTACK_R(KeyCode.RIGHT),
        ATTACK_U(KeyCode.UP),
        ATTACK_D(KeyCode.DOWN);

        private final KeyCode keyCode;

        KeyBind(KeyCode keyCode) {
            this.keyCode = keyCode;
        }

        public KeyCode keyCode() {
            return keyCode;
        }
    }

    private static Map<KeyCode, Boolean> currentlyPressedKeys = new HashMap<KeyCode, Boolean>();

    public static void initialize(javafx.stage.Stage stage) {
        stage.getScene().setOnKeyPressed(onKeyPressed());
        stage.getScene().setOnKeyReleased(onKeyReleased());
    }

    private static EventHandler<? super KeyEvent> onKeyPressed() {
        return e -> {
            KeyCode code = e.getCode();
            if (code == null)
                throw new NullPointerException(code + "is not a valid keycode");

            if (code == KeyBind.QUIT.keyCode()) {
                Launcher.quit();
            }

            // TODO just for debug
            if (code == KeyCode.DIGIT1) {
                Launcher.getSceneManager().loadLevel(Level.ROOM_1);
            }
            if (code == KeyCode.DIGIT2) {
                Launcher.getSceneManager().loadLevel(Level.ROOM_2);
            }

            InputMap.setKeyPressed(code, true);
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

    public static boolean isKeyPressed(KeyBind keyBind) {
        return currentlyPressedKeys.getOrDefault(keyBind.keyCode(), false);
    }

    private static void setKeyPressed(KeyCode keyCode, boolean b) {
        currentlyPressedKeys.put(keyCode, b);
    }
}
