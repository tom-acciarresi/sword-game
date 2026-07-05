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
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
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

    private Vector2 colliderOffset = new Vector2(8, 56);
    private Vector2 colliderSize = new Vector2(48, 8);

    private Rectangle tmpCollider = new Rectangle(colliderSize.x(), colliderSize.y(), Color.BEIGE);
    private int tempADJASIDjASOI = (int) (32 - colliderSize.y() / 2);

    /** Normalized input vector */
    private Vector2 movementInput = Vector2.ZERO;
    private Vector2 position = Vector2.ZERO;

    private boolean acceptsInput = true;

    public Player() {
        subscribeToUpdates();

        getChildren().add(playerSprite);

        // TODO tmp
        getChildren().add(tmpCollider);
        tmpCollider.setTranslateY(tempADJASIDjASOI);

        CustomImageLoader il = new CustomImageLoader();
        setSprite(il.loadImage("/images/entities/knight/down.png"));
    }

    public Player(Vector2 position) {
        this();
        setPosition(position);
    }

    public void update(double timeDelta) {
        handleInput(timeDelta);
        checkEnemyCollision();
    }

    private void handleInput(double timeDelta) {
        handleMovement(timeDelta);
    }

    private void handleMovement(double timeDelta) {
        movementInput = acceptsInput ? getMovementInput() : Vector2.ZERO;
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
        Vector2 deltaPos = new Vector2(input.x() * movementDelta, input.y() * movementDelta);

        Vector2 newPos = this.position.add(deltaPos);

        newPos = checkTileCollision(newPos);

        setPosition(newPos);
    }

    private Vector2 checkTileCollision(Vector2 pos) {
        boolean intersectsX = false;
        boolean intersectsY = false;
        for (Tile tile : CollisionHandler.collTiles) {
            Bounds tileCollider = tile.getBoundsInParent();

            Rectangle rectangleX = new Rectangle(pos.x() + colliderOffset.x(), position.y() + colliderOffset.y(),
                    colliderSize.x(), colliderSize.y());

            Rectangle rectangleY = new Rectangle(position.x() + colliderOffset.x(), pos.y() + colliderOffset.y(),
                    colliderSize.x(), colliderSize.y());
            if (tileCollider.intersects(rectangleX.getBoundsInParent()))
                intersectsX = true;
            if (tileCollider.intersects(rectangleY.getBoundsInParent()))
                intersectsY = true;
        }

        if (intersectsY)
            pos = new Vector2(pos.x(), position.y());
        if (intersectsX)
            pos = new Vector2(position.x(), pos.y());
        return pos;
    }

    private void checkEnemyCollision() {
        // TODO implement
        return;
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
