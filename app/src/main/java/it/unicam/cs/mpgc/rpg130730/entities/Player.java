package it.unicam.cs.mpgc.rpg130730.entities;

import java.util.HashMap;

import it.unicam.cs.mpgc.rpg130730.util.CustomImageLoader;
import it.unicam.cs.mpgc.rpg130730.util.GlobalConstants;
import it.unicam.cs.mpgc.rpg130730.util.InputMap;
import it.unicam.cs.mpgc.rpg130730.util.Updatable;
import it.unicam.cs.mpgc.rpg130730.util.Vector2f;
import it.unicam.cs.mpgc.rpg130730.util.Vector2i;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

/**
 * Player logic
 *
 * @author Tommaso Acciarresi
 */
public class Player extends StackPane implements Updatable {

    public enum KeyBind {
        ESC(KeyCode.ESCAPE),
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

    private Rectangle playerSprite = new Rectangle(GlobalConstants.TILE_SIZE, GlobalConstants.TILE_SIZE);

    private Vector2i movementInput = Vector2i.ZERO;
    private Vector2f position = Vector2f.ZERO;

    private boolean acceptsInput = true;

    public Player(Vector2f position) {
        subscribeToUpdates();
        getChildren().add(playerSprite);

        setPosition(position);

        CustomImageLoader il = new CustomImageLoader();
        setSprite(il.loadImage("/images/player.png"));
    }

    public void update(double timeDelta) {
        handleInput(timeDelta);
    }

    private void handleInput(double timeDelta) {
        handleMovement(timeDelta);

        checkIfExitPressed();
    }

    private void handleMovement(double timeDelta) {
        movementInput = acceptsInput
                ? getMovementInput()
                : Vector2i.ZERO;
        move(movementInput, timeDelta);
    }

    private Vector2i getMovementInput() {
        HashMap<KeyCode, Boolean> currentlyPressedKeys = InputMap.getCurrentlyPressedKeys();
        return new Vector2i(
                (currentlyPressedKeys.getOrDefault(KeyBind.LEFT.key(), false) ? -1 : 0) +
                        (currentlyPressedKeys.getOrDefault(KeyBind.RIGHT.key(), false) ? +1 : 0),
                (currentlyPressedKeys.getOrDefault(KeyBind.UP.key(), false) ? -1 : 0) +
                        (currentlyPressedKeys.getOrDefault(KeyBind.DOWN.key(), false) ? +1 : 0));
    }

    private void checkIfExitPressed() {
        if (InputMap.getCurrentlyPressedKeys().getOrDefault(KeyBind.ESC.key(), false))
            Platform.exit();
    }

    public void move(Vector2i input, double timeDelta) {
        if (input.equals(Vector2i.ZERO))
            return;
        double r = GlobalConstants.PLAYER_SPEED * timeDelta;
        // WHY ARE THIS FUNCTION'S INPUTS INVERTED??? WHO PUTS Y BEFORE X???
        double angle = Math.atan2(input.y(), input.x());

        setPosition(new Vector2f(
                position.x() + r * Math.cos(angle),
                position.y() + r * Math.sin(angle)));
    }

    public void setPosition(Vector2f pos) {
        this.position = pos;

        updateSprite();
    }

    private void setSprite(Image image) {
        playerSprite.setFill(new ImagePattern(image));
    }

    private void updateSprite() {
        this.setTranslateX(position.x());
        this.setTranslateY(position.y());
    };
}
