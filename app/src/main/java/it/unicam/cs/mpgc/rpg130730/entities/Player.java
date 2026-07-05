package it.unicam.cs.mpgc.rpg130730.entities;

import java.util.HashMap;

import it.unicam.cs.mpgc.rpg130730.Launcher;
import it.unicam.cs.mpgc.rpg130730.environment.Tilemap.Tile;
import it.unicam.cs.mpgc.rpg130730.util.CustomImageLoader;
import it.unicam.cs.mpgc.rpg130730.util.GameLoop;
import it.unicam.cs.mpgc.rpg130730.util.InputMap;
import it.unicam.cs.mpgc.rpg130730.util.Vector2;
import it.unicam.cs.mpgc.rpg130730.util.InputMap.KeyBind;
import javafx.geometry.Bounds;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;

/**
 * Player logic
 *
 * @author Tommaso Acciarresi
 */
public class Player extends Entity {
    private static final int DEFAULT_PLAYER_SPEED = 400; // px/s
    private static final int DEFAULT_PLAYER_HEALTH = 10; // px/s

    private static final Vector2 COLLIDER_SIZE = new Vector2(48, 12);
    private static final Vector2 COLLIDER_OFFSET = new Vector2((Launcher.TILE_SIZE - COLLIDER_SIZE.x()) / 2,
            Launcher.TILE_SIZE - COLLIDER_SIZE.y());

    /** Normalized input vector */
    private Vector2 movementInput = Vector2.ZERO;

    private boolean acceptsInput = true;

    public Player() {
        super();

        setHealth(DEFAULT_PLAYER_HEALTH);

        setSprite(new CustomImageLoader().loadImage("/images/entities/knight/down.png"));

        // DEBUG
        // debugShowCollisionBox();
    }

    public Player(Vector2 position) {
        this();
        setPosition(position);
    }

    // private void debugShowCollisionBox() {
    // Rectangle debugCollider = new Rectangle(COLLIDER_SIZE.x(), COLLIDER_SIZE.y(),
    // javafx.scene.paint.Color.MAGENTA);
    // getChildren().add(debugCollider);
    // debugCollider.setTranslateY(Launcher.TILE_SIZE / 2 - COLLIDER_SIZE.y() / 2);
    // }

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

    @Override
    public void move(Vector2 input) {
        if (input.equals(Vector2.ZERO))
            return;

        double movementDelta = DEFAULT_PLAYER_SPEED * GameLoop.getTimeDelta();
        Vector2 deltaPos = new Vector2(input.x() * movementDelta, input.y() * movementDelta);

        Vector2 newPos = this.getPosition().add(deltaPos);

        newPos = checkTileCollision(newPos);

        setPosition(newPos);
    }

    private Vector2 checkTileCollision(Vector2 newPos) {
        boolean intersectsX = false;
        boolean intersectsY = false;
        for (Tile tile : CollisionHandler.collTiles) {
            Bounds tileBounds = tile.getBoundsInParent();

            // Horizontal collision
            double newX = newPos.x() + COLLIDER_OFFSET.x();
            double oldY = getPosition().y() + COLLIDER_OFFSET.y();
            Bounds boundsX = new Rectangle(newX, oldY, COLLIDER_SIZE.x(), COLLIDER_SIZE.y()).getBoundsInParent();
            if (tileBounds.intersects(boundsX))
                intersectsX = true;

            // Vertical collision
            double oldX = getPosition().x() + COLLIDER_OFFSET.x();
            double newY = newPos.y() + COLLIDER_OFFSET.y();
            Bounds boundsY = new Rectangle(oldX, newY, COLLIDER_SIZE.x(), COLLIDER_SIZE.y()).getBoundsInParent();
            if (tileBounds.intersects(boundsY))
                intersectsY = true;

        }

        // Don't update X if colliding horizontally
        // Don't update Y if colliding vertically
        return new Vector2(intersectsX ? getPosition().x() : newPos.x(), intersectsY ? getPosition().y() : newPos.y());
    }

    private void checkEnemyCollision() {
        // TODO implement
        return;
    }
}
