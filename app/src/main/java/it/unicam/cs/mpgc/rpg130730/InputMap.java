package it.unicam.cs.mpgc.rpg130730;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.jspecify.annotations.Nullable;

import it.unicam.cs.mpgc.rpg130730.util.datatypes.Vector2;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;

/**
 * Interprets player input
 *
 * @author Tommaso Acciarresi
 */
public class InputMap {
    // #region constants
    private final Map<KeyCode, Boolean> currentlyPressedKeys = new HashMap<KeyCode, Boolean>();
    // #endregion

    private static @Nullable InputMap instance;

    // #region get-set
    public static InputMap getInstance() {
        if (instance == null) {
            instance = new InputMap();
        }
        return Objects.requireNonNull(instance);
    }

    public boolean isKeyPressed(KeyBind keyBind) {
        return currentlyPressedKeys.getOrDefault(keyBind.keyCode(), false);
    }

    private void setKeyPressed(KeyCode keyCode, boolean bool) {
        currentlyPressedKeys.put(keyCode, bool);
    }

    public Vector2 getMovementInput() {
        int horizontalAxis = (isKeyPressed(KeyBind.LEFT) ? -1 : 0)
                + (isKeyPressed(KeyBind.RIGHT) ? +1 : 0);

        int verticalAxis = (isKeyPressed(KeyBind.UP) ? -1 : 0)
                + (isKeyPressed(KeyBind.DOWN) ? +1 : 0);

        return new Vector2(horizontalAxis, verticalAxis).normalized();
    }

    public Vector2 getAttackDirection() {
        int horizontalAxis = (isKeyPressed(KeyBind.ATTACK_L) ? -1 : 0)
                + (isKeyPressed(KeyBind.ATTACK_R) ? +1 : 0);

        int verticalAxis = (isKeyPressed(KeyBind.ATTACK_U) ? -1 : 0)
                + (isKeyPressed(KeyBind.ATTACK_D) ? +1 : 0);

        return new Vector2(horizontalAxis, verticalAxis).normalized();
    };
    // #endregion

    public void initialize(Node inputListeningNode) {
        assignOnKeyPressedMethod(inputListeningNode);
        assignOnKeyReleasedMethod(inputListeningNode);
    }

    private void assignOnKeyPressedMethod(Node inputListeningNode) {
        inputListeningNode.getScene().setOnKeyPressed(keyEvent -> {
            KeyCode key = keyEvent.getCode();
            if (key == null)
                throw new NullPointerException();

            if (key.equals(KeyBind.QUIT.keyCode())) {
                Launcher.saveAndQuit();
            }

            setKeyPressed(key, true);
        });
    }

    private void assignOnKeyReleasedMethod(Node inputListeningNode) {
        inputListeningNode.getScene().setOnKeyReleased(keyEvent -> {
            KeyCode code = keyEvent.getCode();
            if (code == null)
                throw new NullPointerException();

            setKeyPressed(code, false);
        });
    }
}
