package it.unicam.cs.mpgc.rpg130730.util;

import java.util.HashMap;

import javafx.scene.input.KeyCode;

public class InputMap {
    public static enum KeyBind {
        QUIT(KeyCode.ESCAPE),
        DOWN(KeyCode.S),
        UP(KeyCode.W),
        RIGHT(KeyCode.D),
        LEFT(KeyCode.A);

        private final KeyCode key;

        // Constructor
        KeyBind(KeyCode s) {
            this.key = s;
        }

        // Getters
        public KeyCode key() {
            return key;
        }
    }

    private static HashMap<KeyCode, Boolean> keys = new HashMap<KeyCode, Boolean>();

    public static HashMap<KeyCode, Boolean> getCurrentlyPressedKeys() {
        return keys;
    }
}
