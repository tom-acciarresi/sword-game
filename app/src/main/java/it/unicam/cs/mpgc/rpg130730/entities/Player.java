package it.unicam.cs.mpgc.rpg130730.entities;

import it.unicam.cs.mpgc.rpg130730.AssetLibrary;
import it.unicam.cs.mpgc.rpg130730.GameLoop;
import it.unicam.cs.mpgc.rpg130730.InputMap;
import it.unicam.cs.mpgc.rpg130730.entities.AnimationPlayer.Animation;
import it.unicam.cs.mpgc.rpg130730.InputMap.KeyBind;
import it.unicam.cs.mpgc.rpg130730.util.Vector2;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class Player extends Entity {
    private static final int DEFAULT_PLAYER_SPEED = 400; // px/s
    public static final int DEFAULT_PLAYER_HEALTH = 5;

    private Vector2 movementInput = Vector2.ZERO;
    private boolean acceptsInput = true;

    private AnimationPlayer ap;
    private Rectangle sword = new Rectangle(28, 48,
            new ImagePattern(AssetLibrary.SWORD_SPRITE));

    private final static int HIT_COOLDOWN_FRAMES = 50;
    private int cooldown = 0;

    public Player() {
        super();
        setHealth(DEFAULT_PLAYER_HEALTH);
        ap = new AnimationPlayer(AssetLibrary.getAnimation("knight/idle_down"));

        sword.setVisible(false);
        getChildren().add(sword);
    }

    public Player(Vector2 position) {
        this();
        setPosition(position);
    }

    @Override
    public void update(double timeDelta) {
        checkEnemyCollision();
        handleMovement(timeDelta);
        handleAnimation();
        setViewOrder(-getPosition().y());
    }

    private void handleMovement(double timeDelta) {
        movementInput = acceptsInput ? getMovementInput() : Vector2.ZERO;
        if (movementInput.equals(Vector2.ZERO))
            return;
        double movementDelta = DEFAULT_PLAYER_SPEED * GameLoop.getTimeDelta();
        Vector2 deltaPos = new Vector2(movementInput.x() * movementDelta, movementInput.y() * movementDelta);
        Vector2 newPos = getPosition().plus(deltaPos);

        move(newPos);
    }

    private void checkEnemyCollision() {
        if (cooldown > 0) {
            cooldown--;
            return;
        }

        Bounds playerBounds = this.getCollisionBounds();
        boolean collision = CollisionHandler.getEnemies().stream().anyMatch(e -> {
            Bounds enemyBounds = e.getCollisionBounds();

            return enemyBounds.intersects(playerBounds);
        });

        if (collision) {
            cooldown = HIT_COOLDOWN_FRAMES;
            collide();
        }
    }

    private void collide() {
        // TODO knockback
        setHealth(getHealth() - 1);
    }

    private Vector2 getMovementInput() {
        int horizontalAxis = (InputMap.isKeyPressed(KeyBind.LEFT) ? -1 : 0)
                + (InputMap.isKeyPressed(KeyBind.RIGHT) ? +1 : 0);
        int verticalAxis = (InputMap.isKeyPressed(KeyBind.UP) ? -1 : 0)
                + (InputMap.isKeyPressed(KeyBind.DOWN) ? +1 : 0);
        return new Vector2(horizontalAxis, verticalAxis).normalized();
    }

    public void setAcceptsInput(boolean acceptsInput) {
        this.acceptsInput = acceptsInput;
    }

    @Override
    public void setHealth(double health) {
        super.setHealth(health);

        if (health <= 0) {
            gameOver();
        }
    }

    private void handleAnimation() {
        ap.tick();
        setSprite(ap.getCurrFrame());

        double x = movementInput.x(), y = movementInput.y();
        String direction = Math.abs(x) > Math.abs(y) ? (x < 0 ? "left" : "right") : (y < 0 ? "up" : "down");

        if (!acceptsInput || movementInput == Vector2.ZERO) {
            Animation newAnim = AssetLibrary.getAnimation("knight/idle_" + direction);
            if (ap.getCurrAnimation().equals(newAnim)) {
                return;
            }
            ap.changeTo(newAnim);
            return;
        }

        Animation newAnim = AssetLibrary.getAnimation("knight/walk_" + direction);
        if (ap.getCurrAnimation().equals(newAnim)) {
            return;
        }

        ap.changeTo(newAnim);
    }

    private void gameOver() {
        Platform.exit();
    }
}
