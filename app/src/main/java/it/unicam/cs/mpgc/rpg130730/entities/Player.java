package it.unicam.cs.mpgc.rpg130730.entities;

import java.util.Optional;

import it.unicam.cs.mpgc.rpg130730.AssetLibrary;
import it.unicam.cs.mpgc.rpg130730.GameLoop;
import it.unicam.cs.mpgc.rpg130730.InputMap;
import it.unicam.cs.mpgc.rpg130730.Launcher;
import it.unicam.cs.mpgc.rpg130730.environment.RoomTransition;
import it.unicam.cs.mpgc.rpg130730.persistence.SaveSystem;
import it.unicam.cs.mpgc.rpg130730.util.datatypes.Vector2;
import javafx.geometry.BoundingBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class Player extends Character2D {
    // #region constants
    private static final int DEFAULT_PLAYER_SPEED = 400; // px/s
    public static final int DEFAULT_PLAYER_HEALTH = 5;
    private static final int HIT_COOLDOWN_FRAMES = 30;
    // #endregion

    private Vector2 movementDirection = Vector2.ZERO;
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
        checkRoomTransition();
    }

    private void handleMovement(double timeDelta) {
        movementDirection = acceptsInput ? InputMap.getMovementInput() : Vector2.ZERO;
        if (movementDirection.equals(Vector2.ZERO))
            return;

        double movementValue = DEFAULT_PLAYER_SPEED * GameLoop.getTimeDelta();
        Vector2 deltaPos = new Vector2(movementDirection.x() * movementValue, movementDirection.y() * movementValue);
        Vector2 newPos = getPosition().plus(deltaPos);

        move(newPos);
    }

    private void checkEnemyCollision() {
        if (cooldown > 0) {
            this.getSprite().setVisible(System.currentTimeMillis() % 2 == 0);
            cooldown--;
            return;
        }

        BoundingBox playerBounds = this.getCollisionBounds();

        Optional<Enemy> enemy = CollisionSystem.collidesWithEnemy(playerBounds);
        if (enemy.isPresent()) {
            cooldown = HIT_COOLDOWN_FRAMES;
            Enemy enemyInstance = enemy.get();
            if (enemyInstance == null)
                throw new NullPointerException(enemyInstance + " enemy is null");
            collide(enemyInstance);
        }
    }

    private void collide(Enemy enemy) {
        knockback(enemy.getPosition().distanceTo(this.getPosition()));
        setHealth(getHealth() - 1);
    }

    private void knockback(Vector2 movementVector) {
        this.move(getPosition().plus(movementVector));
    }

    private void checkRoomTransition() {
        Optional<RoomTransition> transition = CollisionSystem.enteredTransition(getCollisionBounds());
        if (transition.isPresent())
            transition.get().enter();
    }

    private void handleAnimation() {
        setViewOrder(-getPosition().y());

        animationPlayer.tick();
        setSprite(animationPlayer.getCurrFrame());

        double x = movementDirection.x(), y = movementDirection.y();
        String direction = Math.abs(x) > Math.abs(y) ? (x < 0 ? "left" : "right") : (y < 0 ? "up" : "down");

        if (!acceptsInput || movementDirection == Vector2.ZERO) {
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
