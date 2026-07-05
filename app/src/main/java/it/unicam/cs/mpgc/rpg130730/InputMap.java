package it.unicam.cs.mpgc.rpg130730;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.input.KeyCode;

public class InputMap {
    public static enum KeyBind {
        QUIT(KeyCode.ESCAPE),
        DOWN(KeyCode.S),
        UP(KeyCode.W),
        RIGHT(KeyCode.D),
        LEFT(KeyCode.A);

        private final KeyCode key;

        KeyBind(KeyCode key) {
            this.key = key;
        }

        public KeyCode key() {
            return key;
        }
    }

    private static Map<KeyCode, Boolean> keys = new HashMap<KeyCode, Boolean>();

    public static Map<KeyCode, Boolean> getCurrentlyPressedKeys() {
        return keys;
    }
}
