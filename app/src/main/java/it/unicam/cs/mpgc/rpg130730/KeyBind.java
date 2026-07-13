package it.unicam.cs.mpgc.rpg130730;

import javafx.scene.input.KeyCode;

public enum KeyBind {
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

    private KeyBind(KeyCode keyCode) {
        this.keyCode = keyCode;
    }

    public KeyCode keyCode() {
        return keyCode;
    }
}
