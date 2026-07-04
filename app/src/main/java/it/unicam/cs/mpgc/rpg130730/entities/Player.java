package it.unicam.cs.mpgc.rpg130730.entities;

import java.util.HashMap;

import it.unicam.cs.mpgc.rpg130730.Launcher;
import it.unicam.cs.mpgc.rpg130730.environment.Tilemap.Tile;
import it.unicam.cs.mpgc.rpg130730.util.CustomImageLoader;
import it.unicam.cs.mpgc.rpg130730.util.GameLoop;
import it.unicam.cs.mpgc.rpg130730.util.InputMap;
import it.unicam.cs.mpgc.rpg130730.util.Updatable;
import it.unicam.cs.mpgc.rpg130730.util.Vector2;
import it.unicam.cs.mpgc.rpg130730.util.InputMap.KeyBind;
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
    public static final int DEFAULT_PLAYER_SPEED = 400; // px/s

    private Rectangle playerSprite = new Rectangle(Launcher.TILE_SIZE, Launcher.TILE_SIZE);

    /** Normalized input vector */
    private Vector2 movementInput = Vector2.ZERO;
    private Vector2 position = Vector2.ZERO;

    private boolean acceptsInput = true;

    // private CollisionComponent coll = new CollisionComponent(playerSprite);

    public Player() {
        subscribeToUpdates();
        getChildren().add(playerSprite);

        CustomImageLoader il = new CustomImageLoader();
        setSprite(il.loadImage("/images/knight/down.png"));
    }

    public Player(Vector2 position) {
        this();
        setPosition(position);
    }

    public void update(double timeDelta) {
        handleInput(timeDelta);
    }

    private void handleInput(double timeDelta) {
        handleMovement(timeDelta);
    }

    private void handleMovement(double timeDelta) {
        movementInput = acceptsInput
                ? getMovementInput()
                : Vector2.ZERO;
        move(movementInput);
    }

    /** Returns normalized movement input based on currently pressed keys */
    private Vector2 getMovementInput() {
        HashMap<KeyCode, Boolean> currentlyPressedKeys = InputMap.getCurrentlyPressedKeys();

        int horizontalAxis = (currentlyPressedKeys.getOrDefault(KeyBind.LEFT.key(), false) ? -1 : 0)
                + (currentlyPressedKeys.getOrDefault(KeyBind.RIGHT.key(), false) ? +1 : 0);

        int verticalAxis = (currentlyPressedKeys.getOrDefault(KeyBind.UP.key(), false) ? -1 : 0)
                + (currentlyPressedKeys.getOrDefault(KeyBind.DOWN.key(), false) ? +1 : 0);

        return new Vector2(horizontalAxis, verticalAxis).normalized();
    }

    public void setAcceptsInput(boolean acceptsInput) {
        this.acceptsInput = acceptsInput;
    }

    public void move(Vector2 input) {
        if (input.equals(Vector2.ZERO))
            return;

        double movementDelta = DEFAULT_PLAYER_SPEED * GameLoop.timeDelta;
        Vector2 newPos = new Vector2(position.x() + input.x() * movementDelta,
                position.y() + input.y() * movementDelta);

        if (checkCollision(newPos))
            return;

        setPosition(newPos);
    }

    private boolean checkCollision(Vector2 newPos) {
        for (Tile tile : CollisionHandler.collTiles) {
            if (new Rectangle(newPos.x(), newPos.y(), playerSprite.getWidth(), playerSprite.getHeight())
                    .intersects(tile.getBoundsInLocal()))
                ;
            return true;
        }
        return false;
    }

    public void setPosition(Vector2 pos) {
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
