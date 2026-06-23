package it.unicam.cs.mpgc.rpg130730.entities;

import java.util.HashMap;

import it.unicam.cs.mpgc.rpg130730.util.CustomImageLoader;
import it.unicam.cs.mpgc.rpg130730.util.GameLoop;
import it.unicam.cs.mpgc.rpg130730.util.GlobalConstants;
import it.unicam.cs.mpgc.rpg130730.util.InputMap;
import it.unicam.cs.mpgc.rpg130730.util.Movable;
import it.unicam.cs.mpgc.rpg130730.util.Updatable;
import it.unicam.cs.mpgc.rpg130730.util.Vector2f;
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
public class Player extends StackPane implements Updatable, Movable {

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

    private Vector2f movementInput = Vector2f.ZERO;
    private Vector2f position = Vector2f.ZERO;

    private boolean acceptsInput = true;

    // private boolean canCollide = true;

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
                : Vector2f.ZERO;
        move(movementInput);
    }

    private Vector2f getMovementInput() {
        HashMap<KeyCode, Boolean> currentlyPressedKeys = InputMap.getCurrentlyPressedKeys();
        return new Vector2f(
                (currentlyPressedKeys.getOrDefault(KeyBind.LEFT.key(), false) ? -1 : 0) +
                        (currentlyPressedKeys.getOrDefault(KeyBind.RIGHT.key(), false) ? +1 : 0),
                (currentlyPressedKeys.getOrDefault(KeyBind.UP.key(), false) ? -1 : 0) +
                        (currentlyPressedKeys.getOrDefault(KeyBind.DOWN.key(), false) ? +1 : 0));
    }

    private void checkIfExitPressed() {
        if (InputMap.getCurrentlyPressedKeys().getOrDefault(KeyBind.ESC.key(), false))
            Platform.exit();
    }

    public void setAcceptsInput(boolean acceptsInput) {
        this.acceptsInput = acceptsInput;
    }

    public void move(Vector2f input) {
        if (input.equals(Vector2f.ZERO))
            return;

        double r = GlobalConstants.PLAYER_SPEED * GameLoop.timeDelta;
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
