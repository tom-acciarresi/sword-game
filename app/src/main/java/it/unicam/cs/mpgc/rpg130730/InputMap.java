package it.unicam.cs.mpgc.rpg130730;

import java.util.HashMap;
import java.util.Map;

import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public final class InputMap {
    public static enum KeyBind {
        QUIT(KeyCode.ESCAPE),
        DOWN(KeyCode.S),
        UP(KeyCode.W),
        RIGHT(KeyCode.D),
        LEFT(KeyCode.A);

        private final KeyCode keyCode;

        KeyBind(KeyCode keyCode) {
            this.keyCode = keyCode;
        }

        public KeyCode keyCode() {
            return keyCode;
        }
    }

    public static void initialize(Stage stage) {
        stage.getScene().setOnKeyPressed(e -> {
            KeyCode code = e.getCode();
            if (code == null)
                throw new NullPointerException(code + "is not a valid keycode");
            InputMap.setKeyPressed(code, true);

            if (code == KeyBind.QUIT.keyCode())
                Platform.exit();
        });

        stage.getScene().setOnKeyReleased(e -> {
            KeyCode code = e.getCode();
            if (code == null)
                throw new NullPointerException(code + "is not a valid keycode");
            InputMap.setKeyPressed(code, false);
        });
    }

    private static Map<KeyCode, Boolean> currentlyPressedKeys = new HashMap<KeyCode, Boolean>();

    public static boolean isKeyPressed(KeyBind keyBind) {
        return currentlyPressedKeys.getOrDefault(keyBind.keyCode(), false);
    }

    private static void setKeyPressed(KeyCode keyCode, boolean b) {
        currentlyPressedKeys.put(keyCode, b);
    }
}
