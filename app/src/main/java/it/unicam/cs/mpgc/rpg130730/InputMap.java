package it.unicam.cs.mpgc.rpg130730;

import java.util.HashMap;
import java.util.Map;

import it.unicam.cs.mpgc.rpg130730.util.datatypes.Vector2;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class InputMap {
    private static Map<KeyCode, Boolean> currentlyPressedKeys = new HashMap<KeyCode, Boolean>();

    public static void initialize(javafx.scene.Node inputListeningNode) {
        inputListeningNode.getScene().setOnKeyPressed(onKeyPressed());
        inputListeningNode.getScene().setOnKeyReleased(onKeyReleased());
    }

    // #region get-set
    public static boolean isKeyPressed(KeyBind keyBind) {
        return currentlyPressedKeys.getOrDefault(keyBind.keyCode(), false);
    }

    private static void setKeyPressed(KeyCode keyCode, boolean bool) {
        currentlyPressedKeys.put(keyCode, bool);
    }

    public static Vector2 getMovementInput() {
        int horizontalAxis = (InputMap.isKeyPressed(KeyBind.LEFT) ? -1 : 0)
                + (InputMap.isKeyPressed(KeyBind.RIGHT) ? +1 : 0);

        int verticalAxis = (InputMap.isKeyPressed(KeyBind.UP) ? -1 : 0)
                + (InputMap.isKeyPressed(KeyBind.DOWN) ? +1 : 0);

        return new Vector2(horizontalAxis, verticalAxis).normalized();
    }

    public static Vector2 getAttackDirection() {
        int horizontalAxis = (InputMap.isKeyPressed(KeyBind.ATTACK_L) ? -1 : 0)
                + (InputMap.isKeyPressed(KeyBind.ATTACK_R) ? +1 : 0);

        int verticalAxis = (InputMap.isKeyPressed(KeyBind.ATTACK_U) ? -1 : 0)
                + (InputMap.isKeyPressed(KeyBind.ATTACK_D) ? +1 : 0);

        return new Vector2(horizontalAxis, verticalAxis).normalized();
    };
    // #endregion

    private static EventHandler<? super KeyEvent> onKeyPressed() {
        return keyEvent -> {
            KeyCode key = keyEvent.getCode();
            if (key == null)
                throw new NullPointerException();

            if (key.equals(KeyBind.QUIT.keyCode())) {
                Launcher.saveAndQuit();
            }

            InputMap.setKeyPressed(key, true);
        };
    }

    private static EventHandler<? super KeyEvent> onKeyReleased() {
        return keyEvent -> {
            KeyCode code = keyEvent.getCode();
            if (code == null)
                throw new NullPointerException();

            InputMap.setKeyPressed(code, false);
        };
    }

}
