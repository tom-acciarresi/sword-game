package it.unicam.cs.mpgc.rpg130730.entities;

import it.unicam.cs.mpgc.rpg130730.AssetLibrary;
import it.unicam.cs.mpgc.rpg130730.GameLoop;
import it.unicam.cs.mpgc.rpg130730.InputMap;
import it.unicam.cs.mpgc.rpg130730.Launcher;
import it.unicam.cs.mpgc.rpg130730.entities.AnimationPlayer.Animation;
import it.unicam.cs.mpgc.rpg130730.persistence.SaveSystem;
import it.unicam.cs.mpgc.rpg130730.util.datatypes.Vector2;
import it.unicam.cs.mpgc.rpg130730.InputMap.KeyBind;
import javafx.geometry.BoundingBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class Player extends Character2D {
    // #region constants
    private static final int DEFAULT_PLAYER_SPEED = 400; // px/s
    public static final int DEFAULT_PLAYER_HEALTH = 5;
    private static final int HIT_COOLDOWN_FRAMES = 30;
    // #endregion

    private Vector2 movementInput = Vector2.ZERO;
    private boolean acceptsInput = true;

    private AnimationPlayer animationPlayer;
    private Rectangle sword = new Rectangle(28, 48,
            new ImagePattern(AssetLibrary.SWORD_SPRITE));

    private int cooldown = 0;

    // TODO: count kills
    private int kills = 0;

    public Player() {
        super();
        setHealth(DEFAULT_PLAYER_HEALTH);
        animationPlayer = new AnimationPlayer(AssetLibrary.getAnimation("knight/idle_down"));
        setSprite(animationPlayer.getCurrFrame());

        sword.setVisible(false);
        this.getChildren().add(sword);
    }

    public Player(Vector2 position) {
        this();
        setPosition(position);
    }

    // #region get-set
    @Override
    public void setHealth(double health) {
        super.setHealth(health);

        if (health <= 0) {
            System.out.println("You died!!!");
            gameOver();
        }
    }

    public void setAcceptsInput(boolean acceptsInput) {
        this.acceptsInput = acceptsInput;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getKills() {
        return kills;
    }
    // #endregion

    @Override
    public void update(double timeDelta) {
        handleMovement(timeDelta);
        handleAnimation();
        checkEnemyCollision();
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

        BoundingBox playerBounds = this.getCollisionBounds();
        boolean collision = CollisionHandler.getEnemies().stream().anyMatch(e -> {
            BoundingBox enemyBounds = e.getCollisionBounds();

            return enemyBounds.intersects(playerBounds);
        });

        if (collision) {
            cooldown = HIT_COOLDOWN_FRAMES;
            collide();
        }
    }

    private void collide() {
        // TODO: knockback
        setHealth(getHealth() - 1);
    }

    private Vector2 getMovementInput() {
        int horizontalAxis = (InputMap.isKeyPressed(KeyBind.LEFT) ? -1 : 0)
                + (InputMap.isKeyPressed(KeyBind.RIGHT) ? +1 : 0);

        int verticalAxis = (InputMap.isKeyPressed(KeyBind.UP) ? -1 : 0)
                + (InputMap.isKeyPressed(KeyBind.DOWN) ? +1 : 0);

        return new Vector2(horizontalAxis, verticalAxis).normalized();
    }

    private void handleAnimation() {
        setViewOrder(-getPosition().y());

        animationPlayer.tick();
        setSprite(animationPlayer.getCurrFrame());

        double x = movementInput.x(), y = movementInput.y();
        String direction = Math.abs(x) > Math.abs(y) ? (x < 0 ? "left" : "right") : (y < 0 ? "up" : "down");

        if (!acceptsInput || movementInput == Vector2.ZERO) {
            Animation newAnim = AssetLibrary.getAnimation("knight/idle_" + direction);
            if (animationPlayer.getCurrAnimation().equals(newAnim)) {
                return;
            }
            animationPlayer.changeTo(newAnim);
            return;
        }

        Animation newAnim = AssetLibrary.getAnimation("knight/walk_" + direction);
        if (animationPlayer.getCurrAnimation().equals(newAnim)) {
            return;
        }

        animationPlayer.changeTo(newAnim);
    }

    private void gameOver() {
        SaveSystem.deleteSave();
        Launcher.quitWithoutSaving();
    }
}
